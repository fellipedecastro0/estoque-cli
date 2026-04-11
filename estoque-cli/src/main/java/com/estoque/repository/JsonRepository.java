package com.estoque.repository;

import com.estoque.model.Produto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JsonRepository {

    private static final String DATA_FILE = "dados/produtos.json";

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private final Type listType = new TypeToken<ArrayList<Produto>>() { }.getType();

    public void salvar(List<Produto> produtos) throws IOException {
        Path path = Path.of(DATA_FILE);
        Files.createDirectories(path.getParent());
        String json = gson.toJson(produtos);
        Files.writeString(path, json, StandardCharsets.UTF_8);
    }

    public List<Produto> carregar() {
        Path path = Path.of(DATA_FILE);
        if (!Files.exists(path)) {
            return new ArrayList<>();
        }
        try {
            String json = Files.readString(path, StandardCharsets.UTF_8);
            if (json.isBlank()) {
                return new ArrayList<>();
            }
            List<Produto> lista = gson.fromJson(json, listType);
            return lista != null ? lista : new ArrayList<>();
        } catch (IOException | JsonSyntaxException e) {
            return new ArrayList<>();
        }
    }
}
