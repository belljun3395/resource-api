package com.okestro.resource.server.domain.model.flavor;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseFlavor {
	private final String name;
	private final String description;
	private final Float vCpu;
	private final Float memory;
	private final Float rootDiskSize;
}
