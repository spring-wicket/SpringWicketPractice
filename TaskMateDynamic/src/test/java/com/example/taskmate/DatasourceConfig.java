package com.example.taskmate;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

@Configuration
public class DatasourceConfig {
    @Bean
    DataSource dataSource() {
		return new TransactionAwareDataSourceProxy(
				DataSourceBuilder.create()
				.username("postgres")
				.password("postgres")
				.url("jdbc:postgresql://localhost:5432/task_db")
				.driverClassName("org.postgresql.Driver")
				.build());
	}

}
