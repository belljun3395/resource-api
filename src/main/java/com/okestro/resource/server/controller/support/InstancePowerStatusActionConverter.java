package com.okestro.resource.server.controller.support;

import com.okestro.resource.server.controller.request.InstancePowerStatusAction;
import org.springframework.core.convert.converter.Converter;

public class InstancePowerStatusActionConverter
		implements Converter<Long, InstancePowerStatusAction> {
	@Override
	public InstancePowerStatusAction convert(Long source) {
		return InstancePowerStatusAction.fromCode(source);
	}
}
