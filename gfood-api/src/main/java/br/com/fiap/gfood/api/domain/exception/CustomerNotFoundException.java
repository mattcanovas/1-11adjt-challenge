package br.com.fiap.gfood.api.domain.exception;

public class CustomerNotFoundException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public CustomerNotFoundException(String message)
	{
		super(message);
	}
}
