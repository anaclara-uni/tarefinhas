import { HttpClient } from '@angular/common/http';
import { Injectable, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { Usuario } from './modelos';

const CHAVE = 'tarefinhas-usuario';

/**
 * Cuida do login e do perfil: entrar, criar conta, sair, editar perfil, excluir conta
 * e lembrar quem está logado.
 *
 * O usuário logado fica guardado no navegador:
 *  - localStorage   -> continua logado mesmo fechando o navegador ("Lembrar de mim")
 *  - sessionStorage -> sai ao fechar o navegador
 */
@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);

  // signal = valor que, quando muda, atualiza a tela sozinho
  readonly usuario = signal<Usuario | null>(this.lerGuardado());

  entrar(email: string, senha: string, lembrar: boolean): Observable<Usuario> {
    return this.http
      .post<Usuario>('/api/auth/login', { email, senha })
      .pipe(tap((usuario) => this.guardar(usuario, lembrar)));
  }

  cadastrar(nome: string, email: string, senha: string): Observable<Usuario> {
    return this.http
      .post<Usuario>('/api/auth/cadastro', { nome, email, senha })
      .pipe(tap((usuario) => this.guardar(usuario, true)));
  }

  // GET /api/usuarios/me -> busca os dados atualizados de quem está logado
  recarregar(): void {
    this.http.get<Usuario>('/api/usuarios/me').subscribe((usuario) => this.atualizar(usuario));
  }

  // PUT /api/usuarios/me
  editarPerfil(nome: string, papel: string, novaSenha: string): Observable<Usuario> {
    return this.http
      .put<Usuario>('/api/usuarios/me', { nome, papel, novaSenha })
      .pipe(tap((usuario) => this.atualizar(usuario)));
  }

  // DELETE /api/usuarios/me
  excluirConta(): Observable<void> {
    return this.http.delete<void>('/api/usuarios/me').pipe(tap(() => this.sair()));
  }

  sair(): void {
    localStorage.removeItem(CHAVE);
    sessionStorage.removeItem(CHAVE);
    this.usuario.set(null);
    this.router.navigate(['/login']);
  }

  private guardar(usuario: Usuario, lembrar: boolean): void {
    const armazenamento = lembrar ? localStorage : sessionStorage;
    armazenamento.setItem(CHAVE, JSON.stringify(usuario));
    this.usuario.set(usuario);
  }

  // Atualiza o usuário guardado, no mesmo lugar onde ele já estava
  private atualizar(usuario: Usuario): void {
    const lembrar = localStorage.getItem(CHAVE) !== null;
    this.guardar(usuario, lembrar);
  }

  private lerGuardado(): Usuario | null {
    const texto = localStorage.getItem(CHAVE) ?? sessionStorage.getItem(CHAVE);
    return texto ? JSON.parse(texto) : null;
  }
}
