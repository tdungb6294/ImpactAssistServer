package com.bdserver.impactassist.controller;

import com.bdserver.impactassist.model.PartialDamageReportDAO;
import com.bdserver.impactassist.model.RequestDamageReportDAO;
import com.bdserver.impactassist.model.ResponseDamageReportDAO;
import com.bdserver.impactassist.service.DamageReportService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("damage-report")
public class DamageReportController {
    private final DamageReportService damageReportService;

    public DamageReportController(DamageReportService damageReportService) {
        this.damageReportService = damageReportService;
    }

    @PostMapping
    public Integer createDamageReport(@RequestBody @Valid RequestDamageReportDAO request) {
        return damageReportService.createDamageReport(request);
    }

    @GetMapping("/ai/{id}")
    @RateLimiter(name = "myRateLimiter")
    public Integer createDamageReportUsingAI(@PathVariable Integer id) throws IOException {
        return damageReportService.createDamageReportUsingAI(id);
    }

    @GetMapping("/claim/{claimId}")
    public List<PartialDamageReportDAO> getReportList(@PathVariable Integer claimId) {
        return damageReportService.getReportList(claimId);
    }

    @GetMapping("/{id}")
    public ResponseDamageReportDAO getReport(@PathVariable Integer id, @RequestParam(required = false) String lang) {
        return damageReportService.getReport(id, lang);
    }

    @GetMapping(value = "/generate-pdf/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generatePdf(@PathVariable Integer id, @RequestParam(required = false) String lang) {
        byte[] pdfFile = damageReportService.generatePdf(id, lang);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=damage_report.pdf");
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentLength(pdfFile.length);
        return ResponseEntity.ok().headers(headers).body(pdfFile);
    }
}
