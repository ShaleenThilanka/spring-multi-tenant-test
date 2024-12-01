package com.shaliya.springmultitenant.springmultitenant.service.impl;

import com.shaliya.springmultitenant.springmultitenant.config.CurrentTenantIdentifierResolverImpl;
import com.shaliya.springmultitenant.springmultitenant.config.MultiTenantConnectionProvider;
import com.shaliya.springmultitenant.springmultitenant.service.TenantManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Service
public class TenantManagementServiceImpl implements TenantManagementService {
    @Autowired
    private DataSource dataSource;

    public void provisionTenantDatabase(String tenantId) {
        try (Connection conn = dataSource.getConnection()) {
            // Create database for tenant
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS pos_" + tenantId);
            }

            // Set current tenant context
            CurrentTenantIdentifierResolverImpl.setCurrentTenant(tenantId);

        } catch (SQLException e) {
            throw new RuntimeException("Failed to provision tenant database", e);
        } finally {
            // Clear tenant context
            CurrentTenantIdentifierResolverImpl.clearCurrentTenant();
        }
    }

//    private void createTenantTables(String tenantId) {
//        // Use JdbcTemplate to create tables specific to this tenant
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
//
//        // Create products table
//        jdbcTemplate.execute(
//                "CREATE TABLE IF NOT EXISTS products (" +
//                        "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
//                        "name VARCHAR(255) NOT NULL," +
//                        "price DECIMAL(10,2) NOT NULL," +
//                        "quantity INT NOT NULL)"
//        );
//    }
}
