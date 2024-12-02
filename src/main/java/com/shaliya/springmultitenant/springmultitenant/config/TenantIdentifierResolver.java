package com.shaliya.springmultitenant.springmultitenant.config;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

@Component
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver {

    private static final String DEFAULT_TENANT = "master_db"; // Fallback to the master DB

    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenantId = TenantContext.getCurrentTenant();
        System.out.println("Resolving connection for tenant: " + tenantId);
        return (tenantId != null) ? tenantId : DEFAULT_TENANT;  // Default to master_db if tenant is null
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}

