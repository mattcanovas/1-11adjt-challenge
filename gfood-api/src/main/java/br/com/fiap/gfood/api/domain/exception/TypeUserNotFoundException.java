package br.com.fiap.gfood.api.domain.exception;

public class TypeUserNotFoundException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public TypeUserNotFoundException(String message)
	{
		super(message);
	}
}
