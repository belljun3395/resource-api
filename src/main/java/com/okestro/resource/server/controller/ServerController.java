package com.okestro.resource.server.controller;

import com.okestro.resource.server.application.FindInstanceUseCase;
import com.okestro.resource.server.application.dto.FindInstanceUseCaseDto;
import com.okestro.resource.support.web.ApiResponse;
import com.okestro.resource.support.web.ApiResponseGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/servers")
@RequiredArgsConstructor
@RestController
public class ServerController {
	private final FindInstanceUseCase findInstanceUseCase;

	@GetMapping("/instances/{instanceId}")
	public ApiResponse<ApiResponse.SuccessBody<FindInstanceUseCaseDto.FindInstanceUseCaseOut>>
			findInstance(@PathVariable Long instanceId) {
		FindInstanceUseCaseDto.FindInstanceUseCaseOut useCaseOut =
				findInstanceUseCase.execute(FindInstanceUseCaseDto.in(instanceId));
		return ApiResponseGenerator.success(useCaseOut, HttpStatus.OK);
	}
}
