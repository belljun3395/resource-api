package com.okestro.resource.server.domain.support;

import com.okestro.resource.server.domain.vo.InstanceAlias;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class InstanceAliasConverter implements AttributeConverter<InstanceAlias, String> {
	@Override
	public String convertToDatabaseColumn(InstanceAlias instanceAlias) {
		if (instanceAlias == null) {
			throw new IllegalArgumentException("ImageName cannot be null");
		}
		return instanceAlias.getValue();
	}

	@Override
	public InstanceAlias convertToEntityAttribute(String s) {
		if (s == null || s.isEmpty()) {
			throw new IllegalArgumentException("ImageName string cannot be null or empty");
		}
		return InstanceAlias.exist(s);
	}
}
