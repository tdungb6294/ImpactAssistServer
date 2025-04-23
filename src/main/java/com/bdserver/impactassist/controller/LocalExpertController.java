package com.bdserver.impactassist.controller;

import com.bdserver.impactassist.model.ResponseLocalExpertDAO;
import com.bdserver.impactassist.service.LocalExpertService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("local_expert")
public class LocalExpertController {
    private final LocalExpertService localExpertService;

    public LocalExpertController(LocalExpertService localExpertService) {
        this.localExpertService = localExpertService;
    }

    @GetMapping
    List<ResponseLocalExpertDAO> getLocalExperts(@RequestParam(required = false) String search) {
        return localExpertService.getLocalExpertList(search);
    }

    @GetMapping("/{id}")
    ResponseLocalExpertDAO getLocalExpert(@PathVariable int id) {
        return localExpertService.getLocalExpertById(id);
    }
}
