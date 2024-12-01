package com.shaliya.springmultitenant.springmultitenant.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "tenant")
public class Tenant {
    @Id
    @Column(length = 80, name = "tenant_id")
    private String id;
    @Column(length = 80, name = "name")
    private String name;

    private String dbUsername;
    private String dbPassword;
}
