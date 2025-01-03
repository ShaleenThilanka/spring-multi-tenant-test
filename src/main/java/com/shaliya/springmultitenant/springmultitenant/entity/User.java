package com.shaliya.springmultitenant.springmultitenant.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@ToString
@Table(name = "user")
public class User {
    @Id
    @Column(length = 80, name = "user_id")
    private String userId;

    @Column(length = 100, name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(length = 45, name = "first_name")
    private String firstName;

    @Column(length = 45, name = "last_name")
    private String lastName;

    @Column(name = "active_state", columnDefinition = "TINYINT")
    private boolean activeState;

    @Temporal(TemporalType.DATE)
    @Column(name = "register_date")
    private Date registerDate;

    @Column(name = "is_account_non_expired", columnDefinition = "TINYINT")
    private boolean isAccountNonExpired;

    @Column(name = "is_account_non_locked", columnDefinition = "TINYINT")
    private boolean isAccountNonLocked;


    @Column(length = 45, name = "prefix")
    private String prefix;

    @Column(length = 100, name = "user_name", unique = true)
    private String username;
    @OneToOne(mappedBy = "owner", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Business business;

}
