package com.shaliya.springmultitenant.springmultitenant.service.impl;

import com.shaliya.springmultitenant.springmultitenant.entity.Business;
import com.shaliya.springmultitenant.springmultitenant.entity.User;
import com.shaliya.springmultitenant.springmultitenant.repo.BusinessRepo;
import com.shaliya.springmultitenant.springmultitenant.service.BusinessService;
import com.shaliya.springmultitenant.springmultitenant.service.TenantManagementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.UUID;

@Service
@Transactional
public class BusinessServiceImpl implements BusinessService {
    private final BusinessRepo businessRepo;
    private final TenantManagementService tenantManagementService;

    public BusinessServiceImpl(BusinessRepo businessRepo, TenantManagementService tenantManagementService) {
        this.businessRepo = businessRepo;
        this.tenantManagementService = tenantManagementService;
    }

    @Override
    @Transactional
    public String createBusiness(String businessName, User user) throws IOException {
        String tenant = businessName.toLowerCase()
                .replaceAll("\\s+", "_")
                .concat("_" + System.currentTimeMillis());
        Business business = Business.builder()
                .id(UUID.randomUUID().toString())
                .name(businessName)
                .tenantId(tenant)
                .owner(user)
                .build();
        tenantManagementService.provisionTenantDatabase(tenant);
        businessRepo.save(business);
        return business.getTenantId();
    }
}
