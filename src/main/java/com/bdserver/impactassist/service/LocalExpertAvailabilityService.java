package com.bdserver.impactassist.service;

import com.bdserver.impactassist.model.AvailabilityDAO;
import com.bdserver.impactassist.model.RequestAvailabilityDAO;
import com.bdserver.impactassist.repo.LocalExpertAvailabilityRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocalExpertAvailabilityService {
    private final LocalExpertAvailabilityRepo localExpertAvailabilityRepo;

    public LocalExpertAvailabilityService(LocalExpertAvailabilityRepo localExpertAvailabilityRepo) {
        this.localExpertAvailabilityRepo = localExpertAvailabilityRepo;
    }

    public List<AvailabilityDAO> getAvailabilitiesByExpertId(int localExpertId) {
        return localExpertAvailabilityRepo.getAvailabilitiesById(localExpertId);
    }

    public void createNewAvailability(RequestAvailabilityDAO requestAvailabilityDAO) {
        localExpertAvailabilityRepo.createAvailability(requestAvailabilityDAO);
    }

    public void deleteAvailability(int id) {
        localExpertAvailabilityRepo.deleteAvailability(id);
    }
}
