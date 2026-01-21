package br.com.fiap.gfood.api.core.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
@OpenAPIDefinition
public class OpenApiConfig
{
	@Bean
	OpenAPI gfood() {
		return new OpenAPI().info(new Info().title("GFood API")
				.description("Projeto desenvolvido durante o tech challenge 11-adjt, Spring MVC")
				.version("v0.0.1")
				.license(new License().name("Apache 2.0").url("https://github.com/mattcanovas")));
	}
}
