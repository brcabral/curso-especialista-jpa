package com.algaworks.ecommerce.iniciandocomjpa;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Produto;
import org.junit.jupiter.api.Test;

public class OperacoesComTransacaoTest extends EntityManagerTest {
    @Test
    public void abrirEFecharATransacao() {
        Produto produto = new Produto();  //  Somente para os métodos abaixo não ficarem dando erro

        entityManager.getTransaction().begin();

        entityManager.persist(produto);
        entityManager.merge(produto);
        entityManager.remove(produto);

        entityManager.getTransaction().rollback();
    }
}
