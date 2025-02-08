package com.bdserver.impactassist.service;

import com.bdserver.impactassist.model.AvailabilityDAO;
import com.bdserver.impactassist.model.AvailabilitySummaryDAO;
import com.bdserver.impactassist.model.RequestAvailabilityDAO;
import com.bdserver.impactassist.model.UnavailabilityDAO;
import com.bdserver.impactassist.repo.LocalExpertAvailabilityRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocalExpertAvailabilityService {
    private final LocalExpertAvailabilityRepo localExpertAvailabilityRepo;

    public LocalExpertAvailabilityService(LocalExpertAvailabilityRepo localExpertAvailabilityRepo) {
        this.localExpertAvailabilityRepo = localExpertAvailabilityRepo;
    }

    public AvailabilitySummaryDAO getAvailabilitiesByExpertId(int localExpertId) {
        List<AvailabilityDAO> availabilities = localExpertAvailabilityRepo.getAvailabilitiesById(localExpertId);
        List<UnavailabilityDAO> unavailability = localExpertAvailabilityRepo.getUnavailableAppointments(localExpertId);
        return new AvailabilitySummaryDAO(availabilities, unavailability);
    }

    public void createNewAvailability(RequestAvailabilityDAO requestAvailabilityDAO) {
        localExpertAvailabilityRepo.createAvailability(requestAvailabilityDAO);
    }

    public void deleteAvailability(int id) {
        localExpertAvailabilityRepo.deleteAvailability(id);
    }
}
