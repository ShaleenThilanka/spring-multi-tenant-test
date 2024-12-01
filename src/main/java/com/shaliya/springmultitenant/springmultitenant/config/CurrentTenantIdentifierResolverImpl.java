package com.shaliya.springmultitenant.springmultitenant.config;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;


@Component
public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {
    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();

    public static void setCurrentTenant(String tenantId) {
        currentTenant.set(tenantId);
    }

    public static void clearCurrentTenant() {
        currentTenant.remove();
    }

    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenant = currentTenant.get();
        return tenant != null ? tenant : "default";
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}

