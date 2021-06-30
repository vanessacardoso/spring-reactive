package com.vanessacds.pokedex;

import com.vanessacds.pokedex.model.Pokemon;
import com.vanessacds.pokedex.repository.PokedexRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class PokedexApplication {

    public static void main(String[] args) {
        SpringApplication.run(PokedexApplication.class, args);
    }

    /**
     * Estamos usando o Mongo Embeded, sendo assim, não é necessário criar um conta ou etc, esta tudo local na nossa máquina.
     *
     * Na classe Main() criaremos uma chamada do nosso banco de dados, usando o CommandLiner que é uma interface funcional que
     * recebe variável de argumentos em cadeia.. o mesmo deve estar anotado com o
     * @Bean para o spring boot entender que aquilo deve ser processado
    */
    @Bean
    CommandLineRunner init(ReactiveMongoOperations operations, PokedexRepository repository) {
        return args -> {
            Flux<Pokemon> pokemonFlux = Flux.just(
                    new Pokemon(null, "Bulbassauro", "Semente", "OverGrow", 6.09),
                    new Pokemon(null, "Charizard", "Fogo", "Blaze", 90.05),
                    new Pokemon(null, "Caterpie", "Minhoca", "Poeira do Escudo", 2.09),
                    new Pokemon(null, "Blastoise", "Marisco", "Torrente", 6.09))
                    .flatMap(repository::save);

            pokemonFlux
                    .thenMany(repository.findAll())
                    .subscribe(System.out::println);
        };
    }

}

