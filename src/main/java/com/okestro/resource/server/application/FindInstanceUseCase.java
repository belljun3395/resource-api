package com.okestro.resource.server.application;

import static com.okestro.resource.server.application.dto.FindInstanceUseCaseDto.out;

import com.okestro.resource.server.application.dto.FindInstanceUseCaseDto;
import com.okestro.resource.server.application.service.InstanceSourceService;
import com.okestro.resource.server.domain.model.flavor.Flavor;
import com.okestro.resource.server.domain.model.instance.Instance;
import com.okestro.resource.server.domain.repository.FlavorRepository;
import com.okestro.resource.server.domain.repository.InstanceRepository;
import com.okestro.resource.server.domain.vo.ImageSource;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FindInstanceUseCase {
	private final InstanceRepository instanceRepository;
	private final FlavorRepository flavorRepository;
	private final InstanceSourceService instanceSourceService;

	public FindInstanceUseCaseDto.FindInstanceUseCaseOut execute(
			FindInstanceUseCaseDto.FindInstanceUseCaseIn useCaseIn) {
		Long instanceId = useCaseIn.getInstanceId();
		Instance instance =
				instanceRepository
						.findById(instanceId)
						.map(Instance::from)
						.orElseThrow(
								() -> new IllegalArgumentException("can not find instance by id " + instanceId));

		Long flavorId = instance.getFlavorId();
		ImageSource instanceImageSource = instance.getImageSource();

		Pair<Flavor, ImageSource> details = findInstanceDetails(flavorId, instanceImageSource);
		Flavor flavor = findFlavor(details);
		ImageSource imageSource = findImageSource(details);

		return out(instance, flavor, imageSource);
	}

	private Pair<Flavor, ImageSource> findInstanceDetails(Long flavorId, ImageSource imageSource) {
		CompletableFuture<Flavor> flavorFuture =
				CompletableFuture.supplyAsync(() -> flavorRepository.findById(flavorId))
						.thenApply(
								entity ->
										entity.orElseThrow(
												() ->
														new IllegalArgumentException("can not find flavor by id " + flavorId)))
						.thenApply(Flavor::from);

		CompletableFuture<ImageSource> imageSourceFuture =
				CompletableFuture.supplyAsync(() -> instanceSourceService.find(imageSource))
						.thenApply(
								source ->
										source.orElseThrow(
												() ->
														new IllegalArgumentException(
																"can not find image source by id "
																		+ imageSource.getSourceTargetId())));

		CompletableFuture.allOf(flavorFuture, imageSourceFuture).join();
		Flavor flavor = flavorFuture.join();
		ImageSource resolvedImageSource = imageSourceFuture.join();
		return Pair.of(flavor, resolvedImageSource);
	}

	private ImageSource findImageSource(Pair<Flavor, ImageSource> details) {
		return details.getSecond();
	}

	private Flavor findFlavor(Pair<Flavor, ImageSource> details) {
		return details.getFirst();
	}
}
