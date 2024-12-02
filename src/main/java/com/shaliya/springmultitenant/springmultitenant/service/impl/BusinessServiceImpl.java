package com.shaliya.springmultitenant.springmultitenant.service.impl;

import com.shaliya.springmultitenant.springmultitenant.entity.Business;
import com.shaliya.springmultitenant.springmultitenant.entity.User;
import com.shaliya.springmultitenant.springmultitenant.repo.BusinessRepo;
import com.shaliya.springmultitenant.springmultitenant.service.BusinessService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.UUID;

@Service
@Transactional
public class BusinessServiceImpl implements BusinessService {
    private final BusinessRepo businessRepo;

    public BusinessServiceImpl(BusinessRepo businessRepo) {
        this.businessRepo = businessRepo;
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
        businessRepo.save(business);
        return business.getTenantId();
    }
}
