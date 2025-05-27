package com.okestro.resource.server.application;

import com.okestro.resource.server.application.dto.*;
import com.okestro.resource.server.application.service.InstanceSourceService;
import com.okestro.resource.server.domain.InstanceEntity;
import com.okestro.resource.server.domain.model.flavor.Flavor;
import com.okestro.resource.server.domain.model.instance.Instance;
import com.okestro.resource.server.domain.model.instance.NewInstance;
import com.okestro.resource.server.domain.repository.FlavorRepository;
import com.okestro.resource.server.domain.repository.InstanceRepository;
import com.okestro.resource.server.domain.vo.ImageSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostInstanceUseCase {
	private final InstanceRepository instanceRepository;
	private final FlavorRepository flavorRepository;
	private final InstanceSourceService instanceSourceService;

	@Transactional
	public PostInstanceUsecaseDto.PostInstanceUseCaseOut execute(
			PostInstanceUsecaseDto.PostInstanceUseCaseIn useCaseIn) {
		final String name = useCaseIn.getName();
		final String description = useCaseIn.getDescription();
		final String host = useCaseIn.getHost();
		final Long flavorId = useCaseIn.getFlavorId();
		final String sourceType = useCaseIn.getSourceType();
		final Long sourceId = useCaseIn.getSourceId();

		final NewInstance newInstance =
				NewInstance.create(name, description, host, sourceType, sourceId, flavorId);

		Flavor flavor = findFlavor(flavorId);
		ImageSource imageSource = findImageSource(newInstance, sourceType, sourceId);

		Instance savedInstance =
				Instance.from(instanceRepository.save(InstanceEntity.createNew(newInstance)));

		return PostInstanceUsecaseDto.out(savedInstance, flavor, imageSource);
	}

	private Flavor findFlavor(Long flavorId) {
		return Flavor.from(
				flavorRepository
						.findById(flavorId)
						.orElseThrow(
								() -> new IllegalArgumentException("can not find flavor by id: " + flavorId)));
	}

	private ImageSource findImageSource(NewInstance newInstance, String sourceType, Long sourceId) {
		return instanceSourceService
				.find(newInstance.getImageSource())
				.orElseThrow(
						() ->
								new IllegalArgumentException(
										"can not find image source by type: " + sourceType + ", id: " + sourceId));
	}
}
