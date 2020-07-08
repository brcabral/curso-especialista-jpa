package com.algaworks.ecommerce.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "item_pedido")
@SqlResultSetMappings({
        @SqlResultSetMapping(name = "item_pedido-produto.ItemPedido-Produto",
                entities = {@EntityResult(entityClass = ItemPedido.class),
                        @EntityResult(entityClass = Produto.class)})
})
public class ItemPedido {
    @EmbeddedId
    private ItemPedidoId id;

    @NotNull
    @MapsId("pedidoId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "pedido_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_item_pedido_pedido"))
    private Pedido pedido;

    @NotNull
    @MapsId("produtoId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_item_pedido_produto"))
    private Produto produto;

    @NotNull
    @Positive
    @Column(name = "preco_produto", nullable = false)
    private BigDecimal precoProduto;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Integer quantidade;
}
