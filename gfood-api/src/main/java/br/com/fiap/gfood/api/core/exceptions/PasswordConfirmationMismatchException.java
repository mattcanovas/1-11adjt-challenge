package br.com.fiap.gfood.api.core.exceptions;

public class PasswordConfirmationMismatchException extends RuntimeException
{
	private static final long serialVersionUID = 8101462539824712503L;

	public PasswordConfirmationMismatchException(String message)
	{
		super(message);
	}
}
