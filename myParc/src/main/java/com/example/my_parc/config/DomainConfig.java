package com.example.my_parc.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("com.example.my_parc.domain")
@EnableJpaRepositories("com.example.my_parc.repos")
@EnableTransactionManagement
public class DomainConfig {
}
