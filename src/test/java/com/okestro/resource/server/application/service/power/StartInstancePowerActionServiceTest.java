package com.okestro.resource.server.application.service.power;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.okestro.resource.server.controller.request.InstancePowerStatusAction;
import com.okestro.resource.server.domain.InstanceEntity;
import com.okestro.resource.server.domain.enums.PowerStatus;
import com.okestro.resource.server.domain.enums.SourceType;
import com.okestro.resource.server.domain.model.instance.UpdatedInstance;
import com.okestro.resource.server.domain.repository.InstanceRepository;
import com.okestro.resource.server.domain.vo.ImageSource;
import com.okestro.resource.server.domain.vo.InstanceAlias;
import com.okestro.resource.server.domain.vo.InstanceHost;
import java.time.LocalDateTime;
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
class StartInstancePowerActionServiceTest {
	@Mock private InstanceRepository instanceRepository;

	private StartInstancePowerActionService startInstancePowerActionService;

	@BeforeEach
	void setUp() {
		startInstancePowerActionService = new StartInstancePowerActionService(instanceRepository);
	}

	@Test
	void support_action() {
		// when
		InstancePowerStatusAction action = startInstancePowerActionService.supportAction();

		// then
		assertEquals(InstancePowerStatusAction.START, action);
	}

	@Test
	void update_instance() {
		// given
		Long instanceId = 1L;
		PowerStatus expected = PowerStatus.RUNNING;
		LocalDateTime createdDate = LocalDateTime.now().minusMinutes(10);
		given(instanceRepository.findById(instanceId))
				.willReturn(
						Optional.of(
								InstanceEntity.builder()
										.id(instanceId)
										.name("Test Instance")
										.description("This is a test instance")
										.alias(InstanceAlias.create("test"))
										.powerStatus(PowerStatus.SHUTDOWN)
										.host(new InstanceHost("localhost"))
										.imageSource(ImageSource.create(SourceType.IMAGE, 1L))
										.flavorId(1L)
										.createdAt(createdDate)
										.updatedAt(createdDate)
										.build()));

		given(instanceRepository.save(Mockito.any(InstanceEntity.class)))
				.willAnswer(invocation -> invocation.getArgument(0));

		// when
		UpdatedInstance updatedInstance =
				startInstancePowerActionService.updateInstance(instanceId, expected);

		// then
		assertEquals(expected, updatedInstance.getPowerStatus());
		then(instanceRepository).should(times(1)).findById(instanceId);
		then(instanceRepository).should(times(1)).save(Mockito.any(InstanceEntity.class));
	}
}
