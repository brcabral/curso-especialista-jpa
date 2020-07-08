package com.algaworks.ecommerce.detalhesimportantes;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Produto;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;

public class ConversorTest extends EntityManagerTest {
    @Test
    public void converter() {
        Produto produto = new Produto();
        produto.setDataCriacao(LocalDateTime.now());
        produto.setNome("Carregador de notebook Dell");
        produto.setAtivo(Boolean.TRUE);

        entityManager.getTransaction().begin();
        entityManager.persist(produto);
        entityManager.getTransaction().commit();
        entityManager.clear();

        Produto produtoInserido = entityManager.find(Produto.class, produto.getId());
        Assert.assertTrue(produtoInserido.getAtivo());
    }
}
