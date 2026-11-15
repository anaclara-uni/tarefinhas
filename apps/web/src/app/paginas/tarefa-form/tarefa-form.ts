import { Component, inject, input, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { TarefaService } from '../../servicos/tarefa.service';
import { ListaService } from '../../servicos/lista.service';
import { mensagemDeErro } from '../../servicos/usuario.interceptor';
import { Lista, Prioridade, PRIORIDADES, Subtarefa } from '../../servicos/modelos';
import { hojeIso } from '../../servicos/datas';
import { Autofoco } from '../../compartilhado/autofoco';

/**
 * Formulário de tarefa. Serve para as duas rotas:
 *  - /tarefas/nova          -> cria uma tarefa
 *  - /tarefas/6/editar      -> edita a tarefa 6
 */
@Component({
  selector: 'app-tarefa-form',
  imports: [FormsModule, RouterLink, Autofoco],
  templateUrl: './tarefa-form.html',
  styleUrl: './tarefa-form.scss',
})
export class TarefaFormPagina implements OnInit {
  private tarefaService = inject(TarefaService);
  private listaService = inject(ListaService);
  private router = inject(Router);

  // Vêm da URL (o Angular preenche sozinho): /tarefas/:id/editar e ?lista=2
  id = input<string>();
  lista = input<string>();

  listas = signal<Lista[]>([]);
  prioridades = Object.entries(PRIORIDADES).map(([valor, p]) => ({ valor: valor as Prioridade, ...p }));

  // Campos do formulário
  titulo = signal('');
  descricao = signal('');
  dataEntrega = signal(hojeIso());
  listaId = signal<number | null>(null);
  prioridade = signal<Prioridade>('MEDIA');
  lembrete = signal(false);
  subtarefas = signal<Subtarefa[]>([]);

  // Campo da nova subtarefa
  adicionandoSubtarefa = signal(false);
  textoSubtarefa = signal('');

  erro = signal('');
  salvando = signal(false);

  ngOnInit() {
    // Carrega as listas para o campo "Lista"
    this.listaService.listar().subscribe((listas) => {
      this.listas.set(listas);
      if (!this.id() && this.listaId() === null) {
        this.listaId.set(this.lista() ? Number(this.lista()) : (listas[0]?.id ?? null));
      }
    });

    // Se for edição, busca a tarefa e preenche os campos
    if (this.id()) {
      this.tarefaService.buscar(Number(this.id())).subscribe({
        next: (t) => {
          this.titulo.set(t.titulo);
          this.descricao.set(t.descricao ?? '');
          this.dataEntrega.set(t.dataEntrega ?? '');
          this.listaId.set(t.listaId);
          this.prioridade.set(t.prioridade);
          this.lembrete.set(t.lembrete);
          this.subtarefas.set(t.subtarefas);
        },
        error: (e: HttpErrorResponse) => this.erro.set(mensagemDeErro(e)),
      });
    }
  }

  // ----- Subtarefas (ficam só na tela até clicar em "Salvar tarefa") -----

  adicionarSubtarefa() {
    const titulo = this.textoSubtarefa().trim();
    if (titulo) {
      this.subtarefas.update((lista) => [...lista, { titulo, concluida: false }]);
    }
    this.textoSubtarefa.set('');
    this.adicionandoSubtarefa.set(false);
  }

  marcarSubtarefa(indice: number) {
    this.subtarefas.update((lista) =>
      lista.map((s, i) => (i === indice ? { ...s, concluida: !s.concluida } : s)),
    );
  }

  removerSubtarefa(indice: number) {
    this.subtarefas.update((lista) => lista.filter((_, i) => i !== indice));
  }

  // ----- Salvar / excluir -----

  salvar() {
    this.erro.set('');
    this.salvando.set(true);

    const dados = {
      titulo: this.titulo(),
      descricao: this.descricao() || null,
      dataEntrega: this.dataEntrega() || null,
      listaId: this.listaId(),
      prioridade: this.prioridade(),
      lembrete: this.lembrete(),
      subtarefas: this.subtarefas(),
    };

    const pedido = this.id()
      ? this.tarefaService.editar(Number(this.id()), dados)
      : this.tarefaService.criar(dados);

    pedido.subscribe({
      next: () => this.router.navigate(['/tarefas']),
      error: (e: HttpErrorResponse) => {
        this.erro.set(mensagemDeErro(e));
        this.salvando.set(false);
      },
    });
  }

  excluir() {
    if (!confirm('Excluir esta tarefa?')) return;
    this.tarefaService.excluir(Number(this.id())).subscribe(() => this.router.navigate(['/tarefas']));
  }
}
