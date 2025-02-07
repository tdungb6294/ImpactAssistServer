package com.bdserver.impactassist.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LocalExpertDAO extends UserDAO {
    private double longitude;
    private double latitude;
    private String description;
}
