package com.okestro.resource.server.event.instance.handler;

import com.okestro.resource.server.domain.InstanceActiveLogEntity;
import com.okestro.resource.server.domain.repository.InstanceActiveLogRepository;
import com.okestro.resource.server.event.instance.InstanceEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InstanceUpdateLogHandler {
	private final InstanceActiveLogRepository instanceActiveLogRepository;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void handle(
			InstanceEvent.InstanceTransactionEvent.InstanceTransactionLogEvent.InstanceUpdateLogEvent
					event) {
		Long instanceId = event.getId();
		String log = event.getLog();

		InstanceActiveLogEntity logEntity =
				InstanceActiveLogEntity.builder().instanceId(instanceId).log(log).build();

		instanceActiveLogRepository.save(logEntity);
	}
}
