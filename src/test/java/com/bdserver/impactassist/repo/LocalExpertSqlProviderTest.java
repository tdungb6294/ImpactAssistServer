package com.bdserver.impactassist.repo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocalExpertSqlProviderTest {
    private final LocalExpertSqlProvider provider = new LocalExpertSqlProvider();

    @Test
    void testGetLocalExperts_noSearch() {
        String sql = provider.getLocalExperts(null);

        assertTrue(sql.contains("FROM local_experts le"));
        assertTrue(sql.contains("JOIN users u ON le.user_id=u.id"));
        assertFalse(sql.toLowerCase().contains("where"));
    }

    @Test
    void testGetLocalExperts_withSearch() {
        String sql = provider.getLocalExperts("john");

        assertTrue(sql.contains("full_name ILIKE CONCAT('%', #{search}, '%')"));
    }
}