package com.algaworks.ecommerce.model;

import com.algaworks.ecommerce.dto.ProdutoDTO;
import com.algaworks.ecommerce.listener.GenericoListener;
import com.algaworks.ecommerce.model.converter.BooleanToSimNaoConverter;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "produto",
        uniqueConstraints = {@UniqueConstraint(name = "unq_produto_nome", columnNames = {"nome"})},
        indexes = {@Index(name = "idx_produto_nome", columnList = "nome")})
@EntityListeners({GenericoListener.class})
@NamedQueries({
        @NamedQuery(name = "Produto.listar", query = "select p from Produto p"),
        @NamedQuery(name = "Produto.listarPorCategoria",
                query = "select p from Produto p " +
                        "where exists (select 1 from Categoria c2 join c2.produtos p2 " +
                        "              where p2 = p and c2.id = :categoria)")
})
@SqlResultSetMappings({
        @SqlResultSetMapping(name = "produto_loja.Produto",
                entities = {@EntityResult(entityClass = Produto.class)}),
        @SqlResultSetMapping(name = "ecm_produto.Produto",
                entities = {@EntityResult(entityClass = Produto.class,
                        fields = {
                                @FieldResult(name = "id", column = "prd_id"),
                                @FieldResult(name = "nome", column = "prd_nome"),
                                @FieldResult(name = "descricao", column = "prd_descricao"),
                                @FieldResult(name = "preco", column = "prd_preco"),
                                @FieldResult(name = "foto", column = "prd_foto"),
                                @FieldResult(name = "dataCriacao", column = "prd_data_criacao"),
                                @FieldResult(name = "dataUltimaAtualizacao",
                                        column = "prd_data_ultima_atualizacao")
                        })}),
        @SqlResultSetMapping(name = "ecm_produto.ProdutoDTO",
                classes = {
                        @ConstructorResult(targetClass = ProdutoDTO.class,
                                columns = {
                                        @ColumnResult(name = "prd_id", type = Integer.class),
                                        @ColumnResult(name = "prd_nome", type = String.class)
                                })
                })
})
@NamedNativeQueries({
        @NamedNativeQuery(name = "produto_loja.listar",
                query = "select id, nome, descricao, data_criacao, data_ultima_atualizacao, preco, foto " +
                        "from produto_loja", resultClass = Produto.class),
        @NamedNativeQuery(name = "ecm_produto.listar",
                query = "select * from ecm_produto", resultSetMapping = "ecm_produto.Produto")
})
public class Produto extends EntidadeBaseInteger {
    @NotBlank
    @Column(length = 100, nullable = false) // nome varchar(100) not null
    private String nome;

    @NotNull
    @PastOrPresent
    @Column(name = "data_criacao", updatable = false, nullable = false)
    private LocalDateTime dataCriacao;

    @PastOrPresent
    @Column(name = "data_ultima_atualizacao", insertable = false)
    private LocalDateTime dataUltimaAtualizacao;

    // @Lob
    private String descricao;

    @Positive
    private BigDecimal preco;

    @Lob
    @Type(type="org.hibernate.type.BinaryType")
    private byte[] foto;

    @ManyToMany
    @JoinTable(name = "produto_categoria",
            joinColumns = @JoinColumn(name = "produto_id", nullable = false,
                    foreignKey = @ForeignKey(name = "fk_produto_categoria_produto")),
            inverseJoinColumns = @JoinColumn(name = "categoria_id", nullable = false,
                    foreignKey = @ForeignKey(name = "fk_produto_categoria_categoria")))
    private List<Categoria> categorias;

    @OneToOne(mappedBy = "produto")
    private Estoque estoque;

    @ElementCollection
    @CollectionTable(name = "produto_tag",
            joinColumns = @JoinColumn(name = "produto_id", nullable = false),
            foreignKey = @ForeignKey(name = "fk_produto_tag"))
    @Column(name = "tag", length = 50, nullable = false)
    private List<String> tags;

    @ElementCollection
    @CollectionTable(name = "produto_atributo",
            joinColumns = @JoinColumn(name = "produto_id", nullable = false),
            foreignKey = @ForeignKey(name = "fk_produto_atributo"))
    private List<Atributo> atributos;

    @NotNull
    @Convert(converter = BooleanToSimNaoConverter.class)
    @Column(length = 3, nullable = false)
    private Boolean ativo = Boolean.FALSE;
}
