package br.com.fiap.gfood.api.core.domain;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProblemDetail(
		@JsonProperty("success")
		Boolean success,
		@JsonProperty("type")
		String type,
		@JsonProperty("title")
		String title,
		@JsonProperty("details")
		String details,
		@JsonProperty("errors")
		Map<String,String> errors
		)
{
	
}
