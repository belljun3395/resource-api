package com.okestro.resource.server.controller;

import static com.okestro.resource.server.application.dto.BrowseInstanceUseCaseDto.toPage;

import com.okestro.resource.server.application.BrowseInstanceUseCase;
import com.okestro.resource.server.application.DeleteInstanceUseCase;
import com.okestro.resource.server.application.FindInstanceUseCase;
import com.okestro.resource.server.application.PostInstanceUseCase;
import com.okestro.resource.server.application.UpdateInstancePowerUseCase;
import com.okestro.resource.server.application.dto.BrowseInstanceUseCaseDto;
import com.okestro.resource.server.application.dto.DeleteInstanceUseCaseDto;
import com.okestro.resource.server.application.dto.FindInstanceUseCaseDto;
import com.okestro.resource.server.application.dto.PostInstanceUseCaseDto;
import com.okestro.resource.server.application.dto.UpdateInstancePowerUsecaseDto;
import com.okestro.resource.server.controller.request.DeleteInstanceRequest;
import com.okestro.resource.server.controller.request.PostInstanceRequest;
import com.okestro.resource.server.controller.request.UpdateInstancePowerRequest;
import com.okestro.resource.support.web.ApiResponse;
import com.okestro.resource.support.web.ApiResponseGenerator;
import com.okestro.resource.support.web.Page;
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
	private final PostInstanceUseCase postInstanceUseCase;
	private final UpdateInstancePowerUseCase updateInstancePowerUseCase;
	private final DeleteInstanceUseCase deleteInstanceUseCase;

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
