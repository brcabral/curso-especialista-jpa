package com.algaworks.ecommerce.jpql;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Categoria;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class PaginacaoJPQLTest extends EntityManagerTest {
    @Test
    public void paginarResultados() {
        String jpql = "select c from Categoria c order by c.nome";
        TypedQuery<Categoria> typedQuery = entityManager.createQuery(jpql, Categoria.class);

        // FIRST_RESULT * (pagina - 1)
        typedQuery.setFirstResult(6);
        typedQuery.setMaxResults(2);

        List<Categoria> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(c -> System.out.println(c.getId() + ", " + c.getNome()));
    }
}
