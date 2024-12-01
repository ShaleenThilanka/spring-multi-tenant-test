package com.shaliya.springmultitenant.springmultitenant.config;



import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


@Component
public class CustomMultiTenantConnectionProvider implements MultiTenantConnectionProvider {
    private final Map<String, DataSource> tenantDataSources = new HashMap<>();

    @Autowired
    private Environment env;

    @Override
    public Connection getAnyConnection() throws SQLException {
        return getTenantConnection("tenant_db");
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(Object tenantIdentifier) throws SQLException {
        String tenantId = tenantIdentifier.toString();
        String url = env.getProperty("spring.datasource.url", "")
                + "?createDatabaseIfNotExist=true";

        DriverManagerDataSource tenantDataSource = new DriverManagerDataSource();
        tenantDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        tenantDataSource.setUrl(url);
        tenantDataSource.setUsername(env.getProperty("spring.datasource.username", "root"));
        tenantDataSource.setPassword(env.getProperty("spring.datasource.password", ""));

        return tenantDataSource.getConnection();
    }

    private Connection getTenantConnection(String tenantId) throws SQLException {
        // Create or retrieve tenant-specific DataSource
        if (!tenantDataSources.containsKey(tenantId)) {
            createTenantDataSource(tenantId);
        }

        return tenantDataSources.get(tenantId).getConnection();
    }

    private void createTenantDataSource(String tenantId) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        // Construct tenant-specific database URL
        String baseUrl = env.getProperty("spring.datasource.url", "jdbc:mysql://localhost:3306/");
        String databaseName = "tenant_" + tenantId;

        dataSource.setUrl(baseUrl + databaseName);
        dataSource.setUsername(env.getProperty("spring.datasource.username", "root"));
        dataSource.setPassword(env.getProperty("spring.datasource.password", "1234"));
        dataSource.setDriverClassName(
                env.getProperty("spring.datasource.driver-class-name", "com.mysql.cj.jdbc.Driver")
        );

        // Store the DataSource
        tenantDataSources.put(tenantId, dataSource);
    }

    @Override
    public void releaseConnection(Object tenantIdentifier, Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        return null;
    }

    @Override
    public boolean isUnwrappableAs(Class<?> unwrapType) {
        return false;
    }

}
