package io.demo.test.controllers.advice;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.demo.test.dtos.ErrorResponseDto;
import io.demo.test.exceptions.AccountNotFoundException;
import io.demo.test.exceptions.NotFoundException;
import io.demo.test.exceptions.ResourceNotFoundException;

@ControllerAdvice
public class NotFoundControllerAdvice {

	@ExceptionHandler(value = { NotFoundException.class })
	public ResponseEntity<ErrorResponseDto> handleNotFoundException(RuntimeException ex, HttpServletRequest request) {

		ErrorResponseDto errorResponseDto = new ErrorResponseDto();
		errorResponseDto.setStatus(HttpStatus.NOT_FOUND.value());
		errorResponseDto.setError("Not found");
		errorResponseDto.setPath(request.getRequestURL().toString());
		errorResponseDto.setTimestamp(LocalDateTime.now());

		NotFoundException notFoundException = (NotFoundException) ex;
		if (notFoundException instanceof ResourceNotFoundException) {
			ResourceNotFoundException resourceNotFoundException = (ResourceNotFoundException) notFoundException;
			errorResponseDto.setMessage("No record was found for the given id: " + resourceNotFoundException.getId());
		}
		else if (notFoundException instanceof AccountNotFoundException) {
			AccountNotFoundException accountNotFoundException = (AccountNotFoundException) notFoundException;
			errorResponseDto
					.setMessage("No account was found for the given email: " + accountNotFoundException.getEmail());
		}
		else {
			errorResponseDto.setMessage("Not found");
		}

		return new ResponseEntity<ErrorResponseDto>(errorResponseDto, new HttpHeaders(),
				HttpStatus.valueOf(errorResponseDto.getStatus()));

	}

}
