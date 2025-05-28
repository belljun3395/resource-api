package com.okestro.resource.server.event.instance;

import static com.okestro.resource.config.AsyncConfig.DEFAULT_EXECUTOR;

import com.okestro.resource.server.event.instance.handler.InstanceCreateLogHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class InstanceTransactionEventListener {
	private final InstanceCreateLogHandler instanceCreateLogHandler;

    @Async(value = DEFAULT_EXECUTOR)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void afterCompletion(
			InstanceEvent.InstanceTransactionEvent.InstanceTransactionLogEvent event) {
		if (event
				instanceof
				InstanceEvent.InstanceTransactionEvent.InstanceTransactionLogEvent.InstanceCreateLogEvent) {
			instanceCreateLogHandler.handle(
					(InstanceEvent.InstanceTransactionEvent.InstanceTransactionLogEvent
									.InstanceCreateLogEvent)
							event);
		}
	}
}
