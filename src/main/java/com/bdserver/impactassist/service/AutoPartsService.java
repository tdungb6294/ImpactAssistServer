package com.bdserver.impactassist.service;

import com.bdserver.impactassist.model.PartialAutoPartsAndServicesDAO;
import com.bdserver.impactassist.model.ResponseAutoPartsCategoryDAO;
import com.bdserver.impactassist.repo.AutoPartsRepo;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AutoPartsService {

    private final AutoPartsRepo autoPartsRepo;

    public AutoPartsService(AutoPartsRepo autoPartsRepo) {
        this.autoPartsRepo = autoPartsRepo;
    }

    public Map<String, Object> getAutoPartsAndServices(List<Integer> category, String search, int page, int size, String lang) {
        int offset = (page - 1) * size;
        List<PartialAutoPartsAndServicesDAO> autoParts = autoPartsRepo.getAutoPartsAndServices(category, search, offset, size, lang);
        int total = autoPartsRepo.getAutoPartsAndServicesCount(category, search, lang);
        boolean hasMore = offset + size < total;
        Map<String, Object> result = new HashMap<>();
        result.put("autoParts", autoParts);
        result.put("currentPage", page);
        result.put("nextPage", hasMore ? page + 1 : null);
        result.put("totalPages", (int) Math.ceil((double) total / (double) size));
        result.put("total", total);
        return result;
    }

    public Map<String, Object> getCategories(String search, int page, int size, String lang) {
        int offset = (page - 1) * size;
        List<ResponseAutoPartsCategoryDAO> categories = autoPartsRepo.getCategories(search, offset, size, lang);
        int total = autoPartsRepo.getCategoriesCount(search, lang);
        boolean hasMore = offset + size < total;
        Map<String, Object> result = new HashMap<>();
        result.put("autoParts", categories);
        result.put("currentPage", page);
        result.put("nextPage", hasMore ? page + 1 : null);
        result.put("totalPages", (int) Math.ceil((double) total / (double) size));
        result.put("total", total);
        return result;
    }
}
