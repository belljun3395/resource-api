package com.okestro.resource.server.controller.request;

import com.okestro.resource.server.domain.enums.PowerStatus;
import lombok.Getter;

@Getter
public enum InstancePowerStatusAction {
	START(0L) {
		@Override
		public PowerStatus expectedPowerStatus() {
			return PowerStatus.RUNNING;
		}
	},
	SHUTDOWN(1L) {
		@Override
		public PowerStatus expectedPowerStatus() {
			return PowerStatus.SHUTDOWN;
		}
	},
	REBOOT(2L) {
		@Override
		public PowerStatus expectedPowerStatus() {
			return PowerStatus.RUNNING;
		}
	},
	PAUSE(3L) {
		@Override
		public PowerStatus expectedPowerStatus() {
			return PowerStatus.PAUSED;
		}
	};

	private final Long actionCode;

	InstancePowerStatusAction(Long actionCode) {
		this.actionCode = actionCode;
	}

	public static InstancePowerStatusAction fromCode(Long code) {
		for (InstancePowerStatusAction action : values()) {
			if (action.getActionCode().equals(code)) {
				return action;
			}
		}
		throw new IllegalArgumentException("Invalid power status code: " + code);
	}

	public abstract PowerStatus expectedPowerStatus();
}
