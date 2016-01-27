package com.jiayusoft.shengli.bingan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@ComponentScan
@Configuration
@EnableTransactionManagement
@EnableAutoConfiguration
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);
    public static final String baseFolder = "d:/zljy/CMDP/mobile/";
    public static final String describeFolder = baseFolder+"mydescribe/";
    public static final String healthcheckFolder = baseFolder+"healthcheck/";

    @Bean
    DataSource dataSource() {
        log.info("dataSource");
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(oracle.jdbc.driver.OracleDriver.class);
        dataSource.setUsername("ycyl");
        dataSource.setUrl("dbc:oracle:thin:@11.0.0.30:1521:ycyl");
        dataSource.setPassword("ycyl1120");
        return dataSource;
    }

    @Bean
    JdbcTemplate jdbcTemplate(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        log.info("jdbcTemplate");
//        jdbcTemplate.execute("drop table BOOKINGS if exists");
//        jdbcTemplate.execute("create table BOOKINGS("
//                + "ID serial, FIRST_NAME varchar(5) NOT NULL)");
        return jdbcTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
