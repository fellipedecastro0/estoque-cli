package com.estoque;

import com.estoque.model.Produto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProdutoTest {

    @Test
    void construtor_deveGerarIdAutomaticamente() {
        Produto p = new Produto("Item", 1, 9.99, 2);

        assertNotNull(p.getId());
        assertNotEquals("", p.getId().trim());
    }

    @Test
    void construtor_doisProdutos_devemTerIdsDistintos() {
        Produto a = new Produto("A", 1, 1.0, 1);
        Produto b = new Produto("B", 1, 1.0, 1);

        assertNotEquals(a.getId(), b.getId());
    }

    @Test
    void gettersESetters_devemFuncionar() {
        Produto p = new Produto("x", 0, 0.0, 0);

        p.setId("id-1");
        p.setNome("Caneta");
        p.setQuantidade(10);
        p.setPreco(2.5);
        p.setQuantidadeMinima(3);

        assertAll(
                () -> assertEquals("id-1", p.getId()),
                () -> assertEquals("Caneta", p.getNome()),
                () -> assertEquals(10, p.getQuantidade()),
                () -> assertEquals(2.5, p.getPreco()),
                () -> assertEquals(3, p.getQuantidadeMinima())
        );
    }
}
