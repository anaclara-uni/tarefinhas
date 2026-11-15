package br.com.tarefinhas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de partida da API. Rodar este "main" liga o servidor na porta 8080.
 */
@SpringBootApplication
public class TarefinhasApplication {

    public static void main(String[] args) {
        SpringApplication.run(TarefinhasApplication.class, args);
    }
}
