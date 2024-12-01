package com.shaliya.springmultitenant.springmultitenant.service;

import com.shaliya.springmultitenant.springmultitenant.dto.CommonResponseDTO;
import com.shaliya.springmultitenant.springmultitenant.dto.requestdto.RequestUserDTO;

import java.io.IOException;

public interface UserService {
    CommonResponseDTO createUser(RequestUserDTO userDTO) throws IOException;
}
