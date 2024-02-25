package com.challenge.jemersoft.models;

import java.util.ArrayList;
import java.util.List;

public class Pokemon {
//    private Long id;
    private String foto;
    private String type;
    private String weight;
    List ability = new ArrayList<String>();

    public Pokemon(){}
    public Pokemon(Long id, String foto, String type, String weight, List ability) {
//        this.id = id;
        this.foto = foto;
        this.type = type;
        this.weight = weight;
        this.ability = ability;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public List getAbility() {
        return ability;
    }

    public void setAbility(List ability) {
        this.ability = ability;
    }
}
