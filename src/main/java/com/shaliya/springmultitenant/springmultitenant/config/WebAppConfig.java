package com.shaliya.springmultitenant.springmultitenant.config;



import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;


@Configuration
@EntityScan(basePackages = "com.shaliya.springmultitenant.springmultitenant.entity")
public class WebAppConfig extends SpringBootServletInitializer {
}


