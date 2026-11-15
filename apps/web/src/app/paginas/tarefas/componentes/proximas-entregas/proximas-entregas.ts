import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { PRIORIDADES, Resumo } from '../../../../servicos/modelos';
import { abreviarMes, lerData } from '../../../../servicos/datas';

/**
 * Cartão "Próximas entregas": as 3 tarefas pendentes com data mais próxima.
 */
@Component({
  selector: 'app-proximas-entregas',
  imports: [RouterLink],
  templateUrl: './proximas-entregas.html',
  styleUrl: './proximas-entregas.scss',
})
export class ProximasEntregas {
  entregas = input<Resumo['proximasEntregas']>([]);

  // Cores dos quadradinhos de data, em ordem (lavanda, rosa, menta)
  cores = [
    { fundo: '#EEE8FF', texto: '#5B3DB8' },
    { fundo: '#FFE3EC', texto: '#A3264F' },
    { fundo: '#DDF4E8', texto: '#1E7049' },
  ];

  dia(iso: string) {
    return lerData(iso).getDate();
  }

  mes(iso: string) {
    return abreviarMes(lerData(iso).getMonth());
  }

  prioridade(p: keyof typeof PRIORIDADES) {
    return PRIORIDADES[p].nome.toLowerCase();
  }
}
