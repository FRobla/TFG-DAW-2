import { Component, OnInit, HostListener } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-landing',
  standalone: false,
  templateUrl: './landing.component.html',
  styleUrl: './landing.component.css'
})
export class LandingComponent implements OnInit {
  
  isScrolled = false;
  constructor(private router: Router) { }

  @HostListener('window:scroll', [])
  onWindowScroll() {
    // Agregar clase al navbar cuando se hace scroll
    this.isScrolled = window.scrollY > 50;
    const navbar = document.querySelector('.navbar') as HTMLElement;
    if (navbar) {
      if (this.isScrolled) {
        navbar.classList.add('scrolled');
      } else {
        navbar.classList.remove('scrolled');
      }
    }
  }

  ngOnInit(): void {
    // Inicialización de componente
    this.setupCalculatorEvents();
    this.setupSmoothScroll();
  }

  setupSmoothScroll(): void {
    // Scroll suave para enlaces del navbar
    const navLinks = document.querySelectorAll('.nav-link');
    navLinks.forEach(link => {
      link.addEventListener('click', (e: Event) => {
        e.preventDefault();
        const target = link.getAttribute('href');
        
        // Comprobar si el enlace es a otra página (comienza con '/')
        if (target && target.startsWith('/')) {
          this.navegarConTransicion(target);
          return;
        }
        
        // Para enlaces dentro de la misma página
        if (target && target !== '#') {
          const targetElement = document.querySelector(target);
          if (targetElement) {
            const offsetTop = targetElement.getBoundingClientRect().top + window.pageYOffset;
            window.scrollTo({
              top: offsetTop,
              behavior: 'smooth'
            });

            // Actualizar clase active en elementos del menú
            navLinks.forEach(navLink => navLink.parentElement?.classList.remove('active'));
            link.parentElement?.classList.add('active');
          }
        }
      });
    });
  }

  setupCalculatorEvents(): void {
    // Esta función se encargará en el futuro de manejar los eventos de la calculadora
    // Por ahora se deja preparada para implementaciones futuras
  }
  
  /**
   * Navega a otra página con transición visual fluida
   * @param ruta Ruta de destino
   */
  navegarConTransicion(ruta: string): void {
    // Verificar si el navegador soporta View Transitions API
    if (document.startViewTransition) {
      // Aplicar transición visual
      document.startViewTransition(() => {
        // Preparar elementos para la transición
        const hero = document.querySelector('.hero-section');
        const cards = document.querySelectorAll('.feature-card');
        
        // Añadir atributos para mejorar las transiciones
        if (hero) hero.setAttribute('data-view-transition', 'header');
        cards.forEach((card, index) => {
          card.setAttribute('data-view-transition', 'card');
          card.setAttribute('data-view-id', `landing-card-${index}`);
        });
        
        // Navegar a la ruta especificada
        this.router.navigateByUrl(ruta);
        
        return new Promise<void>(resolve => {
          setTimeout(() => {
            // Limpiar atributos después de la transición
            if (hero) hero.removeAttribute('data-view-transition');
            cards.forEach(card => {
              card.removeAttribute('data-view-transition');
              card.removeAttribute('data-view-id');
            });
            resolve();
          }, 300);
        });
      });
    } else {
      // Fallback para navegadores sin soporte
      this.router.navigateByUrl(ruta);
    }
  }
  
  /**
   * Método para navegar a la página de servicios con transición
   */
  irAServicios(): void {
    this.navegarConTransicion('/servicios');
  }
  
  /**
   * Método para navegar a la página de resultados de búsqueda con transición
   */
  irAResultados(): void {
    this.navegarConTransicion('/resultados-busqueda');
  }
}
