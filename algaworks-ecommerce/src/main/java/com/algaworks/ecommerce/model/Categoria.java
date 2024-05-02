package com.algaworks.ecommerce.model;

import jakarta.persistence.Column;
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
@Table(name = "categoria")
public class Categoria {
    @Id
    @EqualsAndHashCode.Include
    private Integer id;

    private String nome;

    @Column(name = "categoria_pai_id")
    private Integer categoriaPaiId;
}
