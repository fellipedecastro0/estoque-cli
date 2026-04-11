package com.estoque.repository;

import com.estoque.model.Produto;

import java.io.IOException;
import java.util.List;

public interface ProdutoRepository {

    List<Produto> carregar();

    void salvar(List<Produto> produtos) throws IOException;
}
