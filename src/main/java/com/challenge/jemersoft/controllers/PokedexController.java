package com.challenge.jemersoft.controllers;


import com.challenge.jemersoft.services.PokemonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pokedex")
public class PokedexController {

    private final PokemonService pokemonService;

    public PokedexController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @GetMapping("/pokemons")
    public ResponseEntity<Object> pokemons() {
        try{
            Object pokemons = this.pokemonService.getPokemons();
            if (pokemons != "")
                return new ResponseEntity<>(pokemons, HttpStatus.OK);
            else
                return new ResponseEntity<> ("Ocurrio un error", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<> ("Ocurrio un error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
