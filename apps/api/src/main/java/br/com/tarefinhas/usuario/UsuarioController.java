package br.com.tarefinhas.usuario;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.tarefinhas.usuario.AuthDtos.PerfilRequest;
import br.com.tarefinhas.usuario.AuthDtos.UsuarioResponse;
import jakarta.validation.Valid;

/**
 * Endpoints do perfil de quem está logado ("me" = eu).
 * Junto com o cadastro, completam o CRUD de usuários.
 */
@RestController
@RequestMapping("/api/usuarios/me")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // GET /api/usuarios/me -> meus dados
    @GetMapping
    public UsuarioResponse ver(@RequestHeader("X-Usuario-Id") Long usuarioId) {
        return usuarioService.ver(usuarioId);
    }

    // PUT /api/usuarios/me -> edita nome, papel e (opcional) senha
    @PutMapping
    public UsuarioResponse editar(@RequestHeader("X-Usuario-Id") Long usuarioId,
            @RequestBody @Valid PerfilRequest dados) {
        return usuarioService.editar(usuarioId, dados);
    }

    // DELETE /api/usuarios/me -> exclui a conta e tudo dela
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@RequestHeader("X-Usuario-Id") Long usuarioId) {
        usuarioService.excluir(usuarioId);
    }
}
