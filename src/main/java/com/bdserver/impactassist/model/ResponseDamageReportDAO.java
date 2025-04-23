package com.bdserver.impactassist.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ResponseDamageReportDAO {
    private DamageReportDAO damageReport;
    private List<AutoPartsAndServicesDAO> autoPartsAndServices;
}
