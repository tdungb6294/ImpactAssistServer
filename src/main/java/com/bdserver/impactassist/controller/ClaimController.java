package com.bdserver.impactassist.controller;

import com.bdserver.impactassist.model.*;
import com.bdserver.impactassist.service.ClaimService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("claim")
public class ClaimController {
    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @PostMapping("/car")
    public Integer registerCarClaim(@RequestPart("images") List<MultipartFile> files,
                                    @RequestPart("documents") List<MultipartFile> documents,
                                    @Valid @RequestPart("data") RegisterCarClaimDAO registerCarClaimDAO) throws IOException {
        return claimService.registerCarClaim(files, documents, registerCarClaimDAO);
    }

    @PostMapping("/object")
    public Integer registerObjectClaim(@RequestPart("images") List<MultipartFile> files,
                                       @RequestPart("documents") List<MultipartFile> documents,
                                       @Valid @RequestPart("data") RegisterObjectClaimDAO registerObjectClaimDAO) throws IOException {
        return claimService.registerObjectClaim(files, documents, registerObjectClaimDAO);
    }

    @GetMapping("/car/{id}")
    public CarClaimDAO getCarClaim(@PathVariable Integer id) throws BadRequestException {
        return claimService.getCarClaim(id);
    }

    @GetMapping("/car")
    public Map<String, Object> getCarClaims(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        return claimService.getCarClaims(page, size);
    }

    @GetMapping("/car-details/{id}")
    public ResponseEntity<MultiValueMap<String, Object>> getClaimDetails(@PathVariable Integer id) throws IOException {
        MultiValueMap<String, Object> claimDetails = claimService.getClaimDetails(id);
        return ResponseEntity.ok()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(claimDetails);
    }

    @GetMapping
    public Map<String, Object> getClaims(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) List<ClaimStatus> status) {
        return claimService.getClaims(page, size, status);
    }

    @GetMapping("/object/{id}")
    public ObjectClaimDAO getObjectClaim(@PathVariable Integer id) throws BadRequestException {
        return claimService.getObjectClaim(id);
    }

    @PreAuthorize("hasAuthority('LOCAL_EXPERT')")
    @GetMapping("/local-expert")
    public Map<String, Object> getPartialClaimsLocalExpert(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) List<ClaimStatus> status) {
        return claimService.getSharedClaims(page, size, status);
    }

    @PutMapping("/share")
    public int shareClaimWithLocalExpert(@RequestBody @Valid RequestShareClaimDAO share) {
        return claimService.shareClaim(share);
    }

    @PreAuthorize("hasAuthority('INSURANCE_COMPANY')")
    @PutMapping
    public void updateClaimStatus(@RequestBody @Valid RequestUpdateClaimStatus updateClaimStatus) {
        claimService.updateClaimStatus(updateClaimStatus);
    }
}
