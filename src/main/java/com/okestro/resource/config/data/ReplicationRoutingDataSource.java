package com.okestro.resource.config.data;

import static org.springframework.transaction.support.TransactionSynchronizationManager.isActualTransactionActive;
import static org.springframework.transaction.support.TransactionSynchronizationManager.isCurrentTransactionReadOnly;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		if (isActualTransactionActive() && isCurrentTransactionReadOnly()) {
			return DatabaseType.READ;
		}

		return DatabaseType.WRITE;
	}

	public enum DatabaseType {
		READ,
		WRITE,
	}
}
