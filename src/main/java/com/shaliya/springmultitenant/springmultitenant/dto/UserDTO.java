package com.shaliya.springmultitenant.springmultitenant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String userId;

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private boolean activeState;

    private Date registerDate;

    private boolean isAccountNonExpired;

    private boolean isAccountNonLocked;

    private String prefix;
    private String username;
}
