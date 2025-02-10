package com.bdserver.impactassist.service;

import com.bdserver.impactassist.model.*;
import com.bdserver.impactassist.repo.ClaimRepo;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ClaimService {
    private final S3Service s3Service;
    private final ClaimRepo claimRepo;
    private final UserService userService;

    public ClaimService(S3Service s3Service, ClaimRepo claimRepo, UserService userService) {
        this.s3Service = s3Service;
        this.claimRepo = claimRepo;
        this.userService = userService;
    }

    @Transactional
    public Integer registerClaim(List<MultipartFile> images, List<MultipartFile> documents, RegisterClaimDAO registerClaimDAO) throws IOException {
        if (registerClaimDAO.getDocumentTypes().size() != documents.size()) {
            throw new BadRequestException();
        }
        List<ClaimImageDAO> claimImages = new ArrayList<>();
        List<ClaimDocumentDAO> claimDocuments = new ArrayList<>();
        for (MultipartFile image : images) {
            UUID uuid = UUID.randomUUID();
            claimImages.add(new ClaimImageDAO(uuid, image.getOriginalFilename()));
            s3Service.uploadFile(image, uuid);
        }
        AtomicInteger index = new AtomicInteger();
        documents.forEach(document -> {
            UUID uuid = UUID.randomUUID();
            claimDocuments.add(new ClaimDocumentDAO(uuid, document.getOriginalFilename(),
                    registerClaimDAO.getDocumentTypes().get(index.getAndIncrement())));
            try {
                s3Service.uploadFile(document, uuid);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        registerClaimDAO.setUserId(userService.getUserId());
        Integer claimId = claimRepo.createNewClaim(registerClaimDAO);
        for (ClaimImageDAO claimImage : claimImages) {
            claimRepo.addClaimImage(claimImage, claimId);
        }
        for (ClaimDocumentDAO claimDocument : claimDocuments) {
            claimRepo.addClaimDocument(claimDocument, claimId);
        }
        return claimId;
    }

    private List<PartialClaimDocumentDAO> getClaimAccidentDocumentUrls(int claimId) {
        List<PartialClaimDocumentDAO> claimAccidentDocumentUrls = new ArrayList<>();
        List<PartialClaimDocumentDAO> claimAccidentDocuments = claimRepo.getClaimAccidentDocumentNames(claimId);
        for (PartialClaimDocumentDAO claimDocument : claimAccidentDocuments) {
            String signedUrl = String.valueOf(s3Service.getSignedUrl(claimDocument.getUrl()));
            claimAccidentDocumentUrls.add(new PartialClaimDocumentDAO(signedUrl, claimDocument.getDocumentType()));
        }
        return claimAccidentDocumentUrls;
    }

    private List<String> getClaimAccidentImageUrls(int claimId) {
        List<String> claimAccidentImageUrls = new ArrayList<>();
        List<String> claimAccidentImageNames = claimRepo.getClaimAccidentImageNames(claimId);
        for (String claimAccidentImageName : claimAccidentImageNames) {
            String signedUrl = String.valueOf(s3Service.getSignedUrl(claimAccidentImageName));
            claimAccidentImageUrls.add(signedUrl);
        }
        return claimAccidentImageUrls;
    }

    public ClaimDAO getClaim(int claimId) throws BadRequestException {
        ClaimDAO claimDAO = claimRepo.getClaimById(claimId);
        if (claimDAO == null) {
            throw new BadRequestException("No claim with this claim id.");
        }
        claimDAO.setClaimAccidentImageUrls(getClaimAccidentImageUrls(claimId));
        claimDAO.setClaimAccidentDocuments(getClaimAccidentDocumentUrls(claimId));
        return claimDAO;
    }

    public List<PartialClaimDAO> getClaims() {
        return claimRepo.getClaimsByUserId(userService.getUserId());
    }
}
