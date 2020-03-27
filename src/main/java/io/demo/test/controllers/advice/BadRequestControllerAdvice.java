package io.demo.test.controllers.advice;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import io.demo.test.dtos.ErrorResponseDto;
import io.demo.test.exceptions.BadRequestException;
import io.demo.test.exceptions.ForeignKeyIntegrityViolationException;
import io.demo.test.exceptions.InvalidEmailAddressException;
import io.demo.test.exceptions.JsonPatchException;
import io.demo.test.exceptions.MissingRequiredArgumentException;
import io.demo.test.exceptions.UniqueConstraintViolationException;

@ControllerAdvice
public class BadRequestControllerAdvice {

	@ExceptionHandler(value = { BadRequestException.class })
	public ResponseEntity<ErrorResponseDto> handleBadRequestException(RuntimeException ex, HttpServletRequest request) {

		BadRequestException badRequestException = (BadRequestException) ex;

		ErrorResponseDto errorResponseDto = new ErrorResponseDto();
		errorResponseDto.setError("Bad Request");
		errorResponseDto.setDetails(ex.getCause() == null ? "" : ex.getCause().getMessage());
		errorResponseDto.setPath(request.getRequestURL().toString());
		errorResponseDto.setTimestamp(LocalDateTime.now());

		if (badRequestException instanceof ForeignKeyIntegrityViolationException) {
			errorResponseDto.setStatus(HttpStatus.BAD_REQUEST.value());
			errorResponseDto.setMessage(
					"Foreign key integrity violation: there might be some records in the database that depend on this resource");
		}
		else if (badRequestException instanceof UniqueConstraintViolationException) {
			errorResponseDto.setStatus(HttpStatus.CONFLICT.value());
			errorResponseDto.setMessage(
					"Unique constraint integrity violation: an attempt to insert or update a record in database that may generate a unique constraint violation");
		}
		else if (badRequestException instanceof MissingRequiredArgumentException) {
			errorResponseDto.setStatus(HttpStatus.BAD_REQUEST.value());
			errorResponseDto.setMessage("A required argument for the requested operation is missing or invalid");
		}
		else if (badRequestException instanceof JsonPatchException) {
			errorResponseDto.setStatus(HttpStatus.BAD_REQUEST.value());
			errorResponseDto.setMessage(
					"The supplied JSON patch operations sequence is malformed or cannot be applied to the JSON document");
		}
		else if (badRequestException instanceof InvalidEmailAddressException) {
			errorResponseDto.setStatus(HttpStatus.BAD_REQUEST.value());
			errorResponseDto.setMessage("Invalid email address");
		}
		else {
			errorResponseDto.setStatus(HttpStatus.BAD_REQUEST.value());
			errorResponseDto.setMessage("Bad request");
		}

		return new ResponseEntity<ErrorResponseDto>(errorResponseDto, new HttpHeaders(),
				HttpStatus.valueOf(errorResponseDto.getStatus()));

	}

	@ExceptionHandler(value = { ConstraintViolationException.class })
	public ResponseEntity<ErrorResponseDto> handleConstraintViolationException(RuntimeException ex,
			HttpServletRequest request) {

		ConstraintViolationException constraintViolationException = (ConstraintViolationException) ex;

		ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), "Bad request",
				"Some validation constraints may be violated due to the recent operation, consult details for more informations",
				constraintViolationException.getConstraintViolations().toString(), request.getRequestURL().toString());

		return new ResponseEntity<ErrorResponseDto>(errorResponseDto, new HttpHeaders(),
				HttpStatus.valueOf(errorResponseDto.getStatus()));

	}

	@ExceptionHandler(value = { HttpMessageNotReadableException.class })
	public ResponseEntity<ErrorResponseDto> handleInvalidFormatException(RuntimeException ex,
			HttpServletRequest request) {

		HttpMessageNotReadableException httpMessageNotReadableException = (HttpMessageNotReadableException) ex;

		if (!httpMessageNotReadableException.contains(InvalidFormatException.class))
			throw ex;

		InvalidFormatException invalidFormatException = (InvalidFormatException) httpMessageNotReadableException
				.getMostSpecificCause();
		ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), "Bad request",
				"JSON Parse Error: Invalid JSON Format", invalidFormatException.getMessage(),
				request.getRequestURL().toString());

		return new ResponseEntity<ErrorResponseDto>(errorResponseDto, new HttpHeaders(),
				HttpStatus.valueOf(errorResponseDto.getStatus()));

	}

}
