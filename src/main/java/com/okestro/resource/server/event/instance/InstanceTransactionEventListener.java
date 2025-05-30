package com.okestro.resource.server.event.instance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class InstanceTransactionEventListener {
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
	public void afterCompletion(
			InstanceEvent.InstanceTransactionEvent.InstanceTransactionLogEvent event) {}
}
