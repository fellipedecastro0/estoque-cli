package com.estoque;

import com.estoque.repository.JsonRepository;
import com.estoque.service.EstoqueService;

public final class Main {

    public static void main(String[] args) {
        JsonRepository repository = new JsonRepository();
        EstoqueService service = new EstoqueService(repository);
        CLI cli = new CLI(service);
        cli.iniciar();
    }
}
