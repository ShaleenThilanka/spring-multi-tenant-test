package com.shaliya.springmultitenant.springmultitenant.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "user_role")
public class UserRole {

    @Id
    @Column(length = 45, name = "role_id")
    private String roleId;

    @Column(length = 45, name = "role_name", unique = true)
    private String roleName;

    @Column(length = 250, name = "description")
    private String description;

    @Column(name = "active_state", columnDefinition = "TINYINT")
    private boolean activeState;

    @OneToMany(mappedBy = "userRole" ,fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<UserHasUserRole> userHasUserRoles;
    public UserRole(String roleId, String roleName, String description, boolean activeState) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.description = description;
        this.activeState = activeState;
    }
}
