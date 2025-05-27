package com.okestro.resource.config.web.gzip;

import com.okestro.resource.config.web.properties.CompressionUrlProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
@RequiredArgsConstructor
public class GzipCompressionFilter extends OncePerRequestFilter {
	private final CompressionUrlProperties properties;
	private final AntPathMatcher pathMatcher = new AntPathMatcher();

	@Override
	protected void doFilterInternal(
			HttpServletRequest servletRequest,
			HttpServletResponse servletResponse,
			FilterChain filterChain)
			throws ServletException, IOException {
		// Skip if URL-based compression is disabled
		if (!properties.isEnabled()) {
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		}

		// Check if the servletRequest path matches any of the configured patterns
		String path = servletRequest.getRequestURI();
		boolean shouldCompress =
				properties.getPatterns().stream().anyMatch(pattern -> pathMatcher.match(pattern, path));

		// Check if the client accepts gzip encoding
		boolean isGzipResponseRequired = GzipCompressionUtils.isGzipResponseRequired(servletRequest);

		// Skip if the request does not match any patterns or if the client does not accept gzip
		if (!shouldCompress || !isGzipResponseRequired) {
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		}

		// Create a gzip response wrapper
		GzipServerHttpResponse gzipResponse = new GzipServerHttpResponse(servletResponse, properties);
		filterChain.doFilter(servletRequest, gzipResponse);
	}
}
