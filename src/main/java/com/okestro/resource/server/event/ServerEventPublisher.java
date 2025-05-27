package com.okestro.resource.server.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServerEventPublisher {
	private final ApplicationEventPublisher applicationEventPublisher;
}
