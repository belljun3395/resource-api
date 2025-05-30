package com.okestro.resource.server.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PostInstanceRequest {
	@Schema(description = "Instance name", example = "test-instance")
	@NotNull
	private String name;

	@Builder.Default private String description = "";

	@Schema(description = "Instance host", example = "192.168.1.1")
	@NotNull
	private String host;

	@Schema(description = "Flavor id", example = "1")
	@NotNull
	private Long flavorId;

	@Schema(
			description = "Source type",
			example = "IMAGE",
			allowableValues = {"IMAGE", "INSTANCE_SNAPSHOT", "BOOTABLE_VOLUME", "VOLUME_SNAPSHOT"})
	@NotNull
	private String sourceType;

	@Schema(description = "Source id", example = "1")
	@NotNull
	private Long sourceId;
}
