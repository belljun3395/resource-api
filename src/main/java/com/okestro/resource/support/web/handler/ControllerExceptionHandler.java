package com.okestro.resource.support.web.handler;

import com.okestro.resource.support.web.ApiResponse;
import com.okestro.resource.support.web.ApiResponseGenerator;
import com.okestro.resource.support.web.ExceptionMessage;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.codec.DecodingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ServerWebInputException;

@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerExceptionHandler {

	private final LoggingHandler loggingHandler;

	@ExceptionHandler(IllegalArgumentException.class)
	public ApiResponse<ApiResponse.FailureBody> handleBadRequest(
			IllegalArgumentException ex, HttpServletRequest request) {
		loggingHandler.writeLog(ex, request);
		return ApiResponseGenerator.fail(ExceptionMessage.FAIL.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({
		MethodArgumentTypeMismatchException.class,
		TypeMismatchException.class,
		WebExchangeBindException.class,
		BindException.class,
		MethodArgumentNotValidException.class,
		DecodingException.class,
		ServerWebInputException.class,
		HttpMessageNotReadableException.class
	})
	public ApiResponse<ApiResponse.FailureBody> handleBadRequest(
			Exception ex, HttpServletRequest request) {
		loggingHandler.writeLog(ex, request);
		return handleRequestDetails(ex);
	}

	private ApiResponse<ApiResponse.FailureBody> handleRequestDetails(Exception ex) {
		if (ex instanceof MethodArgumentTypeMismatchException) {
			return handleRequestDetail((MethodArgumentTypeMismatchException) ex);
		}
		if (ex instanceof MethodArgumentNotValidException) {
			return handleRequestDetail((MethodArgumentNotValidException) ex);
		}
		return ApiResponseGenerator.fail(
				ExceptionMessage.FAIL_REQUEST.getMessage(), HttpStatus.BAD_REQUEST);
	}

	private ApiResponse<ApiResponse.FailureBody> handleRequestDetail(
			MethodArgumentTypeMismatchException ex) {
		String messageDetail =
				ExceptionMessage.REQUEST_INVALID_FORMAT.getMessage() + " : " + ex.getName();
		return ApiResponseGenerator.fail(messageDetail, HttpStatus.BAD_REQUEST);
	}

	private ApiResponse<ApiResponse.FailureBody> handleRequestDetail(
			MethodArgumentNotValidException ex) {
		List<?> fieldErrors = ex.getFieldErrors();
		String messageDetail = ExceptionMessage.REQUEST_INVALID.getMessage() + " : " + fieldErrors;
		return ApiResponseGenerator.fail(messageDetail, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(IllegalStateException.class)
	public ApiResponse<ApiResponse.FailureBody> handleIllegalState(
			Exception ex, HttpServletRequest request) {
		loggingHandler.writeLog(ex, request);
		return ApiResponseGenerator.fail(
				ExceptionMessage.FAIL.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(NoSuchElementException.class)
	public ApiResponse<ApiResponse.FailureBody> handleForbidden(
			AccessDeniedException ex, HttpServletRequest request) {
		loggingHandler.writeLog(ex, request);
		return ApiResponseGenerator.fail(
				ExceptionMessage.ACCESS_DENIED.getMessage(), HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(Exception.class)
	public ApiResponse<ApiResponse.FailureBody> handleInternalServerError(
			Exception ex, HttpServletRequest request) {
		loggingHandler.writeLog(ex, request);
		return ApiResponseGenerator.fail(
				ExceptionMessage.FAIL.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
