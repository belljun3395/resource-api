package com.okestro.resource.server.domain.vo;

import java.net.InetAddress;
import java.net.UnknownHostException;
import lombok.Getter;

@Getter
public class InstanceHost {
	private final String value;

	public InstanceHost(String value) {
		validateInstanceHost(value);
		this.value = value;
	}

	private static void validateInstanceHost(String value) {
		if (value == null || value.trim().isEmpty()) {
			throw new IllegalArgumentException("host cannot be null or empty");
		}
		if (value.length() > 255) {
			throw new IllegalArgumentException(
					"host length must be less than or equal to 255 characters");
		}
		try {
			InetAddress.getByName(value);
		} catch (UnknownHostException e) {
			throw new IllegalArgumentException("invalid host: " + value, e);
		}
	}
}
