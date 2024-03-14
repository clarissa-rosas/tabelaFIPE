package br.com.alura.TabelaFIPE.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties (ignoreUnknown = true)
public record Veiculos(@JsonAlias ("Marca") String marca,
                       @JsonAlias ("Modelo") String modelo,
                       @JsonAlias ("AnoModelo") Integer ano,
                       @JsonAlias ("Combustivel") String combustivel,
                       @JsonAlias ("Valor") String valor) {
    @Override
    public String toString() {
        return  "\nMarca:" + marca +
                "\nModelo:" + modelo +
                "\nAno:" + ano +
                "\nCombustível:" + combustivel +
                "\nPreço médio:" + valor +
                "\n";
    }
}