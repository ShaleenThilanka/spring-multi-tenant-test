package com.shaliya.springmultitenant.springmultitenant.config;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class TenantDatabaseConfig {
    private Map<String, DataSourceProperties> tenantDatabases = new HashMap<>();

    public void addTenant(String tenantId, String url, String username, String password) {
        DataSourceProperties properties = new DataSourceProperties(url, username, password);
        tenantDatabases.put(tenantId, properties);
    }

    public DataSource createDataSourceForTenant(String tenantId) {
        DataSourceProperties props = tenantDatabases.get(tenantId);
        if (props == null) {
            throw new RuntimeException("Tenant database not configured: " + tenantId);
        }

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(props.url);
        dataSource.setUsername(props.username);
        dataSource.setPassword(props.password);

        return dataSource;
    }

    private static class DataSourceProperties {
        String url;
        String username;
        String password;

        public DataSourceProperties(String url, String username, String password) {
            this.url = url;
            this.username = username;
            this.password = password;
        }
    }

    // Method to get all configured tenants
    public Map<String, DataSourceProperties> getTenantDatabases() {
        return new HashMap<>(tenantDatabases);
    }
}
