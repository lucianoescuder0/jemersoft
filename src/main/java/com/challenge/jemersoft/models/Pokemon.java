package com.challenge.jemersoft.models;

import java.util.ArrayList;
import java.util.List;

public class Pokemon {
    //    private Long id;
    private String foto;
    private String tipo;
    private String peso;
    List habilidades = new ArrayList<String>();

    public Pokemon() {}

    public Pokemon(Long id, String foto, String tipo, String peso, List habilidades) {
//        this.id = id;
        this.foto = foto;
        this.tipo = tipo;
        this.peso = peso;
        this.habilidades = habilidades;
    }

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public List getHabilidades() {
        return habilidades;
    }

    public void setHabilidades(List habilidades) {
        this.habilidades = habilidades;
    }
}
