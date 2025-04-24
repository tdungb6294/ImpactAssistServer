package com.bdserver.impactassist.controller;

import com.bdserver.impactassist.model.DeclarationImageDAO;
import com.bdserver.impactassist.model.RequestDeclarationDAO;
import com.bdserver.impactassist.service.DeclarationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("declaration")
public class DeclarationController {

    private final DeclarationService declarationService;

    public DeclarationController(DeclarationService declarationService) {
        this.declarationService = declarationService;
    }

    @PostMapping(produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getDeclarationPdf(@RequestPart("image") @Valid DeclarationImageDAO image, @Valid @RequestPart("declaration") RequestDeclarationDAO declaration) {
        byte[] pdfFile = declarationService.generatePdf(image, declaration);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=damage_report.pdf");
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentLength(pdfFile.length);
        return ResponseEntity.ok().headers(headers).body(pdfFile);
    }
}
