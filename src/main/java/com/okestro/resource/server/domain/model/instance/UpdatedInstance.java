package com.okestro.resource.server.domain.model.instance;

import com.okestro.resource.server.domain.InstanceEntity;
import com.okestro.resource.server.domain.enums.PowerStatus;
import com.okestro.resource.server.domain.vo.ImageSource;
import com.okestro.resource.server.domain.vo.InstanceAlias;
import com.okestro.resource.server.domain.vo.InstanceHost;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@Getter
public class UpdatedInstance extends Instance {
	@Nullable private LocalDateTime createdAt;
	@Nullable private LocalDateTime updatedAt;

	public UpdatedInstance(
			String name,
			String description,
			InstanceAlias alias,
			PowerStatus powerStatus,
			InstanceHost host,
			ImageSource imageSource,
			Long flavorId,
			Long id) {
		// updatedAt is set to null because it is set by the repository layer
		super(name, description, alias, powerStatus, host, imageSource, flavorId, id);
	}

	public UpdatedInstance(
			String name,
			String description,
			InstanceAlias alias,
			PowerStatus powerStatus,
			InstanceHost host,
			ImageSource imageSource,
			Long flavorId,
			Long id,
			@NonNull LocalDateTime createdAt,
			@NonNull LocalDateTime updatedAt) {
		super(name, description, alias, powerStatus, host, imageSource, flavorId, id);
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public static boolean isPersisted(UpdatedInstance instance) {
		return instance.getCreatedAt() != null && instance.getUpdatedAt() != null;
	}

	/**
	 * Use this method to update the instance with a new power status. This instance is not yet
	 * persisted to the database.
	 */
	public static UpdatedInstance of(Instance instance, PowerStatus powerStatus) {
		return new UpdatedInstance(
				instance.getName(),
				instance.getDescription(),
				instance.getAlias(),
				powerStatus,
				instance.getHost(),
				instance.getImageSource(),
				instance.getFlavorId(),
				instance.getId());
	}

	public static UpdatedInstance from(InstanceEntity entity) {
		return new UpdatedInstance(
				entity.getName(),
				entity.getDescription(),
				entity.getAlias(),
				entity.getPowerStatus(),
				entity.getHost(),
				entity.getImageSource(),
				entity.getFlavorId(),
				entity.getId(),
				entity.getCreatedAt(),
				entity.getUpdatedAt());
	}
}
