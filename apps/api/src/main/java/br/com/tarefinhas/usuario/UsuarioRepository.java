package br.com.tarefinhas.usuario;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Acesso à tabela "usuarios".
 * O JpaRepository já traz pronto: save, findById, findAll, deleteById...
 * Os métodos abaixo o Spring cria sozinho a partir do nome.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // SELECT * FROM usuarios WHERE LOWER(email) = LOWER(?)
    Optional<Usuario> findByEmailIgnoreCase(String email);

    // SELECT COUNT(*) > 0 FROM usuarios WHERE LOWER(email) = LOWER(?)
    boolean existsByEmailIgnoreCase(String email);
}
