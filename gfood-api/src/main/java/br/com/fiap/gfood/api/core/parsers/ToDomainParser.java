package br.com.fiap.gfood.api.core.parsers;

public interface ToDomainParser<D,E>
{
	D toDomain(E entity);
}
