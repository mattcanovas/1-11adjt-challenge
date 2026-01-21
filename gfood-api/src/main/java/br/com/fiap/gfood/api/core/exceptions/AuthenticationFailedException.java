package br.com.fiap.gfood.api.core.exceptions;

public class AuthenticationFailedException extends RuntimeException
{
	private static final long serialVersionUID = -2693408730724215873L;

	public AuthenticationFailedException(String message)
	{
		super(message);
	}
}
