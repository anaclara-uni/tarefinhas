import { Component, computed, input, output, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Lista, Resumo, Usuario, Visao } from '../../../../servicos/modelos';
import { ListasMenu } from '../listas-menu/listas-menu';

// Dicas do cartão "Dica do dia"
const DICAS = [
  'Divida tarefas grandes em passinhos menores. Cada check conta!',
  'Comece pela tarefa mais difícil enquanto a energia está alta.',
  'Anote tudo o que precisa fazer, assim a cabeça fica mais leve.',
  'Use as listas para separar faculdade, estágio e vida pessoal.',
  'Marque com estrela o que é mais importante da semana.',
  'Uma pausa de 5 minutos a cada 25 de estudo ajuda a focar.',
  'Ligue o "Me lembrar" nas entregas importantes.',
  'Revise suas tarefas no começo do dia e escolha 3 prioridades.',
  'Terminou algo? Comemore! Pequenas vitórias motivam.',
  'Deixe para amanhã só o que realmente pode esperar.',
];

/**
 * Menu da esquerda: logo, botão "Nova tarefa", visões, listas, dica do dia e usuário.
 */
@Component({
  selector: 'app-barra-lateral',
  imports: [RouterLink, ListasMenu],
  templateUrl: './barra-lateral.html',
  styleUrl: './barra-lateral.scss',
})
export class BarraLateral {
  // Dados que a tela de tarefas manda para cá
  usuario = input.required<Usuario>();
  listas = input<Lista[]>([]);
  contagens = input<Resumo['contagens'] | null>(null);
  visao = input<Visao | null>('todas'); // null = nenhuma visão marcada (ex: dia do calendário)
  listaId = input<number | null>(null);

  // Avisos que mandamos de volta para a tela de tarefas
  escolherVisao = output<Visao>();
  escolherLista = output<number>();
  listasMudaram = output<void>();

  // As 4 visões do menu
  visoes: { id: Visao; nome: string }[] = [
    { id: 'hoje', nome: 'Hoje' },
    { id: 'semana', nome: 'Próximos 7 dias' },
    { id: 'todas', nome: 'Todas as tarefas' },
    { id: 'concluidas', nome: 'Concluídas' },
  ];

  // Iniciais do nome para o avatar ("Maria" -> "MA")
  iniciais = computed(() => this.usuario().nome.trim().slice(0, 2).toUpperCase());

  // Dica do dia: cada dia do ano mostra uma dica diferente; clicar mostra a próxima
  indiceDica = signal(diaDoAno() % DICAS.length);
  dica = computed(() => DICAS[this.indiceDica()]);

  proximaDica() {
    this.indiceDica.update((i) => (i + 1) % DICAS.length);
  }

  contagemDa(visao: Visao): number {
    const c = this.contagens();
    if (!c) return 0;
    return { hoje: c.hoje, semana: c.semana, todas: c.total, concluidas: c.concluidas }[visao];
  }

  visaoAtiva(visao: Visao): boolean {
    return this.listaId() === null && this.visao() === visao;
  }
}

// Número do dia no ano (1 a 366)
function diaDoAno(): number {
  const hoje = new Date();
  return Math.floor((hoje.getTime() - new Date(hoje.getFullYear(), 0, 0).getTime()) / 86_400_000);
}
