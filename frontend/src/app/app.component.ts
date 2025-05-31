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
  private isNavigating = false;

  constructor(private router: Router) {}

  ngOnInit() {
    this.setupViewTransitions();
  }

  /**
   * Configura las transiciones visuales para la navegación
   */
  private setupViewTransitions() {
    // Verificar si el navegador soporta View Transitions API
    if (!('startViewTransition' in document)) {
      console.log('View Transitions API no está soportada en este navegador');
      return;
    }

    // Interceptar el inicio de navegación
    this.router.events.pipe(
      filter(event => event instanceof NavigationStart)
    ).subscribe((event: NavigationStart) => {
      if (!this.isNavigating) {
        this.isNavigating = true;
        this.startViewTransition(event.url);
      }
    });

    // Detectar fin de navegación para limpiar estado
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      // Pequeño delay para permitir que la nueva página se renderice
      setTimeout(() => {
        this.isNavigating = false;
        this.prepareNewPageElements();
      }, 100);
    });
  }

  /**
   * Inicia la transición visual
   */
  private startViewTransition(targetUrl: string) {
    // Preparar elementos de la página actual para la transición
    this.prepareCurrentPageElements();
    
    // Iniciar la transición
    if (document.startViewTransition) {
      document.startViewTransition(() => {
        // La navegación ya está en progreso, solo necesitamos resolver la promesa
        return Promise.resolve();
      });
    }
  }

  /**
   * Prepara los elementos de la página actual para la transición
   */
  private prepareCurrentPageElements() {
    // Asignar view-transition-name a elementos clave de la página actual
    const dashboardContainer = document.querySelector('.dashboard-container');
    const statsCards = document.querySelectorAll('.stat-card');
    const tableContainer = document.querySelector('.table-container');
    const navbarAdmin = document.querySelector('.dashboard-header');
    const entityTabs = document.querySelector('.entity-tabs');

    if (dashboardContainer) {
      dashboardContainer.setAttribute('style', 'view-transition-name: dashboard-container');
    }

    if (navbarAdmin) {
      navbarAdmin.setAttribute('style', 'view-transition-name: navbar-admin');
    }

    if (entityTabs) {
      entityTabs.setAttribute('style', 'view-transition-name: entity-tabs');
    }

    // Asignar nombres únicos a las tarjetas de estadísticas
    statsCards.forEach((card, index) => {
      card.setAttribute('style', `view-transition-name: stat-card-${index}`);
    });

    if (tableContainer) {
      tableContainer.setAttribute('style', 'view-transition-name: table-container');
    }

    // Preparar elementos con data-view-id
    this.prepareViewTransitionElements();
  }

  /**
   * Prepara los elementos de la nueva página después de la navegación
   */
  private prepareNewPageElements() {
    // Limpiar los estilos de transición de la página anterior
    const elementsWithTransition = document.querySelectorAll('[style*="view-transition-name"]');
    elementsWithTransition.forEach(el => {
      const element = el as HTMLElement;
      const style = element.getAttribute('style') || '';
      const cleanedStyle = style.replace(/view-transition-name:[^;]+;?/g, '').trim();
      if (cleanedStyle) {
        element.setAttribute('style', cleanedStyle);
      } else {
        element.removeAttribute('style');
      }
    });

    // Preparar elementos de la nueva página
    setTimeout(() => {
      this.prepareCurrentPageElements();
    }, 50);
  }

  /**
   * Prepara los elementos con data-view-id para las transiciones
   */
  private prepareViewTransitionElements() {
    const elements = document.querySelectorAll('[data-view-id]');
    
    elements.forEach(el => {
      const viewId = el.getAttribute('data-view-id');
      if (viewId) {
        // Limpiar caracteres especiales para crear un nombre válido de CSS
        const safeName = viewId.replace(/[^a-zA-Z0-9-_]/g, '-');
        (el as HTMLElement).style.setProperty('view-transition-name', safeName);
      }
    });
  }
}
