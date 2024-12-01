package com.shaliya.springmultitenant.springmultitenant.repo;

import com.shaliya.springmultitenant.springmultitenant.entity.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface BusinessRepo extends JpaRepository<Business, String> {
}