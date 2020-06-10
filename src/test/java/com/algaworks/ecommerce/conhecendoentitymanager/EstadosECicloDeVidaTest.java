package com.algaworks.ecommerce.conhecendoentitymanager;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Categoria;
import org.junit.Test;

public class EstadosECicloDeVidaTest extends EntityManagerTest {
    @Test
    public void analisarEstados() {
        Categoria categoria = new Categoria();
        categoria.setNome("Eletrônicos");

        Categoria categoriaGerenciadaMerge = entityManager.merge(categoria);
        Categoria categoriaGerenciada = entityManager.find(Categoria.class, 1);

        entityManager.remove(categoriaGerenciada);
        entityManager.persist(categoriaGerenciada);
        entityManager.detach(categoriaGerenciada);
    }
}
