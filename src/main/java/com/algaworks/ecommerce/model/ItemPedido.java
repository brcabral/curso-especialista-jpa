package com.algaworks.ecommerce.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemPedido {
    @Id
    @EqualsAndHashCode.Include
    private Integer id;

    private Integer pedidoId;

    private Integer produtoId;

    private BigDecimal precoProduto;

    private Integer quantidade;
}
