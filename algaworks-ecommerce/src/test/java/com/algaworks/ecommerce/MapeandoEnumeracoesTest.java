package com.algaworks.ecommerce;

import com.algaworks.ecommerce.model.Cliente;
import com.algaworks.ecommerce.model.SexoCliente;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MapeandoEnumeracoesTest extends EntityManagerTest {
    @Test
    public void testarEnum() {
        Cliente cliente = new Cliente();
        // cliente.setId(4);  -> Comentado porque estamos utilizando IDENTITY
        cliente.setNome("José Mineiro");
        cliente.setSexo(SexoCliente.MASCULINO);
        cliente.setCpf("777");

        entityManager.getTransaction().begin();
        entityManager.persist(cliente);
        entityManager.getTransaction().commit();

        entityManager.clear();

        Cliente clienteVerificacao = entityManager.find(Cliente.class, cliente.getId());
        Assertions.assertNotNull(clienteVerificacao);
    }
}
