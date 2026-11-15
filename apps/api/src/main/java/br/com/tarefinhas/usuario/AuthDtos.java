package br.com.tarefinhas.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTOs = "formatos" dos dados que entram e saem da API no login/cadastro.
 * Um record é uma classe simples só com dados.
 */
public class AuthDtos {

    // O que chega no cadastro
    public record CadastroRequest(
            @NotBlank(message = "Informe seu nome.") String nome,
            @NotBlank(message = "Informe seu e-mail.") @Email(message = "E-mail inválido.") String email,
            @Size(min = 6, message = "A senha precisa ter pelo menos 6 caracteres.") String senha) {
    }

    // O que chega no login
    public record LoginRequest(
            @NotBlank(message = "Informe seu e-mail.") String email,
            @NotBlank(message = "Informe sua senha.") String senha) {
    }

    // O que chega para editar o perfil. A nova senha é opcional (vazia = não muda).
    public record PerfilRequest(
            @NotBlank(message = "Informe seu nome.")
            @Size(max = 100, message = "O nome pode ter no máximo 100 letras.") String nome,
            @Size(max = 60, message = "O texto embaixo do nome pode ter no máximo 60 letras.") String papel,
            String novaSenha) {
    }

    // O que devolvemos (nunca devolvemos a senha!)
    public record UsuarioResponse(Long id, String nome, String email, String papel) {
        static UsuarioResponse de(Usuario u) {
            return new UsuarioResponse(u.getId(), u.getNome(), u.getEmail(), u.getPapel());
        }
    }
}
