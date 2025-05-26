package com.bdserver.impactassist.service;

import com.bdserver.impactassist.model.RequestCompensationDAO;
import com.bdserver.impactassist.model.ResponseDamageReportDAO;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

@Service
public class CompensationService {
    private final SpringTemplateEngine springTemplateEngine;
    private final DamageReportService damageReportService;
    private final UserService userService;

    public CompensationService(SpringTemplateEngine springTemplateEngine, DamageReportService damageReportService, UserService userService) {
        this.springTemplateEngine = springTemplateEngine;
        this.damageReportService = damageReportService;
        this.userService = userService;
    }


    public byte[] generatePdf(RequestCompensationDAO request) {
        int userId = userService.getUserId();
        ResponseDamageReportDAO damageReport = damageReportService.getReport(request.getReportId(), null);
        damageReport.getDamageReport().setFullName(userService.getUserDataById(userId).getFullName());
        Context context = new Context();
        context.setVariable("date", LocalDate.now());
        context.setVariable("fullName", damageReport.getDamageReport().getFullName());
        context.setVariable("estimatedMinPriceWithoutService", damageReport.getDamageReport().getEstimatedMinPriceWithoutService());
        context.setVariable("estimatedMaxPriceWithoutService", damageReport.getDamageReport().getEstimatedMaxPriceWithoutService());
        context.setVariable("estimatedMinPriceWithService", damageReport.getDamageReport().getEstimatedMinPriceWithService());
        context.setVariable("estimatedMaxPriceWithService", damageReport.getDamageReport().getEstimatedMaxPriceWithService());
        context.setVariable("autoPartsAndServices", damageReport.getAutoPartsAndServices());
        context.setVariable("compensationAmount", request.getCompensationAmount());
        String htmlContent = "";
        htmlContent = springTemplateEngine.process("pdf_compensation_template", context);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        renderer.createPDF(outputStream);
        return outputStream.toByteArray();
    }
}
