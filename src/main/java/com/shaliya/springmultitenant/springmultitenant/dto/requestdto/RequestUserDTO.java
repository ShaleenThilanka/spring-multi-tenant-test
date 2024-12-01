package com.shaliya.springmultitenant.springmultitenant.dto.requestdto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class RequestUserDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private Date registerDate;
    private String businessName;
}
