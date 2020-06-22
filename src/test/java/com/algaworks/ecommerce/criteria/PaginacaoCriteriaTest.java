package com.algaworks.ecommerce.criteria;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Categoria;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class PaginacaoCriteriaTest extends EntityManagerTest {
    @Test
    public void paginarResultados() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Categoria> criteriaQuery = criteriaBuilder.createQuery(Categoria.class);
        Root<Categoria> root = criteriaQuery.from(Categoria.class);

        criteriaQuery.select(root);

        TypedQuery<Categoria> typedQuery = entityManager.createQuery(criteriaQuery);

        // FIRST_RESULT = MAX_RESULT * (pagina - 1)
        typedQuery.setFirstResult(0);
        typedQuery.setMaxResults(2);

        List<Categoria> lista = typedQuery.getResultList();
        Assert.assertFalse(lista.isEmpty());

        lista.forEach(c -> System.out.println(c.getId() + ", " + c.getNome()));
    }
}
