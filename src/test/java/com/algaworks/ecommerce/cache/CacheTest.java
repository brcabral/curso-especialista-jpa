package com.algaworks.ecommerce.cache;

import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.Produto;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

public class CacheTest {
    protected static EntityManagerFactory entityManagerFactory;

    @BeforeClass
    public static void setUpBeforeClass() {
        entityManagerFactory = Persistence.createEntityManagerFactory("Ecommerce-PU");
    }

    @AfterClass
    public static void tearDownAfterClass() {
        entityManagerFactory.close();
    }

    private static void esperar(int segundos) {
        try {
            Thread.sleep(segundos * 1000);
        } catch (InterruptedException e) {
            //
        }
    }

    private static void log(Object obj) {
        System.out.println("[LOG " + System.currentTimeMillis() + "] " + obj);
    }

    @Test
    public void buscarDoCache() {
        EntityManager entityManager1 = entityManagerFactory.createEntityManager();
        EntityManager entityManager2 = entityManagerFactory.createEntityManager();

        System.out.println("Buscando a partir da instância 1");
        entityManager1.find(Pedido.class, 2);

        System.out.println("Buscando a partir da instância 2");
        entityManager2.find(Pedido.class, 2);

        entityManager1.close();
        entityManager2.close();
    }

    @Test
    public void adicionarPedidosNoCache() {
        EntityManager entityManager1 = entityManagerFactory.createEntityManager();
        EntityManager entityManager2 = entityManagerFactory.createEntityManager();
        EntityManager entityManager3 = entityManagerFactory.createEntityManager();

        System.out.println("Buscando a partir da instância 1");
        entityManager1
                .createQuery("select p from Pedido p", Pedido.class)
                .getResultList();

        // O JPA não faz cache de select
        // Sempre que for executado um select ele irá no BD buscar os dados
        System.out.println("Buscando a partir da instância 2");
        entityManager2
                .createQuery("select p from Pedido p", Pedido.class)
                .getResultList();

        // O JPA só buscar no cache com o uso do comando find
        System.out.println("Buscando a partir da instância 3");
        entityManager3.find(Pedido.class, 2);

        entityManager1.close();
        entityManager2.close();
        entityManager3.close();

    }

    @Test
    public void removerEntidadeDoCache() {
        EntityManager entityManager1 = entityManagerFactory.createEntityManager();
        EntityManager entityManager2 = entityManagerFactory.createEntityManager();

        System.out.println("Buscando a partir da instância 1");
        entityManager1
                .createQuery("select p from Pedido p", Pedido.class)
                .getResultList();

        System.out.println("Removendo do cache");
        Cache cache = entityManagerFactory.getCache();
        cache.evictAll();
        // cache.evict(Pedido.class);
        // cache.evict(Pedido.class, 1);

        System.out.println("Buscando o pedido 1 a partir da instância 2");
        entityManager2.find(Pedido.class, 1);

        System.out.println("Buscando o pedido 2 a partir da instância 2");
        entityManager2.find(Pedido.class, 2);

        entityManager1.close();
        entityManager2.close();
    }

    @Test
    public void verificarSeEstaNoCache() {
        EntityManager entityManager1 = entityManagerFactory.createEntityManager();
        Cache cache = entityManagerFactory.getCache();

        System.out.println("Buscando a partir da instância 1");
        entityManager1
                .createQuery("select p from Pedido p", Pedido.class)
                .getResultList();

        Assert.assertTrue(cache.contains(Pedido.class, 1));
        Assert.assertTrue(cache.contains(Pedido.class, 2));

        entityManager1.close();
    }

    @Test
    public void analisarOpcoesCache() {
        EntityManager entityManager1 = entityManagerFactory.createEntityManager();
        Cache cache = entityManagerFactory.getCache();

        System.out.println("Buscando a partir da instância 1");
        entityManager1
                .createQuery("select p from Pedido p", Pedido.class)
                .getResultList();

        Assert.assertTrue(cache.contains(Pedido.class, 1));

        entityManager1.close();
    }

    @Test
    public void controlarCacheDinamicamente() {
        // Configura a forma como o cache de segundo nível irá funcionar
        // javax.persistence.cache.storeMode CacheStoreMode

        // Configura se o cache de segundo nível será ou não usado.
        // javax.persistence.cache.retrieveMode CacheRetrieveMode
        Cache cache = entityManagerFactory.getCache();

        System.out.println("Buscando todos os pedidos.................");
        EntityManager entityManager1 = entityManagerFactory.createEntityManager();
        entityManager1.setProperty("javax.persistence.cache.storeMode", CacheStoreMode.BYPASS);
        entityManager1
                .createQuery("select p from Pedido p", Pedido.class)
                .setHint("javax.persistence.cache.storeMode", CacheStoreMode.USE)
                .getResultList();

        System.out.println("Buscdando o pedido de ID igual a 2.................");
        EntityManager entityManager2 = entityManagerFactory.createEntityManager();
        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.cache.storeMode", CacheStoreMode.BYPASS);
        // properties.put("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        entityManager2.find(Pedido.class, 2, properties);

        System.out.println("Buscando a partir da instância 1");
        EntityManager entityManager3 = entityManagerFactory.createEntityManager();
        entityManager3
                .createQuery("select p from Pedido p", Pedido.class)
                // .setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS)
                .getResultList();

        entityManager1.close();
        entityManager2.close();
        entityManager3.close();
    }

    @Test
    public void ehcache() {
        Cache cache = entityManagerFactory.getCache();

        EntityManager entityManager1 = entityManagerFactory.createEntityManager();
        EntityManager entityManager2 = entityManagerFactory.createEntityManager();

        log("Buscando e incluindo no cache...");
        entityManager1
                .createQuery("select p from Produto p", Produto.class)
                .getResultList();
        log("---");

        esperar(2);
        Assert.assertTrue(cache.contains(Produto.class, 1));
        entityManager2.find(Produto.class, 1);

        esperar(2);
        Assert.assertFalse(cache.contains(Produto.class, 1));

        entityManager1.close();
        entityManager2.close();
    }

    @Test
    public void ehcachePorEntidade() {
        Cache cache = entityManagerFactory.getCache();

        EntityManager entityManager1 = entityManagerFactory.createEntityManager();
        EntityManager entityManager2 = entityManagerFactory.createEntityManager();

        log("Buscando e incluindo no cache...");
        entityManager1
                .createQuery("select p from Pedido p", Pedido.class)
                .getResultList();
        log("---");

        esperar(2);
        Assert.assertTrue(cache.contains(Pedido.class, 2));
        entityManager2.find(Pedido.class, 2);

        esperar(2);
        Assert.assertTrue(cache.contains(Pedido.class, 2));

        esperar(4);
        Assert.assertFalse(cache.contains(Pedido.class, 2));

        entityManager1.close();
        entityManager2.close();
    }
}
