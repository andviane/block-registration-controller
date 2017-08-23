package com.smartvalor.db;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * The persistence configuration for relational database.
 */
@EnableAutoConfiguration
@EnableJpaRepositories(basePackageClasses = { Person.class })
@EnableTransactionManagement
@Configuration
@PropertySource("classpath:application.properties")
public class Persistence {

}
