package com.estoque.service;

import com.estoque.model.Produto;
import com.estoque.repository.ProdutoRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;

public class EstoqueService {

    private final ProdutoRepository repository;
    private final List<Produto> produtos;

    public EstoqueService(ProdutoRepository repository) {
        this.repository = repository;
        this.produtos = new ArrayList<>(repository.carregar());
    }

    public void adicionarProduto(String nome, int quantidade, double preco, int quantidadeMinima)
            throws IOException {
        validarNome(nome);
        if (quantidade < 0) {
            throw new IllegalArgumentException("quantidade nao pode ser negativa");
        }
        if (preco < 0) {
            throw new IllegalArgumentException("preco nao pode ser negativo");
        }
        String id = proximoId();
        Produto produto = new Produto(id, nome.trim(), quantidade, preco, quantidadeMinima);
        produtos.add(produto);
        repository.salvar(produtos);
    }

    private String proximoId() {
        int max = 0;
        for (Produto p : produtos) {
            if (p.getId() == null || p.getId().isEmpty()) {
                continue;
            }
            try {
                int n = Integer.parseInt(p.getId().trim());
                if (n > max) {
                    max = n;
                }
            } catch (NumberFormatException ignored) {
                // ignora IDs antigos não numéricos (ex.: UUID)
            }
        }
        return Integer.toString(max + 1);
    }

    public void removerProduto(String id) throws IOException {
        Produto produto = findById(id).orElseThrow(() -> new NoSuchElementException("produto nao encontrado: " + id));
        produtos.remove(produto);
        repository.salvar(produtos);
    }

    public void atualizarQuantidade(String id, int novaQuantidade) throws IOException {
        if (novaQuantidade < 0) {
            throw new IllegalArgumentException("nova quantidade nao pode ser negativa");
        }
        Produto produto = findById(id).orElseThrow(() -> new NoSuchElementException("produto nao encontrado: " + id));
        produto.setQuantidade(novaQuantidade);
        repository.salvar(produtos);
    }

    public List<Produto> listarProdutos() {
        return new ArrayList<>(produtos);
    }

    public List<Produto> buscarPorNome(String nome) {
        if (nome == null) {
            throw new IllegalArgumentException("nome nao pode ser nulo");
        }
        String trecho = nome.toLowerCase(Locale.ROOT);
        List<Produto> resultado = new ArrayList<>();
        for (Produto p : produtos) {
            if (p.getNome() != null && p.getNome().toLowerCase(Locale.ROOT).contains(trecho)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public List<Produto> listarEstoqueBaixo() {
        List<Produto> resultado = new ArrayList<>();
        for (Produto p : produtos) {
            if (p.getQuantidade() <= p.getQuantidadeMinima()) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public Optional<Produto> findById(String id) {
        if (id == null) {
            return Optional.empty();
        }
        for (Produto p : produtos) {
            if (id.equals(p.getId())) {
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }

    private static void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("nome invalido");
        }
    }
}
