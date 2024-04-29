package com.algaworks.ecommerce.iniciandocomjpa;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Produto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class OperacoesComTransacaoTest extends EntityManagerTest {
    @Test
    public void inserirOPrimeiroObjeto() {
        Produto produto = new Produto();

        produto.setId(2);
        produto.setNome("Câmera Canon");
        produto.setDescricao("A melhor definição para suas fotos.");
        produto.setPreco(new BigDecimal(5000));

        entityManager.getTransaction().begin();
        entityManager.persist(produto);
        entityManager.getTransaction().commit();

        // entityManager.clear();  -> Não é necessário para a operação de remoção

        Produto produtoVerificacao = entityManager.find(Produto.class, produto.getId());
        Assertions.assertNotNull(produtoVerificacao);
    }

    @Test
    public void removerObjeto() {
        Produto produto = entityManager.find(Produto.class, 3);

        entityManager.getTransaction().begin();
        entityManager.remove(produto);
        entityManager.getTransaction().commit();

        entityManager.clear();

        Produto produtoVerificacao = entityManager.find(Produto.class, 3);
        Assertions.assertNull(produtoVerificacao);
    }

    @Test
    public void atualizarObjeto() {
        /**
         * Quando o objeto vem de qualquer fonte que não seja o JPA
         * é preciso preencher todos os atritubos do objeto antes de executar o merge, se não,
         * ao fazer o merge os valores não preenchidos serão atualizados para null no banco de dados.
         */
        Produto produto = new Produto();
        produto.setId(1);
        produto.setNome("Kindle Paperwhite");
        produto.setDescricao("Conheça o novo Kindle.");
        produto.setPreco(new BigDecimal(599));

        entityManager.getTransaction().begin();
        entityManager.merge(produto);
        entityManager.getTransaction().commit();

        entityManager.clear();

        Produto produtoVerificacao = entityManager.find(Produto.class, 1);
        Assertions.assertNotNull(produtoVerificacao);
        Assertions.assertEquals("Kindle Paperwhite", produtoVerificacao.getNome());
    }

    @Test
    public void atualizarObjetoGerenciado() {
        Produto produto = entityManager.find(Produto.class, 1);
        produto.setNome("Kindle Paperwhite 2ª Geração");

        /**
         * Ao fazer uma atualização de um objeto gerenciado na memória do entity manager
         * não é necessário usar o comando merge explícito.
         * */
        entityManager.getTransaction().begin();
        entityManager.getTransaction().commit();

        entityManager.clear();

        Produto produtoVerificacao = entityManager.find(Produto.class, 1);
        Assertions.assertEquals("Kindle Paperwhite 2ª Geração", produtoVerificacao.getNome());
    }

    @Test
    public void abrirEFecharATransacao() {
        Produto produto = new Produto();  //  Somente para os métodos abaixo não ficarem dando erro

        entityManager.getTransaction().begin();

        entityManager.persist(produto);
        entityManager.merge(produto);
        entityManager.remove(produto);

        entityManager.getTransaction().rollback();
    }
}
