package br.com.tarefinhas.usuario;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.tarefinhas.lista.ListaService;
import br.com.tarefinhas.usuario.AuthDtos.CadastroRequest;
import br.com.tarefinhas.usuario.AuthDtos.LoginRequest;
import br.com.tarefinhas.usuario.AuthDtos.UsuarioResponse;

/**
 * Regras de cadastro e login.
 */
@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final ListaService listaService;
    // BCrypt transforma "123456" em um texto embaralhado que não dá para desfazer
    private final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

    public AuthService(UsuarioRepository usuarioRepository, ListaService listaService) {
        this.usuarioRepository = usuarioRepository;
        this.listaService = listaService;
    }

    @Transactional
    public UsuarioResponse cadastrar(CadastroRequest dados) {
        if (usuarioRepository.existsByEmailIgnoreCase(dados.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esse e-mail já está cadastrado.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dados.nome().trim());
        usuario.setEmail(dados.email().trim().toLowerCase());
        usuario.setSenhaHash(bcrypt.encode(dados.senha()));
        usuarioRepository.save(usuario);

        // Toda conta nova já começa com as listas padrão
        listaService.criarListasPadrao(usuario.getId());

        return UsuarioResponse.de(usuario);
    }

    public UsuarioResponse entrar(LoginRequest dados) {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(dados.email().trim())
                // matches() compara a senha digitada com o hash guardado no banco
                .filter(u -> bcrypt.matches(dados.senha(), u.getSenhaHash()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "E-mail ou senha incorretos."));

        return UsuarioResponse.de(usuario);
    }
}
