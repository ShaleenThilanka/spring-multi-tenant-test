package com.shaliya.springmultitenant.springmultitenant.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;


public class MultitenantDataSource extends AbstractRoutingDataSource {

    @Override
    protected String determineCurrentLookupKey() {
        return TenantContext.getCurrentTenant(); // Resolving the tenant dynamically
    }
}
