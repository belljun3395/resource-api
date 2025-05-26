package com.okestro.resource.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.Locale;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.support.RequestContext;

/**
 * Swagger
 *
 * <p>Provide detailed explanations based on comments. * @see <a
 * href="http://localhost:8080/webjars/swagger-ui/index.html">Local Swagger UI</a>
 */
@Configuration
@Profile("!test")
public class SpringDocConfig {
	public static final String AUTH_TOKEN_KEY = "Authorization";

	private final BuildProperties buildProperties;
	private final Environment environment;

	public SpringDocConfig(BuildProperties buildProperties, Environment environment) {
		this.buildProperties = buildProperties;
		this.environment = environment;

		SpringDocUtils.getConfig().addRequestWrapperToIgnore(RequestContext.class);
	}

	@Bean
	public OpenAPI openApi() {
		String[] activeProfiles = environment.getActiveProfiles();
		String profiles = java.util.Arrays.toString(activeProfiles);
		String url = profiles.contains("local") ? "http://localhost:8080" : "https://api.example.com";

		SecurityRequirement securityRequirement = new SecurityRequirement().addList(AUTH_TOKEN_KEY);
		return new OpenAPI()
				.components(authSetting())
				.security(java.util.Collections.singletonList(securityRequirement))
				.addServersItem(new Server().url(url))
				.info(
						new Info()
								.title(buildProperties.getName())
								.version(buildProperties.getVersion())
								.description(
										buildProperties.getName().toUpperCase(Locale.getDefault()) + " API Docs"));
	}

	private Components authSetting() {
		return new Components()
				.addSecuritySchemes(
						AUTH_TOKEN_KEY,
						new SecurityScheme()
								.description("Access Token")
								.type(SecurityScheme.Type.APIKEY)
								.in(SecurityScheme.In.HEADER)
								.name(AUTH_TOKEN_KEY));
	}
}
