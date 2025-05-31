import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar-admin',
  standalone: false,
  templateUrl: './navbar-admin.component.html',
  styleUrl: './navbar-admin.component.css'
})
export class NavbarAdminComponent implements OnInit {
  // Propiedades para la búsqueda
  @Output() busquedaChange = new EventEmitter<string>();
  @Output() exportarUsuariosEvent = new EventEmitter<void>();
  @Output() exportarEvent = new EventEmitter<void>();
  termino: string = '';

  constructor(public router: Router) { }

  ngOnInit(): void {
    // No es necesario asignar this.router = this.router;
  }
  
  /**
   * Navega a una ruta específica con transición visual si está disponible
   * @param ruta La ruta a la que navegar
   */
  navegarConTransicion(ruta: string): void {
    // Prevenir navegación si ya estamos en la misma ruta
    if (this.router.url === ruta) {
      return;
    }

    // Verificar si el navegador soporta View Transitions API
    if ('startViewTransition' in document && document.startViewTransition) {
      // Preparar elementos para la transición
      this.prepararElementosParaTransicion();
      
      // Iniciar transición y navegar
      document.startViewTransition(() => {
        this.router.navigateByUrl(ruta);
        return Promise.resolve();
      });
    } else {
      // Fallback para navegadores sin soporte
      this.router.navigateByUrl(ruta);
    }
  }

  /**
   * Prepara los elementos actuales para una transición suave
   */
  private prepararElementosParaTransicion(): void {
    // Agregar atributos de transición a elementos clave
    const navbar = document.querySelector('.dashboard-header');
    const tabs = document.querySelector('.entity-tabs');
    const container = document.querySelector('.dashboard-container');
    
    if (navbar) {
      navbar.setAttribute('data-view-transition', 'navbar');
    }
    
    if (tabs) {
      tabs.setAttribute('data-view-transition', 'tabs');
    }
    
    if (container) {
      container.setAttribute('data-view-transition', 'container');
    }
  }

  /**
   * Devuelve el título correspondiente a la página actual
   */
  getTituloPagina(): string {
    const url = this.router.url;
    switch (true) {
      case url.includes('/admin/usuarios'):
        return 'Gestión de Usuarios';
      case url.includes('/admin/anuncios'):
        return 'Gestión de Anuncios';
      case url.includes('/admin/categorias'):
        return 'Gestión de Categorías';
      case url.includes('/admin/impresoras'):
        return 'Gestión de Impresoras';
      case url.includes('/admin/pedidos'):
        return 'Gestión de Pedidos';
      case url.includes('/admin/materiales'):
        return 'Gestión de Materiales';
      case url.includes('/admin/ubicaciones'):
        return 'Gestión de Ubicaciones';
      default:
        return 'Panel de Administración';
    }
  }
  
  /**
   * Devuelve el placeholder adecuado para el campo de búsqueda según la página actual
   */
  getPlaceholderBusqueda(): string {
    const url = this.router.url;
    switch (true) {
      case url.includes('/admin/usuarios'):
        return 'Buscar usuarios por nombre, email o rol...';
      case url.includes('/admin/anuncios'):
        return 'Buscar anuncios por título, descripción o precio...';
      case url.includes('/admin/categorias'):
        return 'Buscar categorías por nombre o descripción...';
      case url.includes('/admin/impresoras'):
        return 'Buscar impresoras por modelo o marca...';
      case url.includes('/admin/pedidos'):
        return 'Buscar pedidos por número, cliente o estado...';
      case url.includes('/admin/materiales'):
        return 'Buscar materiales por nombre o propiedades...';
      case url.includes('/admin/ubicaciones'):
        return 'Buscar ubicaciones por nombre o provincia...';
      default:
        return 'Buscar...';
    }
  }

  /**
   * Filtra los elementos según el término de búsqueda
   */
  filtrarUsuarios(): void {
    this.busquedaChange.emit(this.termino);
  }

  /**
   * Exporta los datos actuales
   */
  exportarUsuarios(): void {
    this.exportarEvent.emit();
  }
}
