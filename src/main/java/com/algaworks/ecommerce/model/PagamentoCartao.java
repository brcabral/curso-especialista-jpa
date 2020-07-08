package com.algaworks.ecommerce.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Entity
@DiscriminatorValue("cartao")
public class PagamentoCartao extends Pagamento {
    @NotEmpty
    @Column(name = "numero_cartao", length = 50)
    private String numeroCartao;
}
