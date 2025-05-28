package com.okestro.resource.server.application;

import com.okestro.resource.server.application.dto.UpdateInstancePowerUsecaseDto;
import com.okestro.resource.server.application.service.power.InstancePowerActionManager;
import com.okestro.resource.server.controller.request.InstancePowerStatusAction;
import com.okestro.resource.server.domain.model.instance.Instance;
import com.okestro.resource.server.domain.model.instance.UpdatedInstance;
import com.okestro.resource.server.domain.repository.InstanceRepository;
import com.okestro.resource.server.event.ServerEventPublisher;
import com.okestro.resource.server.event.instance.InstanceEvent;
import com.okestro.resource.server.support.json.ServerAction;
import com.okestro.resource.server.support.json.ServerActionJson;
import com.okestro.resource.server.support.json.ServerJsonConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateInstancePowerUseCase {
	private final ServerEventPublisher serverEventPublisher;
	private final ServerJsonConverter serverJsonConverter;

	private final InstanceRepository instanceRepository;
	private final InstancePowerActionManager instancePowerActionManager;

	@Transactional
	public UpdateInstancePowerUsecaseDto.UpdateInstanceUseCaseOut execute(
			UpdateInstancePowerUsecaseDto.UpdateInstancePowerUseCaseIn useCaseIn) {
		final Long instanceId = useCaseIn.getInstanceId();
		final InstancePowerStatusAction powerStatusAction = useCaseIn.getPowerStatusAction();

		final Instance instance = findInstance(instanceId);
		final Instance updatedPowerInstance =
				instancePowerActionManager.execute(instance, powerStatusAction);

		boolean isUpdated = updatedPowerInstance instanceof UpdatedInstance;
		if (isUpdated) {
			publishEvent(updatedPowerInstance);
		}

		return UpdateInstancePowerUsecaseDto.out(updatedPowerInstance, isUpdated);
	}

	private Instance findInstance(Long instanceId) {
		return Instance.from(
				instanceRepository
						.findById(instanceId)
						.orElseThrow(
								() -> new IllegalArgumentException("Instance not found with id: " + instanceId)));
	}

	private void publishEvent(Instance savedInstance) {
		ServerActionJson serverActionJson =
				serverJsonConverter.toJson(ServerAction.UPDATE, savedInstance);
		InstanceEvent.InstanceTransactionEvent.InstanceTransactionLogEvent.InstanceUpdateLogEvent
				event =
						new InstanceEvent.InstanceTransactionEvent.InstanceTransactionLogEvent
								.InstanceUpdateLogEvent(savedInstance.getId(), serverActionJson);
		serverEventPublisher.publishEvent(event);
	}
}
