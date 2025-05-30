package com.okestro.resource.server.application.dto;

import com.okestro.resource.server.controller.request.DeleteInstanceRequest;
import com.okestro.resource.server.domain.model.instance.DeleteInstance;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class DeleteInstanceUseCaseDto {
	public static DeleteInstanceUseCaseIn in(DeleteInstanceRequest request) {
		return DeleteInstanceUseCaseIn.builder().instanceId(request.getInstanceId()).build();
	}

	public static DeleteInstanceUseCaseOut out(DeleteInstance deleteInstance) {
		return DeleteInstanceUseCaseOut.builder()
				.instanceId(deleteInstance.getId())
				.isAccepted(true)
				.isDeleted(deleteInstance.getIsDeleted())
				.build();
	}

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder(toBuilder = true)
	public static class DeleteInstanceUseCaseIn {
		@NotNull private Long instanceId;
	}

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder(toBuilder = true)
	public static class DeleteInstanceUseCaseOut {
		private Long instanceId;
		private Boolean isAccepted;
		private Boolean isDeleted;
	}
}
