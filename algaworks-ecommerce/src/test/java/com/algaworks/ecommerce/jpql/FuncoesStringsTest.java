package com.algaworks.ecommerce.jpql;

import com.algaworks.ecommerce.EntityManagerTest;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class FuncoesStringsTest extends EntityManagerTest {
    @Test
    public void aplicarFuncao() {
        // concat, length, locate, substring, lower, upper, trim

        // concat
        // String jpql = "select c.nome, concat('Categoria: ', c.nome) from Categoria c ";

        // length
        // String jpql = "select c.nome, length(c.nome) from Categoria c ";

        // locate - é similar ao indexOf do Java
        // String jpql = "select c.nome, locate('a', c.nome) from Categoria c ";

        // substring
        // String jpql = "select c.nome, substring(c.nome, 3, 2) from Categoria c ";

        // lower
        // String jpql = "select c.nome, lower(c.nome) from Categoria c ";

        // upper
        // String jpql = "select c.nome, upper(c.nome) from Categoria c ";

        // trim
        // String jpql = "select c.nome, trim(c.nome) from Categoria c ";

        // String jpql = "select c.nome, length(c.nome) from Categoria c where length(c.nome) > 10";

        String jpql = "select c.nome, length(c.nome) from Categoria c where substring(c.nome, 1, 1) = 'N'";

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);
        List<Object[]> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println(arr[0] + " - " + arr[1]));
    }
}
