package com.okestro.resource.server.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.okestro.resource.server.controller.request.InstancePowerStatusAction;
import com.okestro.resource.server.controller.request.UpdateInstancePowerRequest;
import com.okestro.resource.server.domain.model.instance.Instance;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UpdateInstancePowerUsecaseDto {
	public static UpdateInstancePowerUseCaseIn in(UpdateInstancePowerRequest request) {
		return UpdateInstancePowerUseCaseIn.builder()
				.instanceId(request.getInstanceId())
				.powerStatusAction(request.getPowerStatusAction())
				.build();
	}

	public static UpdateInstanceUseCaseOut out(Instance instance, boolean isUpdated) {
		return UpdateInstanceUseCaseOut.builder()
				.id(instance.getId())
				.name(instance.getName())
				.description(instance.getDescription())
				.alias(instance.getAlias().getValue())
				.powerStatus(instance.getPowerStatus().name())
				.host(instance.getHost().getValue())
				.updatedAt(instance.getUpdatedAt())
				.isUpdated(isUpdated)
				.build();
	}

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder(toBuilder = true)
	public static class UpdateInstancePowerUseCaseIn {
		@NotNull private Long instanceId;
		@NotNull private InstancePowerStatusAction powerStatusAction;
	}

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder(toBuilder = true)
	public static class UpdateInstanceUseCaseOut {
		@NotNull private Long id;
		@NotNull private String name;
		@NotNull private String description;
		@NotNull private String alias;
		@NotNull private String powerStatus;
		@NotNull private String host;
		@NotNull private LocalDateTime updatedAt;
		@JsonIgnore @NotNull private Boolean isUpdated;
	}
}
