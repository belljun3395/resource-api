package com.okestro.resource.server.application.dto;

import com.okestro.resource.server.domain.model.flavor.Flavor;
import com.okestro.resource.server.domain.model.instance.Instance;
import com.okestro.resource.server.domain.vo.ImageSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

public class BrowseInstanceUseCaseDto {
	public static BrowseInstanceUseCaseIn in(Long pageNumber, Integer pageSize) {
		return BrowseInstanceUseCaseIn.builder().pageNumber(pageNumber).pageSize(pageSize).build();
	}

	public static BrowseInstanceUseCaseOut out(
			List<BrowseInstanceUseCaseOut.InstanceDto> instances,
			int pageSize,
			int pageNumber,
			Long totalCount) {
		return BrowseInstanceUseCaseOut.builder()
				.data(instances)
				.pageSize(pageSize)
				.pageNumber(pageNumber)
				.totalCount(totalCount)
				.build();
	}

	public static BrowseInstanceUseCaseOut.InstanceDto map(
			Instance instance, Flavor flavor, ImageSource imageSource) {
		String sourceName = Objects.requireNonNullElse(imageSource.getSourceName(), "");
		return BrowseInstanceUseCaseOut.InstanceDto.builder()
				.id(instance.getId())
				.name(instance.getName())
				.description(instance.getDescription())
				.alias(instance.getAlias().getValue())
				.powerStatus(instance.getPowerStatus().name())
				.host(instance.getHost().getValue())
				.source(
						BrowseInstanceUseCaseOut.PostInstanceSourceDto.builder()
								.type(imageSource.getSourceType().name())
								.id(imageSource.getSourceTargetId())
								.name(sourceName)
								.build())
				.flavor(
						BrowseInstanceUseCaseOut.InstanceFlavorDto.builder()
								.id(flavor.getId())
								.name(flavor.getName())
								.description(flavor.getDescription())
								.vCpu(flavor.getVCpu())
								.memory(flavor.getMemory())
								.rootDisk(flavor.getRootDiskSize())
								.build())
				.build();
	}

	public static Page<BrowseInstanceUseCaseOut.InstanceDto> toPage(
			BrowseInstanceUseCaseOut useCaseOut) {
		return new PageImpl<>(
				useCaseOut.getData(),
				PageRequest.of(useCaseOut.getPageNumber(), useCaseOut.getPageSize()),
				useCaseOut.getTotalCount());
	}

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder(toBuilder = true)
	public static class BrowseInstanceUseCaseIn {
		@NotNull private Long pageNumber;
		@NotNull private Integer pageSize;
	}

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder(toBuilder = true)
	public static class BrowseInstanceUseCaseOut {
		@NotNull private int pageSize;
		@NotNull private int pageNumber;
		@NotNull private Long totalCount;
		@NotNull private List<InstanceDto> data;

		@Getter
		@AllArgsConstructor
		@NoArgsConstructor
		@Builder(toBuilder = true)
		public static class InstanceDto {
			@NotNull private Long id;
			@NotNull private String name;
			@NotNull private String description;
			@NotNull private String alias;
			@NotNull private String powerStatus;
			@NotNull private String host;
			@NotNull private PostInstanceSourceDto source;
			@NotNull private InstanceFlavorDto flavor;
			@NotNull private LocalDateTime createdAt;
		}

		@Getter
		@AllArgsConstructor
		@NoArgsConstructor
		@Builder(toBuilder = true)
		public static class PostInstanceSourceDto {
			@NotNull private String type;
			@NotNull private Long id;
			@NotNull private String name;
		}

		@Getter
		@AllArgsConstructor
		@NoArgsConstructor
		@Builder(toBuilder = true)
		public static class InstanceFlavorDto {
			@NotNull private Long id;
			@NotNull private String name;
			@NotNull private String description;
			@NotNull private Float vCpu;
			@NotNull private Float memory;
			@NotNull private Float rootDisk;
		}
	}
}
