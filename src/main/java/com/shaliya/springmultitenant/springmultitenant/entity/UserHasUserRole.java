package com.shaliya.springmultitenant.springmultitenant.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "user_role_has_user")
public class UserHasUserRole {

    @EmbeddedId
    private UserRoleHasUserKey id =
            new UserRoleHasUserKey() ;

    @ManyToOne
    @MapsId("user")
    @JoinColumn(name = "user_id", nullable = false)
    User user;


    @ManyToOne
    @MapsId("userRole")
    @JoinColumn(name = "role_id", nullable = false)
    UserRole userRole;


    @Temporal(TemporalType.DATE)
    @Column( name = "update_date")
    private Date updateDate;
}
