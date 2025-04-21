package com.bdserver.impactassist.controller;

import com.bdserver.impactassist.service.AutoPartsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("auto-parts")
public class AutoPartsController {

    private final AutoPartsService autoPartsService;

    public AutoPartsController(AutoPartsService autoPartsService) {
        this.autoPartsService = autoPartsService;
    }

    @GetMapping
    public Map<String, Object> getAutoPartsAndServices(@RequestParam(required = false) List<Integer> category,
                                                       @RequestParam(defaultValue = "1") int page,
                                                       @RequestParam(defaultValue = "5") int size,
                                                       @RequestParam(required = false) String search,
                                                       @RequestParam(required = false) String lang) {
        return autoPartsService.getAutoPartsAndServices(category, search, page, size, lang);
    }

    @GetMapping("/category")
    public Map<String, Object> getCategories(@RequestParam(required = false) String search,
                                             @RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "5") int size,
                                             @RequestParam(required = false) String lang) {
        return autoPartsService.getCategories(search, page, size, lang);
    }
}
