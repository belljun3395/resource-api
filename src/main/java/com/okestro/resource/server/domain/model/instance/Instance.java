package com.okestro.resource.server.domain.model.instance;

import com.okestro.resource.server.domain.InstanceEntity;
import com.okestro.resource.server.domain.enums.PowerStatus;
import com.okestro.resource.server.domain.vo.ImageSource;
import com.okestro.resource.server.domain.vo.InstanceAlias;
import com.okestro.resource.server.domain.vo.InstanceHost;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class Instance extends BaseInstance {
	private final Long id;
	private final LocalDateTime createdAt;
	private final LocalDateTime updatedAt;

	private Instance(
			String name,
			String description,
			InstanceAlias alias,
			PowerStatus powerStatus,
			InstanceHost host,
			ImageSource imageSource,
			Long flavorId,
			Long id,
			LocalDateTime createdAt,
			LocalDateTime updatedAt) {
		super(name, description, alias, powerStatus, host, imageSource, flavorId);
		this.id = id;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public static Instance from(InstanceEntity instanceEntity) {
		return new Instance(
				instanceEntity.getName(),
				instanceEntity.getDescription(),
				instanceEntity.getAlias(),
				instanceEntity.getPowerStatus(),
				instanceEntity.getHost(),
				instanceEntity.getImageSource(),
				instanceEntity.getFlavorId(),
				instanceEntity.getId(),
				instanceEntity.getCreatedAt(),
				instanceEntity.getUpdatedAt());
	}
}
