package com.shaliya.springmultitenant.springmultitenant.dto.requestdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class RequestTenantDTO {
    private String tenantId;
    private String database;
    private String username;
    private String password;
}
