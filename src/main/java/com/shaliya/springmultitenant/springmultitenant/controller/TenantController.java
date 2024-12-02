package com.shaliya.springmultitenant.springmultitenant.controller;

import com.shaliya.springmultitenant.springmultitenant.config.CustomMultiTenantConnectionProvider;
import com.shaliya.springmultitenant.springmultitenant.dto.requestdto.RequestTenantDTO;
import com.shaliya.springmultitenant.springmultitenant.entity.Business;
import com.shaliya.springmultitenant.springmultitenant.entity.Product;
import com.shaliya.springmultitenant.springmultitenant.entity.User;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.cfg.Environment;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;

import org.hibernate.tool.schema.internal.SchemaCreatorImpl;
import org.hibernate.tool.schema.spi.ExceptionHandler;
import org.hibernate.tool.schema.spi.ExecutionOptions;
import org.hibernate.tool.schema.spi.TargetDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.lang.module.Configuration;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@RestController
@RequestMapping("/api/v1/tenants")
public class TenantController {
    private static final Logger logger = LoggerFactory.getLogger(TenantController.class);

    @Autowired
    private CustomMultiTenantConnectionProvider tenantProvider;

    @PostMapping("/add")
    public ResponseEntity<String> addTenant(@RequestBody RequestTenantDTO request) {
        try {
            // Create the tenant database
            DataSource adminDataSource = DataSourceBuilder.create()
                    .url("jdbc:mysql://localhost:3306/")
                    .username("root") // Admin username
                    .password("1234") // Admin password
                    .driverClassName("com.mysql.cj.jdbc.Driver")
                    .build();

            try (var connection = adminDataSource.getConnection();
                 var statement = connection.createStatement()) {
                statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + request.getDatabase());
            }

            // Create DataSource for the tenant
            DataSource tenantDataSource = DataSourceBuilder.create()
                    .url("jdbc:mysql://localhost:3306/" + request.getDatabase())
                    .username(request.getUsername())
                    .password(request.getPassword())
                    .driverClassName("com.mysql.cj.jdbc.Driver")
                    .build();

            tenantProvider.addTenantDataSource(request.getTenantId(), tenantDataSource);

            // Initialize schema in the tenant database
            initializeSchema(tenantDataSource);

            return ResponseEntity.ok("Tenant added successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    public void initializeSchema(DataSource tenantDataSource) {
        try {
            // Build service registry with configuration properties
            Map<String, Object> settings = new HashMap<>();
            settings.put(Environment.DATASOURCE, tenantDataSource);
            settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
            settings.put(Environment.HBM2DDL_AUTO, "create");
            settings.put(Environment.SHOW_SQL, true);

            // Create MetadataSources
            StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder()
                    .applySettings(settings);

            MetadataSources metadataSources = new MetadataSources(registryBuilder.build());

            // Add annotated entity classes
            metadataSources.addAnnotatedClass(User.class);
            metadataSources.addAnnotatedClass(Business.class);
            metadataSources.addAnnotatedClass(Product.class);

            // Create and execute schema export
            SchemaExport schemaExport = new SchemaExport();
            schemaExport.create(EnumSet.of(TargetType.DATABASE), metadataSources.buildMetadata());

            logger.info("Schema created successfully for tenant database");
        } catch (Exception e) {
            logger.error("Error creating schema for tenant database", e);
            throw new RuntimeException("Failed to initialize tenant schema", e);
        }
    }


}
