package com.shaliya.springmultitenant.springmultitenant.config;

import jakarta.annotation.PostConstruct;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomMultiTenantConnectionProvider implements MultiTenantConnectionProvider {

    private final Map<String, DataSource> dataSources = new HashMap<>();

    @Autowired
    private DataSource dataSource; // Default DataSource

    @PostConstruct
    public void loadDefaultDataSource() {
        // Load the default tenant's database (e.g., tenant master).
        dataSources.put("default", dataSource);
    }

    public void addTenantDataSource(String tenantId, DataSource dataSource) {
        dataSources.put(tenantId, dataSource);
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        String tenantId = TenantContext.getCurrentTenant();
        DataSource tenantDataSource = dataSources.get(tenantId);
        if (tenantDataSource == null) {
            tenantDataSource = dataSources.get("default"); // Fallback to default tenant
        }
        return tenantDataSource.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        DataSource tenantDataSource = dataSources.get(tenantIdentifier);
        if (tenantDataSource == null) {
            tenantDataSource = dataSources.get("default"); // Fallback to default tenant
        }
        return tenantDataSource.getConnection();
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(Class<?> aClass) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        return null;
    }
}
