package com.okestro.resource.config;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {
	private static final String DATASOURCE = "dataSource";

	@Bean(name = DATASOURCE)
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	public DataSource getDataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}
}
