package com.okestro.resource.server.application.service.power;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

import com.okestro.resource.server.controller.request.InstancePowerStatusAction;
import com.okestro.resource.server.domain.InstanceEntity;
import com.okestro.resource.server.domain.enums.PowerStatus;
import com.okestro.resource.server.domain.enums.SourceType;
import com.okestro.resource.server.domain.model.instance.Instance;
import com.okestro.resource.server.domain.model.instance.UpdatedInstance;
import com.okestro.resource.server.domain.vo.ImageSource;
import com.okestro.resource.server.domain.vo.InstanceAlias;
import com.okestro.resource.server.domain.vo.InstanceHost;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class InstancePowerActionManagerTest {
	@Mock private StartInstancePowerActionService startInstancePowerActionService;
	@Mock private ShutdownInstancePowerActionService shutdownInstancePowerActionService;
	@Mock private PauseInstancePowerActionService pauseInstancePowerActionService;
	@Mock private RebootInstancePowerActionService rebootInstancePowerActionService;

	private InstancePowerActionManager instancePowerActionManager;

	@BeforeEach
	void setUp() {
		given(startInstancePowerActionService.supportAction())
				.willReturn(InstancePowerStatusAction.START);
		given(shutdownInstancePowerActionService.supportAction())
				.willReturn(InstancePowerStatusAction.SHUTDOWN);
		given(pauseInstancePowerActionService.supportAction())
				.willReturn(InstancePowerStatusAction.PAUSE);
		given(rebootInstancePowerActionService.supportAction())
				.willReturn(InstancePowerStatusAction.REBOOT);

		instancePowerActionManager =
				new InstancePowerActionManager(
						List.of(
								startInstancePowerActionService,
								shutdownInstancePowerActionService,
								pauseInstancePowerActionService,
								rebootInstancePowerActionService));
	}

	@Test
	void should_select_start_service_when_action_is_start() {
		// given
		Instance instance = createTestInstance(PowerStatus.SHUTDOWN);
		InstancePowerStatusAction action = InstancePowerStatusAction.START;

		Instance expectedInstance = UpdatedInstance.of(instance, action.wantPowerStatus());
		given(startInstancePowerActionService.doExecute(any(Instance.class), any(PowerStatus.class)))
				.willReturn(expectedInstance);

		// when
		Instance result = instancePowerActionManager.execute(instance, action);

		// then
		then(startInstancePowerActionService)
				.should(times(1))
				.doExecute(any(Instance.class), eq(action.wantPowerStatus()));
		then(shutdownInstancePowerActionService)
				.should(never())
				.doExecute(any(Instance.class), any(PowerStatus.class));
		then(pauseInstancePowerActionService)
				.should(never())
				.doExecute(any(Instance.class), any(PowerStatus.class));
		then(rebootInstancePowerActionService)
				.should(never())
				.doExecute(any(Instance.class), any(PowerStatus.class));
	}

	@Test
	void should_select_shutdown_service_when_action_is_shutdown() {
		// given
		Instance instance = createTestInstance(PowerStatus.RUNNING);
		InstancePowerStatusAction action = InstancePowerStatusAction.SHUTDOWN;

		Instance expectedInstance = UpdatedInstance.of(instance, action.wantPowerStatus());
		given(shutdownInstancePowerActionService.doExecute(any(Instance.class), any(PowerStatus.class)))
				.willReturn(expectedInstance);

		// when
		Instance result = instancePowerActionManager.execute(instance, action);

		// then
		then(startInstancePowerActionService)
				.should(never())
				.doExecute(any(Instance.class), any(PowerStatus.class));
		then(shutdownInstancePowerActionService)
				.should(times(1))
				.doExecute(any(Instance.class), eq(action.wantPowerStatus()));
		then(pauseInstancePowerActionService)
				.should(never())
				.doExecute(any(Instance.class), any(PowerStatus.class));
		then(rebootInstancePowerActionService)
				.should(never())
				.doExecute(any(Instance.class), any(PowerStatus.class));
	}

	@Test
	void should_select_pause_service_when_action_is_pause() {
		// given
		Instance instance = createTestInstance(PowerStatus.RUNNING);
		InstancePowerStatusAction action = InstancePowerStatusAction.PAUSE;

		Instance expectedInstance = UpdatedInstance.of(instance, action.wantPowerStatus());
		given(pauseInstancePowerActionService.doExecute(any(Instance.class), any(PowerStatus.class)))
				.willReturn(expectedInstance);

		// when
		Instance result = instancePowerActionManager.execute(instance, action);

		// then
		then(startInstancePowerActionService)
				.should(never())
				.doExecute(any(Instance.class), any(PowerStatus.class));
		then(shutdownInstancePowerActionService)
				.should(never())
				.doExecute(any(Instance.class), any(PowerStatus.class));
		then(pauseInstancePowerActionService)
				.should(times(1))
				.doExecute(any(Instance.class), eq(action.wantPowerStatus()));
		then(rebootInstancePowerActionService)
				.should(never())
				.doExecute(any(Instance.class), any(PowerStatus.class));
	}

	@Test
	void should_select_reboot_service_when_action_is_reboot() {
		// given
		Instance instance = createTestInstance(PowerStatus.RUNNING);
		InstancePowerStatusAction action = InstancePowerStatusAction.REBOOT;

		Instance expectedInstance = UpdatedInstance.of(instance, action.wantPowerStatus());
		given(rebootInstancePowerActionService.doExecute(any(Instance.class), any(PowerStatus.class)))
				.willReturn(expectedInstance);

		// when
		Instance result = instancePowerActionManager.execute(instance, action);

		// then
		then(startInstancePowerActionService)
				.should(never())
				.doExecute(any(Instance.class), any(PowerStatus.class));
		then(shutdownInstancePowerActionService)
				.should(never())
				.doExecute(any(Instance.class), any(PowerStatus.class));
		then(pauseInstancePowerActionService)
				.should(never())
				.doExecute(any(Instance.class), any(PowerStatus.class));
		then(rebootInstancePowerActionService)
				.should(times(1))
				.doExecute(any(Instance.class), eq(action.wantPowerStatus()));
	}

	private Instance createTestInstance(PowerStatus powerStatus) {
		LocalDateTime createdDate = LocalDateTime.now().minusMinutes(10);
		InstanceEntity instanceEntity =
				InstanceEntity.builder()
						.id(1L)
						.name("test-instance")
						.description("This is a test instance")
						.alias(InstanceAlias.create("test"))
						.powerStatus(powerStatus)
						.host(new InstanceHost("localhost"))
						.imageSource(ImageSource.create(SourceType.IMAGE, 1L))
						.flavorId(1L)
						.createdAt(createdDate)
						.updatedAt(createdDate)
						.build();
		return Instance.from(instanceEntity);
	}
}
