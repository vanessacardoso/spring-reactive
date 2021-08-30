package com.vanessacds.pokedex.controller;

import com.vanessacds.pokedex.model.Pokemon;
import com.vanessacds.pokedex.repository.PokedexRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/pokemons")
public class PokemonController {

    private PokedexRepository pokedexRepository;

    public PokemonController(PokedexRepository pokedexRepository) {
        this.pokedexRepository = pokedexRepository;
    }

    @GetMapping
    public Flux<Pokemon> getAllPokemons() {
        return pokedexRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Pokemon>> getPokemonById(@PathVariable String id) {
        return pokedexRepository.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Pokemon> savePokemon(@RequestBody Pokemon pokemon) {
        return pokedexRepository.save(pokemon);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Pokemon>> updatePokemon(@PathVariable(value = "id") String id,
                                                       @RequestBody Pokemon pokemon) {

        return pokedexRepository.findById(id)
                .flatMap(existingPokemon -> {
                    existingPokemon.setNome(pokemon.getNome());
                    existingPokemon.setCategoria(pokemon.getCategoria());
                    existingPokemon.setHabilidade(pokemon.getHabilidade());
                    existingPokemon.setPeso(pokemon.getPeso());
                    return pokedexRepository.save(existingPokemon);
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public Mono<ResponseEntity<Void>> deletePokemon(@PathVariable(value = "id") String id) {
        return pokedexRepository.findById(id)
                .flatMap(existingPokemon ->
                        pokedexRepository.delete(existingPokemon)
                                .then(Mono.just(ResponseEntity.ok().<Void>build()))
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public Mono<Void> deleteAllPokemons() {
        return pokedexRepository.deleteAll();
    }
}




