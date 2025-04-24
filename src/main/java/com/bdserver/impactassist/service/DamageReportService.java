package com.bdserver.impactassist.service;

import com.bdserver.impactassist.model.*;
import com.bdserver.impactassist.repo.DamageReportRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.core.JsonValue;
import com.openai.core.http.HttpResponseFor;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionContentPart;
import com.openai.models.chat.completions.ChatCompletionContentPartText;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.models.chat.completions.ChatCompletionUserMessageParam.Content;
import org.apache.coyote.BadRequestException;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.*;

@Service
public class DamageReportService {

    private final DamageReportRepo damageReportRepo;
    private final UserService userService;
    private final SpringTemplateEngine springTemplateEngine;
    private final OpenAIClient openAiClient;
    private final ObjectMapper objectMapper;
    private final ClaimService claimService;

    public DamageReportService(DamageReportRepo damageReportRepo, UserService userService, SpringTemplateEngine springTemplateEngine, Environment env, ObjectMapper objectMapper, ClaimService claimService) {
        this.damageReportRepo = damageReportRepo;
        this.userService = userService;
        this.springTemplateEngine = springTemplateEngine;
        this.openAiClient = OpenAIOkHttpClient.builder()
                .fromEnv()
                .apiKey(Objects.requireNonNull(env.getProperty("openai.api-key")))
                .build();
        this.objectMapper = objectMapper;
        this.claimService = claimService;
    }

    @Transactional
    public Integer createDamageReport(RequestDamageReportDAO request) {
        Integer reportId = damageReportRepo.createReport(userService.getUserId(), request.getClaimId());
        request.setReportId(reportId);
        damageReportRepo.addReportDataToReport(request);
        return reportId;
    }

    public List<PartialDamageReportDAO> getReportList(Integer claimId) {
        return damageReportRepo.getReportList(claimId);
    }

    public ResponseDamageReportDAO getReport(Integer id, String lang) {
        DamageReportDAO damageReport = damageReportRepo.getDamageReport(id);
        List<AutoPartsAndServicesDAO> autoParts;
        if (lang == null) {
            autoParts = damageReportRepo.getAutoPartsAndServicesByReportId(id);
        } else {
            autoParts = damageReportRepo.getAutoPartsAndServicesByReportIdAndLanguage(id, lang);
        }
        return ResponseDamageReportDAO.builder().damageReport(damageReport).autoPartsAndServices(autoParts).build();
    }

    public byte[] generatePdf(Integer id, String lang) {
        ResponseDamageReportDAO damageReport = getReport(id, lang);
        Context context = new Context();
        context.setVariable("date", LocalDate.now());
        context.setVariable("fullName", damageReport.getDamageReport().getFullName());
        context.setVariable("estimatedMinPriceWithoutService", damageReport.getDamageReport().getEstimatedMinPriceWithoutService());
        context.setVariable("estimatedMaxPriceWithoutService", damageReport.getDamageReport().getEstimatedMaxPriceWithoutService());
        context.setVariable("estimatedMinPriceWithService", damageReport.getDamageReport().getEstimatedMinPriceWithService());
        context.setVariable("estimatedMaxPriceWithService", damageReport.getDamageReport().getEstimatedMaxPriceWithService());
        context.setVariable("autoPartsAndServices", damageReport.getAutoPartsAndServices());
        String htmlContent = "";
        if (lang == null) {
            htmlContent = springTemplateEngine.process("pdf_template", context);
        } else if (lang.equals("lt")) {
            htmlContent = springTemplateEngine.process("pdf_template_lt", context);
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        renderer.createPDF(outputStream);
        return outputStream.toByteArray();
    }

    @Transactional
    public Integer createDamageReportUsingAI(Integer claimId) throws IOException {
        List<String> images = new ArrayList<>();
        OpenAIChatResponseDAO chatResponse;
        String messageContent = "";
        List<byte[]> claimImages = claimService.getClaimImages(claimId);
        if (claimImages.isEmpty()) {
            throw new BadRequestException("No images in a claim");
        }
        for (byte[] claimImage : claimImages) {
            String base64File = Base64.getEncoder().encodeToString(claimImage);
            String file = "data:image/jpeg;base64," + base64File;
            images.add(file);
        }
        List<ChatCompletionContentPart> userRequests = new ArrayList<>();
        ChatCompletionContentPartText userRequest = ChatCompletionContentPartText.builder().text("Detect damage for this vehicle.").build();
        userRequests.add(ChatCompletionContentPart.ofText(userRequest));
        for (String file : images) {
            userRequests.add(ChatCompletionContentPart.ofText(ChatCompletionContentPartText.builder().text(file).build()));
        }
        Content content = Content.ofArrayOfContentParts(userRequests);
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(ChatModel.GPT_4_1_NANO)
                .addSystemMessage("1\tBonnet / Hood\n2\tBonnet / Hood Latch\n3\tBumper (Exposed / Unexposed)\n4\tCowl Screen\n5\tDecklid\n6\tFascia Rear and Support\n7\tFender (Wing or Mudguard)\n8\tFront Clip\n9\tFront Fascia and Header Panel\n10\tGrille\n11\tPillar and Hard Trim\n12\tQuarter Panel\n13\tRadiator Core Support\n14\tRocker Panel\n15\tRoof Rack\n16\tSpoiler (Front / Rear)\n17\tValance\n18\tTrunk / Boot / Hatch\n19\tTrunk / Boot Latch\n20\tWelded Assembly\n21\tMirror\n22\tLicense Plate Bracket\n23\tOuter/Inner Door Handle\n24\tDoor Control Module\n25\tDoor Seal\n26\tDoor Watershield\n27\tHinge\n28\tDoor Latch\n29\tDoor Lock / Power Door Locks\n30\tCenter-locking\n31\tFuel Tank (Filler) Door\n32\tGlass\n33\tSunroof\n34\tSunroof Motor\n35\tWindow Motor\n36\tWindow Regulator\n37\tWindow Seal\n38\tAntenna Assembly\n39\tAntenna Cable\n40\tRadio / Media Player / Tuner\n41\tSpeaker / Subwoofer\n42\tDash Panels / Dashboard\n43\tInstrument Cluster\n44\tCarpet / Floor Material\n45\tCenter Console\n46\tTrap (Secret Compartment)\n47\tArmrest\n48\tBench Seat / Bucket Seat\n49\tHeadrest\n50\tSeat Belt\n51\tSeat Bracket / Track\n52\tSeat Cover\n53\tChildren / Baby Car Seat\n54\tFastener / Nut / Screw / Bolt Cap / Shim / Rivet\n55\tGlove Compartment\n56\tGrab Handle (\"Oh S* Handle\")**\n57\tSun Visor\n58\tBattery\n59\tBattery Tray\n60\tCables\n61\tControl System\n62\tVoltage Regulator\n63\tAlternator\n64\tAlternator Parts\n65\tStarter\n66\tStarter Motor / Solenoid / Drive\n67\tSpark Plug\n68\tCoil\n69\tDistributor\n70\tSwitches:\n71\tAirbag Sensors\n72\tLight Sensor\n73\tKnock Sensor\n74\tFuel/Oil/Temperature/Tire Pressure Sensors\n75\tMass Flow Sensor\n76\tCamshaft / Crankshaft Position Sensors\n77\tWiring Harnesses\n78\tRelays / Fuses / Fuse Box\n79\tSpeedometer / Tachometer / Odometer / Ammeter\n80\tFuel Gauge / Water Temp Meter / Voltmeter / Vacuum Gauge\n81\tEngine Block\n82\tCylinder Head\n83\tCrankshaft\n84\tPiston\n85\tConnecting Rod\n86\tCamshaft\n87\tRocker Arm\n88\tValve (Intake / Exhaust / Spring / Cover)\n89\tTiming Belt / Drive Belt / Accessory Belt\n90\tEngine Mounting / Damper / Vibration Absorber\n91\tOil Pump / Oil Pan / Oil Filter / Oil Strainer\n92\tWater Pump\n93\tRadiator\n94\tRadiator Fan\n95\tGaskets / Seals\n96\tPCV Valve\n97\tTurbocharger / Supercharger\n98\tAir Filter / Air Intake Housing / Manifold\n99\tThrottle Body / Carburetor\n100\tGlowplug\n101\tFuel Injector / Fuel Rail / Fuel Cell\n102\tA/C Compressor\n103\tA/C Condenser\n104\tA/C Evaporator\n105\tA/C Clutch\n106\tA/C Relay\n107\tA/C Hoses\n108\tA/C Valves\n109\tA/C Cooler\n110\tA/C Expansion Valve\n111\tHeater\n112\tBlower Motor\n113\tRims\n114\tHubcap\n115\tTire/Tyre\n116\tWheel Stud\n117\tWheel Cylinder\n118\tTire Pressure Gauge\n119\tBrake Pad\n120\tBrake Disc (Rotor)\n121\tBrake Drum\n122\tBrake Shoe\n123\tCaliper\n124\tMaster Cylinder\n125\tBrake Booster\n126\tBrake Servo\n127\tParking Brake Lever (Hand Brake)\n128\tBrake Lines/Hoses\n129\tBrake Valves\n130\tABS System\n131\tBrake Sensors\n132\tProportioning Valve\n133\tMetering Valve\n134\tBrake Light\n135\tBrake Warning Indicator\n136\tControl Arm\n137\tShock Absorber\n138\tStruts\n139\tCoil/Leaf/Air Springs\n140\tStabilizer Bars (Anti-roll Bars)\n141\tTie Rod\n142\tTie Bar\n143\tPitman Arm\n144\tIdler Arm\n145\tSteering Rack\n146\tSteering Box\n147\tSteering Wheel\n148\tSteering Column Assembly\n149\tRack End\n150\tBall Joint\n151\tBushings\n152\tSpindles\n153\tKingpin\n154\tClutch Assembly\n155\tClutch Pedal\n156\tClutch Cable\n157\tClutch Disk\n158\tFlywheel\n159\tGearbox\n160\tDifferential\n161\tShift Lever\n162\tGear Stick\n163\tGear Knob\n164\tDriveshaft\n165\tProp Shaft\n166\tU-Joint (Universal Joint)\n167\tTorque Converter\n168\tTransmission Pan\n169\tAxle Shaft\n170\tOutput Shaft\n171\tGear Set\n172\tFuel Pump\n173\tFuel Filter\n174\tFuel Cap\n175\tFuel Tank\n176\tFuel Cooler\n177\tFuel Rail\n178\tFuel Water Separator\n179\tFuel Injector\n180\tFuel Lines\n181\tVapor Hose\n182\tFuel Pressure Regulator\n183\tRadiator\n184\tRadiator Cap\n185\tOverflow Tank\n186\tWater Neck\n187\tWater Pipe\n188\tWater Pump\n189\tCooling Fan\n190\tFan Clutch\n191\tFan Blades\n192\tHoses\n193\tGaskets\n194\tMuffler (Silencer)\n195\tResonator\n196\tHeat Shield\n197\tExhaust Manifold\n198\tExhaust Pipe\n199\tExhaust Gaskets\n200\tExhaust Clamps\n201\tCatalytic Converter\n202\tEGR Valve (Exhaust Gas Recirculation Valve)\n203\tGaskets (Flat, Moulded, O-rings)\n204\tDecals\n205\tLabels\n206\tName Plates\n207\tPaint\n208\tAdhesive Tape\n209\tFoil\n210\tRubber Components\n211\tMolded Parts\n212\tHorn\n213\tTrumpet Horn\n214\tShim\n215\tCotter Pin\n216\tBrackets\n217\tMinor Dents\n218\tMajor Dents\n219\tMinor Scratches\n220\tMajor Scratches\n221\tBumper Scratch\n222\tFull Repainting\n223\tFull Car Respray\n224\tClear Coat Repair\n225\tWheel Repair\n226\tHeadlight Restoration\n227\tRust Removal and Treatment\n228\tVinyl Wrap\n\nYour job is to return array of json integers that maps to each auto part in the image and return context of what you see in the damage.")
                .responseFormat(JsonValue.from(Map.of(
                        "type", "json_schema",
                        "json_schema", Map.of(
                                "name", "auto_parts_schema",
                                "schema", Map.of(
                                        "type", "object",
                                        "required", new String[]{"parts_id", "context_message"},
                                        "properties", Map.of(
                                                "parts_id", Map.of(
                                                        "type", "array",
                                                        "items", Map.of("type", "integer"),
                                                        "description", "An array of integers representing the IDs of the auto parts."
                                                ),
                                                "context_message", Map.of(
                                                        "type", "string",
                                                        "description", "A message providing context about the auto parts."
                                                )
                                        ),
                                        "additionalProperties", false
                                ),
                                "strict", true
                        )
                )))
                .temperature(0.2)
                .maxCompletionTokens(2048)
                .frequencyPenalty(0)
                .presencePenalty(0)
                .addUserMessage(content)
                .store(false)
                .build();
        try (HttpResponseFor<ChatCompletion> chatCompletion = openAiClient.chat().completions().withRawResponse().create(params)) {
            StringBuilder stringBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(chatCompletion.body()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append(System.lineSeparator());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String data = stringBuilder.toString();
            System.out.println(data);
            chatResponse = objectMapper.readValue(data, OpenAIChatResponseDAO.class);
            messageContent = chatResponse.getChoices().getFirst().getMessage().getContent();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        ChatResponseData chatResponseData = objectMapper.readValue(messageContent, ChatResponseData.class);
        System.out.println(chatResponseData.getContext_message());
        Integer reportId = damageReportRepo.createReport(0, claimId);
        RequestDamageReportDAO requestDamageReport = RequestDamageReportDAO.builder()
                .reportId(reportId)
                .claimId(claimId)
                .autoPartsAndServices(chatResponseData.getParts_id())
                .build();
        damageReportRepo.addReportDataToReport(requestDamageReport);
        return reportId;
    }
}
