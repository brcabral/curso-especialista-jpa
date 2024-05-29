package com.algaworks.ecommerce.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("cartao")
public class PagamentoCartao extends Pagamento {
    @Column(name = "numero_cartao")
    private String numeroCartao;
}
