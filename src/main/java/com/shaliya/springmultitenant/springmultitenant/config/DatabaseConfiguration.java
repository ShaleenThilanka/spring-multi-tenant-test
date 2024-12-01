package com.shaliya.springmultitenant.springmultitenant.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@PropertySource({"classpath:application.properties"})
@ComponentScan("com.shaliya.springmultitenant.*")
@EnableTransactionManagement
public class DatabaseConfiguration {

    @Autowired
    Environment env;

    @Bean(name = "springmultitenant")
    public JdbcTemplate jdbcTemplateEntRate(
            @Qualifier("dataSource") DataSource dataSource)  {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "dataSource")
    public DataSource entBillDataSourceOne() {
        return dataSource("spring.datasource.url","spring.datasource.username","spring.datasource.password");
    }

    public DataSource dataSource(String dbcUrl, String userName, String password) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(env.getProperty(dbcUrl));
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        dataSource.setPassword(env.getProperty(password));
        dataSource.setUsername(env.getProperty(userName));
        return dataSource;
    }

}
