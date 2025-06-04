package com.okestro.resource.server.event.instance.handler;

import com.okestro.resource.server.domain.InstanceEntity;
import com.okestro.resource.server.domain.enums.PowerStatus;
import com.okestro.resource.server.domain.repository.InstanceRepository;
import com.okestro.resource.server.event.instance.InstanceEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InstanceDeleteCommandHandler {
	private final InstanceRepository instanceRepository;

	// TODO: feat retry logic to delete instance
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void handle(InstanceEvent.InstanceTransactionEvent.DeleteInstanceCommand command) {
		InstanceEntity instance =
				instanceRepository
						.findById(command.getId())
						.orElseThrow(
								() ->
										new IllegalArgumentException("Instance not found with ID: " + command.getId()));

		if (instance.getPowerStatus() != PowerStatus.SHUTDOWN) {
			throw new IllegalStateException(
					"Instance with ID " + command.getId() + " is not in SHUTDOWN state.");
		}
		instanceRepository.delete(instance);
	}
}
