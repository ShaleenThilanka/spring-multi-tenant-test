package com.shaliya.springmultitenant.springmultitenant.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MultiTenantConfig {
    @Bean
    public DataSource dataSource() {
        MultiTenantConnectionProvider dataSource = new MultiTenantConnectionProvider();

        // Configure default and tenant-specific databases
        Map<String, DataSource> tenantDataSources = new HashMap<>();

        // Default datasource
        DriverManagerDataSource defaultDataSource = new DriverManagerDataSource();
        defaultDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        defaultDataSource.setUrl("jdbc:mysql://localhost:3306/pos_default");
        defaultDataSource.setUsername("your_username");
        defaultDataSource.setPassword("your_password");

        // Add tenant-specific datasources can be dynamically added here
        tenantDataSources.put("default", defaultDataSource);

        dataSource.setTenantDataSources(tenantDataSources);
        return dataSource;
    }
}
