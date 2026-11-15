package br.com.tarefinhas.usuario;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.tarefinhas.usuario.AuthDtos.CadastroRequest;
import br.com.tarefinhas.usuario.AuthDtos.LoginRequest;
import br.com.tarefinhas.usuario.AuthDtos.UsuarioResponse;
import jakarta.validation.Valid;

/**
 * Endpoints de cadastro e login.
 *
 * Login simplificado (trabalho de faculdade): a API devolve os dados do usuário e
 * o front envia o id dele no cabeçalho "X-Usuario-Id" nas próximas chamadas.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // POST /api/auth/cadastro
    @PostMapping("/cadastro")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse cadastrar(@RequestBody @Valid CadastroRequest dados) {
        return authService.cadastrar(dados);
    }

    // POST /api/auth/login
    @PostMapping("/login")
    public UsuarioResponse entrar(@RequestBody @Valid LoginRequest dados) {
        return authService.entrar(dados);
    }
}
