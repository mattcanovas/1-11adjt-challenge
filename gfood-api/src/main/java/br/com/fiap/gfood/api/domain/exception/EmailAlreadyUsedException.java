package br.com.fiap.gfood.api.domain.exception;

public class EmailAlreadyUsedException extends RuntimeException
{
	private static final long serialVersionUID = 1690912162481238077L;

	public EmailAlreadyUsedException(String message)
	{
		super(message);
	}
}
