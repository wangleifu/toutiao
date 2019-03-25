package com.nowcoder.dao.split;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置、启动多数据源
 *
 * @author wangleifu
 * @created 2019-03-05 15:32
 */
@Configuration
public class DataSourceConfig {
    @Value("${spring.datasource.master.url}")
    private String masterUrl;
    @Value("${spring.datasource.master.driverClassName}")
    private String masterDriverClassName;
    @Value("${spring.datasource.master.username}")
    private String masterUsername;
    @Value("${spring.datasource.master.password}")
    private String masterPassword;

    @Value("${spring.datasource.slave.url}")
    private String slaveUrl;
    @Value("${spring.datasource.slave.driverClassName}")
    private String slaveDriverClassName;
    @Value("${spring.datasource.slave.username}")
    private String slaveUsername;
    @Value("${spring.datasource.slave.password}")
    private String slavePassword;

    public DataSource master() {
        return getDataSource(masterUrl, masterDriverClassName, masterUsername, masterPassword);
    }

    private DataSource getDataSource(String masterUrl, String masterDriverClassName, String masterUsername, String masterPassword) {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(masterUrl);
        datasource.setDriverClassName(masterDriverClassName);
        datasource.setUsername(masterUsername);
        datasource.setPassword(masterPassword);
        try {
            datasource.setFilters("stat,wall");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datasource;
    }

    public DataSource slave() {
        return getDataSource(slaveUrl, slaveDriverClassName, slaveUsername, slavePassword);
    }

//    @Bean
//    @ConfigurationProperties("spring.datasource.master")
//    public DataSource masterDataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Bean
//    @ConfigurationProperties("spring.datasource.slave")
//    public DataSource slaveDataSource() {
//        return DataSourceBuilder.create().build();
//    }

    @Bean
    public DataSource myRoutingDataSource() {
        DataSource masterDataSource = master();
        DataSource slaveDataSource = slave();
        Map<Object, Object> targetDataSource = new HashMap<>();
        targetDataSource.put(DataSourceType.DB_MASTER, masterDataSource);
        targetDataSource.put(DataSourceType.DB_SLAVE, slaveDataSource);
        MyRoutingDataSource myRoutingDataSource = new MyRoutingDataSource();
        myRoutingDataSource.setDefaultTargetDataSource(masterDataSource);
        myRoutingDataSource.setTargetDataSources(targetDataSource);
        return myRoutingDataSource;
    }
}
