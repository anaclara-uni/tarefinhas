import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { AuthService } from './auth.service';

/**
 * Interceptor = código que roda em TODA chamada para a API.
 *
 * 1. Coloca o cabeçalho "X-Usuario-Id" para a API saber quem está logado.
 * 2. Se a API responder 401 (não logado), manda a pessoa para o login.
 */
export const usuarioInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);
  const usuario = auth.usuario();

  const requisicao = usuario
    ? req.clone({ setHeaders: { 'X-Usuario-Id': String(usuario.id) } })
    : req;

  return next(requisicao).pipe(
    catchError((erro: HttpErrorResponse) => {
      if (erro.status === 401 && !req.url.includes('/api/auth/')) {
        auth.sair();
      }
      return throwError(() => erro);
    }),
  );
};

// Pega a mensagem amigável que a API mandou (ou uma padrão)
export function mensagemDeErro(erro: HttpErrorResponse): string {
  if (erro.status === 0) {
    return 'Não consegui falar com o servidor. Ele está ligado?';
  }
  return erro.error?.mensagem ?? 'Algo deu errado. Tente de novo.';
}
