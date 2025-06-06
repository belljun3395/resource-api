package com.okestro.resource.support.web;

import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import lombok.experimental.UtilityClass;
import org.hibernate.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@UtilityClass
public class FailureBodyResolver {

	public static ApiResponse.FailureBody resolveFrom(final ConstraintViolationException ex) {
		return ex.getConstraintViolations().stream()
				.map(
						v -> {
							String fieldName = "";
							for (Path.Node node : v.getPropertyPath()) {
								fieldName = node.getName();
							}

							return new ApiResponse.FailureBody(fieldName, v.getMessage());
						})
				.findFirst()
				.orElse(null);
	}

	public static ApiResponse.FailureBody resolveFrom(final ServletRequestBindingException ex) {
		return new ApiResponse.FailureBody(ex.getMessage());
	}

	public static ApiResponse.FailureBody resolveFrom(final TypeMismatchException ex) {
		return new ApiResponse.FailureBody(ex.getMessage());
	}

	public static ApiResponse.FailureBody resolveFrom(final BindException ex) {
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			return new ApiResponse.FailureBody(error.getCode(), error.getDefaultMessage());
		}
		return null;
	}

	public static ApiResponse.FailureBody resolveFrom(
			final HttpRequestMethodNotSupportedException ex) {
		return new ApiResponse.FailureBody(ex.getLocalizedMessage());
	}

	public static ApiResponse.FailureBody resolveFrom(final HttpMediaTypeNotSupportedException ex) {
		return new ApiResponse.FailureBody(ex.getLocalizedMessage());
	}

	public static ApiResponse.FailureBody resolveFrom(final HttpMediaTypeNotAcceptableException ex) {
		return new ApiResponse.FailureBody(ex.getLocalizedMessage());
	}

	public static ApiResponse.FailureBody resolveFrom(HttpMessageNotReadableException ex) {
		return new ApiResponse.FailureBody(ex.getLocalizedMessage());
	}

	public static ApiResponse.FailureBody resolveFrom(MissingServletRequestPartException ex) {
		return new ApiResponse.FailureBody(ex.getLocalizedMessage());
	}
}
