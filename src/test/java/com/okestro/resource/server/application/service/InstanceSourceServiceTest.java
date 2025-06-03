package com.okestro.resource.server.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.okestro.resource.server.domain.ImageEntityFixtures;
import com.okestro.resource.server.domain.repository.ImageRepository;
import com.okestro.resource.server.domain.vo.ImageSource;
import com.okestro.resource.server.domain.vo.ImageSourceFixtures;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InstanceSourceServiceTest {
	@Mock private ImageRepository imageRepository;

	@Test
	void find_image_source() {
		// given
		ImageSource imageSource = ImageSourceFixtures.giveMeOne().build();
		given(imageRepository.findById(anyLong()))
				.willReturn(
						Optional.of(
								ImageEntityFixtures.giveMeOne()
										.withId(imageSource.getSourceTargetId())
										.withName(imageSource.getSourceName())
										.build()));

		// when
		InstanceSourceService instanceSourceService = new InstanceSourceService(imageRepository);

		//  then
		Optional<ImageSource> result = instanceSourceService.find(imageSource);
		assertTrue(result.isPresent());
		then(imageRepository).should(times(1)).findById(anyLong());
	}
}
