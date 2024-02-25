package com.challenge.jemersoft.models;

import java.util.ArrayList;
import java.util.List;

public class PokemonDetails extends Pokemon {
    private String descripcion;
    List movimientos = new ArrayList<String>();

    public PokemonDetails() {}

    public PokemonDetails(String descripcion, List movimientos) {
        this.descripcion = descripcion;
        this.movimientos = movimientos;
    }

    public PokemonDetails(/*Long id, */String foto, String tipo, Float peso, List habilidades, String descripcion, List movimientos) {
        super(/*id,*/ foto, tipo, peso, habilidades);
        this.descripcion = descripcion;
        this.movimientos = movimientos;
    }

    public List getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List movimientos) {
        this.movimientos = movimientos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
