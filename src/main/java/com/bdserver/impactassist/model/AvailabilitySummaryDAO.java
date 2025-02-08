package com.bdserver.impactassist.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AvailabilitySummaryDAO {
    private List<AvailabilityDAO> slots;
    private List<UnavailabilityDAO> unavailableSlots;
}
