import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private router: Router) {}

  // Método para verificar si una URL es pública
  private isPublicUrl(url: string): boolean {
    // Extraer la parte de la ruta desde la URL completa
    // Ejemplo: http://localhost:8080/api/anuncios -> /api/anuncios
    const baseUrl = 'http://localhost:8080';
    let path = url;
    if (url.startsWith(baseUrl)) {
      path = url.substring(baseUrl.length);
    }
    
    // Lista de rutas públicas que no necesitan token
    const publicEndpoints = [
      '/api/login',
      '/api/registro', 
      '/api/principal',
      '/api/anuncios',
      '/api/anuncio',
      '/api/impresoras',
      '/api/categorias',
      '/api/search',
      '/api/usuario',
      '/api/usuarios',
      '/api/pedido',
      '/api/admin'
    ];
    
    // Comprobar si la URL comienza con alguno de los endpoints públicos
    return publicEndpoints.some(endpoint => path.startsWith(endpoint));
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Verificar si la URL es una ruta pública que no requiere token
    const isPublicUrl = this.isPublicUrl(request.url);
    
    // Obtener el token del localStorage
    const token = localStorage.getItem('access_token');

    // Clonar la petición y añadir el token si existe y no es una URL pública
    if (token && !isPublicUrl) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }

    // Manejar la respuesta
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        // Si el error es 401 (no autorizado)
        if (error.status === 401) {
          // Limpiar el token y redirigir al login
          localStorage.removeItem('access_token');
          localStorage.removeItem('usuario');
          this.router.navigate(['/login']);
        }
        return throwError(() => error);
      })
    );
  }
}
