package com.okestro.resource.server.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class DeleteInstanceRequest {
	@Schema(
			description = "The ID of the instance to be deleted",
			example = "1",
			requiredMode = Schema.RequiredMode.REQUIRED)
	private Long instanceId;
}
