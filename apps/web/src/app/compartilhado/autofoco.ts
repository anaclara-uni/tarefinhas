import { AfterViewInit, Directive, ElementRef, inject } from '@angular/core';

/**
 * Coloca o cursor no campo assim que ele aparece na tela.
 * Uso: <input appAutofoco>
 */
@Directive({ selector: '[appAutofoco]' })
export class Autofoco implements AfterViewInit {
  private elemento = inject(ElementRef<HTMLElement>);

  ngAfterViewInit() {
    this.elemento.nativeElement.focus();
  }
}
