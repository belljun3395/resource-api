package com.okestro.resource.config;

import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
@RequiredArgsConstructor
public class MessageSourceConfig {

	// TODO: Add message source classpath paths here. eg. "classpath:messages/member"
	private static final List<String> MESSAGE_SOURCE_CLASSPATH_LIST = List.of();

	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource =
				new ReloadableResourceBundleMessageSource();

		messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
		for (String path : MESSAGE_SOURCE_CLASSPATH_LIST) {
			messageSource.addBasenames(path);
		}

		return messageSource;
	}
}
