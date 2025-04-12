package com.bdserver.impactassist.controller;

import com.bdserver.impactassist.model.CarClaimDAO;
import com.bdserver.impactassist.model.PartialClaimDAO;
import com.bdserver.impactassist.model.RegisterCarClaimDAO;
import com.bdserver.impactassist.service.ClaimService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
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

    @PostMapping("/car")
    public Integer registerCarClaim(@RequestPart("images") List<MultipartFile> files,
                                    @RequestPart("documents") List<MultipartFile> documents,
                                    @Valid @RequestPart("data") RegisterCarClaimDAO registerCarClaimDAO) throws IOException {
        return claimService.registerCarClaim(files, documents, registerCarClaimDAO);
    }

    @GetMapping("/car/{id}")
    public CarClaimDAO getCarClaim(@PathVariable Integer id) throws BadRequestException {
        return claimService.getCarClaim(id);
    }

    @GetMapping("/car")
    public List<PartialClaimDAO> getCarClaims() {
        return claimService.getCarClaims();
    }

    @GetMapping("/car-details/{id}")
    public ResponseEntity<MultiValueMap<String, Object>> getClaimDetails(@PathVariable Integer id) throws IOException {
        MultiValueMap<String, Object> claimDetails = claimService.getClaimDetails(id);
        return ResponseEntity.ok()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(claimDetails);
    }
}
