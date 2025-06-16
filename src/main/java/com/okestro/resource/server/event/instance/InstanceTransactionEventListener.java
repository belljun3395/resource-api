package com.okestro.resource.server.event.instance;

import static com.okestro.resource.config.AsyncConfig.DEFAULT_EXECUTOR;

import com.okestro.resource.server.event.instance.handler.InstanceDeleteCommandHandler;
import com.okestro.resource.server.event.instance.handler.InstanceDeleteLogHandler;
import com.okestro.resource.server.event.instance.handler.InstanceUpdateLogHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class InstanceTransactionEventListener {
	// handlers for log events
	private final InstanceUpdateLogHandler instanceUpdateLogHandler;
	private final InstanceDeleteLogHandler instanceDeleteLogHandler;

	// handler for command events
	private final InstanceDeleteCommandHandler instanceDeleteCommandHandler;

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

		if (event
				instanceof
				InstanceEvent.InstanceTransactionEvent.InstanceTransactionLogEvent.InstanceDeleteLogEvent) {
			instanceDeleteLogHandler.handle(
					(InstanceEvent.InstanceTransactionEvent.InstanceTransactionLogEvent
									.InstanceDeleteLogEvent)
							event);
		}
	}

	@Async(value = DEFAULT_EXECUTOR)
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void afterCommit(InstanceEvent.InstanceTransactionEvent.InstanceTxCommand command) {
		if (command instanceof InstanceEvent.InstanceTransactionEvent.DeleteInstanceCommand) {
			instanceDeleteCommandHandler.handle(
					(InstanceEvent.InstanceTransactionEvent.DeleteInstanceCommand) command);
		}
	}
}
