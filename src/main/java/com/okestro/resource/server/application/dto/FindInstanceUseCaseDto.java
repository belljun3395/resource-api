package com.okestro.resource.server.application.dto;

import com.okestro.resource.server.domain.model.flavor.Flavor;
import com.okestro.resource.server.domain.model.instance.Instance;
import com.okestro.resource.server.domain.vo.ImageSource;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class FindInstanceUseCaseDto {
	public static FindInstanceUseCaseIn in(Long instanceId) {
		return FindInstanceUseCaseIn.builder().instanceId(instanceId).build();
	}

	public static FindInstanceUseCaseOut out(
			Instance instance, Flavor flavor, ImageSource imageSource) {
		return FindInstanceUseCaseOut.builder()
				.id(instance.getId())
				.name(instance.getName())
				.description(instance.getDescription())
				.alias(instance.getAlias().getValue())
				.powerStatus(instance.getPowerStatus().name())
				.host(instance.getHost().getValue())
				.source(
						FindInstanceUseCaseOut.PostInstanceSourceDto.builder()
								.type(imageSource.getSourceType().name())
								.id(imageSource.getSourceTargetId())
								.name(imageSource.getSourceName())
								.build())
				.flavor(
						FindInstanceUseCaseOut.InstanceFlavorDto.builder()
								.id(flavor.getId())
								.name(flavor.getName())
								.description(flavor.getDescription())
								.vCpu(flavor.getVCpu())
								.memory(flavor.getMemory())
								.rootDisk(flavor.getRootDiskSize())
								.build())
				.build();
	}

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder(toBuilder = true)
	public static class FindInstanceUseCaseIn {
		@NotNull private Long instanceId;
	}

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder(toBuilder = true)
	public static class FindInstanceUseCaseOut {
		@NotNull private Long id;
		@NotNull private String name;
		@NotNull private String description;
		@NotNull private String alias;
		@NotNull private String powerStatus;
		@NotNull private String host;
		@NotNull private PostInstanceSourceDto source;
		@NotNull private InstanceFlavorDto flavor;
		@NotNull private LocalDateTime createdAt;

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
