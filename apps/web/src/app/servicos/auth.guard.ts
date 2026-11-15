import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';

/**
 * Guard = "porteiro" das rotas. Só deixa entrar nas telas de tarefas quem está logado.
 */
export const authGuard: CanActivateFn = () => {
  const logado = inject(AuthService).usuario() !== null;
  return logado ? true : inject(Router).createUrlTree(['/login']);
};
