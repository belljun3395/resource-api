package com.okestro.resource.server.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.okestro.resource.server.application.dto.DeleteInstanceUseCaseDto;
import com.okestro.resource.server.domain.InstanceEntity;
import com.okestro.resource.server.domain.InstanceEntityFixtures;
import com.okestro.resource.server.domain.enums.PowerStatus;
import com.okestro.resource.server.domain.enums.SourceType;
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
class DeleteInstanceUseCaseTest {

	static VerificationMode notCalled = times(0);

	@Mock private InstanceRepository instanceRepository;

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

	private DeleteInstanceUseCase deleteInstanceUseCase;

	@BeforeEach
	void setUp() {
		deleteInstanceUseCase =
				new DeleteInstanceUseCase(serverEventPublisher, serverJsonConverter, instanceRepository);
	}

	@Test
	void delete_instance() {
		// given
		DeleteInstanceUseCaseDto.DeleteInstanceUseCaseIn useCaseIn =
				new DeleteInstanceUseCaseDto.DeleteInstanceUseCaseIn(1L);

		given(instanceRepository.findById(anyLong())).willReturn(Optional.of(InstanceEntityFixtures.giveMeOne().build()));

		given(instanceRepository.save(any(InstanceEntity.class)))
				.willAnswer(invocation -> invocation.getArgument(0));
		willDoNothing().given(instanceRepository).delete(any(InstanceEntity.class));
		willDoNothing()
				.given(serverEventPublisher)
				.publishEvent(
						any(InstanceEvent.InstanceTransactionEvent.InstanceTransactionLogEvent.class));

		// when
		DeleteInstanceUseCaseDto.DeleteInstanceUseCaseOut result =
				deleteInstanceUseCase.execute(useCaseIn);

		// then
		verify(instanceRepository, times(1)).findById(anyLong());
		verify(instanceRepository, times(1)).save(any(InstanceEntity.class));
		verify(instanceRepository, times(1)).delete(any(InstanceEntity.class));
		verify(serverEventPublisher, times(1))
				.publishEvent(
						any(InstanceEvent.InstanceTransactionEvent.InstanceTransactionLogEvent.class));
		verify(serverEventPublisher, notCalled)
				.publishEvent(any(InstanceEvent.InstanceTransactionEvent.DeleteInstanceCommand.class));
	}

	@Test
	void fail_delete_instance_only_shutdown_instance() {
		// given
		DeleteInstanceUseCaseDto.DeleteInstanceUseCaseIn useCaseIn =
				new DeleteInstanceUseCaseDto.DeleteInstanceUseCaseIn(1L);

		given(instanceRepository.findById(anyLong()))
				.willReturn(
						Optional.of(
								InstanceEntity.builder()
										.id(1L)
										.name("Test Instance")
										.description("Test Instance Description")
										.alias(InstanceAlias.create("test"))
										.powerStatus(PowerStatus.RUNNING)
										.host(new InstanceHost("localhost"))
										.flavorId(1L)
										.imageSource(ImageSource.create(SourceType.IMAGE, 1L))
										.build()));

		given(instanceRepository.save(any(InstanceEntity.class)))
				.willAnswer(invocation -> invocation.getArgument(0));
		willThrow(new RuntimeException("Database error"))
				.given(instanceRepository)
				.delete(any(InstanceEntity.class));
		willDoNothing()
				.given(serverEventPublisher)
				.publishEvent(
						any(InstanceEvent.InstanceTransactionEvent.InstanceTransactionLogEvent.class));
		willDoNothing()
				.given(serverEventPublisher)
				.publishEvent(any(InstanceEvent.InstanceTransactionEvent.DeleteInstanceCommand.class));

		// when
		DeleteInstanceUseCaseDto.DeleteInstanceUseCaseOut result =
				deleteInstanceUseCase.execute(useCaseIn);

		// then
		verify(instanceRepository, times(1)).findById(anyLong());
		verify(instanceRepository, times(1)).save(any(InstanceEntity.class));
		verify(instanceRepository, times(1)).delete(any(InstanceEntity.class));
		verify(serverEventPublisher, times(1))
				.publishEvent(
						any(InstanceEvent.InstanceTransactionEvent.InstanceTransactionLogEvent.class));
		verify(serverEventPublisher, times(1))
				.publishEvent(any(InstanceEvent.InstanceTransactionEvent.DeleteInstanceCommand.class));
	}
}
