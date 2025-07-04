package com.okestro.resource.server.application.service.power;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.okestro.resource.server.controller.request.InstancePowerStatusAction;
import com.okestro.resource.server.domain.InstanceEntity;
import com.okestro.resource.server.domain.InstanceEntityFixtures;
import com.okestro.resource.server.domain.enums.PowerStatus;
import com.okestro.resource.server.domain.model.instance.UpdatedInstance;
import com.okestro.resource.server.domain.repository.InstanceRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class ShutdownInstancePowerActionServiceTest {
	@Mock private InstanceRepository instanceRepository;

	private ShutdownInstancePowerActionService shutdownInstancePowerActionService;

	@BeforeEach
	void setUp() {
		shutdownInstancePowerActionService = new ShutdownInstancePowerActionService(instanceRepository);
	}

	@Test
	void support_action() {
		// when
		InstancePowerStatusAction action = shutdownInstancePowerActionService.supportAction();

		// then
		assertEquals(InstancePowerStatusAction.SHUTDOWN, action);
	}

	@Test
	void update_instance() {
		// given
		Long instanceId = 1L;
		PowerStatus expected = PowerStatus.SHUTDOWN;
		given(instanceRepository.findById(instanceId))
				.willReturn(Optional.of(InstanceEntityFixtures.giveMeOne().withId(instanceId).build()));

		given(instanceRepository.save(Mockito.any(InstanceEntity.class)))
				.willAnswer(invocation -> invocation.getArgument(0));

		// when
		UpdatedInstance updatedInstance =
				shutdownInstancePowerActionService.updateInstance(instanceId, expected);

		// then
		assertEquals(expected, updatedInstance.getPowerStatus());
		then(instanceRepository).should(times(1)).findById(instanceId);
		then(instanceRepository).should(times(1)).save(Mockito.any(InstanceEntity.class));
	}
}
