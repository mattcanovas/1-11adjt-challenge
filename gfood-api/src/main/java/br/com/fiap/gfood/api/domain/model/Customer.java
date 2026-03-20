package br.com.fiap.gfood.api.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.fiap.gfood.api.domain.enums.TypeCustomer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer
{
	private UUID id;
	private String fullName;
	private String login;
	private String password;
	private String email;
	private TypeCustomer type;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String address;
}
