package com.algaworks.ecommerce.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("boleto")
public class PagamentoBoleto extends Pagamento {
    @Column(name = "codigo_barras", length = 100, nullable = false)
    private String codigoBarras;
}
