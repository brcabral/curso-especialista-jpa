package com.algaworks.ecommerce.mapeamentoavancao;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.NotaFiscal;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.Produto;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;

public class SalvandoArquivosTest extends EntityManagerTest {
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

        NotaFiscal notaFiscalInserida = entityManager.find(NotaFiscal.class, notaFiscal.getId());
        Assert.assertNotNull(notaFiscalInserida.getXml());
        Assert.assertTrue(notaFiscalInserida.getXml().length > 0);

        /*
        try {
            OutputStream out = new FileOutputStream(
                    Files.createFile(Paths.get(
                            System.getProperty("user.home") + "/nfe.xml")).toFile());
            out.write(notaFiscalInserida.getXml());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        */
    }

    @Test
    public void salvarFoto() {
        entityManager.getTransaction().begin();

        Produto produto = entityManager.find(Produto.class, 1);
        produto.setFoto(carregarFoto());

        entityManager.getTransaction().commit();
        entityManager.clear();

        Produto produtoInserido = entityManager.find(Produto.class, produto.getId());
        Assert.assertNotNull(produtoInserido.getFoto());
        Assert.assertTrue(produtoInserido.getFoto().length > 0);
    }
}
