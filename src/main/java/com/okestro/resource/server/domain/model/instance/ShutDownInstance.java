package com.okestro.resource.server.domain.model.instance;

import com.okestro.resource.server.domain.InstanceEntity;
import com.okestro.resource.server.domain.enums.PowerStatus;
import com.okestro.resource.server.domain.vo.ImageSource;
import com.okestro.resource.server.domain.vo.InstanceAlias;
import com.okestro.resource.server.domain.vo.InstanceHost;
import lombok.Getter;

@Getter
public class ShutDownInstance extends UpdatedInstance {
	protected ShutDownInstance(
			String name,
			String description,
			InstanceAlias alias,
			PowerStatus powerStatus,
			InstanceHost host,
			ImageSource imageSource,
			Long flavorId,
			Long id) {
		super(name, description, alias, powerStatus, host, imageSource, flavorId, id);
	}

	public static ShutDownInstance of(Instance instance) {
		return new ShutDownInstance(
				instance.getName(),
				instance.getDescription(),
				instance.getAlias(),
				PowerStatus.SHUTDOWN,
				instance.getHost(),
				instance.getImageSource(),
				instance.getFlavorId(),
				instance.getId());
	}

	public static ShutDownInstance from(InstanceEntity entity) {
		if (entity.getPowerStatus() != PowerStatus.SHUTDOWN) {
			throw new IllegalArgumentException(
					"Instance must be in SHUTDOWN status to create ShutdownInstance");
		}

		return new ShutDownInstance(
				entity.getName(),
				entity.getDescription(),
				entity.getAlias(),
				entity.getPowerStatus(),
				entity.getHost(),
				entity.getImageSource(),
				entity.getFlavorId(),
				entity.getId());
	}

	public DeleteInstance delete() {
		return DeleteInstance.of(this);
	}
}
