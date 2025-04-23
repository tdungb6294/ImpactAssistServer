package com.bdserver.impactassist.service;

import com.bdserver.impactassist.model.ResponseLocalExpertDAO;
import com.bdserver.impactassist.repo.LocalExpertRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocalExpertService {
    private final LocalExpertRepo localExpertRepo;

    public LocalExpertService(LocalExpertRepo localExpertRepo) {
        this.localExpertRepo = localExpertRepo;
    }

    public List<ResponseLocalExpertDAO> getLocalExpertList(String search) {
        return localExpertRepo.getLocalExpertList(search);
    }

    public ResponseLocalExpertDAO getLocalExpertById(int id) {
        return localExpertRepo.getLocalExpertById(id);
    }
}
