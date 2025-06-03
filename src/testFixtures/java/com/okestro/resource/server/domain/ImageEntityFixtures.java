package com.okestro.resource.server.domain;

import java.time.LocalDateTime;
import java.util.Random;

public final class ImageEntityFixtures {
	private Long id;
	private String name;
	private LocalDateTime createdAt;

	private ImageEntityFixtures() {}

	public static ImageEntityFixtures anImageEntity() {
		return new ImageEntityFixtures();
	}

	public ImageEntityFixtures withId(Long id) {
		this.id = id;
		return this;
	}

	public ImageEntityFixtures withName(String name) {
		this.name = name;
		return this;
	}

	public ImageEntityFixtures withCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
		return this;
	}

	public ImageEntity build() {
		return ImageEntity.builder().id(id).name(name).createdAt(createdAt).build();
	}

	public static ImageEntityFixtures giveMeOne() {
		int id = new Random().nextInt(100);
		return anImageEntity()
				.withId((long) id)
				.withName("image_" + id)
				.withCreatedAt(LocalDateTime.now());
	}
}
