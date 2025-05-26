package com.okestro.resource.config.web.exception;

public abstract class CompressionException extends RuntimeException {
	public CompressionException(String message) {
		super(message);
	}

	public CompressionException(String message, Throwable cause) {
		super(message, cause);
	}

	public CompressionException(Throwable cause) {
		super(cause);
	}
}
