package com.okestro.resource.support.web.filter;

import com.okestro.resource.support.web.support.ApiRateLimiter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {
	private static final String X_FORWARDED_FOR = "X-Forwarded-For";
	private static final String KEY_PREFIX = "rate::limit::";
	private final ApiRateLimiter apiRateLimiter;

	@Override
	protected void doFilterInternal(
			HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String clientIp = extractClientIp(request);
		String key = KEY_PREFIX + clientIp;

		try {
			if (apiRateLimiter.tryConsume(key)) {
				filterChain.doFilter(request, response);
			} else {
				response.setStatus(429);
				response.getWriter().write("Too many requests");
			}
		} catch (Exception e) {
			response.setStatus(500);
			response.getWriter().write("Rate limiter error");
		}
	}

	private String extractClientIp(HttpServletRequest request) {
		String forwarded = request.getHeader(X_FORWARDED_FOR);
		return (forwarded != null && !forwarded.isBlank())
				? forwarded.split(",")[0]
				: request.getRemoteAddr();
	}
}
