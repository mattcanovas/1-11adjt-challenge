package br.com.fiap.gfood.api.domain.exception;

public class TypeUserNameAlreadyExistsException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public TypeUserNameAlreadyExistsException(String message)
	{
		super(message);
	}
}
