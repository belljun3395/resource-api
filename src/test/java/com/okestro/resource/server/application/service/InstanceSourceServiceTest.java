package com.okestro.resource.server.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.okestro.resource.server.domain.ImageEntity;
import com.okestro.resource.server.domain.enums.SourceType;
import com.okestro.resource.server.domain.repository.ImageRepository;
import com.okestro.resource.server.domain.vo.ImageSource;
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
		ImageSource imageSource = ImageSource.of(SourceType.IMAGE, 1L, "test-image");
		Long imageSourceSourceTargetId = imageSource.getSourceTargetId();
		assert imageSource.getSourceName() != null;
		given(imageRepository.findById(imageSourceSourceTargetId))
				.willReturn(
						Optional.of(
								ImageEntity.builder()
										.id(imageSourceSourceTargetId)
										.name(imageSource.getSourceName())
										.build()));

		// when
		InstanceSourceService instanceSourceService = new InstanceSourceService(imageRepository);

		//  then
		Optional<ImageSource> result = instanceSourceService.find(imageSource);
		assertTrue(result.isPresent());
		then(imageRepository).should(times(1)).findById(imageSourceSourceTargetId);
	}
}
