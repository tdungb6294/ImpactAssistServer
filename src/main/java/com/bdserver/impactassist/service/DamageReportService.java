package com.bdserver.impactassist.service;

import com.bdserver.impactassist.model.*;
import com.bdserver.impactassist.repo.DamageReportRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;

@Service
public class DamageReportService {

    private final DamageReportRepo damageReportRepo;
    private final UserService userService;
    private final SpringTemplateEngine springTemplateEngine;

    public DamageReportService(DamageReportRepo damageReportRepo, UserService userService, SpringTemplateEngine springTemplateEngine) {
        this.damageReportRepo = damageReportRepo;
        this.userService = userService;
        this.springTemplateEngine = springTemplateEngine;
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
}
