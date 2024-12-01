package com.shaliya.springmultitenant.springmultitenant.config;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
@Component
public class MultiTenantConnectionProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {
    private Map<String, DataSource> tenantDataSources = new HashMap<>();

    @Autowired
    private DataSource defaultDataSource;

    public void addTenantDataSource(String tenantId, DataSource dataSource) {
        tenantDataSources.put(tenantId, dataSource);
    }

    @Override
    protected DataSource selectAnyDataSource() {
        return defaultDataSource;
    }

    @Override
    protected DataSource selectDataSource(Object tenantIdentifier) {
        String tenantKey = tenantIdentifier.toString();

        // If you want to use Environment, ensure it's properly injected
        // Example of potentially using Environment:
        // String tenantSpecificProperty = environment.getProperty("tenant." + tenantKey + ".datasource");

        return tenantDataSources.containsKey(tenantKey)
                ? tenantDataSources.get(tenantKey)
                : defaultDataSource;
    }


}
