package com.algaworks.ecommerce.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Estoque {
    @Id
    @EqualsAndHashCode.Include
    private Integer id;

    private Integer produtoId;

    private Integer quantidade;
}
