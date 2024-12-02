package com.shaliya.springmultitenant.springmultitenant.controller;

import com.shaliya.springmultitenant.springmultitenant.config.CustomMultiTenantConnectionProvider;
import com.shaliya.springmultitenant.springmultitenant.dto.requestdto.RequestTenantDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@RestController
@RequestMapping("/api/v1/tenants")
public class TenantController {

    @Autowired
    private CustomMultiTenantConnectionProvider tenantProvider;

    @PostMapping("/add")
    public ResponseEntity<String> addTenant(@RequestBody RequestTenantDTO request) {
        try {
            DataSource tenantDataSource = DataSourceBuilder.create()
                    .url("jdbc:mysql://localhost:3306/" + request.getDatabase())
                    .username(request.getUsername())
                    .password(request.getPassword())
                    .driverClassName("com.mysql.cj.jdbc.Driver")
                    .build();

            // Adding tenant's DataSource to the provider
            tenantProvider.addTenantDataSource(request.getTenantId(), tenantDataSource);
            return ResponseEntity.ok("Tenant added successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
