package com.algaworks.ecommerce.iniciandocomjpa;

import com.algaworks.ecommerce.model.Produto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

public class ConsultandoRegistrosTest {
    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @BeforeAll
    public static void setUpBeforeClass() {
        entityManagerFactory = Persistence.createEntityManagerFactory("Ecommerce-PU");
    }

    @AfterAll
    public static void tearDownAfterClass() {
        entityManagerFactory.close();
    }

    @BeforeEach
    public void setUp() {
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterEach
    public void tearDown() {
        entityManager.close();
    }

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
