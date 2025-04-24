package com.bdserver.impactassist.service;

import com.bdserver.impactassist.model.DeclarationImageDAO;
import com.bdserver.impactassist.model.RequestDeclarationDAO;
import com.bdserver.impactassist.utils.MyStringUtils;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;

@Service
public class DeclarationService {
    private final SpringTemplateEngine springTemplateEngine;

    public DeclarationService(SpringTemplateEngine springTemplateEngine) {
        this.springTemplateEngine = springTemplateEngine;
    }

    public byte[] generatePdf(DeclarationImageDAO image, @Valid RequestDeclarationDAO declaration) {
        Context context = new Context();
        String newFirstCarCircumstance = declaration.getFirstCar().getCircumstance();
        String newSecondCarCircumstance = declaration.getSecondCar().getCircumstance();
        declaration.getFirstCar().setCircumstance(MyStringUtils.camelCaseToSpacedWords(newFirstCarCircumstance));
        declaration.getSecondCar().setCircumstance(MyStringUtils.camelCaseToSpacedWords(newSecondCarCircumstance));
        context.setVariable("image", image);
        context.setVariable("declaration", declaration);
        String htmlContent = springTemplateEngine.process("declaration_template", context);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        renderer.createPDF(outputStream);
        return outputStream.toByteArray();
    }
}
