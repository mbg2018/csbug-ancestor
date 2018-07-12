package com.csbug.ancestor.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;


/**
 * Author : chenbin
 * Date : 2018/1/10
 * Description : hikari配置注入，初始化datasource
 * version : 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "spring.datasource.hikari")
public class DataSourceConfiguration extends HikariConfig {

    @Bean
    public DataSource dataSource() {
        return new HikariDataSource(this);
    }
}
