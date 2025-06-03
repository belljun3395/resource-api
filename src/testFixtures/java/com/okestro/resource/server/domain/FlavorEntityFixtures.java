package com.okestro.resource.server.domain;

import java.time.LocalDateTime;
import java.util.Random;

public final class FlavorEntityFixtures {
	private Long id;
	private String name;
	private String description;
	private Float vCpu;
	private Float memory;
	private Float rootDiskSize;
	private LocalDateTime createdAt;

	private FlavorEntityFixtures() {}

	public static FlavorEntityFixtures aFlavorEntity() {
		return new FlavorEntityFixtures();
	}

	public FlavorEntityFixtures withId(Long id) {
		this.id = id;
		return this;
	}

	public FlavorEntityFixtures withName(String name) {
		this.name = name;
		return this;
	}

	public FlavorEntityFixtures withDescription(String description) {
		this.description = description;
		return this;
	}

	public FlavorEntityFixtures withVCpu(Float vCpu) {
		this.vCpu = vCpu;
		return this;
	}

	public FlavorEntityFixtures withMemory(Float memory) {
		this.memory = memory;
		return this;
	}

	public FlavorEntityFixtures withRootDiskSize(Float rootDiskSize) {
		this.rootDiskSize = rootDiskSize;
		return this;
	}

	public FlavorEntityFixtures withCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
		return this;
	}

	public FlavorEntity build() {
		return new FlavorEntity(id, name, description, vCpu, memory, rootDiskSize, createdAt);
	}

	public static FlavorEntityFixtures giveMeOne() {
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
