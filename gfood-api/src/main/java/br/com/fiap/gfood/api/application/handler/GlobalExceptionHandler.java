package br.com.fiap.gfood.api.application.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.fiap.gfood.api.application.dto.ProblemDetail;
import br.com.fiap.gfood.api.domain.exception.AuthenticationFailedException;
import br.com.fiap.gfood.api.domain.exception.EmailAlreadyUsedException;
import br.com.fiap.gfood.api.domain.exception.PasswordConfirmationMismatchException;
import br.com.fiap.gfood.api.domain.exception.PasswordMismatchException;
import br.com.fiap.gfood.api.domain.exception.OwnerUserRequiredException;
import br.com.fiap.gfood.api.domain.exception.RestaurantNameAlreadyExistsException;
import br.com.fiap.gfood.api.domain.exception.RestaurantNotFoundException;
import br.com.fiap.gfood.api.domain.exception.TypeUserNameAlreadyExistsException;
import br.com.fiap.gfood.api.domain.exception.TypeUserNotFoundException;
import br.com.fiap.gfood.api.domain.exception.UserNotFoundException;
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
				"Your request parameters didn't validate",
				"The parameters that was send in request's body is not valid according with business rules.",
				errors), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(exception = HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<Object> httpRequestMethodNotSupportedExceptionHandler(
			HttpRequestMethodNotSupportedException exception, HttpServletRequest request)
	{
		Map<String, String> errors = new HashMap<>();
		errors.put(exception.getMethod(), "Not allowed");
		return new ResponseEntity<>(new ProblemDetail(Boolean.FALSE, request.getRequestURL().toString(),
				"Method requested not allowed",
				"Method that was requested is not allowed by business logic.", errors),
				HttpStatus.METHOD_NOT_ALLOWED);
	}

	@ExceptionHandler(exception = EmailAlreadyUsedException.class)
	public ResponseEntity<Object> emailAlreadyUsedException(EmailAlreadyUsedException exception,
			HttpServletRequest request)
	{
		Map<String, String> errors = new HashMap<>();
		errors.put("email", exception.getMessage());
		return new ResponseEntity<>(new ProblemDetail(Boolean.FALSE, request.getRequestURL().toString(),
				"The email informed is used in another account.",
				"The email that was sended to register new user is aleady used. Try another one.",
				errors), HttpStatus.PRECONDITION_FAILED);
	}

	@ExceptionHandler(exception = UserNotFoundException.class)
	public ResponseEntity<Object> userNotFoundExceptionHandler(UserNotFoundException exception,
			HttpServletRequest request)
	{
		Map<String, String> errors = new HashMap<>();
		errors.put("id", exception.getMessage());
		return new ResponseEntity<>(new ProblemDetail(Boolean.FALSE, request.getRequestURL().toString(),
				"The user requested was not found.",
				"The user requested to updated was not found. Try again.", errors),
				HttpStatus.PRECONDITION_FAILED);
	}

	@ExceptionHandler(exception = TypeUserNotFoundException.class)
	public ResponseEntity<Object> typeUserNotFoundExceptionHandler(TypeUserNotFoundException exception,
			HttpServletRequest request)
	{
		Map<String, String> errors = new HashMap<>();
		errors.put("id", exception.getMessage());
		return new ResponseEntity<>(new ProblemDetail(Boolean.FALSE, request.getRequestURL().toString(),
				"The type user requested was not found.",
				"The type user requested was not found. Try again.", errors),
				HttpStatus.PRECONDITION_FAILED);
	}

	@ExceptionHandler(exception = TypeUserNameAlreadyExistsException.class)
	public ResponseEntity<Object> typeUserNameAlreadyExistsExceptionHandler(TypeUserNameAlreadyExistsException exception,
			HttpServletRequest request)
	{
		Map<String, String> errors = new HashMap<>();
		errors.put("name", exception.getMessage());
		return new ResponseEntity<>(new ProblemDetail(Boolean.FALSE, request.getRequestURL().toString(),
				"The type user name informed is already in use.",
				"The name that was informed to register or update type user is already used. Try another one.",
				errors), HttpStatus.PRECONDITION_FAILED);
	}

	@ExceptionHandler(exception = PasswordMismatchException.class)
	public ResponseEntity<Object> passwordMismatchExceptionHandler(PasswordMismatchException exception,
			HttpServletRequest request)
	{
		Map<String, String> errors = new HashMap<>();
		errors.put("old_password", exception.getMessage());
		return new ResponseEntity<>(new ProblemDetail(Boolean.FALSE, request.getRequestURL().toString(),
				"The old password informed mismatch",
				"The old password informed on the body requisition mismatched with the real password.", errors),
				HttpStatus.PRECONDITION_FAILED);
	}

	@ExceptionHandler(exception = PasswordConfirmationMismatchException.class)
	public ResponseEntity<Object> passwordConfirmationMismatchException(
			PasswordConfirmationMismatchException exception, HttpServletRequest request)
	{
		Map<String, String> errors = new HashMap<>();
		errors.put("confirmation_password", exception.getMessage());
		return new ResponseEntity<>(new ProblemDetail(Boolean.FALSE, request.getRequestURL().toString(),
				"The confirmation password mismatched.",
				"The confirmation password informed on the request body mismatched with the new password. Verify and try again.",
				errors), HttpStatus.PRECONDITION_FAILED);
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
				"The login and password informed in the request body are invalid. Try again", errors),
				HttpStatus.PRECONDITION_FAILED);
	}

	@ExceptionHandler(exception = RestaurantNotFoundException.class)
	public ResponseEntity<Object> restaurantNotFoundExceptionHandler(RestaurantNotFoundException exception,
			HttpServletRequest request)
	{
		Map<String, String> errors = new HashMap<>();
		errors.put("id", exception.getMessage());
		return new ResponseEntity<>(new ProblemDetail(Boolean.FALSE, request.getRequestURL().toString(),
				"The restaurant requested was not found.",
				"The restaurant requested was not found. Try again.", errors),
				HttpStatus.PRECONDITION_FAILED);
	}

	@ExceptionHandler(exception = RestaurantNameAlreadyExistsException.class)
	public ResponseEntity<Object> restaurantNameAlreadyExistsExceptionHandler(RestaurantNameAlreadyExistsException exception,
			HttpServletRequest request)
	{
		Map<String, String> errors = new HashMap<>();
		errors.put("name", exception.getMessage());
		return new ResponseEntity<>(new ProblemDetail(Boolean.FALSE, request.getRequestURL().toString(),
				"The restaurant name informed is already in use.",
				"The name that was informed to register or update restaurant is already used. Try another one.",
				errors), HttpStatus.PRECONDITION_FAILED);
	}

	@ExceptionHandler(exception = OwnerUserRequiredException.class)
	public ResponseEntity<Object> ownerUserRequiredExceptionHandler(OwnerUserRequiredException exception,
			HttpServletRequest request)
	{
		Map<String, String> errors = new HashMap<>();
		errors.put("ownerId", exception.getMessage());
		return new ResponseEntity<>(new ProblemDetail(Boolean.FALSE, request.getRequestURL().toString(),
				"The user must have the type OWNER.",
				"The user informed as owner must have the type OWNER to create a restaurant.",
				errors), HttpStatus.PRECONDITION_FAILED);
	}
}
