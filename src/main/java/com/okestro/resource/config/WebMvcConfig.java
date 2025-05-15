package com.okestro.resource.config;

import com.okestro.resource.support.web.converter.*;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new LocalDateParamBinder());
		registry.addConverter(new LocalTimeParamBinder());
		registry.addConverter(new LocalDateTimeParamBinder());
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new PageableHandlerMethodArgumentResolver());
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry
				.addMapping("/**")
				.allowedOriginPatterns(CorsConfiguration.ALL)
				.allowedMethods(CorsConfiguration.ALL)
				.allowedHeaders(CorsConfiguration.ALL)
				.allowCredentials(true)
				.maxAge(3600);
	}

	@Override
	public void addResourceHandlers(
			org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry registry) {
		registry
				.addResourceHandler("/**")
				.addResourceLocations("classpath:/static/")
				.setCachePeriod(3600);
	}
}
