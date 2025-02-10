package com.bdserver.impactassist.controller;

import com.bdserver.impactassist.model.RegisterClaimDAO;
import com.bdserver.impactassist.service.ClaimService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("claim")
public class ClaimController {
    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    //TODO create repo for sql queries
    @PostMapping
    public Integer registerClaim(@RequestPart("images") List<MultipartFile> files,
                                 @RequestPart("documents") List<MultipartFile> documents,
                                 @Valid @RequestPart("data") RegisterClaimDAO registerClaimDAO) throws IOException {
        return claimService.registerClaim(files, documents, registerClaimDAO);
    }

    //TODO view claim
}
