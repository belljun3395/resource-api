package com.okestro.resource.server.application.service.power;

import com.okestro.resource.server.controller.request.InstancePowerStatusAction;
import com.okestro.resource.server.domain.enums.PowerStatus;
import com.okestro.resource.server.domain.model.instance.Instance;
import com.okestro.resource.server.domain.model.instance.UpdatedInstance;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class InstancePowerActionService {
	public abstract InstancePowerStatusAction supportAction();

	Instance doExecute(Instance originInstance, PowerStatus wantPowerStatus) {
		Long instanceId = originInstance.getId();
		PowerStatus currentPowerStatus = getCurrentPowerStatus(instanceId);
		if (!isNeedToUpdate(currentPowerStatus, wantPowerStatus)) {
			log.debug(
					"Instance {} is already in the desired power status: {}", instanceId, wantPowerStatus);
			return originInstance;
		}

		return updateInstance(instanceId, wantPowerStatus);
	}

	protected abstract PowerStatus getCurrentPowerStatus(Long instanceId);

	protected boolean isNeedToUpdate(PowerStatus currentPowerStatus, PowerStatus wantPowerStatus) {
		return currentPowerStatus != wantPowerStatus;
	}

	protected abstract UpdatedInstance updateInstance(Long instanceId, PowerStatus powerStatus);
}
