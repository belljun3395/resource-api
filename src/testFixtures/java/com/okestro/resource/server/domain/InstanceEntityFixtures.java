package com.okestro.resource.server.domain;

import com.okestro.resource.server.domain.enums.PowerStatus;
import com.okestro.resource.server.domain.enums.SourceType;
import com.okestro.resource.server.domain.vo.ImageSource;
import com.okestro.resource.server.domain.vo.InstanceAlias;
import com.okestro.resource.server.domain.vo.InstanceHost;
import java.time.LocalDateTime;
import java.util.Random;

public final class InstanceEntityFixtures {
	private Long id;
	private String name;
	private String description;
	private InstanceAlias alias;
	private PowerStatus powerStatus;
	private InstanceHost host;
	private Long flavorId;
	private ImageSource imageSource;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private Boolean deleted;

	private InstanceEntityFixtures() {}

	public static InstanceEntityFixtures anInstanceEntity() {
		return new InstanceEntityFixtures();
	}

	public InstanceEntityFixtures withId(Long id) {
		this.id = id;
		return this;
	}

	public InstanceEntityFixtures withName(String name) {
		this.name = name;
		return this;
	}

	public InstanceEntityFixtures withDescription(String description) {
		this.description = description;
		return this;
	}

	public InstanceEntityFixtures withAlias(InstanceAlias alias) {
		this.alias = alias;
		return this;
	}

	public InstanceEntityFixtures withPowerStatus(PowerStatus powerStatus) {
		this.powerStatus = powerStatus;
		return this;
	}

	public InstanceEntityFixtures withHost(InstanceHost host) {
		this.host = host;
		return this;
	}

	public InstanceEntityFixtures withFlavorId(Long flavorId) {
		this.flavorId = flavorId;
		return this;
	}

	public InstanceEntityFixtures withImageSource(ImageSource imageSource) {
		this.imageSource = imageSource;
		return this;
	}

	public InstanceEntityFixtures withCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
		return this;
	}

	public InstanceEntityFixtures withUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
		return this;
	}

	public InstanceEntityFixtures withDeleted(Boolean deleted) {
		this.deleted = deleted;
		return this;
	}

	public InstanceEntity build() {
		return new InstanceEntity(
				id,
				name,
				description,
				alias,
				powerStatus,
				host,
				flavorId,
				imageSource,
				createdAt,
				updatedAt,
				deleted);
	}

	public static InstanceEntityFixtures giveMeOne() {
		int id = new Random().nextInt(100);
		return anInstanceEntity()
				.withId((long) id)
				.withName("test-Instance-" + id)
				.withDescription("This is a test instance " + id)
				.withAlias(InstanceAlias.create("test" + id))
				.withPowerStatus(PowerStatus.RUNNING)
				.withHost(new InstanceHost("localhost"))
				.withFlavorId(1L)
				.withImageSource(ImageSource.create(SourceType.IMAGE, (long) id))
				.withCreatedAt(LocalDateTime.now())
				.withUpdatedAt(LocalDateTime.now())
				.withDeleted(false);
	}
}
