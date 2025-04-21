package com.bdserver.impactassist.service;

import com.bdserver.impactassist.model.*;
import com.bdserver.impactassist.repo.ClaimRepo;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
    public Integer registerCarClaim(List<MultipartFile> images, List<MultipartFile> documents, RegisterCarClaimDAO registerCarClaimDAO) throws IOException {
        if (registerCarClaimDAO.getDocumentTypes().size() != documents.size()) {
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
                    registerCarClaimDAO.getDocumentTypes().get(index.getAndIncrement())));
            try {
                s3Service.uploadFile(document, uuid);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        Integer claimId = claimRepo.createNewClaim(new ClaimDAO(userService.getUserId(), "Vehicle", ClaimStatus.PENDING));
        registerCarClaimDAO.setId(claimId);
        claimRepo.createNewCarClaim(registerCarClaimDAO);
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

    public CarClaimDAO getCarClaim(int claimId) throws BadRequestException {
        CarClaimDAO carClaimDAO = claimRepo.getCarClaimById(claimId);
        if (carClaimDAO == null) {
            throw new BadRequestException("No claim with this claim id.");
        }
        carClaimDAO.setClaimAccidentImageUrls(getClaimAccidentImageUrls(claimId));
        carClaimDAO.setClaimAccidentDocuments(getClaimAccidentDocumentUrls(claimId));
        return carClaimDAO;
    }

    public List<PartialCarClaimDAO> getCarClaims() {
        return claimRepo.getCarClaimsByUserId(userService.getUserId());
    }

    public Map<String, Object> getCarClaims(int page, int size) {
        int offset = (page - 1) * size;
        int userId = userService.getUserId();
        List<PartialCarClaimDAO> claims = claimRepo.getPagedCarClaimsByUserId(userId, offset, size);
        int total = claimRepo.getCarClaimsCount(userId);
        boolean hasMore = offset + size < total;
        Map<String, Object> result = new HashMap<>();
        result.put("claims", claims);
        result.put("currentPage", page);
        result.put("nextPage", hasMore ? page + 1 : null);
        result.put("totalPages", (int) Math.ceil((double) total / (double) size));
        result.put("total", total);
        return result;
    }

    public MultiValueMap<String, Object> getClaimDetails(int claimId) throws IOException {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        CarClaimMultipartDAO carClaim = claimRepo.getCarClaimDetailsById(claimId);
        if (carClaim == null) {
            throw new BadRequestException("No claim with this claim id.");
        }
        List<PartialClaimDocumentDAO> claimAccidentDocumentNames = claimRepo.getClaimAccidentDocumentNames(claimId);
        List<String> claimAccidentImages = claimRepo.getClaimAccidentImageNames(claimId);
        List<String> claimAccidentDocuments = claimAccidentDocumentNames.stream().map(PartialClaimDocumentDAO::getUrl).collect(Collectors.toList());
        carClaim.setClaimAccidentDocuments(claimAccidentDocuments);
        carClaim.setClaimAccidentImages(claimAccidentImages);
        Map<String, Object> jsonData = Map.of("carClaim", carClaim);
        body.add("carClaim", jsonData);
        for (String document : claimAccidentDocuments) {
            byte[] file = s3Service.downloadFile(document);
            String[] documentParts = document.split("_");
            body.add(documentParts.length > 1 ? documentParts[1] : document, file);
        }
        for (String image : claimAccidentImages) {
            byte[] file = s3Service.downloadFile(image);
            String[] imageParts = image.split("_");
            body.add(imageParts.length > 1 ? imageParts[1] : image, file);
        }
        return body;
    }

    public Map<String, Object> getClaims(int page, int size, List<ClaimStatus> status) {
        int offset = (page - 1) * size;
        int userId = userService.getUserId();
        List<PartialClaimDAO> claims = claimRepo.getFilteredPagedClaimsByUserId(userId, offset, size, status);
        int total = claimRepo.getFilteredPagedClaimCountByUserId(userId, status);
        boolean hasMore = offset + size < total;
        Map<String, Object> result = new HashMap<>();
        result.put("claims", claims);
        result.put("currentPage", page);
        result.put("nextPage", hasMore ? page + 1 : null);
        result.put("totalPages", (int) Math.ceil((double) total / (double) size));
        result.put("total", total);
        return result;
    }

    public ObjectClaimDAO getObjectClaim(Integer claimId) throws BadRequestException {
        ObjectClaimDAO objectClaimDAO = claimRepo.getObjectClaimById(claimId);
        if (objectClaimDAO == null) {
            throw new BadRequestException("No claim with this claim id.");
        }
        objectClaimDAO.setClaimAccidentImageUrls(getClaimAccidentImageUrls(claimId));
        objectClaimDAO.setClaimAccidentDocuments(getClaimAccidentDocumentUrls(claimId));
        return objectClaimDAO;
    }

    @Transactional
    public Integer registerObjectClaim(List<MultipartFile> images, List<MultipartFile> documents, RegisterObjectClaimDAO registerObjectClaimDAO) throws IOException {
        if (registerObjectClaimDAO.getDocumentTypes().size() != documents.size()) {
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
                    registerObjectClaimDAO.getDocumentTypes().get(index.getAndIncrement())));
            try {
                s3Service.uploadFile(document, uuid);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        Integer claimId = claimRepo.createNewClaim(new ClaimDAO(userService.getUserId(), "Object", ClaimStatus.PENDING));
        registerObjectClaimDAO.setId(claimId);
        claimRepo.createNewObjectClaim(registerObjectClaimDAO);
        for (ClaimImageDAO claimImage : claimImages) {
            claimRepo.addClaimImage(claimImage, claimId);
        }
        for (ClaimDocumentDAO claimDocument : claimDocuments) {
            claimRepo.addClaimDocument(claimDocument, claimId);
        }
        return claimId;
    }

    public Map<String, Object> getSharedClaims(int page, int size, List<ClaimStatus> status) {
        int offset = (page - 1) * size;
        int userId = userService.getUserId();
        List<PartialClaimDAO> claims = claimRepo.getFilteredPagedClaimsByLocalExpertId(userId, offset, size, status);
        int total = claimRepo.getFilteredPagedClaimCountByLocalExpertId(userId, status);
        boolean hasMore = offset + size < total;
        Map<String, Object> result = new HashMap<>();
        result.put("claims", claims);
        result.put("currentPage", page);
        result.put("nextPage", hasMore ? page + 1 : null);
        result.put("totalPages", (int) Math.ceil((double) total / (double) size));
        result.put("total", total);
        return result;
    }

    public int shareClaim(RequestShareClaimDAO share) {
        claimRepo.shareClaim(share);
        return 0;
    }
}
