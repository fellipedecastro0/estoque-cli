package com.estoque;

import com.estoque.model.Produto;
import com.estoque.repository.ProdutoRepository;
import com.estoque.service.EstoqueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EstoqueServiceTest {

    private FakeProdutoRepository fake;
    private EstoqueService service;

    @BeforeEach
    void setUp() {
        fake = new FakeProdutoRepository();
        service = new EstoqueService(fake);
    }

    @Test
    void adicionarProduto_deveAdicionarComSucesso() throws IOException {
        service.adicionarProduto("Caneta", 5, 1.5, 2);

        List<Produto> lista = service.listarProdutos();
        assertEquals(1, lista.size());
        assertEquals("Caneta", lista.get(0).getNome());
        assertEquals(5, lista.get(0).getQuantidade());
    }

    @Test
    void adicionarProduto_deveLancarExcecaoSeQuantidadeNegativa() {
        assertThrows(IllegalArgumentException.class, () -> service.adicionarProduto("X", -1, 1.0, 0));
    }

    @Test
    void adicionarProduto_deveLancarExcecaoSeNomeVazio() {
        assertThrows(IllegalArgumentException.class, () -> service.adicionarProduto("", 1, 1.0, 0));
        assertThrows(IllegalArgumentException.class, () -> service.adicionarProduto("   ", 1, 1.0, 0));
    }

    @Test
    void removerProduto_deveRemoverComSucesso() throws IOException {
        service.adicionarProduto("Item", 1, 1.0, 0);
        String id = service.listarProdutos().get(0).getId();

        service.removerProduto(id);

        assertTrue(service.listarProdutos().isEmpty());
    }

    @Test
    void removerProduto_deveLancarExcecaoSeIdInexistente() {
        assertThrows(NoSuchElementException.class, () -> service.removerProduto("id-inexistente"));
    }

    @Test
    void listarEstoqueBaixo_deveRetornarProdutosAbaixoDoMinimo() throws IOException {
        service.adicionarProduto("Baixo", 2, 1.0, 5);
        service.adicionarProduto("Ok", 10, 1.0, 2);

        List<Produto> baixos = service.listarEstoqueBaixo();

        assertEquals(1, baixos.size());
        assertEquals("Baixo", baixos.get(0).getNome());
    }

    @Test
    void buscarPorNome_deveRetornarMatchParcial() throws IOException {
        service.adicionarProduto("Caneta Azul", 1, 2.0, 0);

        List<Produto> encontrados = service.buscarPorNome("caneta");

        assertEquals(1, encontrados.size());
        assertEquals("Caneta Azul", encontrados.get(0).getNome());
    }

    private static final class FakeProdutoRepository implements ProdutoRepository {

        private final List<Produto> dados = new ArrayList<>();

        @Override
        public List<Produto> carregar() {
            return new ArrayList<>(dados);
        }

        @Override
        public void salvar(List<Produto> produtos) {
            dados.clear();
            dados.addAll(produtos);
        }
    }
}
