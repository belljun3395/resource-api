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

		public abstract static class InstanceTxCommand extends InstanceTransactionEvent {
			public InstanceTxCommand(Long id) {
				super(id);
			}
		}

		public static class DeleteInstanceCommand extends InstanceTxCommand {
			public DeleteInstanceCommand(Long id) {
				super(id);
			}
		}

		@Getter
		public abstract static class InstanceTransactionLogEvent extends InstanceTransactionEvent {
			private final String log;

			public InstanceTransactionLogEvent(Long id, ServerActionJson json) {
				super(id);
				this.log = json.getJson();
			}
		}

		public static class InstanceCreateLogEvent extends InstanceTransactionLogEvent {
			public InstanceCreateLogEvent(Long id, ServerActionJson json) {
				super(id, json);
			}
		}

		public static class InstanceUpdateLogEvent extends InstanceTransactionLogEvent {
			public InstanceUpdateLogEvent(Long id, ServerActionJson json) {
				super(id, json);
			}
		}

		public static class InstanceDeleteLogEvent extends InstanceTransactionLogEvent {
			public InstanceDeleteLogEvent(Long id, ServerActionJson json) {
				super(id, json);
			}
		}
	}
}
