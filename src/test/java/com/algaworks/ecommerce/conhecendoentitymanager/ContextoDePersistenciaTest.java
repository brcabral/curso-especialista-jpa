package com.algaworks.ecommerce.conhecendoentitymanager;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Produto;
import org.junit.Test;

import java.math.BigDecimal;

public class ContextoDePersistenciaTest extends EntityManagerTest {
    @Test
    public void usarContextoPersistencia() {
        // Contexto de persistência inicia no begin e finaliza no commit
        entityManager.getTransaction().begin();

        // Conceito dirty checking
        // O JPA verifica se houve uma alteração em um objeto gerenciável dentro do contexto de persistência
        // Se sim, ele realiza o update (não há comando merge explicito)
        Produto produto = entityManager.find(Produto.class, 1);
        System.out.println("Preço: " + produto.getPreco());
        produto.setPreco(new BigDecimal(100.0));

        Produto produto2 = new Produto();
        produto2.setNome("Caneca para café");
        produto2.setPreco(new BigDecimal(10.0));
        produto2.setDescricao("Boa caneca para café");
        entityManager.persist(produto2);

        Produto produto3 = new Produto();
        produto3.setNome("Caneca para chá");
        produto3.setPreco(new BigDecimal(10.0));
        produto3.setDescricao("Boa caneca para chá");
        produto3 = entityManager.merge(produto3);

        entityManager.flush();

        // Conceito dirty checking
        produto3.setDescricao("Alterar descrição");

        entityManager.getTransaction().commit();
    }
}
