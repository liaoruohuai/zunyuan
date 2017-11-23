/*
package com.hzbuvi.chepei;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

*/
/**
 * Created by WANG, RUIQING on 10/13/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 *//*

@Configuration
@EnableJpaRepositories(basePackages = "com.*"
//        ,entityManagerFactoryRef = "entityManagerFactory"
//        ,transactionManagerRef = "transactionManager"
)
@ComponentScan(basePackages = {"com"})
public class DataConfig {
    private String scanPackage="com";
    private String dialect="org.hibernate.dialect.MySQL5Dialect";
    private String url="jdbc:mysql://88.88.88.49:3309/chepei?useUnicode=true&characterEncoding=utf-8";
//    private String url="jdbc:mysql://10.250.3.107:3306/chexiangpei?useUnicode=true&characterEncoding=utf-8";
    private String userName="root";
    private String password="wang";
//    private String password="root";
    private String driverClassName="com.mysql.jdbc.Driver";

//    @Value("${jdbc.dialect}")
//    private String dialect ;
//    @Value("${jdbc.url}")
//    private String url;
//    @Value("${jdbc.userName}")
//    private String userName;
//    @Value("${jdbc.password}")
//    private String password;
//    @Value("${jdbc.driver}")
//    private String driverClassName;

    @Bean(name = "dataSource")
//    @Qualifier("userDataSource")
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        return dataSource;
    }

    //@Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setPackagesToScan(scanPackage);
        factoryBean.setDataSource(dataSource());
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setDatabasePlatform(dialect);
        factoryBean.setJpaVendorAdapter(vendorAdapter);
        return factoryBean;
    }

    @Bean(name = "sessionFactory")
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource());
        sessionFactoryBean.setPackagesToScan(scanPackage);
        Properties hibernateProperties = new Properties();
        hibernateProperties.put("hibernate.dialect", dialect);
        hibernateProperties.put("hibernate.show_sql", true);
        hibernateProperties.put("hibernate.enable_lazy_load_no_trans", true );
        // hibernateProperties.put("hibernate.hbm2ddl.auto", "create");//自动建表
        sessionFactoryBean.setHibernateProperties(hibernateProperties);
        return sessionFactoryBean;
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(){
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setEntityManagerFactory(entityManagerFactory().getObject());
        tm.setDataSource(dataSource());
        return tm;
    }
}
*/
