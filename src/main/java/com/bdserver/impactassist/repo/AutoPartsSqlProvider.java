package com.bdserver.impactassist.repo;

import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.stream.Collectors;

public class AutoPartsSqlProvider {
    public String getCategories(String search, int offset, int limit, String lang) {
        return new SQL() {{
            SELECT("apasc.id as id");
            if (lang != null) {
                SELECT("apasct.category_name as categoryName");
            } else {
                SELECT("apasc.category_name as categoryName");
            }
            FROM("auto_parts_and_services_categories apasc");
            if (lang != null) {
                LEFT_OUTER_JOIN("auto_parts_and_services_categories_translations apasct ON apasct.language_code=#{lang} AND apasc.id=apasct.id");
                if (search != null && !search.isEmpty()) {
                    WHERE("apasct.category_name ILIKE CONCAT('%', #{search}, '%')");
                    ORDER_BY("CASE WHEN apasct.category_name ILIKE CONCAT(#{search}, '%') THEN 1 ELSE 2 END, apasct.id");
                }
            } else {
                if (search != null && !search.isEmpty()) {
                    WHERE("apasc.category_name ILIKE CONCAT('%', #{search}, '%')");
                    ORDER_BY("CASE WHEN apasc.category_name ILIKE CONCAT(#{search}, '%') THEN 1 ELSE 2 END, apasc.id");
                }
            }
            LIMIT(limit);
            OFFSET(offset);
        }}.toString();
    }

    public String getCategoriesCount(String search, String lang) {
        return new SQL() {{
            SELECT("COUNT(*)");
            FROM("auto_parts_and_services_categories apasc");
            if (lang != null) {
                LEFT_OUTER_JOIN("auto_parts_and_services_categories_translations apasct ON apasct.language_code=#{lang} AND apasc.id=apasct.id");
                if (search != null && !search.isEmpty()) {
                    WHERE("apasct.category_name ILIKE CONCAT('%', #{search}, '%')");
                }
            } else {
                if (search != null && !search.isEmpty()) {
                    WHERE("apasc.category_name ILIKE CONCAT('%', #{search}, '%')");
                }
            }
        }}.toString();
    }

    public String getAutoPartsAndServices(List<Integer> category, String search, int offset, int limit, String lang) {
        return new SQL() {{
            SELECT("apas.id as id");
            if (lang != null) {
                SELECT("apast.auto_part as autoPart");
            } else {
                SELECT("apas.auto_part as autoPart");
            }
            FROM("auto_parts_and_services apas");
            JOIN("auto_parts_and_services_categories apasc ON apas.category_id = apasc.id");
            if (category != null && !category.isEmpty()) {
                String inClause = "apasc.id IN " + category.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(", ", "(", ")"));
                WHERE(inClause);
            }
            if (lang != null) {
                LEFT_OUTER_JOIN("auto_parts_and_services_translations apast ON apast.language_code=#{lang} AND apas.id=apast.id");
                if (search != null && !search.isEmpty()) {
                    WHERE("apast.auto_part ILIKE CONCAT('%', #{search}, '%')");
                    OR();
                    WHERE("CASE WHEN #{lang}='lt' THEN to_tsvector('lithuanian', apast.description) @@ plainto_tsquery('lithuanian', #{search}) ELSE to_tsvector('english', apas.description) @@ plainto_tsquery('english', #{search}) END");
                    ORDER_BY("CASE WHEN apast.auto_part ILIKE CONCAT(#{search}, '%') THEN 1 ELSE 2 END, apast.id");
                }
            } else {
                if (search != null && !search.isEmpty()) {
                    WHERE("apas.auto_part ILIKE CONCAT('%', #{search}, '%')");
                    OR();
                    WHERE("to_tsvector('english', apas.description) @@ plainto_tsquery('english', #{search})");
                    ORDER_BY("CASE WHEN apas.auto_part ILIKE CONCAT(#{search}, '%') THEN 1 ELSE 2 END, apas.id");
                }
            }
            LIMIT(limit);
            OFFSET(offset);
        }}.toString();
    }

    public String getAutoPartsAndServicesCount(List<Integer> category, String search, String lang) {
        return new SQL() {{
            SELECT("COUNT(*)");
            FROM("auto_parts_and_services apas");
            JOIN("auto_parts_and_services_categories apasc ON apas.category_id = apasc.id");
            if (category != null && !category.isEmpty()) {
                String inClause = "apasc.id IN " + category.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(", ", "(", ")"));
                System.out.println(inClause);
                WHERE(inClause);
            }
            if (lang != null) {
                LEFT_OUTER_JOIN("auto_parts_and_services_translations apast ON apast.language_code=#{lang} AND apas.id=apast.id");
                if (search != null && !search.isEmpty()) {
                    WHERE("apast.auto_part ILIKE CONCAT('%', #{search}, '%')");
                    OR();
                    WHERE("CASE WHEN #{lang}='lt' THEN to_tsvector('lithuanian', apast.description) @@ plainto_tsquery('lithuanian', #{search}) ELSE to_tsvector('english', apas.description) @@ plainto_tsquery('english', #{search}) END");
                }
            } else {
                if (search != null && !search.isEmpty()) {
                    WHERE("apas.auto_part ILIKE CONCAT('%', #{search}, '%')");
                    OR();
                    WHERE("to_tsvector('english', apas.description) @@ plainto_tsquery('english', #{search})");
                }
            }
        }}.toString();
    }
}
