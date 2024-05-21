package com.algaworks.ecommerce.mapeamentoavancao;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.NotaFiscal;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.Produto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Date;

public class SalvandoArquivosTest extends EntityManagerTest {
    @Test
    public void salvarXmlNota() {
        Pedido pedido = entityManager.find(Pedido.class, 1);

        NotaFiscal notaFiscal = new NotaFiscal();
        notaFiscal.setPedido(pedido);
        notaFiscal.setDataEmissao(new Date());
        notaFiscal.setXml(carregarNotaFiscal());

        entityManager.getTransaction().begin();
        entityManager.persist(notaFiscal);
        entityManager.getTransaction().commit();

        entityManager.clear();

        NotaFiscal notaFiscalVerificacao = entityManager.find(NotaFiscal.class, 1);
        Assertions.assertNotNull(notaFiscalVerificacao.getXml());
        Assertions.assertTrue(notaFiscalVerificacao.getXml().length > 0);
    }

    @Test
    public void salvarFotoProduto() {
        entityManager.getTransaction().begin();
        Produto produto = entityManager.find(Produto.class, 1);
        produto.setFoto(carregarFoto());
        entityManager.getTransaction().commit();

        entityManager.clear();

        Produto produtoVerificacao = entityManager.find(Produto.class, 1);
        Assertions.assertNotNull(produtoVerificacao.getFoto());
        Assertions.assertTrue(produtoVerificacao.getFoto().length > 0);
    }

    private static byte[] carregarArquivo(String nome) {
        try {
            return SalvandoArquivosTest.class.getResourceAsStream(nome).readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] carregarNotaFiscal() {
        return carregarArquivo("/nota-fiscal.xml");
    }

    private static byte[] carregarFoto() {
        return carregarArquivo("/kindle.jpg");
    }
}
