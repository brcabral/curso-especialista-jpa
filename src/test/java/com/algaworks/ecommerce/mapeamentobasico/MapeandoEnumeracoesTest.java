package com.algaworks.ecommerce.mapeamentobasico;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Cliente;
import com.algaworks.ecommerce.model.SexoCliente;
import org.junit.Assert;
import org.junit.Test;

public class MapeandoEnumeracoesTest extends EntityManagerTest {
    @Test
    public void testarEnum() {
        Cliente cliente = new Cliente();
        // Retirado pois foi configurado o auto incremento na entidade (IDENTITY)
        // cliente.setId(4);
        cliente.setNome("José Mineiro");
        cliente.setSexo(SexoCliente.MASCULINO);

        entityManager.getTransaction().begin();
        entityManager.persist(cliente);
        entityManager.getTransaction().commit();

        entityManager.clear();

        Cliente clienteInserido = entityManager.find(Cliente.class, cliente.getId());
        Assert.assertNotNull(clienteInserido);
    }
}
