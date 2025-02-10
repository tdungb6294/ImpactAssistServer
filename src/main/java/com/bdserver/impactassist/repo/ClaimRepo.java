package com.bdserver.impactassist.repo;

import com.bdserver.impactassist.model.ClaimDocumentDAO;
import com.bdserver.impactassist.model.ClaimImageDAO;
import com.bdserver.impactassist.model.RegisterClaimDAO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ClaimRepo {
    Integer createNewClaim(RegisterClaimDAO registerClaimDAO);

    void addClaimImages(List<ClaimImageDAO> claimImages, Integer claimId);

    void addClaimDocuments(List<ClaimDocumentDAO> claimDocuments, Integer claimId);
}
