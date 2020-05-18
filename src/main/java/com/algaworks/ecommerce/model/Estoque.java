package com.algaworks.ecommerce.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

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
