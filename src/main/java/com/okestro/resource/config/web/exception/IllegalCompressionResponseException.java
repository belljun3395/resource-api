package com.okestro.resource.config.web.exception;

public class IllegalCompressionResponseException extends CompressionException {
	public IllegalCompressionResponseException(String message) {
		super(message);
	}

	public IllegalCompressionResponseException(String message, Throwable cause) {
		super(message, cause);
	}
}
