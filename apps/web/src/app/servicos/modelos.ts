// ============================================================
//  "Formatos" dos dados que vêm da API (iguais aos DTOs do Java)
// ============================================================

export type Prioridade = 'BAIXA' | 'MEDIA' | 'ALTA';

// Visões do menu lateral
export type Visao = 'todas' | 'hoje' | 'semana' | 'concluidas';

// Ordem da lista de tarefas ("Ordenar por")
export type Ordem = 'data' | 'prioridade' | 'titulo' | 'recentes';

// Filtros enviados para GET /api/tarefas
export interface FiltrosTarefa {
  visao: Visao;
  listaId: number | null;
  busca: string;
  data: string | null; // dia clicado no calendário ('2026-11-19')
  ordem: Ordem;
}

export interface Usuario {
  id: number;
  nome: string;
  email: string;
  papel: string; // texto embaixo do nome, ex: "Estudante"
}

export interface Lista {
  id: number;
  nome: string;
  cor: string;
  pendentes: number; // quantas tarefas não concluídas a lista tem
}

export interface Subtarefa {
  id?: number;
  titulo: string;
  concluida: boolean;
}

export interface Tarefa {
  id: number;
  titulo: string;
  descricao: string | null;
  dataEntrega: string | null; // formato '2026-11-19'
  prioridade: Prioridade;
  concluida: boolean;
  favorita: boolean;
  lembrete: boolean;
  listaId: number | null;
  listaNome: string | null;
  listaCor: string | null;
  subtarefas: Subtarefa[];
}

// O que enviamos para criar/editar uma tarefa
export interface TarefaForm {
  titulo: string;
  descricao: string | null;
  dataEntrega: string | null;
  listaId: number | null;
  prioridade: Prioridade;
  lembrete: boolean;
  subtarefas: Subtarefa[];
}

export interface Resumo {
  contagens: {
    total: number;
    concluidas: number;
    pendentes: number;
    hoje: number;   // pendentes para hoje
    semana: number; // pendentes nos próximos 7 dias
  };
  diasComTarefa: number[]; // dias do mês com tarefa (bolinhas do calendário)
  proximasEntregas: {
    id: number;
    titulo: string;
    dataEntrega: string;
    prioridade: Prioridade;
    listaNome: string | null;
  }[];
  notificacoes: Notificacao[]; // itens do sino
}

// Uma notificação do sino
export interface Notificacao {
  id: number;
  titulo: string;
  dataEntrega: string;
  tipo: 'ATRASADA' | 'HOJE' | 'LEMBRETE';
}

// Texto e cores de cada prioridade (iguais ao design)
export const PRIORIDADES: Record<Prioridade, { nome: string; fundo: string; texto: string; destaque: string }> = {
  BAIXA: { nome: 'Baixa', fundo: '#DDF4E8', texto: '#1E7049', destaque: '#1E7049' },
  MEDIA: { nome: 'Média', fundo: '#FFE8D6', texto: '#9A4A10', destaque: '#9A4A10' },
  ALTA: { nome: 'Alta', fundo: '#FFE3EC', texto: '#A3264F', destaque: '#C9356A' },
};
