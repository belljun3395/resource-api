package com.okestro.resource.server.application;

import static com.okestro.resource.server.application.dto.DeleteInstanceUseCaseDto.out;

import com.okestro.resource.server.application.dto.DeleteInstanceUseCaseDto;
import com.okestro.resource.server.domain.InstanceEntity;
import com.okestro.resource.server.domain.model.instance.DeleteInstance;
import com.okestro.resource.server.domain.model.instance.Instance;
import com.okestro.resource.server.domain.model.instance.ShutDownInstance;
import com.okestro.resource.server.domain.repository.InstanceRepository;
import com.okestro.resource.server.event.ServerEventPublisher;
import com.okestro.resource.server.event.instance.InstanceEvent;
import com.okestro.resource.server.support.json.ServerAction;
import com.okestro.resource.server.support.json.ServerActionJson;
import com.okestro.resource.server.support.json.ServerJsonConverter;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteInstanceUseCase {
	private final ServerEventPublisher serverEventPublisher;
	private final ServerJsonConverter serverJsonConverter;

	private final InstanceRepository instanceRepository;

	@Transactional
	public DeleteInstanceUseCaseDto.DeleteInstanceUseCaseOut execute(
			DeleteInstanceUseCaseDto.DeleteInstanceUseCaseIn useCaseIn) {
		final Long instanceId = useCaseIn.getInstanceId();

		Instance instance = findInstance(instanceId);
		ShutDownInstance shutDownInstance = instance.shutdown();
		ShutDownInstance shutDownedInstance =
				ShutDownInstance.from(instanceRepository.save(InstanceEntity.updateTo(shutDownInstance)));

		DeleteInstance deleteInstance = deleteInstance(shutDownedInstance);

		if (!deleteInstance.getIsDeleted()) {
			publishEvent(ServerAction.UPDATE, deleteInstance);
		} else {
			publishEvent(ServerAction.DELETE, deleteInstance);
		}

		return out(deleteInstance);
	}

	private Instance findInstance(Long instanceId) {
		return Instance.from(
				instanceRepository
						.findById(instanceId)
						.orElseThrow(
								() -> new IllegalArgumentException("Instance not found with id: " + instanceId)));
	}

	public DeleteInstance deleteInstance(ShutDownInstance shutDownedInstance) {
		DeleteInstance deleteInstance = shutDownedInstance.delete();
		try {
			// If delete with changed power status, it has to use update method.
			// Delete method only changes the deleted flag.
			instanceRepository.save(InstanceEntity.updateTo(deleteInstance));
		} catch (Exception e) {
			log.warn(
					"Failed to delete instance with id: {}, error: {}",
					deleteInstance.getId(),
					e.getMessage());
			deleteInstance.failToDelete();
		}
		return deleteInstance;
	}

	private void publishEvent(ServerAction serverAction, Instance instance) {
		ServerActionJson serverActionJson = serverJsonConverter.toJson(serverAction, instance);
		publishLogEvent(serverAction, instance, serverActionJson);
		if (serverAction == ServerAction.UPDATE) {
			publishDeleteInstanceCommandForRetry(instance);
		}
	}

	private void publishLogEvent(
			ServerAction serverAction, Instance instance, ServerActionJson serverActionJson) {
		InstanceEvent.InstanceTransactionEvent.InstanceTransactionLogEvent logEvent = null;
		if (serverAction == ServerAction.UPDATE) {
			logEvent =
					new InstanceEvent.InstanceTransactionEvent.InstanceTransactionLogEvent
							.InstanceUpdateLogEvent(instance.getId(), serverActionJson);
		}
		if (serverAction == ServerAction.DELETE) {
			logEvent =
					new InstanceEvent.InstanceTransactionEvent.InstanceTransactionLogEvent
							.InstanceDeleteLogEvent(instance.getId(), serverActionJson);
		}

		Objects.requireNonNull(
				logEvent, "Log event should not be null for server action: " + serverAction);
		serverEventPublisher.publishEvent(logEvent);
	}

	private void publishDeleteInstanceCommandForRetry(Instance instance) {
		InstanceEvent.InstanceTransactionEvent.DeleteInstanceCommand deleteInstanceCommandForRetry =
				new InstanceEvent.InstanceTransactionEvent.DeleteInstanceCommand(instance.getId());
		serverEventPublisher.publishEvent(deleteInstanceCommandForRetry);
	}
}
