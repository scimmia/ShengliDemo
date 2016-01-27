package com.jiayusoft.shengli.bingan.utils;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

/**
 * Created by ASUS on 2014/11/19.
 */
public class DataBaseUtil {
    public static JdbcTemplate getJdbcTemplate(){
        // simple DS for test (not for production!)
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(oracle.jdbc.driver.OracleDriver.class);
        dataSource.setUsername("ycyl");
        dataSource.setUrl("dbc:oracle:thin:@11.0.0.30:1521:ycyl");
        dataSource.setPassword("ycyl1120");

//        HikariDataSource dataSource = new HikariDataSource();
//        dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
//        dataSource.setJdbcUrl("dbc:oracle:thin:@11.0.0.30:1521:ycyl");
//        dataSource.setUsername("ycyl");
//        dataSource.setPassword("ycyl1120");

//        HikariConfig config = new HikariConfig();
//        config.setDriverClassName("oracle.jdbc.driver.OracleDriver");
//        config.setDataSourceClassName("oracle.jdbc.pool.OracleDataSource");
//        config.setJdbcUrl("dbc:oracle:thin:@11.0.0.30:1521:ycyl");
//        config.setUsername("ycyl");
//        config.setPassword("ycyl1120");
//        config.addDataSourceProperty("cachePrepStmts", "true");
//        config.addDataSourceProperty("prepStmtCacheSize", "250");
//        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
//        config.addDataSourceProperty("useServerPrepStmts", "true");


        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate;
    }

    public static String buildPage(String sql,String startIndex,String count){
        int countTemp = NumberUtils.toInt(count, 30);
        int startTemp = NumberUtils.toInt(startIndex,0);
        int endTemp = startTemp + countTemp;
        return String.format("SELECT * FROM\n" +
                "(\n" +
                "SELECT A.*, ROWNUM RN\n" +
                "FROM (%s) A\n" +
                "WHERE ROWNUM <= %d\n" +
                ")\n" +
                "WHERE RN > %d",sql,endTemp,startTemp);
    }
}
