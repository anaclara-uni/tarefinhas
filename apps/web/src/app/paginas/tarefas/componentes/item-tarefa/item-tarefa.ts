import { Component, computed, input, output } from '@angular/core';
import { RouterLink } from '@angular/router';
import { PRIORIDADES, Tarefa } from '../../../../servicos/modelos';
import { rotuloData } from '../../../../servicos/datas';

/**
 * Uma linha da lista de tarefas: check, título, lista, data, prioridade, estrela e lixeira.
 */
@Component({
  selector: 'app-item-tarefa',
  imports: [RouterLink],
  templateUrl: './item-tarefa.html',
  styleUrl: './item-tarefa.scss',
})
export class ItemTarefa {
  tarefa = input.required<Tarefa>();

  concluir = output<void>();
  favoritar = output<void>();
  excluir = output<void>();

  prioridade = computed(() => PRIORIDADES[this.tarefa().prioridade]);
  data = computed(() => (this.tarefa().dataEntrega ? rotuloData(this.tarefa().dataEntrega!) : null));
  // Data de hoje fica rosa enquanto a tarefa não foi feita
  ehHoje = computed(() => this.data() === 'Hoje' && !this.tarefa().concluida);
}
