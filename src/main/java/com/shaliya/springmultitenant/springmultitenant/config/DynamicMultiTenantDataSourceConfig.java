package com.shaliya.springmultitenant.springmultitenant.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class DynamicMultiTenantDataSourceConfig {

    private final Map<String, DataSource> tenantDataSources = new ConcurrentHashMap<>();

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

        // Initialize default datasources
        tenantDataSources.put("default", defaultDataSource);
        tenantDataSources.put("master_db", defaultDataSource);

        multitenantDataSource.setTargetDataSources(new HashMap<>(tenantDataSources));
        multitenantDataSource.setDefaultTargetDataSource(defaultDataSource);

        return multitenantDataSource;
    }

    // Method to dynamically add a new tenant datasource
    public synchronized void addTenantDataSource(String tenantId, String jdbcUrl) {
        if (!tenantDataSources.containsKey(tenantId)) {
            DataSource tenantDataSource = createDataSource(
                    jdbcUrl,
                    env.getProperty("spring.datasource.username"),
                    env.getProperty("spring.datasource.password")
            );

            tenantDataSources.put(tenantId, tenantDataSource);

            // If we have an existing MultitenantDataSource, update its sources
            updateDataSourceTargets();
        }
    }

    // Method to create a datasource
    private DataSource createDataSource(String url, String username, String password) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    // Update the existing datasource with new tenant sources
    private void updateDataSourceTargets() {
        ApplicationContext context = ApplicationContextProvider.getApplicationContext();
        if (context != null) {
            MultitenantDataSource multitenantDataSource = context.getBean(MultitenantDataSource.class);
            multitenantDataSource.setTargetDataSources(new HashMap<>(tenantDataSources));
            multitenantDataSource.afterPropertiesSet();
        }
    }

    // Helper method to get available tenant datasources
    public Map<String, DataSource> getTenantDataSources() {
        return new HashMap<>(tenantDataSources);
    }
}
