package com.okestro.resource.server.domain.vo;

import com.okestro.resource.server.domain.enums.SourceType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.NonNull;

@Embeddable
public class ImageSource {
	@NonNull
	@Enumerated(EnumType.STRING)
	@Column(name = "source_type", nullable = false)
	private SourceType sourceType;

	@NonNull
	@Column(name = "source_target_id", nullable = false)
	private Long sourceTargetId;
}
