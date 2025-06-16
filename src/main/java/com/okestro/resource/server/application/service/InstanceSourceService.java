package com.okestro.resource.server.application.service;

import com.okestro.resource.server.domain.enums.SourceType;
import com.okestro.resource.server.domain.repository.ImageRepository;
import com.okestro.resource.server.domain.vo.ImageSource;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InstanceSourceService {
	private final ImageRepository imageRepository;

	public Optional<ImageSource> find(ImageSource imageSource) {
		if (imageSource.getSourceType() == SourceType.IMAGE) {
			return imageRepository
					.findById(imageSource.getSourceTargetId())
					.map(image -> ImageSource.of(SourceType.IMAGE, image.getId(), image.getName()));
		}

		return Optional.empty();
	}
}
