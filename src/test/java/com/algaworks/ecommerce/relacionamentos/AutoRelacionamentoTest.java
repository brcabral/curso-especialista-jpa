package com.algaworks.ecommerce.relacionamentos;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Categoria;
import org.junit.Assert;
import org.junit.Test;

public class AutoRelacionamentoTest extends EntityManagerTest {
    @Test
    public void verificarListaPedidos() {
        Categoria categoriaPai = new Categoria();
        categoriaPai.setNome("Eletrônicos");

        Categoria categoria = new Categoria();
        categoria.setNome("Celulares");
        categoria.setCategoriaPai(categoriaPai);

        entityManager.getTransaction().begin();
        entityManager.persist(categoriaPai);
        entityManager.persist(categoria);
        entityManager.getTransaction().commit();

        entityManager.clear();

        Categoria categoriaLista = entityManager.find(Categoria.class, categoria.getId());
        Assert.assertNotNull(categoriaLista.getCategoriaPai());

        Categoria categoriaPaiLista = entityManager.find(Categoria.class, categoriaPai.getId());
        Assert.assertFalse(categoriaPaiLista.getCategorias().isEmpty());
    }
}
