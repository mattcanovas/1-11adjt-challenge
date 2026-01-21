package br.com.fiap.gfood.api.core.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.fiap.gfood.api.core.commons.Constants;
import br.com.fiap.gfood.api.core.domain.ProblemDetail;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler
{
	@ExceptionHandler(exception = MethodArgumentNotValidException.class)
	public ResponseEntity<Object> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception,
			HttpServletRequest request)
	{
		Map<String, String> errors = new HashMap<>();
		exception.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return new ResponseEntity<>(
				new ProblemDetail(Boolean.FALSE, request.getRequestURL().toString(),
						Constants.YOUR_REQUEST_PARAMETERS_DIDNT_VALIDATE, errors),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(exception = EmailAlreadyUsedException.class)
	public ResponseEntity<Object> emailAlreadyUsedException(EmailAlreadyUsedException exception,
			HttpServletRequest request)
	{
		Map<String, String> errors = new HashMap<>();
		errors.put("email", exception.getMessage());
		return new ResponseEntity<>(
				new ProblemDetail(Boolean.FALSE, request.getRequestURL().toString(),
						Constants.THE_EMAIL_INFORMED_IS_ALREAD_USED_IN_ANOTHER_ACCOUNT, errors),
				HttpStatus.BAD_REQUEST);
	}

}
