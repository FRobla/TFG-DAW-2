import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar-busqueda',
  standalone: false,
  templateUrl: './navbar-busqueda.component.html',
  styleUrls: ['./navbar-busqueda.component.css']
})
export class NavbarBusquedaComponent {
  // Variable para la búsqueda
  busquedaTexto = "";

  constructor(private router: Router) {}

  /**
   * Realiza la búsqueda y navega a la página de resultados
   */
  buscar(): void {
    // No proceder si la búsqueda está vacía
    if (!this.busquedaTexto || this.busquedaTexto.trim() === '') {
      console.log("No hay texto de búsqueda especificado");
      return;
    }
    
    // Crear objeto con los parámetros de búsqueda
    const queryParams: any = {};
    queryParams.q = this.busquedaTexto.trim();
    
    // Redirigir a la página de resultados de búsqueda con el parámetro
    console.log("Redirigiendo a resultados de búsqueda con parámetros:", queryParams);
    this.router.navigate(["resultados-busqueda"], {
      queryParams: queryParams
    });
  }

  /**
   * Redirige al usuario a la página de inicio de sesión
   */
  iniciarSesion(): void {
    // Aquí se añadiría la navegación a la página de login
    console.log("Redirigiendo a página de inicio de sesión");
    this.router.navigate(["/login"]);
  }
}
