package com.eugene.sumarry.transaction;

import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.math.BigDecimal;

public class Entry {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        TransferService transferService = context.getBean(TransferService.class);
        transferService.transfer("avengerEug", "zhangsan", new BigDecimal("1"));
    }

    @ComponentScan("com.eugene.sumarry.transaction")
    @Configuration
    @EnableTransactionManagement
    @EnableAspectJAutoProxy(exposeProxy = true)
    public static class AppConfig {

        @Bean
        public DataSource dataSource() {
            DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
            driverManagerDataSource.setUrl("jdbc:mysql://127.0.0.1:3306/transaction_test?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowMultiQueries=true");
            driverManagerDataSource.setUsername("root");
            driverManagerDataSource.setDriverClassName("com.mysql.jdbc.Driver");
            driverManagerDataSource.setPassword("root");
            return driverManagerDataSource;
        }

        @Bean
        public PlatformTransactionManager platformTransactionManager(DataSource dataSource) {
            DataSourceTransactionManager manager = new DataSourceTransactionManager();
            manager.setDataSource(dataSource);
            return manager;
        }

        @Bean
        public JdbcTemplate jdbcTemplate(DataSource dataSource) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            return jdbcTemplate;
        }

    }
}
