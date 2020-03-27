package io.demo.test.controllers.advice;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.demo.test.dtos.ErrorResponseDto;
import io.demo.test.exceptions.UnauthorizedException;
import io.demo.test.exceptions.WrongPasswordException;

@ControllerAdvice
public class UnauthorizedControllerAdvice {

	@ExceptionHandler(value = { UnauthorizedException.class })
	public ResponseEntity<ErrorResponseDto> handleUnauthorizeException(RuntimeException ex,
			HttpServletRequest request) {

		ErrorResponseDto errorResponseDto = new ErrorResponseDto();
		errorResponseDto.setStatus(HttpStatus.UNAUTHORIZED.value());
		errorResponseDto.setError("Unauthorized");
		errorResponseDto.setPath(request.getRequestURL().toString());
		errorResponseDto.setTimestamp(LocalDateTime.now());

		UnauthorizedException unauthorizedException = (UnauthorizedException) ex;
		if (unauthorizedException instanceof WrongPasswordException) {
			errorResponseDto.setMessage("Wrong password");
		}
		else {
			errorResponseDto
					.setMessage("Access refused due to the lack of the correct credentials or a valid access token");
		}

		return new ResponseEntity<ErrorResponseDto>(errorResponseDto, new HttpHeaders(),
				HttpStatus.valueOf(errorResponseDto.getStatus()));

	}

}
