package com.shaliya.springmultitenant.springmultitenant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(
        basePackages = "com.shaliya.springmultitenant.springmultitenant.repo",
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager"
)
@SpringBootApplication
public class SpringMultiTenantApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringMultiTenantApplication.class, args);
    }

}
