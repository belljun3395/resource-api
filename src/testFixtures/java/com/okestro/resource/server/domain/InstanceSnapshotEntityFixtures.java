package com.okestro.resource.server.domain;

import java.time.LocalDateTime;
import java.util.Random;

public final class InstanceSnapshotEntityFixtures {
	private Long id;
	private Long instanceId;
	private String name;
	private LocalDateTime createdAt;

	private InstanceSnapshotEntityFixtures() {}

	public static InstanceSnapshotEntityFixtures anInstanceSnapshotEntity() {
		return new InstanceSnapshotEntityFixtures();
	}

	public InstanceSnapshotEntityFixtures withId(Long id) {
		this.id = id;
		return this;
	}

	public InstanceSnapshotEntityFixtures withInstanceId(Long instanceId) {
		this.instanceId = instanceId;
		return this;
	}

	public InstanceSnapshotEntityFixtures withName(String name) {
		this.name = name;
		return this;
	}

	public InstanceSnapshotEntityFixtures withCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
		return this;
	}

	public InstanceSnapshotEntity build() {
		return InstanceSnapshotEntity.builder()
				.id(id)
				.instanceId(instanceId)
				.name(name)
				.createdAt(createdAt)
				.build();
	}

	public static InstanceSnapshotEntityFixtures giveMeOne() {
		int id = new Random().nextInt(100) + 1; // Random ID between 1 and 100
		return anInstanceSnapshotEntity()
				.withId((long) id)
				.withInstanceId((long) id)
				.withName("snapshot_" + id)
				.withCreatedAt(LocalDateTime.now());
	}
}
