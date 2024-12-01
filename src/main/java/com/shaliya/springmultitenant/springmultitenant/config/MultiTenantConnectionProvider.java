package com.shaliya.springmultitenant.springmultitenant.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class MultiTenantConnectionProvider extends AbstractRoutingDataSource {
    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();

    public static void setCurrentTenant(String tenantId) {
        currentTenant.set(tenantId);
    }

    public static String getCurrentTenant() {
        return currentTenant.get();
    }

    public static void clear() {
        currentTenant.remove();
    }

    public void setTenantDataSources(Map<String, DataSource> tenantDataSources) {
        super.setTargetDataSources(new HashMap<>(tenantDataSources));
        super.setDefaultTargetDataSource(tenantDataSources.get("default"));
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return currentTenant.get();
    }
}
