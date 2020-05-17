package com.algaworks.ecommerce.iniciandocomjpa;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Produto;
import org.junit.Assert;
import org.junit.Test;

public class ConsultandoRegistrosTest extends EntityManagerTest {
    @Test
    public void buscarPorIndentificador() {
        Produto produto = entityManager.find(Produto.class, 1);
        // Produto produto = entityManager.getReference(Produto.class, 1);

        Assert.assertNotNull(produto);
        Assert.assertEquals("Kindle", produto.getNome());
    }

    @Test
    public void atualizarAReferencia() {
        Produto produto = entityManager.find(Produto.class, 1);
        produto.setNome("Microfone Samson");

        // faz uma nova busca para atualizar os dados do objeto
        entityManager.refresh(produto);

        Assert.assertEquals("Kindle", produto.getNome());
    }
}
