package com.okestro.resource.server.event.instance;

import com.okestro.resource.server.support.json.ServerActionJson;
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

		@Getter
		public abstract static class InstanceTransactionLogEvent extends InstanceTransactionEvent {
			private final String log;

			public InstanceTransactionLogEvent(Long id, ServerActionJson json) {
				super(id);
				this.log = json.getJson();
			}
		}
	}
}
