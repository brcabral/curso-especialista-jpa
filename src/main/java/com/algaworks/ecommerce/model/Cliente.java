package com.algaworks.ecommerce.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "cliente",
        uniqueConstraints = {@UniqueConstraint(name = "unq_cpf", columnNames = {"cpf"})},
        indexes = {@Index(name = "idx_nome", columnList = "nome")})
@SecondaryTable(name = "cliente_detalhe",
        pkJoinColumns = @PrimaryKeyJoinColumn(name = "cliente_id"),
        foreignKey = @ForeignKey(name = "fk_cliente_detalhe_cliente"))
@NamedStoredProcedureQuery(name = "sp_compraram_acima_media", procedureName = "compraram_acima_media",
        resultClasses = Cliente.class,
        parameters = {
                @StoredProcedureParameter(name = "ano", type = Integer.class, mode = ParameterMode.IN)
        }
)
public class Cliente extends EntidadeBaseInteger {
    @NotBlank
    @Column(length = 100, nullable = false)
    private String nome;

    @NotNull
    @Column(length = 100, nullable = false)
    @Pattern(regexp = "(^\\\\d{3}\\\\x2E\\\\d{3}\\\\x2E\\\\d{3}\\\\x2D\\\\d{2}$)")
    private String cpf;

    @Transient
    private String primeiroNome;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(table = "cliente_detalhe", length = 30, nullable = false)
    private SexoCliente sexo;

    @Past
    @Column(name = "data_nascimento", table = "cliente_detalhe")
    private LocalDate dataNascimento;

    @OneToMany(mappedBy = "cliente")
    private List<Pedido> pedidos;

    @ElementCollection
    @CollectionTable(name = "cliente_contato", joinColumns = @JoinColumn(name = "cliente_id"),
            foreignKey = @ForeignKey(name = "fk_cliente_contato"))
    @MapKeyColumn(name = "tipo", length = 20)
    @Column(name = "descricao", length = 100)
    private Map<String, String> contatos;

    @PostLoad
    public void configurarPrimeiroNome() {
        if (nome != null && !nome.isEmpty()) {
            int index = nome.indexOf(" ");
            if (index > -1) {
                primeiroNome = nome.substring(0, index);
            }
        }
    }
}
