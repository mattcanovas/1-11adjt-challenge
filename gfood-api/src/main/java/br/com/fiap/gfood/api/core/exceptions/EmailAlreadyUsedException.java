package br.com.fiap.gfood.api.core.exceptions;

public class EmailAlreadyUsedException extends RuntimeException
{
	private static final long serialVersionUID = 1690912162481238077L;

	public EmailAlreadyUsedException(String message)
	{
		super(message);
	}
}
