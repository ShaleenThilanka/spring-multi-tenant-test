package com.shaliya.springmultitenant.springmultitenant.config;

import com.shaliya.springmultitenant.springmultitenant.auth.ApplicationUser;
import com.shaliya.springmultitenant.springmultitenant.service.impl.ApplicationUserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TenantResolutionFilter extends OncePerRequestFilter {

    private final ApplicationUserServiceImpl applicationUserService;

    public TenantResolutionFilter(ApplicationUserServiceImpl applicationUserService) {
        this.applicationUserService = applicationUserService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()
                && auth.getPrincipal() instanceof String email
                && !email.equals("anonymousUser")) {

            // Load the full ApplicationUser by email
            ApplicationUser appUser = (ApplicationUser) applicationUserService
                    .loadUserByUsername(email);

            String tenantId = appUser.getTenantId();
            if (tenantId != null) {
                TenantContext.setCurrentTenant(tenantId);
                System.out.println("Tenant resolved from user: " + tenantId);
            }
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}