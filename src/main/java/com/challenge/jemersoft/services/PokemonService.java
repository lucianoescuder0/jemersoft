package com.challenge.jemersoft.services;

import com.challenge.jemersoft.models.Pokemon;
import com.challenge.jemersoft.models.PokemonDetails;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class PokemonService {
    @Autowired
    RestTemplate restTemplate;

    @Value("${baseUrl}")
    private String baseUrl;

    private String prefix = "pokemonService.";

    private List<String> getMoves(List movesListMap) {
        List<String> moves = new ArrayList<String>();
        try {
            movesListMap.forEach(it ->
                    moves.add(((Map) ((Map) it).get("move")).get("name").toString())
            );
        } catch (Exception e) {
            System.out.println(prefix + "getDescription - ERROR - Ocurrio un error al obtener los movimientos del pokemon");
            e.printStackTrace();
        }
        return moves;
    }

    private String getDescription(String id) {
        String description = "";
        try {
            String url = baseUrl + "/characteristic/" + id;
            Map res = restTemplate.getForObject(url, Map.class);
            List descriptions = (List) res.get("descriptions");
            description = descriptions.stream().filter(it ->
                    ((Map) ((Map) it).get("language")).get("name").toString().equals("es")
            ).map(pro -> ((Map) pro).get("description")).findFirst().get().toString();
        } catch (Exception e) {
            System.out.println(prefix + "getDescription - ERROR - Ocurrio un error al obtener la descripción del pokemon con id: " + id);
            e.printStackTrace();
        }
        return description;
    }

    private String getTipo(List tipos) {
        String type = "";
        try {
            final String[] tipo = {""};
            tipos.forEach(it ->
                    tipo[0] = tipo[0] + " - " + ((Map) ((Map) it).get("type")).get("name").toString()
            );
            type = tipo[0].substring(3, tipo[0].length());
        } catch (Exception e) {
            System.out.println(prefix + "getTipo - ERROR - Ocurrio un error al obtener el tipo del pokemon");
            e.printStackTrace();
        }
        return type;
    }

    private String getFoto(Map sprites) {
        String foto = "";
        try {
            foto = ((Map) (((Map) sprites.get("other")).get("dream_world"))).get("front_default").toString();
        } catch (Exception e) {
            System.out.println(prefix + "getFoto - ERROR - Ocurrio un error al obtener la foto de mejor calidad");
            e.printStackTrace();
            foto = sprites.get("front_default").toString();
        }
        return foto;
    }

    private List getHabilidades(List listMapHabilidades) {
        List habilidades = new ArrayList<>();

        try {
            listMapHabilidades.forEach(it ->
                    habilidades.add((((Map) ((Map) it).get("ability")).get("name").toString()))
            );

        } catch (Exception e) {
            System.out.println(prefix + "getHabilidades - ERROR - Ocurrio un error al obtener las habilidades");
            e.printStackTrace();
        }
        return habilidades;
    }

    public Object getInfoBasePokemos(Map res, Boolean esPadre) {
        try {
            Pokemon pokemon = esPadre ? new Pokemon() : new PokemonDetails();
            if (res.isEmpty()) {
                //log.debug("No se pudo obtener el pockemon " + name);
                return null;
            }
            String tipo = this.getTipo((List) res.get("types"));
            List habilidades = this.getHabilidades((List) res.get("abilities"));
            String foto = this.getFoto((Map) res.get("sprites"));
//            pokemon.setId(Long.parseLong(res.get("id").toString()));
            pokemon.setWeight(res.get("weight").toString());
            pokemon.setFoto(foto);
            pokemon.setAbility(habilidades);
            pokemon.setType(tipo);
            return pokemon;
        } catch (Exception e) {
            //log.error(...); -- Asi lo manejamos actualmente en la empresa donde estoy. mostramos el stackTrace en los logs
            System.out.println(prefix + "getInfoBasePokemos - ERROR - Ocurrio un error al obtener la información basica del pokemon " + res.get("name").toString());
            e.printStackTrace();
            return null;
        }
    }

    public Map getPokemonResponse(String name) {
        String newUrl = baseUrl + "/pokemon/" + name;
        Map resPoke = restTemplate.getForObject(newUrl, Map.class);
        return resPoke;
    }


    public Object getPokemons() {
        try {
            String url = baseUrl + "/pokemon";
            Map res = restTemplate.getForObject(url, HashMap.class);
            List pokemons = new ArrayList<Pokemon>();
            List<Map<String, Object>> resPokemons = new ArrayList<>();
            resPokemons.addAll((Collection<? extends Map<String, Object>>) res.get("results"));

            resPokemons.forEach(it ->
                    {
                        Map resPoke = this.getPokemonResponse(it.get("name").toString());
                        Pokemon pokemon = (Pokemon) this.getInfoBasePokemos(resPoke, true);
                        pokemons.add(pokemon);
                    }
            );


            res.put("results", pokemons);
            return res;
        } catch (Exception e) {
            //log.error(...); -- Asi lo manejamos actualmente en la empresa donde estoy. mostramos el stackTrace en los logs
            System.out.println(prefix + "getPokemons - ERROR - Ocurrio un error al obtener los pockemons");
            e.printStackTrace();
            return "";
        }

    }

    public PokemonDetails getPokemonWhitDetails(String name) {
        try {
            Map resPoke = this.getPokemonResponse(name);
            PokemonDetails pokemon = (PokemonDetails) this.getInfoBasePokemos(resPoke, false);
            String description = this.getDescription(resPoke.get("id").toString());
            List moves = this.getMoves((List) resPoke.get("moves"));
            pokemon.setDescripcion(description);
            pokemon.setListaMovimientos(moves);
            return pokemon;
        } catch (Exception e) {
            //log.error(...); -- Asi lo manejamos actualmente en la empresa donde estoy. mostramos el stackTrace en los logs
            System.out.println(prefix + "getPokemonWhitDetails - ERROR - Ocurrio un error al obtener los pockemons");
            e.printStackTrace();
            return null;
        }
    }


}
