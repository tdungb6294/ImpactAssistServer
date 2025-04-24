package com.bdserver.impactassist.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeclarationImageDAO {
    @NotNull
    private String firstImage;
    @NotNull
    private String secondImage;
}
