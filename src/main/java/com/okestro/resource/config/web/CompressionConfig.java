package com.okestro.resource.config.web;

import com.okestro.resource.config.web.gzip.GzipCompressionFilter;
import com.okestro.resource.config.web.gzip.GzipDecompressionFilter;
import com.okestro.resource.config.web.properties.CompressionUrlProperties;
import com.okestro.resource.config.web.properties.DecompressionRequestProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CompressionConfig {
	@Bean
	@ConfigurationProperties(prefix = "server.compression.url")
	public CompressionUrlProperties compressionUrlProperties() {
		return new CompressionUrlProperties();
	}

	@Bean
	@ConfigurationProperties(prefix = "server.decompression.request")
	public DecompressionRequestProperties decompressionRequestProperties() {
		return new DecompressionRequestProperties();
	}

	@Bean
	@ConditionalOnProperty(prefix = "server.compression.url", name = "enabled", havingValue = "true")
	public GzipCompressionFilter gzipCompressionFilter(CompressionUrlProperties properties) {
		return new GzipCompressionFilter(properties);
	}

	@Bean
	@ConditionalOnProperty(prefix = "server.compression.url", name = "enabled", havingValue = "true")
	public GzipDecompressionFilter gzipDecompressionFilter() {
		return new GzipDecompressionFilter();
	}
}
