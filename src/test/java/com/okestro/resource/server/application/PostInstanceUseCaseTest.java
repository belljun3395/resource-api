package com.okestro.resource.server.application;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

import com.okestro.resource.server.application.dto.PostInstanceUsecaseDto;
import com.okestro.resource.server.application.service.InstanceSourceService;
import com.okestro.resource.server.domain.FlavorEntity;
import com.okestro.resource.server.domain.InstanceEntity;
import com.okestro.resource.server.domain.enums.SourceType;
import com.okestro.resource.server.domain.model.instance.NewInstance;
import com.okestro.resource.server.domain.repository.FlavorRepository;
import com.okestro.resource.server.domain.repository.InstanceRepository;
import com.okestro.resource.server.domain.vo.ImageSource;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;

@ExtendWith(MockitoExtension.class)
class PostInstanceUseCaseTest {
	static VerificationMode notCalled = times(0);

	@Mock private InstanceRepository instanceRepository;
	@Mock private FlavorRepository flavorRepository;
	@Mock private InstanceSourceService instanceSourceService;

	@Test
	void create_new_instance() {
		// given
		PostInstanceUsecaseDto.PostInstanceUseCaseIn useCaseIn =
				PostInstanceUsecaseDto.PostInstanceUseCaseIn.builder()
						.name("test-instance")
						.description("This is a test instance")
						.host("192.168.1.1")
						.flavorId(1L)
						.sourceType("IMAGE")
						.sourceId(1L)
						.build();
		Long flavorId = useCaseIn.getFlavorId();
		given(flavorRepository.findById(flavorId))
				.willReturn(
						Optional.of(
								FlavorEntity.builder()
										.id(flavorId)
										.name("test-flavor")
										.description("Test flavor description")
										.vCpu(2.0f)
										.memory(4096.0f)
										.rootDiskSize(20.0f)
										.build()));
		given(instanceSourceService.find(any(ImageSource.class)))
				.willReturn(
						Optional.of(ImageSource.of(SourceType.IMAGE, useCaseIn.getSourceId(), "test-image")));

		given(instanceRepository.save(any(InstanceEntity.class)))
				.willReturn(
						InstanceEntity.createNew(
								NewInstance.create(
										useCaseIn.getName(),
										useCaseIn.getDescription(),
										useCaseIn.getHost(),
										useCaseIn.getSourceType(),
										useCaseIn.getSourceId(),
										flavorId)));

		// when
		PostInstanceUseCase postInstanceUseCase =
				new PostInstanceUseCase(instanceRepository, flavorRepository, instanceSourceService);
		postInstanceUseCase.execute(useCaseIn);

		// then
		then(flavorRepository).should(times(1)).findById(flavorId);
		then(instanceSourceService).should(times(1)).find(any(ImageSource.class));
		then(instanceRepository).should(times(1)).save(any(InstanceEntity.class));
	}

	@Test
	void fail_to_create_new_instance_cause_not_found_flavor() {
		// given
		PostInstanceUsecaseDto.PostInstanceUseCaseIn useCaseIn =
				PostInstanceUsecaseDto.PostInstanceUseCaseIn.builder()
						.name("test-instance")
						.description("This is a test instance")
						.host("192.168.1.1")
						.flavorId(1L)
						.sourceType("IMAGE")
						.sourceId(1L)
						.build();
		Long flavorId = useCaseIn.getFlavorId();
		given(flavorRepository.findById(flavorId)).willReturn(Optional.empty());

		// when
		PostInstanceUseCase postInstanceUseCase =
				new PostInstanceUseCase(instanceRepository, flavorRepository, instanceSourceService);
		assertThrows(
				RuntimeException.class,
				() -> {
					postInstanceUseCase.execute(useCaseIn);
				});

		// then
		then(flavorRepository).should(times(1)).findById(flavorId);
		then(instanceSourceService).should(notCalled).find(any(ImageSource.class));
		then(instanceRepository).should(notCalled).save(any(InstanceEntity.class));
	}

	@Test
	void fail_to_create_new_instance_cause_not_found_image_source() {
		// given
		PostInstanceUsecaseDto.PostInstanceUseCaseIn useCaseIn =
				PostInstanceUsecaseDto.PostInstanceUseCaseIn.builder()
						.name("test-instance")
						.description("This is a test instance")
						.host("192.168.1.1")
						.flavorId(1L)
						.sourceType("IMAGE")
						.sourceId(1L)
						.build();
		Long flavorId = useCaseIn.getFlavorId();
		given(flavorRepository.findById(flavorId))
				.willReturn(
						Optional.of(
								FlavorEntity.builder()
										.id(flavorId)
										.name("test-flavor")
										.description("Test flavor description")
										.vCpu(2.0f)
										.memory(4096.0f)
										.rootDiskSize(20.0f)
										.build()));
		given(instanceSourceService.find(any(ImageSource.class))).willReturn(Optional.empty());

		// when
		PostInstanceUseCase postInstanceUseCase =
				new PostInstanceUseCase(instanceRepository, flavorRepository, instanceSourceService);
		assertThrows(
				RuntimeException.class,
				() -> {
					postInstanceUseCase.execute(useCaseIn);
				});

		// then
		then(flavorRepository).should(times(1)).findById(flavorId);
		then(instanceSourceService).should(times(1)).find(any(ImageSource.class));
		then(instanceRepository).should(notCalled).save(any(InstanceEntity.class));
	}
}
