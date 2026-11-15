import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

// Componente raiz: só mostra a tela da rota atual
@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  template: '<router-outlet />',
})
export class App {}
