import { Component, OnInit, HostListener } from '@angular/core';

@Component({
  selector: 'app-landing',
  standalone: false,
  templateUrl: './landing.component.html',
  styleUrl: './landing.component.css'
})
export class LandingComponent implements OnInit {
  
  isScrolled = false;
  constructor() { }

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
}
