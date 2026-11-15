import { Routes } from '@angular/router';
import { authGuard } from './servicos/auth.guard';
import { LoginPagina } from './paginas/login/login';
import { TarefasPagina } from './paginas/tarefas/tarefas';
import { TarefaFormPagina } from './paginas/tarefa-form/tarefa-form';
import { PerfilPagina } from './paginas/perfil/perfil';

// Rotas = qual tela aparece para cada endereço
export const routes: Routes = [
  { path: 'login', component: LoginPagina },
  { path: 'tarefas', component: TarefasPagina, canActivate: [authGuard] },
  { path: 'tarefas/nova', component: TarefaFormPagina, canActivate: [authGuard] },
  { path: 'tarefas/:id/editar', component: TarefaFormPagina, canActivate: [authGuard] },
  { path: 'perfil', component: PerfilPagina, canActivate: [authGuard] },
  // Qualquer outro endereço vai para a lista de tarefas
  { path: '**', redirectTo: 'tarefas' },
];
