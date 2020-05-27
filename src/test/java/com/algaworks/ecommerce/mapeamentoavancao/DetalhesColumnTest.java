package com.algaworks.ecommerce.mapeamentoavancao;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Produto;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DetalhesColumnTest extends EntityManagerTest {
    @Test
    public void impedirInsercaoColunaAtualizacao() {
        Produto produto = new Produto();
        produto.setNome("Teclado para smartphone");
        produto.setDescricao("O mais confortável");
        produto.setPreco(BigDecimal.ONE);
        produto.setDataCriacao(LocalDateTime.now());
        produto.setDataUltimaAtualizacao(LocalDateTime.now());

        entityManager.getTransaction().begin();
        entityManager.persist(produto);
        entityManager.getTransaction().commit();

        entityManager.clear();

        Produto produtoInserido = entityManager.find(Produto.class, produto.getId());
        Assert.assertNotNull(produtoInserido.getDataCriacao());
        Assert.assertNull(produtoInserido.getDataUltimaAtualizacao());
    }

    @Test
    public void impedirAtualizacaoDaColunaCriacao() {
        entityManager.getTransaction().begin();

        Produto produto = entityManager.find(Produto.class, 1);
        produto.setPreco(BigDecimal.TEN);
        produto.setDataCriacao(LocalDateTime.now());
        produto.setDataUltimaAtualizacao(LocalDateTime.now());

        entityManager.getTransaction().commit();

        entityManager.clear();

        Produto produtoAtualizado = entityManager.find(Produto.class, produto.getId());
        Assert.assertNotEquals(produto.getDataCriacao().truncatedTo(ChronoUnit.SECONDS),
                produtoAtualizado.getDataCriacao().truncatedTo(ChronoUnit.SECONDS));
        Assert.assertEquals(produto.getDataUltimaAtualizacao().truncatedTo(ChronoUnit.SECONDS),
                produtoAtualizado.getDataUltimaAtualizacao().truncatedTo(ChronoUnit.SECONDS));
    }
}
