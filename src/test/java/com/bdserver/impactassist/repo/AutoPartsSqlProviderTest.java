package com.bdserver.impactassist.repo;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AutoPartsSqlProviderTest {
    private final AutoPartsSqlProvider provider = new AutoPartsSqlProvider();

    @Test
    void testGetCategories_noLangNoSearch() {
        String sql = provider.getCategories(null, 0, 10, null);

        assertTrue(sql.contains("FROM auto_parts_and_services_categories apasc"));
        assertTrue(sql.contains("SELECT apasc.id as id, apasc.category_name as categoryName"));
        assertTrue(sql.contains("LIMIT 10"));
        assertTrue(sql.contains("OFFSET 0"));
    }

    @Test
    void testGetCategories_withLangAndSearch() {
        String sql = provider.getCategories("brakes", 5, 20, "lt");

        assertTrue(sql.contains("LEFT OUTER JOIN auto_parts_and_services_categories_translations apasct"));
        assertTrue(sql.contains("apasct.category_name ILIKE CONCAT('%', #{search}, '%')"));
        assertTrue(sql.contains("apasct.category_name ILIKE CONCAT(#{search}, '%')"));
        assertTrue(sql.contains("LIMIT 20"));
        assertTrue(sql.contains("OFFSET 5"));
    }

    @Test
    void testGetCategoriesCount_noLangNoSearch() {
        String sql = provider.getCategoriesCount(null, null);

        assertTrue(sql.contains("SELECT COUNT(*)"));
        assertTrue(sql.contains("FROM auto_parts_and_services_categories apasc"));
    }

    @Test
    void testGetCategoriesCount_withLangAndSearch() {
        String sql = provider.getCategoriesCount("engine", "en");

        assertTrue(sql.contains("LEFT OUTER JOIN auto_parts_and_services_categories_translations apasct"));
        assertTrue(sql.contains("apasct.category_name ILIKE CONCAT('%', #{search}, '%')"));
    }

    @Test
    void testGetAutoPartsAndServices_noLangNoCategoryNoSearch() {
        String sql = provider.getAutoPartsAndServices(null, null, 0, 10, null);

        assertTrue(sql.contains("FROM auto_parts_and_services apas"));
        assertTrue(sql.contains("JOIN auto_parts_and_services_categories apasc"));
        assertTrue(sql.contains("SELECT apas.id as id, apas.auto_part as autoPart"));
        assertTrue(sql.contains("LIMIT 10"));
        assertTrue(sql.contains("OFFSET 0"));
    }

    @Test
    void testGetAutoPartsAndServices_withLangCategoryAndSearch() {
        List<Integer> category = Arrays.asList(1, 2, 3);
        String sql = provider.getAutoPartsAndServices(category, "wheel", 0, 50, "en");

        assertTrue(sql.contains("apasc.id IN (1, 2, 3)"));
        assertTrue(sql.contains("LEFT OUTER JOIN auto_parts_and_services_translations apast"));
        assertTrue(sql.contains("apast.auto_part ILIKE CONCAT('%', #{search}, '%')"));
        assertTrue(sql.contains("CASE WHEN apast.auto_part ILIKE CONCAT(#{search}, '%') THEN 1 ELSE 2 END"));
        assertTrue(sql.contains("LIMIT 50"));
    }

    @Test
    void testGetAutoPartsAndServicesCount_noLangNoCategoryNoSearch() {
        String sql = provider.getAutoPartsAndServicesCount(null, null, null);

        assertTrue(sql.contains("SELECT COUNT(*)"));
        assertTrue(sql.contains("FROM auto_parts_and_services apas"));
        assertTrue(sql.contains("JOIN auto_parts_and_services_categories apasc"));
    }

    @Test
    void testGetAutoPartsAndServicesCount_withLangCategoryAndSearch() {
        List<Integer> category = Collections.singletonList(5);
        String sql = provider.getAutoPartsAndServicesCount(category, "filter", "lt");

        assertTrue(sql.contains("apasc.id IN (5)"));
        assertTrue(sql.contains("LEFT OUTER JOIN auto_parts_and_services_translations apast"));
        assertTrue(sql.contains("apast.auto_part ILIKE CONCAT('%', #{search}, '%')"));
    }
}