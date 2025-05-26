package com.okestro.resource.support.web.converter;

import java.time.LocalDateTime;
import org.springframework.core.convert.converter.Converter;

public class LocalDateTimeParamBinder implements Converter<String, LocalDateTime> {

	@Override
	public LocalDateTime convert(String dateTime) {
		return LocalDateTimeConverter.from(dateTime);
	}
}
