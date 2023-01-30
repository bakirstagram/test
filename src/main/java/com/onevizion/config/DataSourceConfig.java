package com.onevizion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Objects;

import static com.onevizion.util.Constants.*;
import static com.onevizion.util.Constants.DB_PASSWORD_PROPERTY;

@Configuration
public class DataSourceConfig {

    private final Environment environment;

    public DataSourceConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(environment.getProperty(DB_DRIVER_PROPERTY)));
        dataSource.setUrl(environment.getProperty(DB_URL_PROPERTY));
        dataSource.setUsername(environment.getProperty(DB_USERNAME_PROPERTY));
        dataSource.setPassword(environment.getProperty(DB_PASSWORD_PROPERTY));

        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(getDataSource());
    }

}
