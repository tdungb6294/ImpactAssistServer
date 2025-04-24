package com.bdserver.impactassist.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequestDeclarationDAO {
    @NotNull
    private LocalDateTime datetime;
    @NotNull
    private String accidentCountryLocation;
    @NotNull
    private String peopleInjuries;
    @NotNull
    private boolean damageToCars;
    @NotNull
    private boolean damageToObjects;
    @NotNull
    private String witnesses;
    @NotNull
    private LatLngDAO accidentLatLng;
    @NotNull
    private CarDetailsDAO firstCar;
    @NotNull
    private CarDetailsDAO secondCar;
    @NotNull
    private CulpritDAO culprit;
}