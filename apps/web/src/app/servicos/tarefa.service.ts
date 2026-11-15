import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { FiltrosTarefa, Resumo, Tarefa, TarefaForm } from './modelos';

/**
 * Chamadas da API de tarefas (CRUD + concluir + favoritar) e do resumo.
 */
@Injectable({ providedIn: 'root' })
export class TarefaService {
  private http = inject(HttpClient);

  // GET /api/tarefas?visao=...&listaId=...&busca=...&data=...&ordem=...
  listar(filtros: FiltrosTarefa) {
    let params = new HttpParams().set('visao', filtros.visao).set('ordem', filtros.ordem);
    if (filtros.listaId !== null) params = params.set('listaId', filtros.listaId);
    if (filtros.busca.trim()) params = params.set('busca', filtros.busca.trim());
    if (filtros.data) params = params.set('data', filtros.data);
    return this.http.get<Tarefa[]>('/api/tarefas', { params });
  }

  buscar(id: number) {
    return this.http.get<Tarefa>(`/api/tarefas/${id}`);
  }

  criar(tarefa: TarefaForm) {
    return this.http.post<Tarefa>('/api/tarefas', tarefa);
  }

  editar(id: number, tarefa: TarefaForm) {
    return this.http.put<Tarefa>(`/api/tarefas/${id}`, tarefa);
  }

  alternarConcluida(id: number) {
    return this.http.patch<Tarefa>(`/api/tarefas/${id}/concluir`, {});
  }

  alternarFavorita(id: number) {
    return this.http.patch<Tarefa>(`/api/tarefas/${id}/favoritar`, {});
  }

  excluir(id: number) {
    return this.http.delete<void>(`/api/tarefas/${id}`);
  }

  // GET /api/resumo?mes=2026-11
  resumo(mes: Date) {
    const texto = `${mes.getFullYear()}-${String(mes.getMonth() + 1).padStart(2, '0')}`;
    return this.http.get<Resumo>('/api/resumo', { params: { mes: texto } });
  }
}
