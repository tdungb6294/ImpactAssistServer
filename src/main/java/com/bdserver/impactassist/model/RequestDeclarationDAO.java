package com.bdserver.impactassist.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequestDeclarationDAO {
    @NotNull
    private LocalDateTime datetime;
    @NotBlank
    private String accidentCountryLocation;
    @NotBlank
    private String peopleInjuries;
    @NotNull
    private boolean damageToCars;
    @NotNull
    private boolean damageToObjects;
    @NotBlank
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