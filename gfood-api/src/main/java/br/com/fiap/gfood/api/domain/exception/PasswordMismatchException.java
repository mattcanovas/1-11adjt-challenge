package br.com.fiap.gfood.api.domain.exception;

public class PasswordMismatchException extends RuntimeException
{
	private static final long serialVersionUID = -1942589454447847462L;

	public PasswordMismatchException(String message)
	{
		super(message);
	}
}
