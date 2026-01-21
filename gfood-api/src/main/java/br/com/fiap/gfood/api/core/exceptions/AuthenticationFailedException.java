package br.com.fiap.gfood.api.core.exceptions;

public class AuthenticationFailedException extends RuntimeException
{
	public AuthenticationFailedException(String message)
	{
		super(message);
	}
}
