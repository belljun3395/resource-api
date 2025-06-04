package com.okestro.resource.server.application;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.okestro.resource.server.application.dto.FindInstanceUseCaseDto;
import com.okestro.resource.server.application.service.InstanceSourceService;
import com.okestro.resource.server.domain.FlavorEntity;
import com.okestro.resource.server.domain.FlavorEntityFixtures;
import com.okestro.resource.server.domain.InstanceEntity;
import com.okestro.resource.server.domain.InstanceEntityFixtures;
import com.okestro.resource.server.domain.repository.FlavorRepository;
import com.okestro.resource.server.domain.repository.InstanceRepository;
import com.okestro.resource.server.domain.vo.ImageSource;
import com.okestro.resource.server.domain.vo.ImageSourceFixtures;
import java.util.Optional;
import java.util.concurrent.CompletionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class FindInstanceUseCaseTest {

	@Mock private InstanceRepository instanceRepository;
	@Mock private FlavorRepository flavorRepository;
	@Mock private InstanceSourceService instanceSourceService;

	private FindInstanceUseCase findInstanceUseCase;

	@BeforeEach
	void setUp() {
		findInstanceUseCase =
				new FindInstanceUseCase(instanceRepository, flavorRepository, instanceSourceService);
	}

	@Test
	void find_instance_success() {
		// given
		Long instanceId = 1L;
		Long flavorId = 10L;
		ImageSource imageSource = ImageSourceFixtures.giveMeOne().build();

		InstanceEntity instanceEntity =
				InstanceEntityFixtures.giveMeOne()
						.withId(instanceId)
						.withFlavorId(flavorId)
						.withImageSource(imageSource)
						.build();
		FlavorEntity flavorEntity = FlavorEntityFixtures.giveMeOne().withId(flavorId).build();

		ImageSource resolvedImageSource = ImageSourceFixtures.giveMeOne().build();

		given(instanceRepository.findById(instanceId)).willReturn(Optional.of(instanceEntity));
		given(flavorRepository.findById(flavorId)).willReturn(Optional.of(flavorEntity));
		given(instanceSourceService.find(imageSource)).willReturn(Optional.of(resolvedImageSource));

		FindInstanceUseCaseDto.FindInstanceUseCaseIn useCaseIn =
				new FindInstanceUseCaseDto.FindInstanceUseCaseIn(instanceId);

		// when
		FindInstanceUseCaseDto.FindInstanceUseCaseOut result = findInstanceUseCase.execute(useCaseIn);

		// then
		verify(instanceRepository, times(1)).findById(instanceId);
		verify(flavorRepository, times(1)).findById(flavorId);
		verify(instanceSourceService, times(1)).find(imageSource);
	}

	@Test
	void find_instance_not_found() {
		// given
		Long instanceId = 1L;
		given(instanceRepository.findById(instanceId)).willReturn(Optional.empty());
		FindInstanceUseCaseDto.FindInstanceUseCaseIn useCaseIn =
				new FindInstanceUseCaseDto.FindInstanceUseCaseIn(instanceId);

		// when/then
		assertThrows(IllegalArgumentException.class, () -> findInstanceUseCase.execute(useCaseIn));
		verify(instanceRepository, times(1)).findById(instanceId);
		verify(flavorRepository, times(0)).findById(anyLong());
		verify(instanceSourceService, times(0)).find(any());
	}

	@Test
	void find_flavor_not_found() {
		// given
		Long instanceId = 1L;
		Long flavorId = 10L;
		ImageSource imageSource = ImageSourceFixtures.giveMeOne().build();
		InstanceEntity instanceEntity =
				InstanceEntityFixtures.giveMeOne()
						.withId(instanceId)
						.withFlavorId(flavorId)
						.withImageSource(imageSource)
						.build();

		given(instanceRepository.findById(instanceId)).willReturn(Optional.of(instanceEntity));
		given(flavorRepository.findById(flavorId)).willReturn(Optional.empty());
		given(instanceSourceService.find(imageSource)).willReturn(Optional.of(imageSource));
		FindInstanceUseCaseDto.FindInstanceUseCaseIn useCaseIn =
				new FindInstanceUseCaseDto.FindInstanceUseCaseIn(instanceId);

		// when & then
		assertThrows(CompletionException.class, () -> findInstanceUseCase.execute(useCaseIn));
		verify(instanceRepository, times(1)).findById(instanceId);
		verify(flavorRepository, times(1)).findById(flavorId);
		verify(instanceSourceService, times(1)).find(any());
	}

	@Test
	void find_image_source_not_found() {
		// given
		Long instanceId = 1L;
		Long flavorId = 10L;
		ImageSource imageSource = ImageSourceFixtures.giveMeOne().build();
		InstanceEntity instanceEntity =
				InstanceEntityFixtures.giveMeOne()
						.withId(instanceId)
						.withFlavorId(flavorId)
						.withImageSource(imageSource)
						.build();

		FlavorEntity flavorEntity = FlavorEntityFixtures.giveMeOne().withId(flavorId).build();

		given(instanceRepository.findById(instanceId)).willReturn(Optional.of(instanceEntity));
		given(flavorRepository.findById(flavorId)).willReturn(Optional.of(flavorEntity));
		given(instanceSourceService.find(imageSource)).willReturn(Optional.empty());
		FindInstanceUseCaseDto.FindInstanceUseCaseIn useCaseIn =
				new FindInstanceUseCaseDto.FindInstanceUseCaseIn(instanceId);

		// when & then
		assertThrows(CompletionException.class, () -> findInstanceUseCase.execute(useCaseIn));
		verify(instanceRepository, times(1)).findById(instanceId);
		verify(flavorRepository, times(1)).findById(flavorId);
		verify(instanceSourceService, times(1)).find(any());
	}
}
