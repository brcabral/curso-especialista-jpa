package com.algaworks.ecommerce.criteria;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.NotaFiscal;
import com.algaworks.ecommerce.model.Pedido;
import jakarta.persistence.Parameter;
import jakarta.persistence.TemporalType;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PassandoParametrosCriteriaTest extends EntityManagerTest {
    @Test
    public void passarParametro() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);

        criteriaQuery.select(root);
        // ParameterExpression<Integer> parameterExpressionId = criteriaBuilder.parameter(Integer.class);
        ParameterExpression<Integer> parameterExpressionId = criteriaBuilder.parameter(Integer.class, "id");
        criteriaQuery.where(criteriaBuilder.equal(root.get("id"), parameterExpressionId));

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);
        // typedQuery.setParameter(parameterExpressionId, 1);
        typedQuery.setParameter("id", 1);

        Pedido pedido = typedQuery.getSingleResult();
        Assertions.assertNotNull(pedido);
    }

    @Test
    public void passarParametroDate() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<NotaFiscal> criteriaQuery = criteriaBuilder.createQuery(NotaFiscal.class);
        Root<NotaFiscal> root = criteriaQuery.from(NotaFiscal.class);

        criteriaQuery.select(root);
        ParameterExpression<Date> parameterExpressionDate = criteriaBuilder.parameter(Date.class, "dataInicial");
        criteriaQuery.where(criteriaBuilder.greaterThan(root.get("dataEmissao"), parameterExpressionDate));

        TypedQuery<NotaFiscal> typedQuery = entityManager.createQuery(criteriaQuery);

        Calendar dataInicial = Calendar.getInstance();
        dataInicial.add(Calendar.DATE, -30);
        typedQuery.setParameter("dataInicial", dataInicial.getTime(), TemporalType.TIMESTAMP);

        List<NotaFiscal> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());
    }
}
