package com.shaliya.springmultitenant.springmultitenant.controller;

import com.shaliya.springmultitenant.springmultitenant.dto.CommonResponseDTO;
import com.shaliya.springmultitenant.springmultitenant.dto.requestdto.RequestUserDTO;
import com.shaliya.springmultitenant.springmultitenant.service.TenantManagementService;
import com.shaliya.springmultitenant.springmultitenant.service.UserService;
import com.shaliya.springmultitenant.springmultitenant.util.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final TenantManagementService tenantManagementService;
    private final UserService userService;

    public UserController(TenantManagementService tenantManagementService, UserService userService) {
        this.tenantManagementService = tenantManagementService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<StandardResponse> registerUserAndBusiness(@RequestBody RequestUserDTO requestUserDTO
    ) throws IOException {
        CommonResponseDTO user = userService.createUser(requestUserDTO);
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(
                        user.getCode(), user.getMessage(), user.getData()
                ),
                HttpStatus.CREATED
        );
    }

}
