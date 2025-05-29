package com.okestro.resource.server.controller;

import com.okestro.resource.server.application.DeleteInstanceUseCase;
import com.okestro.resource.server.application.dto.DeleteInstanceUseCaseDto;
import com.okestro.resource.server.controller.request.DeleteInstanceRequest;
import com.okestro.resource.support.web.ApiResponse;
import com.okestro.resource.support.web.ApiResponseGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/servers")
@RequiredArgsConstructor
@RestController
public class ServerController {
	private final DeleteInstanceUseCase deleteInstanceUseCase;

	@DeleteMapping("/instances")
	public ApiResponse<ApiResponse.SuccessBody<DeleteInstanceUseCaseDto.DeleteInstanceUseCaseOut>>
			deleteInstance(@RequestBody DeleteInstanceRequest request) {
		DeleteInstanceUseCaseDto.DeleteInstanceUseCaseOut useCaseOut =
				deleteInstanceUseCase.execute(DeleteInstanceUseCaseDto.in(request));

		if (!useCaseOut.getIsDeleted()) {
			return ApiResponseGenerator.success(useCaseOut, HttpStatus.ACCEPTED);
		}
		return ApiResponseGenerator.success(useCaseOut, HttpStatus.OK);
	}
}
