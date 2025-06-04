package com.okestro.resource.server.application.service.power;

import com.okestro.resource.server.controller.request.InstancePowerStatusAction;
import com.okestro.resource.server.domain.enums.PowerStatus;
import lombok.experimental.UtilityClass;

@UtilityClass
public class InstancePowerStatusActionSupporter {
	public static PowerStatus getPowerStatus(InstancePowerStatusAction action) {
		if (action == null) {
			throw new IllegalArgumentException("Action cannot be null");
		}

		if (action == InstancePowerStatusAction.START) {
			return PowerStatus.RUNNING;
		}
		if (action == InstancePowerStatusAction.SHUTDOWN) {
			return PowerStatus.SHUTDOWN;
		}
		if (action == InstancePowerStatusAction.REBOOT) {
			return PowerStatus.RUNNING;
		}
		if (action == InstancePowerStatusAction.PAUSE) {
			return PowerStatus.PAUSED;
		}

		throw new IllegalArgumentException("Unsupported action: " + action);
	}
}
