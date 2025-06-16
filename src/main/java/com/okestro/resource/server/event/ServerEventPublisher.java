package com.okestro.resource.server.event;

import com.okestro.resource.server.event.instance.InstanceEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServerEventPublisher {
	private final ApplicationEventPublisher applicationEventPublisher;

	public void publishEvent(
			InstanceEvent.InstanceTransactionEvent.InstanceTransactionLogEvent.InstanceUpdateLogEvent
					event) {
		applicationEventPublisher.publishEvent(event);
	}

	public void publishEvent(
			InstanceEvent.InstanceTransactionEvent.InstanceTransactionLogEvent.InstanceCreateLogEvent
					event) {
		applicationEventPublisher.publishEvent(event);
	}
}
