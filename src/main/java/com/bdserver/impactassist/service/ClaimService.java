package com.bdserver.impactassist.service;

import com.bdserver.impactassist.model.ClaimDocumentDAO;
import com.bdserver.impactassist.model.ClaimImageDAO;
import com.bdserver.impactassist.model.RegisterClaimDAO;
import com.bdserver.impactassist.repo.ClaimRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ClaimService {
    private final S3Service s3Service;
    private final ClaimRepo claimRepo;

    public ClaimService(S3Service s3Service, ClaimRepo claimRepo) {
        this.s3Service = s3Service;
        this.claimRepo = claimRepo;
    }

    @Transactional
    public Integer registerClaim(List<MultipartFile> images, List<MultipartFile> documents, RegisterClaimDAO registerClaimDAO) throws IOException {
        List<ClaimImageDAO> claimImages = new ArrayList<>();
        List<ClaimDocumentDAO> claimDocuments = new ArrayList<>();
        for (MultipartFile image : images) {
            UUID uuid = UUID.randomUUID();
            claimImages.add(new ClaimImageDAO(uuid, image.getOriginalFilename()));
            s3Service.uploadFile(image, uuid);
        }
        for (MultipartFile document : documents) {
            UUID uuid = UUID.randomUUID();
            claimDocuments.add(new ClaimDocumentDAO(uuid, document.getOriginalFilename(), "declaration"));
            s3Service.uploadFile(document, uuid);
        }
        Integer claimId = claimRepo.createNewClaim(registerClaimDAO);
        claimRepo.addClaimImages(claimImages, claimId);
        claimRepo.addClaimDocuments(claimDocuments, claimId);
        return claimId;
    }
}
