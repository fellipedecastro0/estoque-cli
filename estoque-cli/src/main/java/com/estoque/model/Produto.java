package com.estoque.model;

import java.util.Objects;
import java.util.UUID;

public class Produto {

    private String id;
    private String nome;
    private int quantidade;
    private double preco;
    private int quantidadeMinima;

    private Produto() {
        // Uso pelo Gson na desserialização de JSON
    }

    public Produto(String nome, int quantidade, double preco, int quantidadeMinima) {
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.quantidade = quantidade;
        this.preco = preco;
        this.quantidadeMinima = quantidadeMinima;
    }

    public Produto(String id, String nome, int quantidade, double preco, int quantidadeMinima) {
        this.id = id;
        this.nome = nome;
        this.quantidade = quantidade;
        this.preco = preco;
        this.quantidadeMinima = quantidadeMinima;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQuantidadeMinima() {
        return quantidadeMinima;
    }

    public void setQuantidadeMinima(int quantidadeMinima) {
        this.quantidadeMinima = quantidadeMinima;
    }

    @Override
    public String toString() {
        return "Produto{"
                + "id='" + id + '\''
                + ", nome='" + nome + '\''
                + ", quantidade=" + quantidade
                + ", preco=" + preco
                + ", quantidadeMinima=" + quantidadeMinima
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Produto produto = (Produto) o;
        return Objects.equals(id, produto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
