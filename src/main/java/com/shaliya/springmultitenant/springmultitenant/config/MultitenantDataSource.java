package com.shaliya.springmultitenant.springmultitenant.config;

import com.zaxxer.hikari.HikariDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


public class MultitenantDataSource extends AbstractRoutingDataSource {

    @Autowired
    private Environment env;

    @Bean
    public MultitenantDataSource dataSource() {
        MultitenantDataSource multitenantDataSource = new MultitenantDataSource();

        // Default datasource
        DataSource defaultDataSource = createDataSource(
                env.getProperty("spring.datasource.url"),
                env.getProperty("spring.datasource.username"),
                env.getProperty("spring.datasource.password")
        );

        // Map to hold tenant datasources
        Map<Object, Object> tenantDataSources = new HashMap<>();
        tenantDataSources.put("default", defaultDataSource);

        // Add tenant-specific datasources
        tenantDataSources.put("tenant_a", createDataSource(
                "jdbc:mysql://localhost:3306/tenant_a_db",
                env.getProperty("spring.datasource.username"),
                env.getProperty("spring.datasource.password")
        ));

        // You can add more tenants here

        multitenantDataSource.setTargetDataSources(tenantDataSources);
        multitenantDataSource.setDefaultTargetDataSource(defaultDataSource);

        return multitenantDataSource;
    }

    private DataSource createDataSource(String url, String username, String password) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }    @Override
    protected String determineCurrentLookupKey() {
        return TenantContext.getCurrentTenant(); // Returns tenant identifier to resolve the datasource
    }
}
