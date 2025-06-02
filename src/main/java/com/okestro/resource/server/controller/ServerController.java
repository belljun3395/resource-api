package com.okestro.resource.server.controller;

import static com.okestro.resource.server.application.dto.BrowseInstanceUseCaseDto.toPage;

import com.okestro.resource.server.application.BrowseInstanceUseCase;
import com.okestro.resource.server.application.FindInstanceUseCase;
import com.okestro.resource.server.application.dto.BrowseInstanceUseCaseDto;
import com.okestro.resource.server.application.dto.FindInstanceUseCaseDto;
import com.okestro.resource.support.web.ApiResponse;
import com.okestro.resource.support.web.ApiResponseGenerator;
import com.okestro.resource.support.web.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/servers")
@RequiredArgsConstructor
@RestController
public class ServerController {
	private final FindInstanceUseCase findInstanceUseCase;
	private final BrowseInstanceUseCase browseInstanceUseCase;

	@GetMapping("/instances/{instanceId}")
	public ApiResponse<ApiResponse.SuccessBody<FindInstanceUseCaseDto.FindInstanceUseCaseOut>>
			findInstance(@PathVariable Long instanceId) {
		FindInstanceUseCaseDto.FindInstanceUseCaseOut useCaseOut =
				findInstanceUseCase.execute(FindInstanceUseCaseDto.in(instanceId));
		return ApiResponseGenerator.success(useCaseOut, HttpStatus.OK);
	}

	@GetMapping("/instances")
	public ApiResponse<Page<BrowseInstanceUseCaseDto.BrowseInstanceUseCaseOut.InstanceDto>>
			browseInstances(
					@RequestParam(value = "page", defaultValue = "0") Long pageNumber,
					@RequestParam(value = "size", defaultValue = "10") Integer pageSize) {
		BrowseInstanceUseCaseDto.BrowseInstanceUseCaseOut useCaseOut =
				browseInstanceUseCase.execute(BrowseInstanceUseCaseDto.in(pageNumber, pageSize));
		return ApiResponseGenerator.success(toPage(useCaseOut), HttpStatus.OK);
	}
}
