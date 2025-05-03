package com.bdserver.impactassist.repo;

import com.bdserver.impactassist.model.ClaimStatus;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClaimSqlProviderTest {
    private final ClaimSqlProvider provider = new ClaimSqlProvider();

    @Test
    void testGetFilteredPagedClaimsByUserId_noStatus() {
        String sql = provider.getFilteredPagedClaimsByUserId(10, 0, 10, null);

        assertTrue(sql.contains("FROM claims c"));
        assertTrue(sql.contains("c.user_id = #{userId}"));
        assertTrue(sql.contains("c.claim_status != 'REJECTED'"));
        assertTrue(sql.contains("ORDER BY CASE c.claim_status"));
        assertTrue(sql.contains("LIMIT #{limit}"));
        assertTrue(sql.contains("OFFSET #{offset}"));
    }

    @Test
    void testGetFilteredPagedClaimsByUserId_withStatus() {
        List<ClaimStatus> status = Arrays.asList(ClaimStatus.APPROVED, ClaimStatus.PENDING);
        String sql = provider.getFilteredPagedClaimsByUserId(10, 0, 10, status);

        assertTrue(sql.contains("c.claim_status IN ('APPROVED', 'PENDING')"));
    }

    @Test
    void testGetFilteredPagedClaimCountByUserId_noStatus() {
        String sql = provider.getFilteredPagedClaimCountByUserId(15, null);

        assertTrue(sql.contains("SELECT COUNT(*)"));
        assertTrue(sql.contains("c.user_id = #{userId}"));
        assertTrue(sql.contains("c.claim_status != 'REJECTED'"));
    }

    @Test
    void testGetFilteredPagedClaimCountByUserId_withStatus() {
        List<ClaimStatus> status = Collections.singletonList(ClaimStatus.REJECTED);
        String sql = provider.getFilteredPagedClaimCountByUserId(15, status);

        assertTrue(sql.contains("c.claim_status IN ('REJECTED')"));
    }

    @Test
    void testGetFilteredPagedClaimsByLocalExpertId_noStatus() {
        String sql = provider.getFilteredPagedClaimsByLocalExpertId(20, 5, 15, null);

        assertTrue(sql.contains("FROM claims c"));
        assertTrue(sql.contains("c.shared_id = #{userId}"));
        assertTrue(sql.contains("c.claim_status != 'REJECTED'"));
        assertTrue(sql.contains("ORDER BY CASE c.claim_status"));
        assertTrue(sql.contains("LIMIT #{limit}"));
        assertTrue(sql.contains("OFFSET #{offset}"));
    }

    @Test
    void testGetFilteredPagedClaimsByLocalExpertId_withStatus() {
        List<ClaimStatus> status = Arrays.asList(ClaimStatus.PENDING, ClaimStatus.APPROVED);
        String sql = provider.getFilteredPagedClaimsByLocalExpertId(20, 5, 15, status);

        assertTrue(sql.contains("c.claim_status IN ('PENDING', 'APPROVED')"));
    }

    @Test
    void testGetFilteredPagedClaimCountByLocalExpertId_noStatus() {
        String sql = provider.getFilteredPagedClaimCountByLocalExpertId(25, null);

        assertTrue(sql.contains("SELECT COUNT(*)"));
        assertTrue(sql.contains("c.shared_id = #{userId}"));
        assertTrue(sql.contains("c.claim_status != 'REJECTED'"));
    }

    @Test
    void testGetFilteredPagedClaimCountByLocalExpertId_withStatus() {
        List<ClaimStatus> status = Collections.singletonList(ClaimStatus.PENDING);
        String sql = provider.getFilteredPagedClaimCountByLocalExpertId(25, status);

        assertTrue(sql.contains("c.claim_status IN ('PENDING')"));
    }

    @Test
    void testGetFilteredPagedClaimsByUserId_withStatusEmpty() {
        String sql = provider.getFilteredPagedClaimsByUserId(1, 0, 10, List.of());
        assertFalse(sql.contains("c.claim_status IN "));
    }

    @Test
    void testGetFilteredPagedClaimCountByUserId_withStatusEmpty() {
        String sql = provider.getFilteredPagedClaimCountByUserId(1, List.of());
        assertFalse(sql.contains("c.claim_status IN "));
    }

    @Test
    void testGetFilteredPagedClaimsByLocalExpertId_withStatusEmpty() {
        String sql = provider.getFilteredPagedClaimsByLocalExpertId(1, 0, 10, List.of());
        assertFalse(sql.contains("c.claim_status IN "));
    }

    @Test
    void testGetFilteredPagedClaimCountByLocalExpertId_withStatusEmpty() {
        String sql = provider.getFilteredPagedClaimCountByLocalExpertId(1, List.of());
        assertFalse(sql.contains("c.claim_status IN "));
    }
}