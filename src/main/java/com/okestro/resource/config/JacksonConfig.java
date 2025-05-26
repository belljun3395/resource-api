package com.okestro.resource.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.okestro.resource.support.web.converter.LocalDateJsonConverter;
import com.okestro.resource.support.web.converter.LocalDateTimeJsonConverter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.hash.Jackson2HashMapper;

@Configuration
public class JacksonConfig {

	/**
	 * this Bean can customize spring boot auto-configured ObjectMapper. you can also customize via
	 * yml configuration. spring.jackson.x
	 */
	@Bean
	public Jackson2ObjectMapperBuilderCustomizer customizeJson() {
		return builder ->
				builder
						.failOnUnknownProperties(false)
						.serializationInclusion(JsonInclude.Include.NON_ABSENT)
						.modules(javaTimeModule())
						.featuresToEnable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
						.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}

	private SimpleModule javaTimeModule() {
		return new JavaTimeModule()
				.addSerializer(LocalDateTime.class, new LocalDateTimeJsonConverter.Serializer())
				.addDeserializer(LocalDateTime.class, new LocalDateTimeJsonConverter.Deserializer())
				.addSerializer(LocalDate.class, new LocalDateJsonConverter.Serializer())
				.addDeserializer(LocalDate.class, new LocalDateJsonConverter.Deserializer());
	}

	@Bean
	public Jackson2HashMapper jackson2HashMapper(ObjectMapper objectMapper) {
		return new Jackson2HashMapper(objectMapper, true);
	}
}
