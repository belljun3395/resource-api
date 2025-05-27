package com.okestro.resource.server.event.server;

import lombok.Getter;

@Getter
public abstract class InstanceEvent {
	private final Long id;

	public InstanceEvent(Long id) {
		this.id = id;
	}

	public abstract static class InstanceTransactionEvent extends InstanceEvent {
		public InstanceTransactionEvent(Long id) {
			super(id);
		}
	}
}
