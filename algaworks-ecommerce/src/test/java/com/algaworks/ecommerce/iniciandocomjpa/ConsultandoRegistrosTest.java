package com.algaworks.ecommerce.iniciandocomjpa;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Produto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConsultandoRegistrosTest extends EntityManagerTest {
    @Test
    public void buscarPorIndentificador() {
        /** find -> Os dados são retornado assim que é feito a consulta. */
        Produto produto = entityManager.find(Produto.class, 1);
        /** reference -> Os dados só são buscados no banco de dados quando uma propriedade do produto (obj) for usada. */
        // Produto produto = entityManager.getReference(Produto.class, 1);

        Assertions.assertNotNull(produto);
        Assertions.assertEquals("Kindle", produto.getNome());
    }

    @Test
    public void atualizarReferencia() {
        Produto produto = entityManager.find(Produto.class, 1);
        produto.setNome("Microfone Samson");

        /** Faz uma nova busca no banco de dados para atualizar os dados do produto (obj) */
        entityManager.refresh(produto);
        Assertions.assertEquals("Kindle", produto.getNome());
    }
}
