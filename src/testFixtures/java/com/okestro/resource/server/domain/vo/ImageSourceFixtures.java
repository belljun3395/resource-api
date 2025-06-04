package com.okestro.resource.server.domain.vo;

import com.okestro.resource.server.domain.enums.SourceType;
import java.util.Random;

public final class ImageSourceFixtures {
	private SourceType sourceType;
	private Long sourceTargetId;
	private String sourceName;

	private ImageSourceFixtures() {}

	public static ImageSourceFixtures anImageSource() {
		return new ImageSourceFixtures();
	}

	public ImageSourceFixtures withSourceType(SourceType sourceType) {
		this.sourceType = sourceType;
		return this;
	}

	public ImageSourceFixtures withSourceTargetId(Long sourceTargetId) {
		this.sourceTargetId = sourceTargetId;
		return this;
	}

	public ImageSourceFixtures withSourceName(String sourceName) {
		this.sourceName = sourceName;
		return this;
	}

	public ImageSource build() {
		if (sourceName != null) {
			return ImageSource.of(sourceType, sourceTargetId, sourceName);
		}
		return ImageSource.create(sourceType, sourceTargetId);
	}

	public static ImageSourceFixtures giveMeOne() {
		long id = new Random().nextInt(100) + 1; // Random ID between 1 and 100
		return anImageSource()
				.withSourceType(SourceType.IMAGE)
				.withSourceTargetId(id)
				.withSourceName("image_" + id);
	}
}
