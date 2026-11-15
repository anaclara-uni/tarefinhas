import { Component, computed, input } from '@angular/core';

/**
 * Cartão rosa "Seu progresso": X de Y tarefas concluídas + barrinha.
 */
@Component({
  selector: 'app-cartao-progresso',
  templateUrl: './cartao-progresso.html',
  styleUrl: './cartao-progresso.scss',
})
export class CartaoProgresso {
  concluidas = input(0);
  total = input(0);

  porcentagem = computed(() => (this.total() ? Math.round((this.concluidas() / this.total()) * 100) : 0));

  // Frase de incentivo conforme o progresso (mesmas do design)
  incentivo = computed(() => mensagemDeIncentivo(this.porcentagem()));
}

export function mensagemDeIncentivo(porcentagem: number): string {
  if (porcentagem >= 100) return 'Tudo feito! Hora de descansar.';
  if (porcentagem >= 60) return 'Quase lá, continue assim!';
  if (porcentagem > 0) return 'Bom começo, um passinho de cada vez.';
  return 'Bora começar? Você consegue!';
}
