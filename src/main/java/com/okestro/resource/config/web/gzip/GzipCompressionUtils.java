package com.okestro.resource.config.web.gzip;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

@Slf4j
@UtilityClass
public class GzipCompressionUtils {
	public static final String GZIP = "gzip";

	public static boolean isGzipRequest(HttpServletRequest request) {
		boolean containsGzip = containsGzip(request, HttpHeaders.CONTENT_ENCODING);
		if (containsGzip) {
			log.debug("Gzip decompression enabled for request");
		}
		return containsGzip;
	}

	public static boolean isGzipResponseRequired(HttpServletRequest request) {
		boolean containsGzip = containsGzip(request, HttpHeaders.ACCEPT_ENCODING);
		if (containsGzip) {
			log.debug("Gzip compression enabled for response");
		}
		return containsGzip;
	}

	private static boolean containsGzip(HttpServletRequest request, String headerName) {
		String header = request.getHeader(headerName);
		return header != null && header.toLowerCase().contains(GZIP);
	}
}
