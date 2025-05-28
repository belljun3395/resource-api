package com.okestro.resource.server.controller;

import com.okestro.resource.server.application.UpdateInstancePowerUseCase;
import com.okestro.resource.server.application.dto.UpdateInstancePowerUsecaseDto;
import com.okestro.resource.server.controller.request.UpdateInstancePowerRequest;
import com.okestro.resource.support.web.ApiResponse;
import com.okestro.resource.support.web.ApiResponseGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/servers")
@RequiredArgsConstructor
@RestController
public class ServerController {
	private final UpdateInstancePowerUseCase updateInstancePowerUseCase;

	@PutMapping("/instances/power")
	public ApiResponse<
					ApiResponse.SuccessBody<UpdateInstancePowerUsecaseDto.UpdateInstanceUseCaseOut>>
			updateInstancePower(@RequestBody UpdateInstancePowerRequest request) {
		UpdateInstancePowerUsecaseDto.UpdateInstanceUseCaseOut useCaseOut =
				updateInstancePowerUseCase.execute(UpdateInstancePowerUsecaseDto.in(request));
		if (useCaseOut.getIsUpdated()) {
			return ApiResponseGenerator.success(useCaseOut, HttpStatus.OK);
		}
		return ApiResponseGenerator.success(useCaseOut, HttpStatus.NOT_MODIFIED);
	}
}
