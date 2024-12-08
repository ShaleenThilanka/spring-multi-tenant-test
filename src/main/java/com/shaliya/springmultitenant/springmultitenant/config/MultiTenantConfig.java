package com.shaliya.springmultitenant.springmultitenant.config;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MultiTenantConfig {
    @Autowired(required = true)
    private CustomMultiTenantConnectionProvider multiTenantConnectionProvider;

    @Autowired(required = true)
    private TenantIdentifierResolver tenantIdentifierResolver;

    // Consider using @Qualifier if you have multiple DataSource beans
    @Autowired(required = true)
    private DataSource dataSource;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        Map<String, Object> hibernateProperties = new HashMap<>();

        hibernateProperties.put(AvailableSettings.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
        hibernateProperties.put("hibernate.multiTenancy", "DATABASE");
        hibernateProperties.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
        hibernateProperties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantIdentifierResolver);
        hibernateProperties.put(AvailableSettings.HBM2DDL_AUTO, "update");
        hibernateProperties.put(AvailableSettings.SHOW_SQL, true);
        hibernateProperties.put(AvailableSettings.FORMAT_SQL, true);

        LocalContainerEntityManagerFactoryBean emfBean = new LocalContainerEntityManagerFactoryBean();
        emfBean.setDataSource(dataSource);
        emfBean.setPackagesToScan("com.shaliya.springmultitenant.springmultitenant.entity");
        emfBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        emfBean.setJpaPropertyMap(hibernateProperties);

        return emfBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
