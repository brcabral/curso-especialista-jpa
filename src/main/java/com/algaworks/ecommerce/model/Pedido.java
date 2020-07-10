package com.algaworks.ecommerce.model;

import com.algaworks.ecommerce.listener.GenericoListener;
import com.algaworks.ecommerce.listener.GerarNotaFiscalListener;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.engine.spi.PersistentAttributeInterceptable;
import org.hibernate.engine.spi.PersistentAttributeInterceptor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "pedido")
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "Pedido.dadosEssenciais",
                attributeNodes = {
                        @NamedAttributeNode("dataCriacao"),
                        @NamedAttributeNode("status"),
                        @NamedAttributeNode("total"),
                        @NamedAttributeNode(
                                value = "cliente",
                                subgraph = "cli"
                        )
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "cli",
                                attributeNodes = {
                                        @NamedAttributeNode("nome"),
                                        @NamedAttributeNode("cpf")
                                }
                        )
                }
        )
})
@EntityListeners({GerarNotaFiscalListener.class, GenericoListener.class})
public class Pedido extends EntidadeBaseInteger implements PersistentAttributeInterceptable {
    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_pedido_cliente"))
    private Cliente cliente;

    @NotNull
    @PastOrPresent
    @Column(name = "data_criacao", updatable = false, nullable = false)
    private LocalDateTime dataCriacao;

    @PastOrPresent
    @Column(name = "data_ultima_atualizacao", insertable = false)
    private LocalDateTime dataUltimaAtualizacao;

    @PastOrPresent
    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao;

    @OneToOne(mappedBy = "pedido", fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private NotaFiscal notaFiscal;

    @NotNull
    @Positive
    @Column(nullable = false)
    private BigDecimal total;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private StatusPedido status;

    @Embedded
    private EnderecoEntregaPedido enderecoEntrega;

    @OneToOne(mappedBy = "pedido", fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private Pagamento pagamento;

    @NotEmpty
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<ItemPedido> itensPedido;

    @Transient
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private PersistentAttributeInterceptor persistentAttributeInterceptor;

    @Override
    public PersistentAttributeInterceptor $$_hibernate_getInterceptor() {
        return this.persistentAttributeInterceptor;
    }

    @Override
    public void $$_hibernate_setInterceptor(PersistentAttributeInterceptor persistentAttributeInterceptor) {
        this.persistentAttributeInterceptor = persistentAttributeInterceptor;
    }

    public NotaFiscal getNotaFiscal() {
        if (this.persistentAttributeInterceptor != null) {
            return (NotaFiscal) persistentAttributeInterceptor
                    .readObject(this, "notaFiscal", this.notaFiscal);
        }

        return this.notaFiscal;
    }

    public void setNotaFiscal(NotaFiscal notaFiscal) {
        if (this.persistentAttributeInterceptor != null) {
            this.notaFiscal = (NotaFiscal) persistentAttributeInterceptor
                    .writeObject(this, "notaFiscal", this.notaFiscal, notaFiscal);
        } else {
            this.notaFiscal = notaFiscal;
        }
    }

    public Pagamento getPagamento() {
        if (this.persistentAttributeInterceptor != null) {
            return (Pagamento) persistentAttributeInterceptor
                    .readObject(this, "pagamento", this.pagamento);
        }

        return this.pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        if (this.persistentAttributeInterceptor != null) {
            this.pagamento = (Pagamento) persistentAttributeInterceptor
                    .writeObject(this, "pagamento", this.pagamento, pagamento);
        } else {
            this.pagamento = pagamento;
        }
    }

    // @PrePersist
    // @PreUpdate
    // É possível ter PrePersist e o PreUpdate no mesmo método.
    // Porém é permitido apenas um PrePersist e o PreUpdate por classe.
    public void calcularTotal() {
        if (itensPedido != null) {
            total = itensPedido.stream().map(
                    i -> new BigDecimal(i.getQuantidade()).multiply(i.getPrecoProduto()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } else {
            total = BigDecimal.ZERO;
        }
    }

    public boolean isPago() {
        return StatusPedido.PAGO.equals(status);
    }

    @PrePersist
    public void aoPersistir() {
        dataCriacao = LocalDateTime.now();
        calcularTotal();
    }

    @PreUpdate
    public void aoAtualizar() {
        dataUltimaAtualizacao = LocalDateTime.now();
        calcularTotal();
    }

    @PostPersist
    public void aposPersistir() {
        System.out.println("Após persistir Pedido.");
    }

    @PostUpdate
    public void aposAtualizar() {
        System.out.println("Após atualizar Pedido.");
    }

    @PreRemove
    public void aoRemover() {
        System.out.println("Antes de remover Pedido.");
    }

    @PostRemove
    public void aposRemover() {
        System.out.println("Após remover Pedido.");
    }

    @PostLoad
    public void aoCarregar() {
        System.out.println("Após carregar o Pedido.");
    }
}