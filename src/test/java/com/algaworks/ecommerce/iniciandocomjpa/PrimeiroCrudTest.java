package com.algaworks.ecommerce.iniciandocomjpa;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Cliente;
import org.junit.Assert;
import org.junit.Test;

public class PrimeiroCrudTest extends EntityManagerTest {
    @Test
    public void inserirCliente() {
        Cliente cliente = new Cliente();

        // cliente.setId(3);
        cliente.setNome("Leonardo Pereira");

        entityManager.getTransaction().begin();
        entityManager.persist(cliente);
        entityManager.getTransaction().commit();

        entityManager.clear();

        Cliente clienteInserido = entityManager.find(Cliente.class, cliente.getId());
        Assert.assertNotNull(clienteInserido);
        Assert.assertEquals("Leonardo Pereira", clienteInserido.getNome());
    }

    @Test
    public void pesquisarCliente() {
        Cliente cliente = entityManager.find(Cliente.class, 1);

        Assert.assertNotNull(cliente);
        Assert.assertEquals("Fernando Medeiros", cliente.getNome());
    }

    @Test
    public void atualizarCliente() {
        Cliente cliente = entityManager.find(Cliente.class, 2);
        cliente.setNome("Saulo Fernandes");

        entityManager.getTransaction().begin();
        entityManager.merge(cliente);
        entityManager.getTransaction().commit();

        entityManager.clear();

        Cliente clienteAtualizado = entityManager.find(Cliente.class, cliente.getId());
        Assert.assertNotNull(clienteAtualizado);
        Assert.assertEquals("Saulo Fernandes", clienteAtualizado.getNome());
    }

    @Test
    public void removerRegistro() {
        Cliente cliente = entityManager.find(Cliente.class, 2);

        entityManager.getTransaction().begin();
        entityManager.remove(cliente);
        entityManager.getTransaction().commit();

        Cliente clienteRemovido = entityManager.find(Cliente.class, cliente.getId());
        Assert.assertNull(clienteRemovido);
    }
}
