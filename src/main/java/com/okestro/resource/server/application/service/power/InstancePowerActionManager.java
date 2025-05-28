package com.okestro.resource.server.application.service.power;

import com.okestro.resource.server.controller.request.InstancePowerStatusAction;
import com.okestro.resource.server.domain.enums.PowerStatus;
import com.okestro.resource.server.domain.model.instance.Instance;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InstancePowerActionManager {
	private final Map<InstancePowerStatusAction, InstancePowerActionService> instancePowerActions;

	public InstancePowerActionManager(List<InstancePowerActionService> services) {
		this.instancePowerActions =
				services.stream()
						.collect(
								Collectors.toMap(
										InstancePowerActionService::supportAction,
										Function.identity(),
										(existing, duplicate) -> {
											throw new IllegalStateException(
													"Duplicate key for supportAction: " + existing.supportAction());
										}));

		log.info(
				"InstancePowerActionManager initialized with actions: {}", instancePowerActions.keySet());
	}

	public Instance execute(Instance instance, InstancePowerStatusAction action) {
		PowerStatus wantPowerStatus = action.wantPowerStatus();
		InstancePowerActionService instancePowerActionService =
				Optional.ofNullable(instancePowerActions.getOrDefault(action, null))
						.orElseThrow(() -> new IllegalArgumentException("Unsupported action: " + action));
		log.debug(
				"{} execute: {} for instance: {}",
				instancePowerActionService.getClass().getSimpleName(),
				action,
				instance.getId());
		return instancePowerActionService.doExecute(instance, wantPowerStatus);
	}
}
