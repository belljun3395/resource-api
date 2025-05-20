package com.okestro.resource.config;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class FlywayConfig {
	public static final String FLYWAY = "flyway";
	public static final String FLYWAY_VALIDATE_INITIALIZER = "flywayValidateInitializer";
	public static final String FLYWAY_MIGRATION_INITIALIZER = "flywayMigrationInitializer";
	public static final String FLYWAY_PROPERTIES = "flywayProperties";
	public static final String FLYWAY_CONFIGURATION = "flywayConfiguration";

	@Bean(name = FLYWAY)
	public Flyway flyway(org.flywaydb.core.api.configuration.Configuration configuration) {
		return new Flyway(configuration);
	}

	@Profile("!new")
	@Bean(name = FLYWAY_VALIDATE_INITIALIZER)
	public FlywayMigrationInitializer flywayValidateInitializer(Flyway flyway) {
		return new FlywayMigrationInitializer(flyway, Flyway::validate);
	}

	@Bean(name = FLYWAY_MIGRATION_INITIALIZER)
	public FlywayMigrationInitializer flywayMigrationInitializer(Flyway flyway) {
		return new FlywayMigrationInitializer(flyway, Flyway::migrate);
	}

	@Bean(name = FLYWAY_PROPERTIES)
	@ConfigurationProperties(prefix = "spring.flyway")
	public FlywayProperties flywayProperties() {
		return new FlywayProperties();
	}

	@Bean(name = FLYWAY_CONFIGURATION)
	public org.flywaydb.core.api.configuration.Configuration configuration() {
		FluentConfiguration configuration = new FluentConfiguration();
		configuration.dataSource(url, username, password);

		for (String location : flywayProperties().getLocations()) {
			configuration.locations(location);
		}

		return configuration;
	}

	// ----------------- Database -----------------
	@Value("${spring.datasource.write.hikari.jdbcUrl}")
	private String url;

	@Value("${spring.datasource.write.hikari.username}")
	private String username;

	@Value("${spring.datasource.write.hikari.password}")
	private String password;
}
