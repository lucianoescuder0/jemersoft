package com.challenge.jemersoft.services;

import com.challenge.jemersoft.models.Pokemon;
import com.challenge.jemersoft.models.PokemonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class PokemonService {
    @Autowired
    RestTemplate restTemplate;

    @Value("${baseUrl}")
    private String baseUrl;

    private String prefix = getClass().getName() + "-";

    private List<String> getMoves(List movesListMap) {
        List<String> moves = new ArrayList<String>();
        try {
            movesListMap.forEach(it ->
                    moves.add(((Map) ((Map) it).get("move")).get("name").toString())
            );
        } catch (Exception e) {
            System.out.println(prefix + "getMoves - ERROR - Ocurrio un error al obtener los movimientos del pokemon");
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

    private String getType(List types) {
        String typeResult = "";
        try {
            final String[] type = {""};
            types.forEach(it ->
                    type[0] = type[0] + " - " + ((Map) ((Map) it).get("type")).get("name").toString()
            );
            typeResult = type[0].substring(3, type[0].length());
        } catch (Exception e) {
            System.out.println(prefix + "getType - ERROR - Ocurrio un error al obtener el tipo del pokemon");
            e.printStackTrace();
        }
        return typeResult;
    }

    private String getPhoto(Map sprites) {
        String photo = "";
        try {
            photo = ((Map) (((Map) sprites.get("other")).get("dream_world"))).get("front_default").toString();
        } catch (Exception e) {
            try {
                System.out.println(prefix + "getPhoto - ERROR - Ocurrio un error al obtener la foto de mejor calidad");
                photo = sprites.get("front_default").toString();
            } catch (Exception ignore) {}

        }
        return photo;
    }

    private List getAbilities(List abilities) {
        List abilitiesResult = new ArrayList<>();
        try {
            abilities.forEach(it ->
                    abilitiesResult.add((((Map) ((Map) it).get("ability")).get("name").toString()))
            );
        } catch (Exception e) {
            System.out.println(prefix + "getAbilities - ERROR - Ocurrio un error al obtener las habilidades");
            e.printStackTrace();
        }
        return abilitiesResult;
    }

    public Object getInfoBasePokemos(Map res, Boolean isFather) {
        try {
            Pokemon pokemon = isFather ? new Pokemon() : new PokemonDetails();
            if (res.isEmpty()) {
                //log.debug("No se pudo obtener el pockemon " + name);
                return null;
            }
            String type = this.getType((List) res.get("types"));
            List abilities = this.getAbilities((List) res.get("abilities"));
            String photo = this.getPhoto((Map) res.get("sprites"));
//            pokemon.setId(Long.parseLong(res.get("id").toString()));
            pokemon.setPeso(Float.parseFloat(res.get("weight").toString()));
            pokemon.setFoto(photo);
            pokemon.setHabilidades(abilities);
            pokemon.setTipo(type);
            return pokemon;
        } catch (Exception e) {
            //log.error(...); -- Asi lo manejamos actualmente en la empresa donde estoy. mostramos el stackTrace en los logs
            System.out.println(prefix + "getInfoBasePokemos - ERROR - Ocurrio un error al obtener la información basica del pokemon " + res.get("name").toString());
            e.printStackTrace();
            return null;
        }
    }

    public Map getPokemonResponse(String name) {
        Map resPoke = new HashMap<>();
        try {
            String newUrl = baseUrl + "/pokemon/" + name;
            resPoke = restTemplate.getForObject(newUrl, Map.class);
        } catch (Exception ignore) {
        }
        return resPoke;
    }

    public Object getPokemons(Integer offs) {
        Object result = "";
        try {
            Integer offset = offs * 10;
            String url = baseUrl + "/pokemon/?offset=" + offset + "&limit=6";
            Map res = restTemplate.getForObject(url, HashMap.class);
            if (res.isEmpty()) {
                result = "No se encontro listado de pokemons";
                System.out.println(prefix + "getPokemons - WARN - " + result);
                return result;
            }
            List pokemons = new ArrayList<Pokemon>();
            List<Map<String, Object>> resPokemons = new ArrayList<>();
            resPokemons.addAll((Collection<? extends Map<String, Object>>) res.get("results"));
            long inicio = System.currentTimeMillis();
            resPokemons.forEach(it -> {
                Map resPoke = this.getPokemonResponse(it.get("name").toString());
                Pokemon pokemon = (Pokemon) this.getInfoBasePokemos(resPoke, true);
                pokemons.add(pokemon);
            });
            long fin = System.currentTimeMillis();
            System.out.println("Tiempo : " + ((fin - inicio) / 1000));
            result = pokemons.size() > 0 ? pokemons : "No se encontraron resultados";
        } catch (Exception e) {
            result = "Ocurrio un error al obtener los pokemons";
            System.out.println(prefix + "getPokemons - ERROR - " + result);
            e.printStackTrace();
        }
        return result;
    }

    public Object getPokemonWhitDetails(String name) {
        String result = "";
        try {
            long inicio = System.currentTimeMillis();
            Map resPoke = this.getPokemonResponse(name);
            if (resPoke.isEmpty()) {
                result = "No se encuentra el pokemon " + name;
                System.out.println(prefix + "getPokemonWhitDetails - WARN - " + result);
                return result;
            }
            PokemonDetails pokemon = (PokemonDetails) this.getInfoBasePokemos(resPoke, false);
            String description = this.getDescription(resPoke.get("id").toString());
            List moves = this.getMoves((List) resPoke.get("moves"));
            pokemon.setDescripcion(description);
            pokemon.setMovimientos(moves);
            long fin = System.currentTimeMillis();
            System.out.println("Tiempo : " + ((fin - inicio) / 1000));
            return pokemon;
        } catch (Exception e) {
            //log.error(...); -- Asi lo manejamos actualmente en la empresa donde estoy. mostramos el stackTrace en los logs
            result = "Ocurrio un error al obtener el pokemon " + name;
            System.out.println(prefix + "getPokemonWhitDetails - ERROR - " + result);
            e.printStackTrace();
        }
        return result;
    }
}
