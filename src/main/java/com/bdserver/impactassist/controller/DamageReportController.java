package com.bdserver.impactassist.controller;

import com.bdserver.impactassist.model.PartialDamageReportDAO;
import com.bdserver.impactassist.model.RequestDamageReportDAO;
import com.bdserver.impactassist.model.ResponseDamageReportDAO;
import com.bdserver.impactassist.service.DamageReportService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("damage-report")
public class DamageReportController {
    public final DamageReportService damageReportService;

    public DamageReportController(DamageReportService damageReportService) {
        this.damageReportService = damageReportService;
    }

    @PostMapping
    public Integer createDamageReport(@RequestBody @Valid RequestDamageReportDAO request) {
        return damageReportService.createDamageReport(request);
    }

    @GetMapping("/claim/{claimId}")
    public List<PartialDamageReportDAO> getReportList(@PathVariable Integer claimId) {
        return damageReportService.getReportList(claimId);
    }

    @GetMapping("/{id}")
    public ResponseDamageReportDAO getReport(@PathVariable Integer id) {
        return damageReportService.getReport(id);
    }
}
