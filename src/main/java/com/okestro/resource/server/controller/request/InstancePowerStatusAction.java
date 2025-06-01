package com.okestro.resource.server.controller.request;

import lombok.Getter;

@Getter
public enum InstancePowerStatusAction {
	START(0L),
	SHUTDOWN(1L),
	REBOOT(2L),
	PAUSE(3L);

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
}
