package com.algaworks.ecommerce.consultasnativas;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.dto.ProdutoDTO;
import com.algaworks.ecommerce.model.ItemPedido;
import com.algaworks.ecommerce.model.Produto;
import org.junit.Test;

import javax.persistence.Query;
import java.util.List;

public class ConsultasNativaTest extends EntityManagerTest {
    @Test
    public void executarSQL() {
        String sql = "select id, nome from produto";
        Query query = entityManager.createNativeQuery(sql);

        List<Object[]> lista = query.getResultList();
        lista.stream().forEach(arr -> System.out.println(
                String.format("Produto => ID: %s, Nome: %s", arr[0], arr[1])));
    }

    @Test
    public void executarSQLRetornandoEntidade() {
        // Tabela e colunas com os mesmos nomes da entidade
        // String sql = "select id, nome, descricao, data_criacao, data_ultima_atualizacao, preco, foto " +
        //         "from produto";

        // Tabela com nome diferente, porém colunas com o mesmo nome da entidade
        // String sql = "select id, nome, descricao, data_criacao, data_ultima_atualizacao, preco, foto " +
        //         " from produto_loja";

        // Tabela e coluna com nomes diferentes, é preciso colocar alias nas colunas
        // String sql = "select prd_id id, prd_nome nome, prd_descricao descricao, " +
        //         "prd_data_criacao data_criacao, prd_data_ultima_atualizacao data_ultima_atualizacao, " +
        //         "prd_preco preco, prd_foto foto " +
        //         "from ecm_produto";

        // Tabela com nome diferente e coluna a menos
        // É necessário simular as colunas e colocar alias nelas
        String sql = "select id, nome, descricao, " +
                "            null data_criacao, null data_ultima_atualizacao, " +
                "            preco, null foto " +
                " from erp_produto";

        Query query = entityManager.createNativeQuery(sql, Produto.class);

        List<Produto> lista = query.getResultList();
        lista.stream().forEach(obj -> System.out.println(
                String.format("Produto => ID: %s, Nome: %s", obj.getId(), obj.getNome())));
    }

    @Test
    public void executarSQLComParametro() {
        String sql = "select prd_id id, prd_nome nome, prd_descricao descricao, " +
                "prd_data_criacao data_criacao, prd_data_ultima_atualizacao data_ultima_atualizacao, " +
                "prd_preco preco, prd_foto foto " +
                "from ecm_produto " +
                "where prd_id = :id";

        Query query = entityManager.createNativeQuery(sql, Produto.class);
        query.setParameter("id", 201);

        List<Produto> lista = query.getResultList();
        lista.stream().forEach(obj -> System.out.println(
                String.format("Produto => ID: %s, Nome: %s", obj.getId(), obj.getNome())));
    }

    @Test
    public void usarSQLResultSetMapping01() {
        String sql = "select id, nome, descricao, data_criacao, data_ultima_atualizacao, preco, foto " +
                " from produto_loja";

        Query query = entityManager.createNativeQuery(sql, "produto_loja.Produto");

        List<Produto> lista = query.getResultList();
        lista.stream().forEach(obj -> System.out.println(
                String.format("Produto => ID: %s, Nome: %s", obj.getId(), obj.getNome())));
    }

    @Test
    public void usarSQLResultSetMapping02() {
        String sql = "select ip.*, p.* " +
                "from item_pedido ip join produto p on p.id = ip.produto_id";

        Query query = entityManager.createNativeQuery(sql,
                "item_pedido-produto.ItemPedido-Produto");

        List<Object[]> lista = query.getResultList();
        lista.stream().forEach(arr -> System.out.println(
                String.format("Pedido => ID: %s --- Produto => ID: %s, Nome: %s",
                        ((ItemPedido) arr[0]).getId().getPedidoId(),
                        ((Produto) arr[1]).getId(), ((Produto) arr[1]).getNome())));
    }

    @Test
    public void usarFieldResult() {
        String sql = "select * from ecm_produto";

        Query query = entityManager.createNativeQuery(sql, "ecm_produto.Produto");

        List<Produto> lista = query.getResultList();
        lista.stream().forEach(obj -> System.out.println(
                String.format("Produto => ID: %s, Nome: %s", obj.getId(), obj.getNome())));
    }

    @Test
    public void usarColumnResultRetornarDTO() {
        String sql = "select * from ecm_produto";

        Query query = entityManager.createNativeQuery(sql, "ecm_produto.ProdutoDTO");

        List<ProdutoDTO> lista = query.getResultList();
        lista.stream().forEach(obj -> System.out.println(
                String.format("ProdutoDTO => ID: %s, Nome: %s", obj.getId(), obj.getNome())));
    }
}
