package com.algaworks.ecommerce.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@DiscriminatorValue("boleto")
public class PagamentoBoleto extends Pagamento {
    @NotBlank
    @Column(name = "codigo_barras", length = 100)
    private String codigoBarras;

    @NotBlank
    @FutureOrPresent
    @Column(name = "data_vencimento")
    private LocalDate dataVencimento;
}
