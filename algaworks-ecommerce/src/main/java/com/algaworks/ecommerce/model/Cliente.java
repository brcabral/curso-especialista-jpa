package com.algaworks.ecommerce.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "cliente")
public class Cliente {
    @Id
    @EqualsAndHashCode.Include
    private Integer id;

    private String nome;

    private SexoCliente sexo;
}
