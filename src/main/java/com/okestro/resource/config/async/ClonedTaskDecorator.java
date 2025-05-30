package com.okestro.resource.config.async;

import java.util.Map;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

public class ClonedTaskDecorator implements TaskDecorator {
	@Override
	public Runnable decorate(Runnable runnable) {
		Map<String, String> contextMap = MDC.getCopyOfContextMap();
		return () -> {
			if (contextMap != null) {
				MDC.setContextMap(contextMap);
			}
			try {
				runnable.run();
			} finally {
				MDC.clear();
			}
		};
	}
}
