package com.okestro.resource.config.web;

import com.okestro.resource.config.web.properties.CompressionUrlProperties;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import java.util.Optional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public abstract class CompressionHttpResponse extends HttpServletResponseWrapper {
	private final CompressionUrlProperties properties;
	private final Integer minResponseSize;

	public CompressionHttpResponse(
			HttpServletResponse delegate, CompressionUrlProperties properties) {
		super(delegate);
		this.properties = properties;
		this.minResponseSize = properties.getMinResponseSize();
	}

	public Boolean isMimeTypeMatches() {
		Optional<MediaType> optionalMediaType =
				Optional.ofNullable(getContentType()).map(MediaType::parseMediaType);
		if (optionalMediaType.isPresent()) {
			MediaType type = optionalMediaType.get();
			return properties.getMimeTypes().stream()
					.anyMatch(
							mt -> {
								String mimeType = type.toString();
								return mimeType.equals(mt)
										|| mimeType.startsWith(mt + ";")
										|| mimeType.endsWith("/*")
												&& mimeType.startsWith(mt.substring(0, mt.length() - 1));
							});
		} else {
			return false;
		}
	}

	public Boolean isResponseSizeValid(Long size) {
		return size >= minResponseSize;
	}

	public void setCompressionHeaders(String encoding) {
		// Add the Vary header to indicate that the response varies based on the Accept-Encoding header
		addHeader(HttpHeaders.VARY, HttpHeaders.ACCEPT_ENCODING);
		// Set the Content-Encoding header to indicate that the response is compressed
		setHeader(HttpHeaders.CONTENT_ENCODING, encoding);
		// Remove any existing Content-Length header to enable chunked transfer
		setHeader(HttpHeaders.CONTENT_LENGTH, null);
	}
}
