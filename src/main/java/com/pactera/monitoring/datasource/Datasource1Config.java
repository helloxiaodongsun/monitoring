package com.pactera.monitoring.datasource;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;

import java.sql.SQLException;

@Configuration
//扫描 Mapper 接口并容器管理
@MapperScan(basePackages = Datasource1Config.PACKAGE, sqlSessionFactoryRef = "ds1SqlSessionFactory")
public class Datasource1Config {
    private static final Logger logger = LoggerFactory.getLogger(Datasource1Config.class);
    // 精确到 master 目录，以便跟其他数据源隔离
    static final String PACKAGE = "com.pactera.monitoring.dao.ds1";
    static final String MAPPER_LOCATION = "classpath:mapper/ds1/*.xml";

    @Value("${ds1.datasource.url}")
    private String url;
    @Value("${ds1.datasource.username}")
    private String user;
    @Value("${ds1.datasource.password}")
    private String password;
    @Value("${ds1.datasource.driverClassName}")
    private String driverClass;

    @Value("${ds1.datasource.validationQuery}")
    private String validationQuery;
    @Value("${ds1.datasource.maxActive}")
    private Integer maxActive;
    @Value("${ds1.datasource.minIdle}")
    private Integer minIdle;
    @Value("${ds1.datasource.initialSize}")
    private Integer initialSize;
    @Value("${ds1.datasource.maxWait}")
    private Long maxWait;
    @Value("${ds1.datasource.timeBetweenEvictionRunsMillis}")
    private Long timeBetweenEvictionRunsMillis;
    @Value("${ds1.datasource.minEvictableIdleTimeMillis}")
    private Long minEvictableIdleTimeMillis;
    @Value("${ds1.datasource.testWhileIdle}")
    private Boolean testWhileIdle;
    @Value("${ds1.datasource.testWhileIdle}")
    private Boolean testOnBorrow;
    @Value("${ds1.datasource.testOnBorrow}")
    private Boolean testOnReturn;
    /**
     * filters
     */
    @Value("${spring.datasource.druid.filters}")
    private String filters;


    @Bean(name = "ds1DataSource")
    @Primary
    public DataSource ds1DataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);

        //连接池配置
        dataSource.setMaxActive(maxActive);
        dataSource.setMinIdle(minIdle);
        dataSource.setInitialSize(initialSize);
        dataSource.setMaxWait(maxWait);
        dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        dataSource.setTestWhileIdle(testWhileIdle);
        dataSource.setTestOnBorrow(testOnBorrow);
        dataSource.setTestOnReturn(testOnReturn);
        dataSource.setValidationQuery(validationQuery);

        dataSource.setPoolPreparedStatements(true);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
        try {
            dataSource.setFilters(filters);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dataSource;
    }

    @Bean(name = "ds1TransactionManager")
    @Primary
    public DataSourceTransactionManager ds1TransactionManager() {
        return new DataSourceTransactionManager(ds1DataSource());
    }

    @Bean(name = "ds1SqlSessionFactory")
    @Primary
    public SqlSessionFactory ds1SqlSessionFactory(@Qualifier("ds1DataSource") DataSource ds1DataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(ds1DataSource);
        sessionFactory.setTypeAliasesPackage("com.pactera.monitoring.entity");
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(Datasource1Config.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
}
