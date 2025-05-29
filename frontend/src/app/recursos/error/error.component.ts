import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { ErrorService } from './error.service';

@Component({
  selector: 'app-error',
  standalone: false,
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.css']
})
export class ErrorComponent implements OnInit {
  errorCode: string = '404';
  errorMessage: string = 'Lo sentimos, la página que buscas no existe';
  errorTitle: string = 'Página no encontrada';
  errorIcon: string = 'info'; // Valor predeterminado: info, warning, error, lock, search, connection

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private errorService: ErrorService
  ) {}

  ngOnInit(): void {
    // Obtenemos los parámetros de error si existen
    this.route.queryParams.subscribe(params => {
      if (params['code']) {
        this.errorCode = params['code'];
        this.updateErrorIcon();
      }
      
      if (params['message']) {
        this.errorMessage = params['message'];
      }

      if (params['title']) {
        this.errorTitle = params['title'];
      }
    });
  }

  /**
   * Actualiza el icono según el código de error
   */
  updateErrorIcon(): void {
    // Si es un error de conexión (código CONN_ERROR o 0)
    if (this.errorCode === 'CONN_ERROR' || this.errorCode === '0') {
      this.errorIcon = 'connection';
      return;
    }
    
    // Para los demás errores, convertimos a número si es posible
    const code = parseInt(this.errorCode, 10);
    
    // No es un número, usamos ícono genérico
    if (isNaN(code)) {
      this.errorIcon = 'info';
      return;
    }
    
    if (code >= 500) {
      this.errorIcon = 'error';
    } else if (code >= 400 && code < 500) {
      if (code === 401 || code === 403) {
        this.errorIcon = 'lock';
      } else if (code === 404) {
        this.errorIcon = 'search';
      } else {
        this.errorIcon = 'warning';
      }
    } else {
      this.errorIcon = 'info';
    }
  }

  volverAlInicio() {
    this.router.navigate(['/']);
  }

  explorarServicios() {
    this.router.navigate(['/servicios']);
  }
}
