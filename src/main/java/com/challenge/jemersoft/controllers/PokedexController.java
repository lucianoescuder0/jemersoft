package com.challenge.jemersoft.controllers;


import com.challenge.jemersoft.services.PokemonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pokedex")
public class PokedexController {
    private final PokemonService pokemonService;

    public PokedexController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @GetMapping("/pokemons")
    public ResponseEntity<Object> pokemons(@RequestParam(required = false, defaultValue = "0") Integer page) {
        Map error = new HashMap<String, String>();
        error.put("error", "Ocurrio un error.");
        try {
            if (page < 0) {
                error.put("error", "paginación errónea.");
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            }
            Object pokemons = this.pokemonService.getPokemons(page);
            if (pokemons != "")
                return new ResponseEntity<>(pokemons, HttpStatus.OK);
            else {
                error.put("error", "Ocurrio un error al obtener los pockemons.");
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/pokemon/{name}")
    public ResponseEntity<Object> pokemonDetails(@PathVariable String name) {
        Map error = new HashMap<String, String>();
        error.put("error", "Ocurrio un error.");
        try {
            Object pokemon = this.pokemonService.getPokemonWhitDetails(name);
            if (pokemon != null)
                return new ResponseEntity<>(pokemon, HttpStatus.OK);
            else {
                error.put("error", "No se encuentra el pokemon " + name);
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
