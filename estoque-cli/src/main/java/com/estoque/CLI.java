package com.estoque;

import com.estoque.model.Produto;
import com.estoque.service.EstoqueService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class CLI {

    private final EstoqueService estoque;
    private final Scanner scanner;

    public CLI(EstoqueService estoque) {
        this(estoque, new Scanner(System.in, StandardCharsets.UTF_8));
    }

    public CLI(EstoqueService estoque, Scanner scanner) {
        this.estoque = estoque;
        this.scanner = scanner;
    }

    public void iniciar() {
        System.out.println("Estoque CLI v1.0.0");
        boolean rodando = true;
        while (rodando) {
            exibirMenu();
            String linha = scanner.nextLine().trim();
            try {
                int opcao = Integer.parseInt(linha);
                rodando = processarOpcao(opcao);
            } catch (NumberFormatException e) {
                System.out.println("Opcao invalida. Digite um numero.");
            }
        }
        System.out.println("Ate logo.");
    }

    private void exibirMenu() {
        System.out.println();
        System.out.println("=== CONTROLE DE ESTOQUE ===");
        System.out.println("1. Adicionar produto");
        System.out.println("2. Listar todos os produtos");
        System.out.println("3. Atualizar quantidade");
        System.out.println("4. Remover produto");
        System.out.println("5. Buscar produto por nome");
        System.out.println("6. Ver estoque baixo");
        System.out.println("0. Sair");
        System.out.print("Escolha: ");
    }

    private boolean processarOpcao(int opcao) {
        switch (opcao) {
            case 0:
                return false;
            case 1:
                adicionarProduto();
                break;
            case 2:
                listarTodos();
                break;
            case 3:
                atualizarQuantidade();
                break;
            case 4:
                removerProduto();
                break;
            case 5:
                buscarPorNome();
                break;
            case 6:
                listarEstoqueBaixo();
                break;
            default:
                System.out.println("Opcao invalida.");
        }
        return true;
    }

    private void adicionarProduto() {
        try {
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            System.out.print("Quantidade: ");
            int qtd = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Preco: ");
            double preco = Double.parseDouble(scanner.nextLine().trim().replace(',', '.'));
            System.out.print("Quantidade minima (alerta): ");
            int min = Integer.parseInt(scanner.nextLine().trim());
            estoque.adicionarProduto(nome, qtd, preco, min);
            System.out.println("Produto adicionado.");
        } catch (NumberFormatException e) {
            System.out.println("Valor numerico invalido.");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Erro ao salvar os dados.");
        }
    }

    private void listarTodos() {
        List<Produto> lista = estoque.listarProdutos();
        if (lista.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }
        imprimirCabecalhoTabela();
        for (Produto p : lista) {
            imprimirLinhaProduto(p);
        }
    }

    private void atualizarQuantidade() {
        try {
            System.out.print("ID do produto: ");
            String id = scanner.nextLine().trim();
            System.out.print("Nova quantidade: ");
            int qtd = Integer.parseInt(scanner.nextLine().trim());
            estoque.atualizarQuantidade(id, qtd);
            System.out.println("Quantidade atualizada.");
        } catch (NumberFormatException e) {
            System.out.println("Quantidade invalida.");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (NoSuchElementException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Erro ao salvar os dados.");
        }
    }

    private void removerProduto() {
        try {
            System.out.print("ID do produto: ");
            String id = scanner.nextLine().trim();
            estoque.removerProduto(id);
            System.out.println("Produto removido.");
        } catch (NoSuchElementException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Erro ao salvar os dados.");
        }
    }

    private void buscarPorNome() {
        try {
            System.out.print("Trecho do nome: ");
            String trecho = scanner.nextLine();
            List<Produto> lista = estoque.buscarPorNome(trecho);
            if (lista.isEmpty()) {
                System.out.println("Nenhum produto encontrado.");
                return;
            }
            imprimirCabecalhoTabela();
            for (Produto p : lista) {
                imprimirLinhaProduto(p);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void listarEstoqueBaixo() {
        List<Produto> lista = estoque.listarEstoqueBaixo();
        if (lista.isEmpty()) {
            System.out.println("Nenhum produto com estoque baixo.");
            return;
        }
        imprimirCabecalhoTabela();
        for (Produto p : lista) {
            imprimirLinhaProduto(p);
        }
    }

    private static void imprimirCabecalhoTabela() {
        System.out.printf(Locale.US, "%-10s %-28s %10s %12s%n", "ID", "Nome", "Qtd", "Preco");
        System.out.println("-".repeat(64));
    }

    private static void imprimirLinhaProduto(Produto p) {
        String id = p.getId() == null ? "" : p.getId();
        String nome = p.getNome() == null ? "" : p.getNome();
        if (nome.length() > 28) {
            nome = nome.substring(0, 25) + "...";
        }
        System.out.printf(Locale.US, "%-10s %-28s %10d %12.2f%n",
                id, nome, p.getQuantidade(), p.getPreco());
    }
}
