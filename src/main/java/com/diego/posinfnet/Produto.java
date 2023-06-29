package com.diego.posinfnet;

import java.math.BigDecimal;
import java.nio.file.Path;

class Produto {
    private String nome;
    private Path file;
    private BigDecimal preco;

    public Produto(String nome, Path file, BigDecimal preco) {
        this.nome = nome;
        this.file = file;
        this.preco = preco;
    }

    public String getNome() {
        return nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }
}