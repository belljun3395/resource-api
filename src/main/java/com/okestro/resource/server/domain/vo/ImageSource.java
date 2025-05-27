package com.okestro.resource.server.domain.vo;

import com.okestro.resource.server.domain.enums.SourceType;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Embeddable
public class ImageSource {
	@NonNull
	@Enumerated(EnumType.STRING)
	@Column(name = "source_type", nullable = false)
	private SourceType sourceType;

	@NonNull
	@Column(name = "source_target_id", nullable = false)
	private Long sourceTargetId;

	@Nullable @Transient private String sourceName;

	public static ImageSource create(SourceType sourceType, Long sourceTargetId) {
		return new ImageSource(sourceType, sourceTargetId, null);
	}

	public static ImageSource of(SourceType sourceType, Long sourceTargetId, String sourceName) {
		return new ImageSource(sourceType, sourceTargetId, sourceName);
	}
}
