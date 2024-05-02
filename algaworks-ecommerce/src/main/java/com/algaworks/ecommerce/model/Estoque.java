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
@Table(name = "estoque")
public class Estoque {
    @Id
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(name = "produto_id")
    private Integer produtoId;

    private Integer quantidade;
}
