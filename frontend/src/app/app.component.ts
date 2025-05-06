import { Component, OnInit } from '@angular/core';
import { Router, NavigationStart, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: false,
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  title = 'TFG-Frontend';

  constructor(private router: Router) {}

  ngOnInit() {
    this.setupViewTransitions();
  }

  /**
   * Configura las transiciones visuales para la navegación
   */
  private setupViewTransitions() {
    // Verificar si el navegador soporta View Transitions API
    if (!document.startViewTransition) {
      console.log('View Transitions API no está soportada en este navegador');
      return;
    }

    this.router.events.pipe(
      filter(event => event instanceof NavigationStart)
    ).subscribe(() => {
      // Antes de iniciar la navegación, configurar variables CSS para los elementos con data-view-id
      this.prepareViewTransitionElements();
      
      // Iniciar transición cuando comienza la navegación
      document.documentElement.classList.add('view-transition-active');
    });

    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      // Crear transición suave con más tiempo para las animaciones
      document.startViewTransition?.(() => {
        // Configurar nuevos elementos tras la navegación
        setTimeout(() => {
          this.prepareViewTransitionElements();
          // Remover la clase cuando termina la navegación
          document.documentElement.classList.remove('view-transition-active');
        }, 50);
        return Promise.resolve();
      });
    });
  }

  /**
   * Prepara los elementos con data-view-id para las transiciones
   * Establece variables CSS necesarias para las transiciones
   */
  private prepareViewTransitionElements() {
    // Seleccionar todos los elementos con atributo data-view-id
    const elements = document.querySelectorAll('[data-view-id]');
    
    // Para cada elemento, establecer la variable CSS personalizada con su ID
    elements.forEach(el => {
      const viewId = el.getAttribute('data-view-id');
      if (viewId) {
        (el as HTMLElement).style.setProperty('--view-id', viewId);
      }
    });
  }
}
