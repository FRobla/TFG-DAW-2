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
  termino: string = '';

  constructor(public router: Router) { }

  ngOnInit(): void {
    this.router = this.router;
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
  }
}
