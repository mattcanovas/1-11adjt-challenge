package br.com.fiap.gfood.api.core.domain;

import org.springframework.data.domain.Page;

public record ApiPageResponse(Boolean success, Page<?> page)
{

}
