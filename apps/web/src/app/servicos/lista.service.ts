import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Lista } from './modelos';

/**
 * Chamadas da API de listas (CRUD).
 */
@Injectable({ providedIn: 'root' })
export class ListaService {
  private http = inject(HttpClient);

  listar() {
    return this.http.get<Lista[]>('/api/listas');
  }

  criar(nome: string) {
    return this.http.post<Lista>('/api/listas', { nome });
  }

  editar(id: number, nome: string) {
    return this.http.put<Lista>(`/api/listas/${id}`, { nome });
  }

  excluir(id: number) {
    return this.http.delete<void>(`/api/listas/${id}`);
  }
}
