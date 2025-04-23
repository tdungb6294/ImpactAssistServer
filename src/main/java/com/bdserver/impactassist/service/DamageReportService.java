package com.bdserver.impactassist.service;

import com.bdserver.impactassist.model.*;
import com.bdserver.impactassist.repo.DamageReportRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DamageReportService {

    private final DamageReportRepo damageReportRepo;
    private final UserService userService;

    public DamageReportService(DamageReportRepo damageReportRepo, UserService userService) {
        this.damageReportRepo = damageReportRepo;
        this.userService = userService;
    }

    @Transactional
    public Integer createDamageReport(RequestDamageReportDAO request) {
        Integer reportId = damageReportRepo.createReport(userService.getUserId(), request.getClaimId());
        request.setReportId(reportId);
        damageReportRepo.addReportDataToReport(request);
        return reportId;
    }

    public List<PartialDamageReportDAO> getReportList(Integer claimId) {
        return damageReportRepo.getReportList(claimId);
    }

    public ResponseDamageReportDAO getReport(Integer id, String lang) {
        DamageReportDAO damageReport = damageReportRepo.getDamageReport(id);
        List<AutoPartsAndServicesDAO> autoParts;
        if (lang == null) {
            autoParts = damageReportRepo.getAutoPartsAndServicesByReportId(id);
        } else {
            autoParts = damageReportRepo.getAutoPartsAndServicesByReportIdAndLanguage(id, lang);
        }
        return ResponseDamageReportDAO.builder().damageReport(damageReport).autoPartsAndServices(autoParts).build();
    }
}
