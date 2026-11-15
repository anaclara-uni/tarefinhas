import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { AuthService } from '../../servicos/auth.service';
import { TarefaService } from '../../servicos/tarefa.service';
import { ListaService } from '../../servicos/lista.service';
import { mensagemDeErro } from '../../servicos/usuario.interceptor';
import { Lista, Ordem, PRIORIDADES, Resumo, Tarefa, Visao } from '../../servicos/modelos';
import { dataPorExtenso, hojeIso, rotuloData } from '../../servicos/datas';
import { BarraLateral } from './componentes/barra-lateral/barra-lateral';
import { ItemTarefa } from './componentes/item-tarefa/item-tarefa';
import { CartaoProgresso, mensagemDeIncentivo } from './componentes/cartao-progresso/cartao-progresso';
import { Calendario } from './componentes/calendario/calendario';
import { ProximasEntregas } from './componentes/proximas-entregas/proximas-entregas';
import { Notificacoes } from './componentes/notificacoes/notificacoes';
import { ListasMenu } from './componentes/listas-menu/listas-menu';

type Status = 'todas' | 'pendentes' | 'concluidas';

/**
 * Tela principal "Minhas tarefas".
 * No computador mostra o layout com menu lateral; no celular, o layout de celular do design.
 */
@Component({
  selector: 'app-tarefas',
  imports: [FormsModule, RouterLink, BarraLateral, ItemTarefa, CartaoProgresso, Calendario,
    ProximasEntregas, Notificacoes, ListasMenu],
  templateUrl: './tarefas.html',
  styleUrl: './tarefas.scss',
})
export class TarefasPagina {
  private auth = inject(AuthService);
  private tarefaService = inject(TarefaService);
  private listaService = inject(ListaService);
  private router = inject(Router);

  usuario = computed(() => this.auth.usuario()!);
  iniciais = computed(() => this.usuario().nome.trim().slice(0, 2).toUpperCase());
  hoje = dataPorExtenso(new Date());

  // ----- Dados que vêm da API -----
  tarefas = signal<Tarefa[]>([]);
  listas = signal<Lista[]>([]);
  resumo = signal<Resumo | null>(null);
  erro = signal('');

  // ----- Filtros escolhidos na tela -----
  visao = signal<Visao>('todas');             // menu lateral
  listaId = signal<number | null>(null);      // lista clicada no menu
  dia = signal<string | null>(null);          // dia clicado no calendário
  busca = signal('');                         // campo "Buscar tarefas..."
  ordem = signal<Ordem>('data');              // "Ordenar por"
  status = signal<Status>('todas');           // botões Todas / Pendentes / Concluídas
  mesCalendario = signal(new Date(new Date().getFullYear(), new Date().getMonth(), 1));

  novaTarefa = signal('');                    // campo "Adicionar uma tarefa..."
  menuOrdemAberto = signal(false);
  gavetaListasAberta = signal(false);         // celular: gaveta "Minhas listas"
  private esperaBusca?: ReturnType<typeof setTimeout>;

  // Opções do "Ordenar por"
  ordens: { id: Ordem; nome: string }[] = [
    { id: 'data', nome: 'data de entrega' },
    { id: 'prioridade', nome: 'prioridade' },
    { id: 'titulo', nome: 'nome (A–Z)' },
    { id: 'recentes', nome: 'mais recentes' },
  ];
  nomeOrdem = computed(() => this.ordens.find((o) => o.id === this.ordem())!.nome);

  // Visão marcada no menu (nenhuma quando um dia do calendário está escolhido)
  visaoMarcada = computed(() => (this.dia() ? null : this.visao()));
  listaSelecionada = computed(() => this.listas().find((l) => l.id === this.listaId()) ?? null);
  rotuloDia = computed(() => (this.dia() ? rotuloData(this.dia()!) : ''));

  // Tarefas depois do filtro Todas/Pendentes/Concluídas
  tarefasVisiveis = computed(() => {
    const status = this.status();
    return this.tarefas().filter((t) =>
      status === 'pendentes' ? !t.concluida : status === 'concluidas' ? t.concluida : true,
    );
  });

  // Botões de filtro com as quantidades
  filtros = computed(() => {
    const todas = this.tarefas();
    const feitas = todas.filter((t) => t.concluida).length;
    return [
      { id: 'todas' as Status, nome: 'Todas', quantidade: todas.length },
      { id: 'pendentes' as Status, nome: 'Pendentes', quantidade: todas.length - feitas },
      { id: 'concluidas' as Status, nome: 'Concluídas', quantidade: feitas },
    ];
  });

  // Para o anel de progresso do celular
  porcentagem = computed(() => {
    const c = this.resumo()?.contagens;
    return c && c.total ? Math.round((c.concluidas / c.total) * 100) : 0;
  });
  incentivo = computed(() => mensagemDeIncentivo(this.porcentagem()));

  constructor() {
    this.auth.recarregar(); // atualiza nome e papel de quem está logado
    this.carregarTudo();
  }

  // ================= Carregar dados =================

  carregarTudo() {
    this.carregarTarefas();
    this.carregarListas();
    this.carregarResumo();
  }

  carregarTarefas() {
    this.tarefaService
      .listar({
        visao: this.visao(),
        listaId: this.listaId(),
        busca: this.busca(),
        data: this.dia(),
        ordem: this.ordem(),
      })
      .subscribe({
        next: (tarefas) => {
          this.tarefas.set(tarefas);
          this.erro.set('');
        },
        error: (e: HttpErrorResponse) => this.erro.set(mensagemDeErro(e)),
      });
  }

  carregarListas() {
    this.listaService.listar().subscribe((listas) => this.listas.set(listas));
  }

  carregarResumo() {
    this.tarefaService.resumo(this.mesCalendario()).subscribe((resumo) => this.resumo.set(resumo));
  }

  // ================= Filtros =================

  escolherVisao(visao: Visao) {
    this.visao.set(visao);
    this.listaId.set(null);
    this.dia.set(null);
    this.carregarTarefas();
  }

  escolherLista(id: number) {
    this.listaId.set(id);
    this.visao.set('todas');
    this.dia.set(null);
    this.gavetaListasAberta.set(false);
    this.carregarTarefas();
  }

  // Dia clicado no calendário (null = desmarcou)
  escolherDia(dia: string | null) {
    this.dia.set(dia);
    this.visao.set('todas');
    this.listaId.set(null);
    this.carregarTarefas();
  }

  escolherOrdem(ordem: Ordem) {
    this.ordem.set(ordem);
    this.menuOrdemAberto.set(false);
    this.carregarTarefas();
  }

  // Espera a pessoa parar de digitar (300ms) antes de buscar na API
  buscar(texto: string) {
    this.busca.set(texto);
    clearTimeout(this.esperaBusca);
    this.esperaBusca = setTimeout(() => this.carregarTarefas(), 300);
  }

  trocarMes(quantos: number) {
    const atual = this.mesCalendario();
    this.mesCalendario.set(new Date(atual.getFullYear(), atual.getMonth() + quantos, 1));
    this.carregarResumo();
  }

  // ================= Ações nas tarefas =================

  // Adicionar rápido: só o título. Vai para a lista selecionada (ou a primeira),
  // para o dia escolhido no calendário (ou hoje), prioridade média.
  adicionar() {
    const titulo = this.novaTarefa().trim();
    if (!titulo) return;

    this.tarefaService
      .criar({
        titulo,
        descricao: null,
        dataEntrega: this.dia() ?? hojeIso(),
        listaId: this.listaId() ?? this.listas()[0]?.id ?? null,
        prioridade: 'MEDIA',
        lembrete: false,
        subtarefas: [],
      })
      .subscribe({
        next: () => {
          this.novaTarefa.set('');
          if (this.status() === 'concluidas') this.status.set('todas');
          this.carregarTudo();
        },
        error: (e: HttpErrorResponse) => alert(mensagemDeErro(e)),
      });
  }

  concluir(tarefa: Tarefa) {
    this.tarefaService.alternarConcluida(tarefa.id).subscribe(() => this.carregarTudo());
  }

  favoritar(tarefa: Tarefa) {
    this.tarefaService.alternarFavorita(tarefa.id).subscribe(() => this.carregarTarefas());
  }

  excluir(tarefa: Tarefa) {
    if (!confirm(`Excluir a tarefa "${tarefa.titulo}"?`)) return;
    this.tarefaService.excluir(tarefa.id).subscribe(() => this.carregarTudo());
  }

  novaTarefaCompleta() {
    this.router.navigate(['/tarefas/nova'], { queryParams: this.listaId() ? { lista: this.listaId() } : {} });
  }

  // Ajudantes usados no layout de celular
  prioridade(t: Tarefa) {
    return PRIORIDADES[t.prioridade];
  }

  dataCurta(t: Tarefa) {
    const rotulo = t.dataEntrega ? rotuloData(t.dataEntrega) : null;
    return rotulo === 'Hoje' ? null : rotulo; // "Hoje" não aparece no cartão do celular
  }
}
