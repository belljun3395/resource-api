package com.okestro.resource.support.web.converter;

import java.time.LocalTime;
import org.springframework.core.convert.converter.Converter;

public class LocalTimeParamBinder implements Converter<String, LocalTime> {

	@Override
	public LocalTime convert(final String time) {
		return LocalTimeConverter.from(time);
	}
}
