package com.shaliya.springmultitenant.springmultitenant.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
public class UserRoleHasUserKey implements Serializable {
    @Column(length = 80, name = "user_id")
    private String user;

    @Column(length = 45, name = "role_id")
    private String userRole;
}
