//package com.shaliya.springmultitenant.springmultitenant.config;
//
//import jakarta.persistence.EntityManagerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import javax.sql.DataSource;
//import java.util.Map;
//
//@Configuration
//public class DataSourceConfig {
//
//    @Bean
//    public DataSource dataSource(@Lazy CustomMultiTenantConnectionProvider multiTenantConnectionProvider) {
//        MultitenantDataSource dataSource = new MultitenantDataSource();
//
//        // Set the target data sources
//        dataSource.setTargetDataSources(multiTenantConnectionProvider.getDataSources());
//
//        // Set the default data source for the multi-tenant routing
//        dataSource.setDefaultTargetDataSource(multiTenantConnectionProvider.getDefaultDataSource());
//
//        return dataSource;
//    }
//
//    @Bean
//    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
//        return new JpaTransactionManager(emf);
//    }
//
//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
//        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
//        factoryBean.setDataSource(dataSource);
//        // additional JPA configuration
//        return factoryBean;
//    }
//}
