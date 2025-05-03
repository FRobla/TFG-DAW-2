import { Component, OnInit, ElementRef, ViewChild, AfterViewInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar-busqueda',
  standalone: false,
  templateUrl: './navbar-busqueda.component.html',
  styleUrls: ['./navbar-busqueda.component.css']
})
export class NavbarBusquedaComponent implements AfterViewInit {
  // Variable para la búsqueda
  busquedaTexto = "";
  
  // Referencias a elementos del DOM
  @ViewChild('navbarToggler') navbarToggler!: ElementRef;
  @ViewChild('navbarCollapse') navbarCollapse!: ElementRef;
  
  // Propiedad para rastrear el estado del menú
  private isMenuOpen = false;

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
    // Cerrar menú desplegable si está abierto
    this.cerrarMenuSiAbierto();
  }

  /**
   * Después de inicializar la vista, configuramos el comportamiento del botón toggle
   */
  ngAfterViewInit(): void {
    // Agregamos un listener al documento para cerrar el menú al hacer clic fuera
    document.addEventListener('click', (event: Event) => {
      const clickedElement = event.target as HTMLElement;
      const isNavbarToggler = this.navbarToggler?.nativeElement.contains(clickedElement);
      const isNavbarCollapse = this.navbarCollapse?.nativeElement.contains(clickedElement);
      
      // Si el menú está abierto y se hace clic fuera del menú y del toggler
      if (this.isMenuOpen && !isNavbarToggler && !isNavbarCollapse) {
        this.cerrarMenu();
      }
    });

    // Si existe el toggler, agregamos listener para gestionar el menú
    if (this.navbarToggler?.nativeElement) {
      this.navbarToggler.nativeElement.addEventListener('click', () => {
        this.toggleMenu();
      });
    }
  }

  /**
   * Alterna el estado del menú (abrir/cerrar)
   */
  toggleMenu(): void {
    if (this.isMenuOpen) {
      this.cerrarMenu();
    } else {
      this.abrirMenu();
    }
  }

  /**
   * Abre el menú desplegable
   */
  abrirMenu(): void {
    if (this.navbarCollapse?.nativeElement) {
      this.navbarCollapse.nativeElement.classList.add('show');
      this.isMenuOpen = true;
    }
  }

  /**
   * Cierra el menú desplegable
   */
  cerrarMenu(): void {
    if (this.navbarCollapse?.nativeElement) {
      this.navbarCollapse.nativeElement.classList.remove('show');
      this.isMenuOpen = false;
    }
  }

  /**
   * Cierra el menú si está abierto
   */
  cerrarMenuSiAbierto(): void {
    if (this.isMenuOpen) {
      this.cerrarMenu();
    }
  }
}

