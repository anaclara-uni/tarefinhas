import { Component, inject, input, output, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { Lista } from '../../../../servicos/modelos';
import { ListaService } from '../../../../servicos/lista.service';
import { mensagemDeErro } from '../../../../servicos/usuario.interceptor';
import { Autofoco } from '../../../../compartilhado/autofoco';

/**
 * "Minhas listas": mostra as listas e faz o CRUD delas (criar, renomear, excluir).
 * Usado no menu lateral (computador) e na gaveta de listas (celular).
 */
@Component({
  selector: 'app-listas-menu',
  imports: [FormsModule, Autofoco],
  templateUrl: './listas-menu.html',
  styleUrl: './listas-menu.scss',
})
export class ListasMenu {
  private listaService = inject(ListaService);

  listas = input<Lista[]>([]);
  listaId = input<number | null>(null);
  // No celular não existe "passar o mouse", então os botões ficam sempre visíveis
  acoesSempreVisiveis = input(false);

  escolherLista = output<number>();
  listasMudaram = output<void>();

  criandoLista = signal(false);
  nomeNovaLista = signal('');
  editandoId = signal<number | null>(null);
  nomeEdicao = signal('');

  // ----- Criar lista -----
  salvarNovaLista() {
    if (!this.criandoLista()) return; // evita salvar duas vezes (Enter + sair do campo)
    const nome = this.nomeNovaLista().trim();
    this.criandoLista.set(false);
    this.nomeNovaLista.set('');
    if (!nome) return;
    this.listaService.criar(nome).subscribe({
      next: () => this.listasMudaram.emit(),
      error: (e: HttpErrorResponse) => alert(mensagemDeErro(e)),
    });
  }

  // ----- Editar lista -----
  comecarEdicao(lista: Lista) {
    this.editandoId.set(lista.id);
    this.nomeEdicao.set(lista.nome);
  }

  salvarEdicao(lista: Lista) {
    if (this.editandoId() !== lista.id) return; // evita salvar duas vezes (Enter + sair do campo)
    const nome = this.nomeEdicao().trim();
    this.editandoId.set(null);
    if (!nome || nome === lista.nome) return;
    this.listaService.editar(lista.id, nome).subscribe({
      next: () => this.listasMudaram.emit(),
      error: (e: HttpErrorResponse) => alert(mensagemDeErro(e)),
    });
  }

  // ----- Excluir lista -----
  excluir(lista: Lista) {
    if (!confirm(`Excluir a lista "${lista.nome}"? As tarefas dela continuam existindo, só ficam sem lista.`)) return;
    this.listaService.excluir(lista.id).subscribe({
      next: () => this.listasMudaram.emit(),
      error: (e: HttpErrorResponse) => alert(mensagemDeErro(e)),
    });
  }
}
