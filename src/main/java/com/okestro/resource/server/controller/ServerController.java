package com.okestro.resource.server.controller;

import com.okestro.resource.server.application.PostInstanceUseCase;
import com.okestro.resource.server.application.UpdateInstancePowerUseCase;
import com.okestro.resource.server.application.dto.PostInstanceUseCaseDto;
import com.okestro.resource.server.application.dto.UpdateInstancePowerUsecaseDto;
import com.okestro.resource.server.controller.request.PostInstanceRequest;
import com.okestro.resource.server.controller.request.UpdateInstancePowerRequest;
import com.okestro.resource.support.web.ApiResponse;
import com.okestro.resource.support.web.ApiResponseGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/servers")
@RequiredArgsConstructor
@RestController
public class ServerController {
	private final UpdateInstancePowerUseCase updateInstancePowerUseCase;
	private final PostInstanceUseCase postInstanceUseCase;

	@PostMapping("/instances")
	public ApiResponse<ApiResponse.SuccessBody<PostInstanceUseCaseDto.PostInstanceUseCaseOut>>
			postInstance(@RequestBody PostInstanceRequest request) {
		PostInstanceUseCaseDto.PostInstanceUseCaseOut useCaseOut =
				postInstanceUseCase.execute(PostInstanceUseCaseDto.in(request));
		return ApiResponseGenerator.success(useCaseOut, HttpStatus.CREATED);
	}

	@PutMapping("/instances/power")
	public ApiResponse<
					ApiResponse.SuccessBody<UpdateInstancePowerUsecaseDto.UpdateInstanceUseCaseOut>>
			updateInstancePower(@RequestBody UpdateInstancePowerRequest request) {
		UpdateInstancePowerUsecaseDto.UpdateInstanceUseCaseOut useCaseOut =
				updateInstancePowerUseCase.execute(UpdateInstancePowerUsecaseDto.in(request));
		return ApiResponseGenerator.success(useCaseOut, HttpStatus.OK);
	}
}
