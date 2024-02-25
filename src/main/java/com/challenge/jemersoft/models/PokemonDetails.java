package com.challenge.jemersoft.models;

import java.util.ArrayList;
import java.util.List;

public class PokemonDetails extends Pokemon {
    private String descripcion;
    List ListaMovimientos = new ArrayList<String>();


    public PokemonDetails() {}

    public PokemonDetails(String descripcion, List listaMovimientos) {
        this.descripcion = descripcion;
        ListaMovimientos = listaMovimientos;
    }

    public PokemonDetails(Long id, String foto, String type, String weight, List ability, String descripcion, List listaMovimientos) {
        super(id, foto, type, weight, ability);
        this.descripcion = descripcion;
        ListaMovimientos = listaMovimientos;
    }

    public List getListaMovimientos() {
        return ListaMovimientos;
    }

    public void setListaMovimientos(List listaMovimientos) {
        ListaMovimientos = listaMovimientos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
