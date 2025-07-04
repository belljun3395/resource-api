package com.okestro.resource.server.application;

import com.okestro.resource.server.application.dto.*;
import com.okestro.resource.server.application.service.InstanceSourceService;
import com.okestro.resource.server.domain.InstanceEntity;
import com.okestro.resource.server.domain.enums.SourceType;
import com.okestro.resource.server.domain.model.flavor.Flavor;
import com.okestro.resource.server.domain.model.instance.Instance;
import com.okestro.resource.server.domain.model.instance.NewInstance;
import com.okestro.resource.server.domain.repository.FlavorRepository;
import com.okestro.resource.server.domain.repository.InstanceRepository;
import com.okestro.resource.server.domain.vo.ImageSource;
import com.okestro.resource.server.event.ServerEventPublisher;
import com.okestro.resource.server.event.instance.InstanceEvent;
import com.okestro.resource.server.support.json.ServerAction;
import com.okestro.resource.server.support.json.ServerActionJson;
import com.okestro.resource.server.support.json.ServerJsonConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostInstanceUseCase {
	private final ServerEventPublisher serverEventPublisher;
	private final ServerJsonConverter serverJsonConverter;

	private final InstanceRepository instanceRepository;
	private final FlavorRepository flavorRepository;
	private final InstanceSourceService instanceSourceService;

	@Transactional
	public PostInstanceUseCaseDto.PostInstanceUseCaseOut execute(
			PostInstanceUseCaseDto.PostInstanceUseCaseIn useCaseIn) {
		final String name = useCaseIn.getName();
		final String description = useCaseIn.getDescription();
		final String host = useCaseIn.getHost();
		final Long flavorId = useCaseIn.getFlavorId();
		final String sourceType = useCaseIn.getSourceType();
		final Long sourceId = useCaseIn.getSourceId();

		final NewInstance newInstance =
				NewInstance.create(name, description, host, sourceType, sourceId, flavorId);

		Flavor flavor = findFlavor(flavorId);
		ImageSource imageSource = findImageSource(newInstance, sourceId);

		Instance savedInstance =
				Instance.from(instanceRepository.save(InstanceEntity.createNew(newInstance)));

		publishEvent(savedInstance, flavor, imageSource);
		return PostInstanceUseCaseDto.out(savedInstance, flavor, imageSource);
	}

	private Flavor findFlavor(Long flavorId) {
		return flavorRepository
				.findById(flavorId)
				.map(Flavor::from)
				.orElseThrow(() -> new IllegalArgumentException("can not find flavor by id: " + flavorId));
	}

	private ImageSource findImageSource(NewInstance newInstance, Long sourceId) {
		SourceType sourceType = newInstance.getImageSource().getSourceType();
		if (sourceType != SourceType.IMAGE) {
			throw new IllegalArgumentException(
					"source type must be IMAGE, but got: " + sourceType.name());
		}
		return instanceSourceService.find(newInstance.getImageSource()).orElseThrow();
	}

	private void publishEvent(Instance savedInstance, Flavor flavor, ImageSource imageSource) {
		ServerActionJson serverActionJson =
				serverJsonConverter.toJson(ServerAction.CREATE, savedInstance, flavor, imageSource);
		InstanceEvent.InstanceTransactionEvent.InstanceTransactionLogEvent.InstanceCreateLogEvent
				event =
						new InstanceEvent.InstanceTransactionEvent.InstanceTransactionLogEvent
								.InstanceCreateLogEvent(savedInstance.getId(), serverActionJson);
		serverEventPublisher.publishEvent(event);
	}
}
