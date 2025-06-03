package com.okestro.resource.server.domain;

import java.time.LocalDateTime;
import java.util.Random;

public final class FlavorEntityBuilder {
	private Long id;
	private String name;
	private String description;
	private Float vCpu;
	private Float memory;
	private Float rootDiskSize;
	private LocalDateTime createdAt;

	private FlavorEntityBuilder() {}

	public static FlavorEntityBuilder aFlavorEntity() {
		return new FlavorEntityBuilder();
	}

	public FlavorEntityBuilder withId(Long id) {
		this.id = id;
		return this;
	}

	public FlavorEntityBuilder withName(String name) {
		this.name = name;
		return this;
	}

	public FlavorEntityBuilder withDescription(String description) {
		this.description = description;
		return this;
	}

	public FlavorEntityBuilder withVCpu(Float vCpu) {
		this.vCpu = vCpu;
		return this;
	}

	public FlavorEntityBuilder withMemory(Float memory) {
		this.memory = memory;
		return this;
	}

	public FlavorEntityBuilder withRootDiskSize(Float rootDiskSize) {
		this.rootDiskSize = rootDiskSize;
		return this;
	}

	public FlavorEntityBuilder withCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
		return this;
	}

	public FlavorEntity build() {
		return new FlavorEntity(id, name, description, vCpu, memory, rootDiskSize, createdAt);
	}

	public FlavorEntityBuilder giveMeOne() {
		int id = new Random().nextInt(100);
		float vCpu = new Random().nextFloat(10);
		float memory = new Random().nextFloat(100);
		float rootDiskSize = new Random().nextFloat(500);
		return aFlavorEntity()
				.withId((long) id)
				.withName("test-flavor-" + id)
				.withDescription("This is a test flavor " + id)
				.withVCpu(vCpu)
				.withMemory(memory)
				.withRootDiskSize(rootDiskSize)
				.withCreatedAt(LocalDateTime.now());
	}
}
