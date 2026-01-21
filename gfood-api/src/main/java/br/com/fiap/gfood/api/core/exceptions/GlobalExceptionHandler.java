package br.com.fiap.gfood.api.core.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.fiap.gfood.api.core.commons.Constants;
import br.com.fiap.gfood.api.core.domain.ProblemDetail;
import jakarta.persistence.EntityNotFoundException;
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
		return new ResponseEntity<>(new ProblemDetail(Boolean.FALSE, request.getRequestURL().toString(),
				Constants.YOUR_REQUEST_PARAMETERS_DIDNT_VALIDATE,
				Constants.THE_PARAMETERS_THAT_WAS_SEND_IN_REQUESTS_BODY_IS_NOT_VALID_ACCORDING_BUSINESS_RULES,
				errors), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(exception = HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<Object> methodArgumentNotValidExceptionHandler(
			HttpRequestMethodNotSupportedException exception, HttpServletRequest request)
	{
		Map<String, String> errors = new HashMap<>();
		errors.put(exception.getMethod(), "Not allowed");
		return new ResponseEntity<>(new ProblemDetail(Boolean.FALSE, request.getRequestURL().toString(),
				Constants.METHOD_NOT_ALLOWED,
				Constants.METHOD_THAT_WAS_REQUESTED_IS_NOT_ALLOWED_BY_BUSINESS_LOGIC, errors),
				HttpStatus.METHOD_NOT_ALLOWED);
	}

	@ExceptionHandler(exception = EmailAlreadyUsedException.class)
	public ResponseEntity<Object> emailAlreadyUsedException(EmailAlreadyUsedException exception,
			HttpServletRequest request)
	{
		Map<String, String> errors = new HashMap<>();
		errors.put("email", exception.getMessage());
		return new ResponseEntity<>(new ProblemDetail(Boolean.FALSE, request.getRequestURL().toString(),
				Constants.THE_EMAIL_INFORMED_IS_ALREAD_USED_IN_ANOTHER_ACCOUNT,
				Constants.THE_EMAIL_THAT_WAS_SENDED_TO_REGISTER_NEW_CUSTOMER_IS_ALREADY_USED_TRY_ANOTHER_ONE,
				errors), HttpStatus.PRECONDITION_FAILED);
	}

	@ExceptionHandler(exception = EntityNotFoundException.class)
	public ResponseEntity<Object> entityNotFoundExceptionHandler(EntityNotFoundException exception,
			HttpServletRequest request)
	{
		Map<String, String> errors = new HashMap<>();
		errors.put("id", exception.getMessage());
		return new ResponseEntity<>(new ProblemDetail(Boolean.FALSE, request.getRequestURL().toString(),
				Constants.MESSAGE_ERROR_CUSTOMER_NOT_FOUND,
				Constants.THE_CUSTOMER_REQUESTED_TO_UPDATE_WAS_NOT_FOUND_TRY_AGAIN, errors),
				HttpStatus.PRECONDITION_FAILED);
	}

	@ExceptionHandler(exception = PasswordMismatchException.class)
	public ResponseEntity<Object> passwordMismatchExceptionHandler(PasswordMismatchException exception,
			HttpServletRequest request)
	{
		Map<String, String> errors = new HashMap<>();
		errors.put("old_password", exception.getMessage());
		return new ResponseEntity<>(new ProblemDetail(Boolean.FALSE, request.getRequestURL().toString(),
				Constants.THE_OLD_PASSWORD_MISMATCH,
				Constants.THE_OLD_PASSWORD_INFORMED_BODY_REQUISITION_MISMATCHED_DETAILED, errors),
				HttpStatus.PRECONDITION_FAILED);
	}

	@ExceptionHandler(exception = PasswordConfirmationMismatchException.class)
	public ResponseEntity<Object> passwordConfirmationMismatchException(
			PasswordConfirmationMismatchException exception, HttpServletRequest request)
	{
		Map<String, String> errors = new HashMap<>();
		errors.put("confirmation_password", exception.getMessage());
		return new ResponseEntity<>(new ProblemDetail(Boolean.FALSE, request.getRequestURL().toString(),
				Constants.THE_CONFIRMATION_OF_NEW_PASSWORD_MISMATCH,
				Constants.THE_CONFIRMATION_PASSWORD_INFORMED_ON_REQUEST_BODY_MISMATCHED, errors),
				HttpStatus.PRECONDITION_FAILED);
	}

	@ExceptionHandler(exception = AuthenticationFailedException.class)
	public ResponseEntity<Object> authenticationFailedExceptionHandler(AuthenticationFailedException exception,
			HttpServletRequest request)
	{
		Map<String, String> errors = new HashMap<>();
		errors.put("login", exception.getMessage());
		errors.put("password", exception.getMessage());
		return new ResponseEntity<>(new ProblemDetail(Boolean.FALSE, request.getRequestURL().toString(),
				exception.getMessage(),
				Constants.THE_LOGIN_AND_PASSWORD_INFORMED_ON_REQUEST_BODY_ARE_INVALID, errors),
				HttpStatus.PRECONDITION_FAILED);
	}

}
