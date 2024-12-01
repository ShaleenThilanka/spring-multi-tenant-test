package com.shaliya.springmultitenant.springmultitenant.service.impl;

import com.shaliya.springmultitenant.springmultitenant.config.CurrentTenantIdentifierResolverImpl;
import com.shaliya.springmultitenant.springmultitenant.service.TenantManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Service
public class TenantManagementServiceImpl implements TenantManagementService {

    private static final Logger logger = LoggerFactory.getLogger(TenantManagementService.class);

    @Autowired
    private DataSource dataSource;

    public void provisionTenantDatabase(String tenantId) {
        try (Connection conn = dataSource.getConnection()) {
            // Create database for tenant
            try (Statement stmt = conn.createStatement()) {
                String databaseName = "tenant_" + tenantId;
                stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + databaseName);
                logger.info("Provisioned database for tenant: {}", databaseName);
            }

            // Set current tenant context
            CurrentTenantIdentifierResolverImpl.setCurrentTenant(tenantId);
        } catch (SQLException e) {
            logger.error("Failed to provision tenant database", e);
            throw new RuntimeException("Failed to provision tenant database", e);
        } finally {
            // Clear tenant context
            CurrentTenantIdentifierResolverImpl.clearCurrentTenant();
        }
    }
}
