package com.shaliya.springmultitenant.springmultitenant.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@ToString
@Table(name = "businesses")
public class Business {
    @Id
    @Column(length = 80, name = "business_id")
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String tenantId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User owner;
}
