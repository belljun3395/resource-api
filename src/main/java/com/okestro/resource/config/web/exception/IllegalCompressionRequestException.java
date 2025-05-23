package com.okestro.resource.config.web.exception;

public class IllegalCompressionRequestException extends CompressionException {
	public IllegalCompressionRequestException(String message) {
		super(message);
	}

	public IllegalCompressionRequestException(String message, Throwable cause) {
		super(message, cause);
	}
}
