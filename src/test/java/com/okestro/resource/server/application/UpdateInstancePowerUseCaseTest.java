package com.okestro.resource.server.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.okestro.resource.server.application.dto.UpdateInstancePowerUsecaseDto;
import com.okestro.resource.server.application.service.power.*;
import com.okestro.resource.server.controller.request.InstancePowerStatusAction;
import com.okestro.resource.server.domain.InstanceEntity;
import com.okestro.resource.server.domain.enums.PowerStatus;
import com.okestro.resource.server.domain.enums.SourceType;
import com.okestro.resource.server.domain.model.instance.Instance;
import com.okestro.resource.server.domain.model.instance.UpdatedInstance;
import com.okestro.resource.server.domain.repository.InstanceRepository;
import com.okestro.resource.server.domain.vo.ImageSource;
import com.okestro.resource.server.domain.vo.InstanceAlias;
import com.okestro.resource.server.domain.vo.InstanceHost;
import com.okestro.resource.server.event.ServerEventPublisher;
import com.okestro.resource.server.event.instance.InstanceEvent;
import com.okestro.resource.server.support.json.ServerJsonConverter;
import com.okestro.resource.support.web.converter.LocalDateJsonConverter;
import com.okestro.resource.support.web.converter.LocalDateTimeJsonConverter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.verification.VerificationMode;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class UpdateInstancePowerUseCaseTest {
	static VerificationMode notCalled = times(0);

	@Mock private ServerEventPublisher serverEventPublisher;
	private final ServerJsonConverter serverJsonConverter =
			new ServerJsonConverter(
					Jackson2ObjectMapperBuilder.json()
							.failOnUnknownProperties(false)
							.serializationInclusion(JsonInclude.Include.NON_ABSENT)
							.modules(
									new JavaTimeModule()
											.addSerializer(
													LocalDateTime.class, new LocalDateTimeJsonConverter.Serializer())
											.addDeserializer(
													LocalDateTime.class, new LocalDateTimeJsonConverter.Deserializer())
											.addSerializer(LocalDate.class, new LocalDateJsonConverter.Serializer())
											.addDeserializer(LocalDate.class, new LocalDateJsonConverter.Deserializer()))
							.featuresToEnable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
							.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
							.build());
	@Mock private InstanceRepository instanceRepository;
	@Mock private InstancePowerActionManager instancePowerActionManager;

	private UpdateInstancePowerUseCase updateInstancePowerUseCase;

	@BeforeEach
	void setUp() {
		updateInstancePowerUseCase =
				new UpdateInstancePowerUseCase(
						serverEventPublisher,
						serverJsonConverter,
						instanceRepository,
						instancePowerActionManager);
	}

	@Test
	void update_instance_power_status_start() {
		// given
		Long instanceId = 1L;
		InstancePowerStatusAction action = InstancePowerStatusAction.START;
		LocalDateTime createdDate = LocalDateTime.now().minusMinutes(10);
		InstanceEntity instanceEntity =
				InstanceEntity.builder()
						.id(instanceId)
						.name("test-instance")
						.description("This is a test instance")
						.alias(InstanceAlias.create("test"))
						.powerStatus(PowerStatus.SHUTDOWN)
						.host(new InstanceHost("localhost"))
						.imageSource(ImageSource.create(SourceType.IMAGE, 1L))
						.flavorId(1L)
						.createdAt(createdDate)
						.updatedAt(createdDate)
						.build();

		InstanceEntity updatedInstanceEntity =
				InstanceEntity.builder()
						.id(instanceEntity.getId())
						.name(instanceEntity.getName())
						.description(instanceEntity.getDescription())
						.alias(instanceEntity.getAlias())
						.powerStatus(PowerStatus.RUNNING)
						.host(instanceEntity.getHost())
						.imageSource(instanceEntity.getImageSource())
						.flavorId(instanceEntity.getFlavorId())
						.createdAt(instanceEntity.getCreatedAt())
						.updatedAt(LocalDateTime.now())
						.build();
		UpdatedInstance updatedInstance = UpdatedInstance.from(updatedInstanceEntity);

		given(instanceRepository.findById(instanceId)).willReturn(Optional.of(instanceEntity));
		given(instancePowerActionManager.execute(any(Instance.class), eq(action)))
				.willReturn(updatedInstance);
		willDoNothing()
				.given(serverEventPublisher)
				.publishEvent(any(InstanceEvent.InstanceTransactionEvent.InstanceUpdateLogEvent.class));

		// when
		UpdateInstancePowerUsecaseDto.UpdateInstanceUseCaseOut result =
				updateInstancePowerUseCase.execute(
						UpdateInstancePowerUsecaseDto.UpdateInstancePowerUseCaseIn.builder()
								.instanceId(instanceId)
								.powerStatusAction(action)
								.build());

		// then
		then(instanceRepository).should(times(1)).findById(instanceId);
		then(instancePowerActionManager).should(times(1)).execute(any(Instance.class), eq(action));
		then(serverEventPublisher)
				.should(times(1))
				.publishEvent(any(InstanceEvent.InstanceTransactionEvent.InstanceUpdateLogEvent.class));
	}

	@Test
	void update_instance_power_status_start_but_already_started() {
		// given
		Long instanceId = 1L;
		InstancePowerStatusAction action = InstancePowerStatusAction.START;
		LocalDateTime createdDate = LocalDateTime.now().minusMinutes(10);
		InstanceEntity instanceEntity =
				InstanceEntity.builder()
						.id(instanceId)
						.name("test-instance")
						.description("This is a test instance")
						.alias(InstanceAlias.create("test"))
						.powerStatus(PowerStatus.RUNNING)
						.host(new InstanceHost("localhost"))
						.imageSource(ImageSource.create(SourceType.IMAGE, 1L))
						.flavorId(1L)
						.createdAt(createdDate)
						.updatedAt(createdDate)
						.build();
		Instance instance = Instance.from(instanceEntity);

		given(instanceRepository.findById(instanceId)).willReturn(Optional.of(instanceEntity));
		given(instancePowerActionManager.execute(any(Instance.class), eq(action))).willReturn(instance);

		// when
		UpdateInstancePowerUsecaseDto.UpdateInstanceUseCaseOut result =
				updateInstancePowerUseCase.execute(
						UpdateInstancePowerUsecaseDto.UpdateInstancePowerUseCaseIn.builder()
								.instanceId(instanceId)
								.powerStatusAction(action)
								.build());

		// then
		then(instanceRepository).should(times(1)).findById(instanceId);
		then(instancePowerActionManager).should(times(1)).execute(any(Instance.class), eq(action));
		then(serverEventPublisher)
				.should(notCalled)
				.publishEvent(any(InstanceEvent.InstanceTransactionEvent.InstanceUpdateLogEvent.class));
	}

	@Test
	void fail_to_update_instance_power_status_cause_not_found_instance() {
		// given
		Long instanceId = 1L;
		InstancePowerStatusAction action = InstancePowerStatusAction.START;
		given(instanceRepository.findById(instanceId)).willReturn(Optional.empty());

		// when
		assertThrows(
				IllegalArgumentException.class,
				() ->
						updateInstancePowerUseCase.execute(
								UpdateInstancePowerUsecaseDto.UpdateInstancePowerUseCaseIn.builder()
										.instanceId(instanceId)
										.powerStatusAction(action)
										.build()));

		// then
		then(instanceRepository).should(times(1)).findById(instanceId);
		then(instancePowerActionManager)
				.should(notCalled)
				.execute(any(Instance.class), any(InstancePowerStatusAction.class));
		then(serverEventPublisher)
				.should(notCalled)
				.publishEvent(any(InstanceEvent.InstanceTransactionEvent.InstanceUpdateLogEvent.class));
	}

	@Test
	void fail_to_update_instance_power_status_cause_unsupported_action() {
		// given
		Long instanceId = 1L;
		InstancePowerStatusAction action = InstancePowerStatusAction.START;
		LocalDateTime createdDate = LocalDateTime.now().minusMinutes(10);
		InstanceEntity instanceEntity =
				InstanceEntity.builder()
						.id(instanceId)
						.name("test-instance")
						.description("This is a test instance")
						.alias(InstanceAlias.create("test"))
						.powerStatus(PowerStatus.SHUTDOWN)
						.host(new InstanceHost("localhost"))
						.imageSource(ImageSource.create(SourceType.IMAGE, 1L))
						.flavorId(1L)
						.createdAt(createdDate)
						.updatedAt(createdDate)
						.build();

		given(instanceRepository.findById(instanceId)).willReturn(Optional.of(instanceEntity));
		given(instancePowerActionManager.execute(any(Instance.class), eq(action)))
				.willThrow(new IllegalArgumentException("Unsupported action: " + action));

		// when
		assertThrows(
				IllegalArgumentException.class,
				() ->
						updateInstancePowerUseCase.execute(
								UpdateInstancePowerUsecaseDto.UpdateInstancePowerUseCaseIn.builder()
										.instanceId(instanceId)
										.powerStatusAction(action)
										.build()));

		// then
		then(instanceRepository).should(times(1)).findById(instanceId);
		then(instancePowerActionManager).should(times(1)).execute(any(Instance.class), eq(action));
		then(serverEventPublisher)
				.should(notCalled)
				.publishEvent(any(InstanceEvent.InstanceTransactionEvent.InstanceUpdateLogEvent.class));
	}
}
