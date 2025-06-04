package com.okestro.resource.server.application;

import com.okestro.resource.server.application.dto.BrowseInstanceUseCaseDto;
import com.okestro.resource.server.application.service.InstanceSourceService;
import com.okestro.resource.server.domain.model.flavor.Flavor;
import com.okestro.resource.server.domain.model.instance.BaseInstance;
import com.okestro.resource.server.domain.model.instance.Instance;
import com.okestro.resource.server.domain.repository.FlavorRepository;
import com.okestro.resource.server.domain.repository.InstanceRepository;
import com.okestro.resource.server.domain.vo.ImageSource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrowseInstanceUseCase {
	private final InstanceRepository instanceRepository;
	private final FlavorRepository flavorRepository;
	private final InstanceSourceService instanceSourceService;

	public BrowseInstanceUseCaseDto.BrowseInstanceUseCaseOut execute(
			BrowseInstanceUseCaseDto.BrowseInstanceUseCaseIn useCaseIn) {
		Long pageNumber = useCaseIn.getPageNumber();
		Integer pageSize = useCaseIn.getPageSize();

		Page<Instance> instancePage =
				instanceRepository
						.search(PageRequest.of(pageNumber.intValue(), pageSize))
						.map(Instance::from);

		List<Long> flavorIds =
				instancePage.getContent().stream().map(BaseInstance::getFlavorId).distinct().toList();
		List<ImageSource> imageSourcesTarget =
				instancePage.getContent().stream()
						.map(BaseInstance::getImageSource)
						.filter(Objects::nonNull)
						.distinct()
						.toList();

		Pair<Map<Long, Flavor>, List<ImageSource>> details =
				findInstanceDetails(flavorIds, imageSourcesTarget);
		Map<Long, Flavor> flavors = details.getFirst();
		List<ImageSource> imageSources = details.getSecond();

		List<BrowseInstanceUseCaseDto.BrowseInstanceUseCaseOut.InstanceDto> completeInstance =
				mapInstance(instancePage, flavors, imageSources);

		return BrowseInstanceUseCaseDto.out(
				completeInstance,
				instancePage.getSize(),
				instancePage.getNumber(),
				instancePage.getTotalElements());
	}

	private Pair<Map<Long, Flavor>, List<ImageSource>> findInstanceDetails(
			List<Long> flavorIds, List<ImageSource> imageSources) {
		CompletableFuture<Map<Long, Flavor>> flavorFutures =
				CompletableFuture.supplyAsync(() -> flavorRepository.findAllByIdIn(flavorIds))
						.thenApply(
								entities ->
										entities.stream()
												.map(Flavor::from)
												.collect(Collectors.toMap(Flavor::getId, flavor -> flavor)));

		CompletableFuture<List<ImageSource>> imageSourceFutures =
				CompletableFuture.supplyAsync(() -> instanceSourceService.findAll(imageSources));

		CompletableFuture.allOf(flavorFutures, imageSourceFutures).join();
		Map<Long, Flavor> flavors = flavorFutures.join();
		List<ImageSource> instanceImageSources = imageSourceFutures.join();
		return Pair.of(flavors, instanceImageSources);
	}

	private List<BrowseInstanceUseCaseDto.BrowseInstanceUseCaseOut.InstanceDto> mapInstance(
			Page<Instance> instancePage, Map<Long, Flavor> flavors, List<ImageSource> imageSources) {
		return instancePage.getContent().stream()
				.map(
						instance -> {
							Optional<Flavor> flavor = mapFlavor(instance, flavors);
							if (flavor.isEmpty()) {
								log.warn(
										"Flavor not found for instance {}: {}",
										instance.getId(),
										instance.getFlavorId());
								return Optional
										.<BrowseInstanceUseCaseDto.BrowseInstanceUseCaseOut.InstanceDto>empty();
							}
							Optional<ImageSource> imageSource = mapImageSource(instance, imageSources);
							if (imageSource.isEmpty()) {
								log.warn(
										"Image source not found for instance {}: {}",
										instance.getId(),
										instance.getImageSource().getSourceTargetId());
								return Optional
										.<BrowseInstanceUseCaseDto.BrowseInstanceUseCaseOut.InstanceDto>empty();
							}
							return Optional.of(
									BrowseInstanceUseCaseDto.map(instance, flavor.get(), imageSource.get()));
						})
				.filter(Optional::isPresent)
				.map(Optional::get)
				.toList();
	}

	private Optional<Flavor> mapFlavor(Instance instance, Map<Long, Flavor> flavors) {
		return Optional.ofNullable(flavors.getOrDefault(instance.getFlavorId(), null));
	}

	private Optional<ImageSource> mapImageSource(Instance instance, List<ImageSource> imageSources) {
		ImageSource instanceImageSource = Objects.requireNonNull(instance.getImageSource());
		return imageSources.stream().filter(source -> source.equals(instanceImageSource)).findFirst();
	}
}
