import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { AuthService } from '../../servicos/auth.service';
import { mensagemDeErro } from '../../servicos/usuario.interceptor';

/**
 * Tela de perfil: editar nome, o texto embaixo do nome e a senha, sair e excluir a conta.
 */
@Component({
  selector: 'app-perfil',
  imports: [FormsModule, RouterLink],
  templateUrl: './perfil.html',
  styleUrl: './perfil.scss',
})
export class PerfilPagina {
  private auth = inject(AuthService);
  private router = inject(Router);

  usuario = computed(() => this.auth.usuario()!);

  // Campos do formulário, já preenchidos com os dados atuais
  nome = signal(this.usuario().nome);
  papel = signal(this.usuario().papel ?? 'Estudante');
  novaSenha = signal('');

  iniciais = computed(() => (this.nome().trim() || '?').slice(0, 2).toUpperCase());

  erro = signal('');
  salvando = signal(false);

  salvar() {
    this.erro.set('');
    this.salvando.set(true);
    this.auth.editarPerfil(this.nome(), this.papel(), this.novaSenha()).subscribe({
      next: () => this.router.navigate(['/tarefas']),
      error: (e: HttpErrorResponse) => {
        this.erro.set(mensagemDeErro(e));
        this.salvando.set(false);
      },
    });
  }

  sair() {
    if (confirm('Deseja sair da sua conta?')) {
      this.auth.sair();
    }
  }

  excluirConta() {
    if (!confirm('Excluir sua conta? Todas as suas listas e tarefas serão apagadas para sempre.')) return;
    this.auth.excluirConta().subscribe({
      error: (e: HttpErrorResponse) => this.erro.set(mensagemDeErro(e)),
    });
  }
}
