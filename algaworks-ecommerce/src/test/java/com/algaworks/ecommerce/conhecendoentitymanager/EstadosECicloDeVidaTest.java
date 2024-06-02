package com.algaworks.ecommerce.conhecendoentitymanager;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Categoria;
import org.junit.jupiter.api.Test;

public class EstadosECicloDeVidaTest extends EntityManagerTest {
    @Test
    public void analisarEstados() {
        // Estado novo (transient)
        Categoria categoriaNovo = new Categoria();
        categoriaNovo.setNome("Eletrônicos");

        // O retorno passa para o Estado gerenciado (managed)
        Categoria categoriaGerenciadaMerge = entityManager.merge(categoriaNovo);

        Categoria categoriaGerenciada = entityManager.find(Categoria.class, 1);

        // Estado de remoção (removed)
        entityManager.remove(categoriaGerenciada);

        // Ao chamar o persist, o objeto vai para o estado gerenciado
        entityManager.persist(categoriaGerenciada);

        // Estado desanexar (detached)
        entityManager.detach(categoriaGerenciada);
    }
}
