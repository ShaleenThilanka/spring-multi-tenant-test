package com.shaliya.springmultitenant.springmultitenant.config;

import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class TenantDatabaseConfig {
    @Autowired
    private Environment env;

    @Autowired
    private MultiTenantConnectionProvider multiTenantConnectionProvider;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource defaultDataSource = new DriverManagerDataSource();
        defaultDataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        defaultDataSource.setUrl(env.getProperty("spring.datasource.url"));
        defaultDataSource.setUsername(env.getProperty("spring.datasource.username"));
        defaultDataSource.setPassword(env.getProperty("spring.datasource.password"));

        // Configure tenant datasources
        Map<String, DataSource> tenantDataSources = new HashMap<>();
        tenantDataSources.put("default", defaultDataSource);

        // Add method to dynamically add tenant datasources
        multiTenantConnectionProvider.addTenantDataSource("default", defaultDataSource);

        return defaultDataSource;
    }

    public void addTenantDataSource(String tenantId, String url, String username, String password) {
        DriverManagerDataSource tenantDataSource = new DriverManagerDataSource();
        tenantDataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        tenantDataSource.setUrl(url);
        tenantDataSource.setUsername(username);
        tenantDataSource.setPassword(password);

        multiTenantConnectionProvider.addTenantDataSource(tenantId, tenantDataSource);
    }
}
