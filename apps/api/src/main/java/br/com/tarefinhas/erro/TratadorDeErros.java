package br.com.tarefinhas.erro;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

/**
 * Transforma os erros da API em uma resposta simples: { "mensagem": "..." }.
 * Assim o front consegue mostrar a mensagem direto para a pessoa.
 */
@RestControllerAdvice
public class TratadorDeErros {

    // Erros que nós mesmos lançamos nos services (ex: "Tarefa não encontrada")
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> erroConhecido(ResponseStatusException e) {
        return ResponseEntity.status(e.getStatusCode()).body(Map.of("mensagem", e.getReason()));
    }

    // Erros de validação (@NotBlank, @Email...): devolvemos a primeira mensagem
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> dadosInvalidos(MethodArgumentNotValidException e) {
        String mensagem = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return ResponseEntity.badRequest().body(Map.of("mensagem", mensagem));
    }

    // O JSON enviado está quebrado ou com algum valor de tipo errado
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> jsonInvalido() {
        return ResponseEntity.badRequest().body(Map.of("mensagem", "Os dados enviados estão em um formato inválido."));
    }

    // Faltou o cabeçalho X-Usuario-Id: a pessoa não está logada
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<Map<String, String>> semLogin() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("mensagem", "Faça login para continuar."));
    }
}
