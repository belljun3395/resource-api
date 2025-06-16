package com.okestro.resource.server.domain.model.instance;

import com.okestro.resource.server.domain.InstanceEntity;
import com.okestro.resource.server.domain.enums.PowerStatus;
import com.okestro.resource.server.domain.vo.ImageSource;
import com.okestro.resource.server.domain.vo.InstanceAlias;
import com.okestro.resource.server.domain.vo.InstanceHost;
import lombok.Getter;

@Getter
public class Instance extends BaseInstance {
	private final Long id;

	protected Instance(
			String name,
			String description,
			InstanceAlias alias,
			PowerStatus powerStatus,
			InstanceHost host,
			ImageSource imageSource,
			Long flavorId,
			Long id) {
		super(name, description, alias, powerStatus, host, imageSource, flavorId);
		this.id = id;
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
				instanceEntity.getId());
	}

	public UpdatedInstance start() {
		return updatePowerStatus(PowerStatus.RUNNING);
	}

	public ShutDownInstance shutdown() {
		return ShutDownInstance.of(this);
	}

	public UpdatedInstance pause() {
		return updatePowerStatus(PowerStatus.PAUSED);
	}

	private UpdatedInstance updatePowerStatus(PowerStatus powerStatus) {
		return UpdatedInstance.of(this, powerStatus);
	}
}
