package com.bdserver.impactassist.controller;

import com.bdserver.impactassist.model.ResponseLocalExpertDAO;
import com.bdserver.impactassist.service.LocalExpertService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("local_expert")
public class LocalExpertController {
    private final LocalExpertService localExpertService;

    public LocalExpertController(LocalExpertService localExpertService) {
        this.localExpertService = localExpertService;
    }

    @GetMapping
    List<ResponseLocalExpertDAO> getLocalExperts() {
        return localExpertService.getLocalExpertList();
    }
}
