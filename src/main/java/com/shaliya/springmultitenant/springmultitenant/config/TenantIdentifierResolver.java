package com.shaliya.springmultitenant.springmultitenant.config;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver {

    @Autowired
    private DynamicMultiTenantDataSourceConfig multiTenantDataSourceConfig;

    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenantId = TenantContext.getCurrentTenant();

        // If tenant doesn't exist, attempt to register it
        if (tenantId != null &&
                !multiTenantDataSourceConfig.getTenantDataSources().containsKey(tenantId)) {
            // Dynamically register the new tenant datasource
            multiTenantDataSourceConfig.addTenantDataSource(
                    tenantId,
                    String.format("jdbc:mysql://localhost:3306/%s", tenantId)
            );
        }

        return tenantId != null ? tenantId : "default";
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}

