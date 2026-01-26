package com.sachet.order_service.config.database;

import com.sachet.order_service.config.BeanConfigurationBase;
import com.sachet.order_service.config.EnvironmentConfiguration;
import com.sachet.order_service.config.model.DatabaseConfiguration;
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration extends BeanConfigurationBase {
    public DataSourceConfiguration(AutowireCapableBeanFactory beanFactory) {
        super(beanFactory);
    }

    @Bean(name = "orderDS")
    public DataSource getProductDataSource(EnvironmentConfiguration environmentConfiguration) {
        DatabaseConfiguration databaseConfiguration = environmentConfiguration.getDatabaseConfiguration();
        return setUpDataBaseConnection(databaseConfiguration);
    }

    private DataSource setUpDataBaseConnection(DatabaseConfiguration databaseConfiguration) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(databaseConfiguration.getUrl());
        hikariConfig.setUsername(databaseConfiguration.getUserName());
        hikariConfig.setPassword(databaseConfiguration.getPassword());
        hikariConfig.setMaximumPoolSize(databaseConfiguration.getMaxPoolSize());
        hikariConfig.setDriverClassName(databaseConfiguration.getDriverClassName());
        hikariConfig.setConnectionTimeout(databaseConfiguration.getConnectionTimeOut());
        return new HikariDataSource(hikariConfig);
    }
}
