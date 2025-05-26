package com.okestro.resource.server.domain.support;

import com.okestro.resource.server.domain.vo.InstanceHost;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class InstanceHostConverter implements AttributeConverter<InstanceHost, String> {
	@Override
	public String convertToDatabaseColumn(InstanceHost instanceHost) {
		if (instanceHost == null) {
			throw new IllegalArgumentException("InstanceHost cannot be null");
		}
		return instanceHost.getValue();
	}

	@Override
	public InstanceHost convertToEntityAttribute(String s) {
		if (s == null || s.isEmpty()) {
			throw new IllegalArgumentException("InstanceHost string cannot be null or empty");
		}
		return new InstanceHost(s);
	}
}
