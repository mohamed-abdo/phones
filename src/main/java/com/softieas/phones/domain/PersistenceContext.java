package com.softieas.phones.domain;

import org.h2.jdbcx.JdbcDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;
import javax.persistence.SharedCacheMode;
import javax.sql.DataSource;
import java.util.Properties;

//@Configuration
public class PersistenceContext {
    private final String[] entityPackages = {"com.softideas.phones.domain.entities"};

    @Value("${hibernate.dialect}")
    private String hibernate_dialect;
    @Value("${hibernate.format_sql}")
    private String hibernate_format_sql;
    @Value("${hibernate.hbm2ddl.auto}")
    private String hibernate_hbm2ddl_auto;
    @Value("${hibernate.ejb.naming_strategy}")
    private String hibernate_ejb_naming_strategy;
    @Value("${hibernate.show_sql}")
    private String hibernate_show_sql;


    @Bean
    public Properties jpaProperties() {
        var props = new Properties();
        props.put("hibernate.dialect", hibernate_dialect);
        props.put("hibernate.format_sql", hibernate_format_sql);
        props.put("hibernate.hbm2ddl.auto", hibernate_hbm2ddl_auto);
        props.put("hibernate.ejb.naming_strategy", hibernate_ejb_naming_strategy);
        props.put("hibernate.show_sql", hibernate_show_sql);

        return props;
    }

    @Bean
    @Primary
    @Qualifier("H2")
    public DataSource dataSource() {
        var dataSource = new EmbeddedDatabaseBuilder();
        dataSource.setType(EmbeddedDatabaseType.H2);

        return dataSource.build();
    }

    @Profile("test")
    @Qualifier("test_H2")
    @Bean
    public DataSource testDataSource() {
        var dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:file:~/phones_db;DB_CLOSE_ON_EXIT=FALSE;AUTO_SERVER=True;");
        dataSource.setUser("sa");
        dataSource.setPassword("sa");
        return dataSource;

    }

    @Bean
    @Qualifier("EntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("H2") DataSource
                                                                               dataSource, Properties jpaProperties) {
        var em = new LocalContainerEntityManagerFactoryBean();
        em.setPackagesToScan(entityPackages);
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setPersistenceUnitName("phones_domain");
        em.setSharedCacheMode(SharedCacheMode.ALL);
        em.setJpaDialect(new HibernateJpaDialect());
        em.setJpaProperties(jpaProperties);
        em.setDataSource(dataSource);
        return em;
    }

    @Bean
    public JpaTransactionManager transactionManager(@Qualifier("EntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager() {
            {
                setEntityManagerFactory(entityManagerFactory);
            }
        };
    }
}
