package com.algaworks.ecommerce.mapeamentobasico;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.EnderecoEntregaPedido;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.StatusPedido;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MapeamentoObjetoEmbutidoTest extends EntityManagerTest {
    @Test
    public void analisarMapeamentoObjetoEmbutido() {
        EnderecoEntregaPedido endereco = new EnderecoEntregaPedido();
        endereco.setCep("14800-000");
        endereco.setLogradouro("Rua São Bento");
        endereco.setNumero("1234");
        endereco.setBairro("Centro");
        endereco.setCidade("Araraquara");
        endereco.setEstado("MG");

        Pedido pedido = new Pedido();
        // Retirado pois foi configurado o auto incremento na entidade (IDENTITY)
        // pedido.setId(1);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus(StatusPedido.AGUARDANDO);
        pedido.setTotal(new BigDecimal(1000));
        pedido.setEnderecoEntrega(endereco);

        entityManager.getTransaction().begin();
        entityManager.persist(pedido);
        entityManager.getTransaction().commit();

        entityManager.clear();

        Pedido pedidoInserido = entityManager.find(Pedido.class, pedido.getId());
        Assert.assertNotNull(pedidoInserido);
        Assert.assertNotNull(pedidoInserido.getEnderecoEntrega());
        Assert.assertNotNull(pedidoInserido.getEnderecoEntrega().getCep());
    }
}
