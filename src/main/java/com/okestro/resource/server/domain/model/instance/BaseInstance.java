package com.okestro.resource.server.domain.model.instance;

import com.okestro.resource.server.domain.enums.PowerStatus;
import com.okestro.resource.server.domain.vo.ImageSource;
import com.okestro.resource.server.domain.vo.InstanceAlias;
import com.okestro.resource.server.domain.vo.InstanceHost;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseInstance {
	private final String name;
	private final String description;
	private final InstanceAlias alias;
	private final PowerStatus powerStatus;
	private final InstanceHost host;
	private final ImageSource imageSource;
	private final Long flavorId;
}
