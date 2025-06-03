package com.okestro.resource.server.domain;

import java.time.LocalDateTime;
import java.util.Random;

public final class InstanceActiveLogEntityFixtures {
	private Long id;
	private Long instanceId;
	private String log;
	private LocalDateTime createdAt;

	private InstanceActiveLogEntityFixtures() {}

	public static InstanceActiveLogEntityFixtures anInstanceActiveLogEntity() {
		return new InstanceActiveLogEntityFixtures();
	}

	public InstanceActiveLogEntityFixtures withId(Long id) {
		this.id = id;
		return this;
	}

	public InstanceActiveLogEntityFixtures withInstanceId(Long instanceId) {
		this.instanceId = instanceId;
		return this;
	}

	public InstanceActiveLogEntityFixtures withLog(String log) {
		this.log = log;
		return this;
	}

	public InstanceActiveLogEntityFixtures withCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
		return this;
	}

	public InstanceActiveLogEntity build() {
		return InstanceActiveLogEntity.builder()
				.id(id)
				.instanceId(instanceId)
				.log(log)
				.createdAt(createdAt)
				.build();
	}

	public static InstanceActiveLogEntityFixtures giveMeOne() {
		int id = new Random().nextInt(100);
		return anInstanceActiveLogEntity()
				.withId((long) id)
				.withInstanceId((long) id)
				.withLog("log message " + id)
				.withCreatedAt(LocalDateTime.now());
	}
}
