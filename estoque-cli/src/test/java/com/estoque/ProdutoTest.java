package com.estoque;

import com.estoque.model.Produto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ProdutoTest {

    @Test
    void construtor_deveDefinirIdInformado() {
        Produto p = new Produto("1", "Item", 1, 9.99, 2);

        assertEquals("1", p.getId());
        assertEquals("Item", p.getNome());
    }

    @Test
    void construtor_doisProdutos_comIdsDiferentes_saoDistintos() {
        Produto a = new Produto("1", "A", 1, 1.0, 1);
        Produto b = new Produto("2", "B", 1, 1.0, 1);

        assertNotEquals(a.getId(), b.getId());
    }

    @Test
    void gettersESetters_devemFuncionar() {
        Produto p = new Produto("0", "x", 0, 0.0, 0);

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
