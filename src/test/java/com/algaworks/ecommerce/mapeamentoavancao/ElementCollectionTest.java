package com.algaworks.ecommerce.mapeamentoavancao;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Atributo;
import com.algaworks.ecommerce.model.Cliente;
import com.algaworks.ecommerce.model.Produto;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

public class ElementCollectionTest extends EntityManagerTest {
    @Test
    public void aplicarTags() {
        entityManager.getTransaction().begin();

        Produto produto = entityManager.find(Produto.class, 1);
        produto.setTags(Arrays.asList("ebooks", "livro-digital"));

        entityManager.getTransaction().commit();
        entityManager.clear();

        Produto produtoAlterado = entityManager.find(Produto.class, produto.getId());
        Assert.assertFalse(produtoAlterado.getTags().isEmpty());
    }

    @Test
    public void aplicarAtributos() {
        entityManager.getTransaction().begin();

        Produto produto = entityManager.find(Produto.class, 1);
        produto.setAtributos(Arrays.asList(new Atributo("tela", "320x600"),
                new Atributo("cor", "preta")));
        entityManager.getTransaction().commit();
        entityManager.clear();

        Produto produtoAlterado = entityManager.find(Produto.class, produto.getId());
        Assert.assertFalse(produtoAlterado.getAtributos().isEmpty());
    }

    @Test
    public void aplicarContato() {
        entityManager.getTransaction().begin();

        Cliente cliente = entityManager.find(Cliente.class, 1);
        cliente.setContatos(Collections.singletonMap("email", "fernando@email.com"));

        entityManager.getTransaction().commit();
        entityManager.clear();

        Cliente clienteAlterado = entityManager.find(Cliente.class, cliente.getId());
        Assert.assertEquals("fernando@email.com", clienteAlterado.getContatos().get("email"));
    }
}
