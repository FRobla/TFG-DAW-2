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
   * Devuelve el título correspondiente a la página actual
   */
  getTituloPagina(): string {
    const ruta = this.router.url;
    
    if (ruta.includes('/admin/usuarios')) {
      return 'Administración de Usuarios';
    } else if (ruta.includes('/admin/anuncios')) {
      return 'Administración de Anuncios';
    } else if (ruta.includes('/admin/categorias')) {
      return 'Administración de Categorías';
    } else if (ruta.includes('/admin/servicios')) {
      return 'Administración de Servicios';
    } else if (ruta.includes('/admin/pedidos')) {
      return 'Administración de Pedidos';
    } else if (ruta.includes('/admin/impresoras')) {
      return 'Administración de Impresoras';
    }
    
    return 'Panel de Administración';
  }
  
  /**
   * Devuelve el placeholder adecuado para el campo de búsqueda según la página actual
   */
  getPlaceholderBusqueda(): string {
    const ruta = this.router.url;
    
    if (ruta.includes('/admin/usuarios')) {
      return 'Buscar usuario...';
    } else if (ruta.includes('/admin/anuncios')) {
      return 'Buscar anuncio...';
    } else if (ruta.includes('/admin/categorias')) {
      return 'Buscar categoría...';
    } else if (ruta.includes('/admin/servicios')) {
      return 'Buscar servicio...';
    } else if (ruta.includes('/admin/pedidos')) {
      return 'Buscar pedido...';
    } else if (ruta.includes('/admin/impresoras')) {
      return 'Buscar impresora...';
    }
    
    return 'Buscar...';
  }

  /**
   * Filtra los usuarios según el término de búsqueda
   */
  filtrarUsuarios(): void {
    this.busquedaChange.emit(this.termino);
  }

  /**
   * Exporta los usuarios a un archivo CSV
   * Esta función emite un evento que será capturado por el componente padre
   */
  exportarUsuarios(): void {
    this.exportarUsuariosEvent.emit();
    // También emitimos el evento genérico para componentes que lo usen
    this.exportarEvent.emit();
  }
}
