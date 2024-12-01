package com.shaliya.springmultitenant.springmultitenant.service.impl;

import com.shaliya.springmultitenant.springmultitenant.dto.CommonResponseDTO;
import com.shaliya.springmultitenant.springmultitenant.dto.UserDTO;
import com.shaliya.springmultitenant.springmultitenant.dto.requestdto.RequestUserDTO;
import com.shaliya.springmultitenant.springmultitenant.entity.User;
import com.shaliya.springmultitenant.springmultitenant.repo.UserRepo;
import com.shaliya.springmultitenant.springmultitenant.service.BusinessService;
import com.shaliya.springmultitenant.springmultitenant.service.TenantManagementService;
import com.shaliya.springmultitenant.springmultitenant.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    private final BusinessService businessService;

    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepo userRepo, BusinessService businessService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
        this.businessService = businessService;
    }


    @Override
    public CommonResponseDTO createUser(RequestUserDTO dto) throws IOException {

        System.out.println(dto.getEmail());
        User user = User.builder()
                .userId(UUID.randomUUID().toString())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .email(dto.getFirstName())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .activeState(false)
                .registerDate(dto.getRegisterDate())
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .prefix("prefix")
                .username(dto.getEmail()).build();
        userRepo.save(user);
        String business = businessService.createBusiness(dto.getBusinessName(), user);
        return new CommonResponseDTO(
                201, "Registration was successful!", business, new ArrayList<>()
        );
    }
}
