import { Component, input, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Notificacao } from '../../../../servicos/modelos';
import { rotuloData } from '../../../../servicos/datas';

/**
 * Botão do sino + a caixinha de notificações que abre ao clicar.
 * Mostra tarefas atrasadas, que vencem hoje e lembretes (vencem amanhã com "Me lembrar" ligado).
 * Os dados vêm da API em /api/resumo.
 */
@Component({
  selector: 'app-notificacoes',
  imports: [RouterLink],
  templateUrl: './notificacoes.html',
  styleUrl: './notificacoes.scss',
  host: { '(document:keydown.escape)': 'aberto.set(false)' }, // Esc fecha a caixinha
})
export class Notificacoes {
  itens = input<Notificacao[]>([]);
  aberto = signal(false);

  // Cor e texto de cada tipo de notificação
  estilos = {
    ATRASADA: { fundo: '#FFE3EC', texto: '#A3264F', rotulo: 'Atrasada' },
    HOJE: { fundo: '#FFE8D6', texto: '#9A4A10', rotulo: 'Vence hoje' },
    LEMBRETE: { fundo: '#EEE8FF', texto: '#5B3DB8', rotulo: 'Lembrete · vence amanhã' },
  };

  descricao(n: Notificacao): string {
    // Para atrasadas, mostramos também a data ("Atrasada · 12 nov")
    return n.tipo === 'ATRASADA'
      ? `${this.estilos.ATRASADA.rotulo} · ${rotuloData(n.dataEntrega)}`
      : this.estilos[n.tipo].rotulo;
  }
}
