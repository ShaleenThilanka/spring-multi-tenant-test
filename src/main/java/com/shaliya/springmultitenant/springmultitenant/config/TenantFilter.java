package com.shaliya.springmultitenant.springmultitenant.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Enumeration;

@Component
@Order(1)
public class TenantFilter implements Filter {

//    @Override
//    public void doFilter(
//            jakarta.servlet.ServletRequest request,
//            jakarta.servlet.ServletResponse response,
//            FilterChain chain) throws IOException, ServletException {
//
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//
//        // Log headers to check if X-Tenant-ID is present
//        Enumeration<String> headerNames = httpRequest.getHeaderNames();
//        while (headerNames.hasMoreElements()) {
//            String headerName = headerNames.nextElement();
//            System.out.println(headerName + ": " + httpRequest.getHeader(headerName));
//        }
//
//        // Extract tenant ID from header
//        String tenantId = httpRequest.getHeader("X-Tenant-ID");
//        if (tenantId == null) {
//            tenantId = "default_tenant"; // Default fallback
//        }
//
//        // Log the tenant ID to verify extraction
//        System.out.println("Tenant ID extracted: " + tenantId);
//
//        // Set tenant in the context
//        TenantContext.setCurrentTenant(tenantId);
//        System.out.println("Tenant set in context: " + TenantContext.getCurrentTenant());
//
//        try {
//            chain.doFilter(request, response);
//        } finally {
//            TenantContext.clear();
//        }
//    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String tenantName = req.getHeader("X-Tenant-ID");
        System.out.println("doFilter=" + tenantName);
        TenantContext.setCurrentTenant(tenantName);

        try {
            chain.doFilter(request, response);
        } finally {
            TenantContext.setCurrentTenant("");
        }
    }
}
