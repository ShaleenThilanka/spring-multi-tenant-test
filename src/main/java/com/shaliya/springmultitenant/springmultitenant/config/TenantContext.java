package com.shaliya.springmultitenant.springmultitenant.config;

public class TenantContext {
    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    public static String getCurrentTenant() {
        return CURRENT_TENANT.get();
    }

    public static void setCurrentTenant(String tenant) {
        CURRENT_TENANT.set(tenant);
        System.out.println(CURRENT_TENANT.get() + " set to " + tenant);
    }
    public static void clear() {
        CURRENT_TENANT.remove();
    }
}

