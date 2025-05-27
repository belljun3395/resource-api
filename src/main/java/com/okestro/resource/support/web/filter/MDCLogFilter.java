package com.okestro.resource.support.web.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MDCLogFilter implements Filter {
	private final ObjectMapper mapper;

	@Override
	public void doFilter(
			ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		long requestStartTime = System.currentTimeMillis();
		SecureRandom random = new SecureRandom();
		String traceId =
				random
						.ints(10, 'a', 'z' + 1)
						.mapToObj(i -> String.valueOf((char) i))
						.collect(Collectors.joining());
		try {
			HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

			MDC.put("Type", "Request MDC Info");
			MDC.put("RequestId", httpRequest.getRequestedSessionId());
			MDC.put("Request-Remote-Address", servletRequest.getRemoteAddr());
			MDC.put("Request-URL", httpRequest.getRequestURL().toString());
			MDC.put("Request-Method", httpRequest.getMethod());
			MDC.put("TraceId", traceId);
			MDC.put(HttpHeaders.REFERER, httpRequest.getHeader(HttpHeaders.REFERER));
			MDC.put(HttpHeaders.USER_AGENT, httpRequest.getHeader(HttpHeaders.USER_AGENT));

			filterChain.doFilter(servletRequest, servletResponse);
			long requestEndTime = System.currentTimeMillis();
			long elapsedTime = requestEndTime - requestStartTime;
			MDC.put("ElapsedTime", elapsedTime + "ms");

			System.out.println(mapper.writeValueAsString(MDC.getCopyOfContextMap()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			MDC.clear();
		}
	}
}
