package com.okestro.resource.server.domain.model.flavor;

import java.time.LocalDateTime;
import java.util.Random;

public final class FlavorFixtures {
	private Long id;
	private LocalDateTime createdAt;
	private String name;
	private String description;
	private Float vCpu;
	private Float memory;
	private Float rootDiskSize;

	private FlavorFixtures() {}

	public static FlavorFixtures aFlavor() {
		return new FlavorFixtures();
	}

	public FlavorFixtures withId(Long id) {
		this.id = id;
		return this;
	}

	public FlavorFixtures withCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
		return this;
	}

	public FlavorFixtures withName(String name) {
		this.name = name;
		return this;
	}

	public FlavorFixtures withDescription(String description) {
		this.description = description;
		return this;
	}

	public FlavorFixtures withVCpu(Float vCpu) {
		this.vCpu = vCpu;
		return this;
	}

	public FlavorFixtures withMemory(Float memory) {
		this.memory = memory;
		return this;
	}

	public FlavorFixtures withRootDiskSize(Float rootDiskSize) {
		this.rootDiskSize = rootDiskSize;
		return this;
	}

	public Flavor build() {
		return new Flavor(name, description, vCpu, memory, rootDiskSize, id, createdAt);
	}

	public static FlavorFixtures giveMeOne() {
		int id = new Random().nextInt(100) + 1; // Random ID between 1 and 100
		float vCpu = new Random().nextFloat(10) + 1; // // Random vCPU between 1 and 10
		float memory = new Random().nextFloat(100) + 1; // // Random memory between 1 and 100 GB
		float rootDiskSize =
				new Random().nextFloat(500) + 1; // // Random root disk size between 1 and 500 GB
		return aFlavor()
				.withId((long) id)
				.withCreatedAt(LocalDateTime.now())
				.withName("Flavor " + id)
				.withDescription("Description for flavor " + id)
				.withVCpu(vCpu)
				.withMemory(memory)
				.withRootDiskSize(rootDiskSize);
	}
}
