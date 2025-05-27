package com.okestro.resource.config.web.gzip;

import static com.okestro.resource.config.web.gzip.GzipCompressionUtils.isGzipRequest;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GzipDecompressionFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(
			HttpServletRequest servletRequest,
			HttpServletResponse servletResponse,
			FilterChain filterChain)
			throws ServletException, IOException {
		// Skip if the request is not a Gzip request
		if (!isGzipRequest(servletRequest)) {
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		}

		GzipServerHttpRequest gzipRequest = new GzipServerHttpRequest(servletRequest);
		filterChain.doFilter(gzipRequest, servletResponse);
	}
}
