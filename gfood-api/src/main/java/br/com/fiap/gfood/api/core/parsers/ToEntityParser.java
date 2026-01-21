package br.com.fiap.gfood.api.core.parsers;

public interface ToEntityParser<D, E>
{
	E toEntity(D domain);
}
