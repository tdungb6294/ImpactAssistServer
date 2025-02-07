package com.bdserver.impactassist.controller;

import com.bdserver.impactassist.model.AvailabilityDAO;
import com.bdserver.impactassist.model.RequestAvailabilityDAO;
import com.bdserver.impactassist.service.LocalExpertAvailabilityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("local_expert_availability")
public class LocalExpertAvailabilityController {
    private final LocalExpertAvailabilityService localExpertAvailabilityService;

    public LocalExpertAvailabilityController(LocalExpertAvailabilityService localExpertAvailabilityService) {
        this.localExpertAvailabilityService = localExpertAvailabilityService;
    }

    @PostMapping()
    void createAvailability(@RequestBody RequestAvailabilityDAO requestAvailabilityDAO) {
        this.localExpertAvailabilityService.createNewAvailability(requestAvailabilityDAO);
    }

    @DeleteMapping("/{id}")
    void deleteAvailability(@PathVariable int id) {
        this.localExpertAvailabilityService.deleteAvailability(id);
    }

    @GetMapping("/{id}")
    List<AvailabilityDAO> getAvailability(@PathVariable int id) {
        return localExpertAvailabilityService.getAvailabilitiesByExpertId(id);
    }
}
