package br.com.fiap.gfood.api.application.usecase;

import org.springframework.stereotype.Service;

import br.com.fiap.gfood.api.application.dto.SignInRequest;
import br.com.fiap.gfood.api.domain.exception.AuthenticationFailedException;
import br.com.fiap.gfood.api.domain.gateway.CustomerGateway;

@Service
public class SignInUseCase
{
	private static final String INVALID_LOGIN_AND_PASSWORD = "Invalid login and password.";

	private final CustomerGateway gateway;

	public SignInUseCase(CustomerGateway gateway)
	{
		this.gateway = gateway;
	}

	public void execute(SignInRequest request)
	{
		gateway.findByLoginAndPassword(request.login(), request.password())
				.orElseThrow(() -> new AuthenticationFailedException(INVALID_LOGIN_AND_PASSWORD));
	}
}
