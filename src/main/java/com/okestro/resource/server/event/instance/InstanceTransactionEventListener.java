package com.okestro.resource.server.event.instance;

import static com.okestro.resource.config.AsyncConfig.DEFAULT_EXECUTOR;

import com.okestro.resource.server.event.instance.handler.InstanceUpdateLogHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class InstanceTransactionEventListener {
	private final InstanceUpdateLogHandler instanceUpdateLogHandler;

	@Async(value = DEFAULT_EXECUTOR)
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
	public void afterCompletion(
			InstanceEvent.InstanceTransactionEvent.InstanceTransactionLogEvent event) {
		if (event
				instanceof
				InstanceEvent.InstanceTransactionEvent.InstanceTransactionLogEvent.InstanceUpdateLogEvent) {
			instanceUpdateLogHandler.handle(
					(InstanceEvent.InstanceTransactionEvent.InstanceTransactionLogEvent
									.InstanceUpdateLogEvent)
							event);
		}
	}
}
