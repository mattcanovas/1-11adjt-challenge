package br.com.fiap.gfood.api.data.entities;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import br.com.fiap.gfood.api.core.commons.enums.TypeCustomer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "customer")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerData
{
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false)
	private String fullName;

	@Column(nullable = false)
	private String login;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@Column(nullable = false)
	private LocalDateTime updatedAt;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private TypeCustomer type;
	
	@Column(nullable = false)
	private String address;

	@PrePersist
	public void prePersist()
	{
		this.setCreatedAt(LocalDateTime.now());
		this.setUpdatedAt(LocalDateTime.now());
	}

	@PreUpdate
	public void preUpdate()
	{
		this.setUpdatedAt(LocalDateTime.now());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(email, id, login);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomerData other = (CustomerData) obj;
		return Objects.equals(email, other.email) && Objects.equals(id, other.id)
				&& Objects.equals(login, other.login);
	}

	@Override
	public String toString()
	{
		return reflectionToString(this);
	}
}
