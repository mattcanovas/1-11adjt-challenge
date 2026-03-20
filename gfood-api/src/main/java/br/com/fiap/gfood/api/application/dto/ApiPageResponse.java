package br.com.fiap.gfood.api.application.dto;

import org.springframework.data.domain.Page;

public record ApiPageResponse(Boolean success, Page<?> page)
{
}
