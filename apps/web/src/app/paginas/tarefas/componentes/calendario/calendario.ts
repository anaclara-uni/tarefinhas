import { Component, computed, input, output } from '@angular/core';
import { mesEAno, paraIso } from '../../../../servicos/datas';

/**
 * Calendário do mês. Mostra o dia de hoje em destaque e uma bolinha nos dias com tarefa.
 * Clicar em um dia mostra só as tarefas daquele dia (clicar de novo desmarca).
 */
@Component({
  selector: 'app-calendario',
  templateUrl: './calendario.html',
  styleUrl: './calendario.scss',
})
export class Calendario {
  mes = input.required<Date>();          // primeiro dia do mês mostrado
  diasComTarefa = input<number[]>([]);   // vem da API (/api/resumo)
  diaSelecionado = input<string | null>(null); // '2026-11-19' ou null

  trocarMes = output<number>();          // -1 = mês anterior, +1 = próximo
  escolherDia = output<string | null>(); // dia clicado ('2026-11-19') ou null para desmarcar

  semana = ['D', 'S', 'T', 'Q', 'Q', 'S', 'S'];
  titulo = computed(() => mesEAno(this.mes()));

  // Monta as casinhas: espaços vazios até o 1º dia cair no dia da semana certo, depois 1, 2, 3...
  casas = computed(() => {
    const ano = this.mes().getFullYear();
    const mes = this.mes().getMonth();
    const vazios = new Date(ano, mes, 1).getDay();
    const totalDias = new Date(ano, mes + 1, 0).getDate();

    const hoje = new Date();
    hoje.setHours(0, 0, 0, 0);

    const casas: ({ dia: number; iso: string; hoje: boolean; passou: boolean; temTarefa: boolean } | null)[] = [];
    for (let i = 0; i < vazios; i++) casas.push(null);
    for (let dia = 1; dia <= totalDias; dia++) {
      const data = new Date(ano, mes, dia);
      casas.push({
        dia,
        iso: paraIso(data),
        hoje: data.getTime() === hoje.getTime(),
        passou: data < hoje,
        temTarefa: this.diasComTarefa().includes(dia),
      });
    }
    return casas;
  });

  clicar(iso: string) {
    this.escolherDia.emit(this.diaSelecionado() === iso ? null : iso);
  }
}
