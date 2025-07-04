package com.okestro.resource.server.application;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.okestro.resource.server.application.dto.PostInstanceUseCaseDto;
import com.okestro.resource.server.application.service.InstanceSourceService;
import com.okestro.resource.server.domain.FlavorEntity;
import com.okestro.resource.server.domain.FlavorEntityFixtures;
import com.okestro.resource.server.domain.InstanceEntity;
import com.okestro.resource.server.domain.model.instance.Instance;
import com.okestro.resource.server.domain.model.instance.InstanceFixtures;
import com.okestro.resource.server.domain.model.instance.NewInstance;
import com.okestro.resource.server.domain.repository.FlavorRepository;
import com.okestro.resource.server.domain.repository.InstanceRepository;
import com.okestro.resource.server.domain.vo.ImageSource;
import com.okestro.resource.server.domain.vo.ImageSourceFixtures;
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
class PostInstanceUseCaseTest {
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
	@Mock private FlavorRepository flavorRepository;
	@Mock private InstanceSourceService instanceSourceService;
	private PostInstanceUseCase postInstanceUseCase;

	@BeforeEach
	void setUp() {
		postInstanceUseCase =
				new PostInstanceUseCase(
						serverEventPublisher,
						serverJsonConverter,
						instanceRepository,
						flavorRepository,
						instanceSourceService);
	}

	@Test
	void create_new_instance() {
		// given
		FlavorEntity flavorEntity = FlavorEntityFixtures.giveMeOne().build();
		given(flavorRepository.findById(anyLong())).willReturn(Optional.of(flavorEntity));
		ImageSource imageSource = ImageSourceFixtures.giveMeOne().build();
		given(instanceSourceService.find(any(ImageSource.class))).willReturn(Optional.of(imageSource));

		Instance instance =
				InstanceFixtures.giveMeOne()
						.withFlavorId(flavorEntity.getId())
						.withImageSource(imageSource)
						.build();
		given(instanceRepository.save(any(InstanceEntity.class)))
				.willReturn(
						InstanceEntity.createNew(
								NewInstance.create(
										instance.getName(),
										instance.getDescription(),
										instance.getHost().getValue(),
										instance.getImageSource().getSourceType().name(),
										instance.getImageSource().getSourceTargetId(),
										instance.getFlavorId())));

		willDoNothing()
				.given(serverEventPublisher)
				.publishEvent(
						any(
								InstanceEvent.InstanceTransactionEvent.InstanceTransactionLogEvent
										.InstanceCreateLogEvent.class));

		// when
		postInstanceUseCase.execute(
				PostInstanceUseCaseDto.PostInstanceUseCaseIn.builder()
						.name(instance.getName())
						.description(instance.getDescription())
						.host(instance.getHost().getValue())
						.flavorId(instance.getFlavorId())
						.sourceType(instance.getImageSource().getSourceType().name())
						.sourceId(instance.getImageSource().getSourceTargetId())
						.build());

		// then
		then(flavorRepository).should(times(1)).findById(anyLong());
		then(instanceSourceService).should(times(1)).find(any(ImageSource.class));
		then(instanceRepository).should(times(1)).save(any(InstanceEntity.class));
		then(serverEventPublisher)
				.should(times(1))
				.publishEvent(
						any(
								InstanceEvent.InstanceTransactionEvent.InstanceTransactionLogEvent
										.InstanceCreateLogEvent.class));
	}

	@Test
	void fail_to_create_new_instance_cause_not_found_flavor() {
		// given
		given(flavorRepository.findById(anyLong())).willReturn(Optional.empty());

		// when
		assertThrows(
				RuntimeException.class,
				() -> {
					postInstanceUseCase.execute(
							PostInstanceUseCaseDto.PostInstanceUseCaseIn.builder()
									.name("test-instance")
									.description("This is a test instance")
									.host("192.168.1.1")
									.flavorId(1L)
									.sourceType("IMAGE")
									.sourceId(1L)
									.build());
				});

		// then
		then(flavorRepository).should(times(1)).findById(anyLong());
		then(instanceSourceService).should(notCalled).find(any(ImageSource.class));
		then(instanceRepository).should(notCalled).save(any(InstanceEntity.class));
		then(serverEventPublisher)
				.should(notCalled)
				.publishEvent(
						any(
								InstanceEvent.InstanceTransactionEvent.InstanceTransactionLogEvent
										.InstanceCreateLogEvent.class));
	}

	@Test
	void fail_to_create_new_instance_cause_not_found_image_source() {
		// given
		given(flavorRepository.findById(anyLong()))
				.willReturn(Optional.of(FlavorEntityFixtures.giveMeOne().build()));
		given(instanceSourceService.find(any(ImageSource.class))).willReturn(Optional.empty());

		// when
		assertThrows(
				RuntimeException.class,
				() -> {
					postInstanceUseCase.execute(
							PostInstanceUseCaseDto.PostInstanceUseCaseIn.builder()
									.name("test-instance")
									.description("This is a test instance")
									.host("192.168.1.1")
									.flavorId(1L)
									.sourceType("IMAGE")
									.sourceId(1L)
									.build());
				});

		// then
		then(flavorRepository).should(times(1)).findById(anyLong());
		then(instanceSourceService).should(times(1)).find(any(ImageSource.class));
		then(instanceRepository).should(notCalled).save(any(InstanceEntity.class));
		then(serverEventPublisher)
				.should(notCalled)
				.publishEvent(
						any(
								InstanceEvent.InstanceTransactionEvent.InstanceTransactionLogEvent
										.InstanceCreateLogEvent.class));
	}
}
