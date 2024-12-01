package com.shaliya.springmultitenant.springmultitenant.service.impl;

import com.shaliya.springmultitenant.springmultitenant.config.TenantDatabaseConfig;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Service
public class TenantManagementServiceImpl {
    private final TenantDatabaseConfig tenantDatabaseConfig;

    public TenantManagementServiceImpl(TenantDatabaseConfig tenantDatabaseConfig) {
        this.tenantDatabaseConfig = tenantDatabaseConfig;
    }
    public void provisionTenantDatabase(String tenantId) {
        // Generate unique database for each tenant
        String dbUrl = String.format("jdbc:mysql://localhost:3306/pos_tenant_%s", tenantId);
        String username = "tenant_" + tenantId;
        String password = generateSecurePassword();

        // Add tenant database configuration
        tenantDatabaseConfig.addTenant(tenantId, dbUrl, username, password);

        // Optionally create the actual database
        createDatabaseForTenant(tenantId);
    }

    private String generateSecurePassword() {
        // Implement secure password generation logic
        return "SecurePass_" + System.currentTimeMillis();
    }

    private void createDatabaseForTenant(String tenantId) {
        // Use JDBC or JPA to create database programmatically
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "1234")) {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS pos_tenant_" + tenantId);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create tenant database", e);
        }
    }
}
