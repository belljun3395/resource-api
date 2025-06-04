package com.okestro.resource.server.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.okestro.resource.server.application.dto.BrowseInstanceUseCaseDto;
import com.okestro.resource.server.application.service.InstanceSourceService;
import com.okestro.resource.server.domain.FlavorEntity;
import com.okestro.resource.server.domain.FlavorEntityFixtures;
import com.okestro.resource.server.domain.InstanceEntity;
import com.okestro.resource.server.domain.InstanceEntityFixtures;
import com.okestro.resource.server.domain.repository.FlavorRepository;
import com.okestro.resource.server.domain.repository.InstanceRepository;
import com.okestro.resource.server.domain.vo.ImageSource;
import com.okestro.resource.server.domain.vo.ImageSourceFixtures;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class BrowseInstanceUseCaseTest {

	@Mock private InstanceRepository instanceRepository;
	@Mock private FlavorRepository flavorRepository;
	@Mock private InstanceSourceService instanceSourceService;

	private BrowseInstanceUseCase browseInstanceUseCase;

	@BeforeEach
	void setUp() {
		browseInstanceUseCase =
				new BrowseInstanceUseCase(instanceRepository, flavorRepository, instanceSourceService);
	}

	@Test
	void browse_instance_success() {
		// given
		Long pageNumber = 0L;
		int pageSize = 10;

		ImageSource imageSource1 = ImageSourceFixtures.giveMeOne().build();
		ImageSource imageSource2 = ImageSourceFixtures.giveMeOne().build();
		List<ImageSource> imageSources = Arrays.asList(imageSource1, imageSource2);

		InstanceEntity instanceEntity1 =
				InstanceEntityFixtures.giveMeOne().withId(1L).withImageSource(imageSource1).build();
		InstanceEntity instanceEntity2 =
				InstanceEntityFixtures.giveMeOne().withId(2L).withImageSource(imageSource2).build();
		List<InstanceEntity> instanceEntities = Arrays.asList(instanceEntity1, instanceEntity2);

		FlavorEntity flavorEntity1 =
				FlavorEntityFixtures.giveMeOne().withId(instanceEntity1.getFlavorId()).build();
		FlavorEntity flavorEntity2 =
				FlavorEntityFixtures.giveMeOne().withId(instanceEntity2.getFlavorId()).build();
		List<FlavorEntity> flavorEntities = Arrays.asList(flavorEntity1, flavorEntity2);

		Page<InstanceEntity> instancePage =
				new PageImpl<>(
						instanceEntities,
						PageRequest.of(pageNumber.intValue(), pageSize),
						instanceEntities.size());

		given(instanceRepository.search(PageRequest.of(pageNumber.intValue(), pageSize)))
				.willReturn(instancePage);
		given(flavorRepository.findAllByIdIn(any())).willReturn(flavorEntities);
		given(instanceSourceService.findAll(any())).willReturn(imageSources);

		BrowseInstanceUseCaseDto.BrowseInstanceUseCaseIn useCaseIn =
				new BrowseInstanceUseCaseDto.BrowseInstanceUseCaseIn(pageNumber, pageSize);

		// when
		BrowseInstanceUseCaseDto.BrowseInstanceUseCaseOut result =
				browseInstanceUseCase.execute(useCaseIn);

		// then
		verify(instanceRepository, times(1)).search(PageRequest.of(pageNumber.intValue(), pageSize));
		verify(flavorRepository, times(1)).findAllByIdIn(any());
		verify(instanceSourceService, times(1)).findAll(any());
	}

	@Test
	void browse_instance_empty_result() {
		// given
		Long pageNumber = 0L;
		Integer pageSize = 10;

		Page<InstanceEntity> instancePage =
				new PageImpl<>(Collections.emptyList(), PageRequest.of(pageNumber.intValue(), pageSize), 0);

		given(instanceRepository.search(PageRequest.of(pageNumber.intValue(), pageSize)))
				.willReturn(instancePage);
		given(flavorRepository.findAllByIdIn(anyList())).willReturn(Collections.emptyList());
		given(instanceSourceService.findAll(anyList())).willReturn(Collections.emptyList());

		BrowseInstanceUseCaseDto.BrowseInstanceUseCaseIn useCaseIn =
				new BrowseInstanceUseCaseDto.BrowseInstanceUseCaseIn(pageNumber, pageSize);

		// when
		BrowseInstanceUseCaseDto.BrowseInstanceUseCaseOut result =
				browseInstanceUseCase.execute(useCaseIn);

		// then
		verify(instanceRepository, times(1)).search(PageRequest.of(pageNumber.intValue(), pageSize));
		verify(flavorRepository, times(1)).findAllByIdIn(anyList());
		verify(instanceSourceService, times(1)).findAll(anyList());
	}
}
