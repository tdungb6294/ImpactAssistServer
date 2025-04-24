package com.bdserver.impactassist.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LatLngDAO {
    @NotNull
    private double latitude;
    @NotNull
    private double longitude;
}
