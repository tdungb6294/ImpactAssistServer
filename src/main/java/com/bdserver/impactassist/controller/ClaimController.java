package com.bdserver.impactassist.controller;

import com.bdserver.impactassist.model.ClaimDAO;
import com.bdserver.impactassist.model.PartialClaimDAO;
import com.bdserver.impactassist.model.RegisterClaimDAO;
import com.bdserver.impactassist.service.ClaimService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping
    public Integer registerClaim(@RequestPart("images") List<MultipartFile> files,
                                 @RequestPart("documents") List<MultipartFile> documents,
                                 @Valid @RequestPart("data") RegisterClaimDAO registerClaimDAO) throws IOException {
        return claimService.registerClaim(files, documents, registerClaimDAO);
    }

    @GetMapping("/{id}")
    public ClaimDAO getClaim(@PathVariable Integer id) throws BadRequestException {
        return claimService.getClaim(id);
    }

    @GetMapping
    public List<PartialClaimDAO> getClaims() {
        return claimService.getClaims();
    }
}
