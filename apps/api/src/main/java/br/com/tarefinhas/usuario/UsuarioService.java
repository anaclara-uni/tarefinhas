package br.com.tarefinhas.usuario;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.tarefinhas.usuario.AuthDtos.PerfilRequest;
import br.com.tarefinhas.usuario.AuthDtos.UsuarioResponse;

/**
 * Regras do perfil: ver, editar e excluir a própria conta.
 */
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public UsuarioResponse ver(Long id) {
        return UsuarioResponse.de(buscar(id));
    }

    @Transactional
    public UsuarioResponse editar(Long id, PerfilRequest dados) {
        Usuario usuario = buscar(id);
        usuario.setNome(dados.nome().trim());
        usuario.setPapel(dados.papel() == null || dados.papel().isBlank() ? "Estudante" : dados.papel().trim());

        // Só troca a senha se uma nova foi digitada
        if (dados.novaSenha() != null && !dados.novaSenha().isBlank()) {
            if (dados.novaSenha().length() < 6) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A nova senha precisa ter pelo menos 6 caracteres.");
            }
            usuario.setSenhaHash(bcrypt.encode(dados.novaSenha()));
        }
        return UsuarioResponse.de(usuarioRepository.save(usuario));
    }

    @Transactional
    public void excluir(Long id) {
        // O banco apaga junto as listas, tarefas e subtarefas (ON DELETE CASCADE)
        usuarioRepository.delete(buscar(id));
    }

    private Usuario buscar(Long id) {
        // 401 faz o site voltar para a tela de login
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Faça login novamente."));
    }
}
