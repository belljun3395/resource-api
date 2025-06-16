package com.okestro.resource.server.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UpdateInstancePowerRequest {
	@Schema(description = "Instance id", example = "1")
	@NotNull
	private Long instanceId;

	@Schema(
			description = "Power status",
			example = "1",
			allowableValues = {"0", "1", "2", "3"})
	@NotNull
	private InstancePowerStatusAction powerStatusAction;
}
