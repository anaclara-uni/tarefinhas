import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { AuthService } from '../../servicos/auth.service';
import { mensagemDeErro } from '../../servicos/usuario.interceptor';

/**
 * Tela de Entrar / Criar conta.
 */
@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class LoginPagina {
  private auth = inject(AuthService);
  private router = inject(Router);

  // Qual aba está aberta
  modo = signal<'entrar' | 'cadastrar'>('entrar');

  // Campos do formulário (ligados aos inputs com [(ngModel)])
  nome = signal('');
  email = signal('');
  senha = signal('');
  lembrar = signal(true);
  mostrarSenha = signal(false);

  erro = signal('');
  enviando = signal(false);

  constructor() {
    // Se já está logado, vai direto para as tarefas
    if (this.auth.usuario()) {
      this.router.navigate(['/tarefas']);
    }
  }

  trocarModo(modo: 'entrar' | 'cadastrar') {
    this.modo.set(modo);
    this.erro.set('');
  }

  enviar() {
    this.erro.set('');
    this.enviando.set(true);

    const pedido =
      this.modo() === 'entrar'
        ? this.auth.entrar(this.email(), this.senha(), this.lembrar())
        : this.auth.cadastrar(this.nome(), this.email(), this.senha());

    pedido.subscribe({
      next: () => this.router.navigate(['/tarefas']),
      error: (e: HttpErrorResponse) => {
        this.erro.set(mensagemDeErro(e));
        this.enviando.set(false);
      },
    });
  }
}
