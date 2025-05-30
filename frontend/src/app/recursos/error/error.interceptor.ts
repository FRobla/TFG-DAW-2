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
import { ErrorService } from './error.service';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

  constructor(private errorService: ErrorService) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        let errorMessage = '';
        
        if (error.error instanceof ErrorEvent) {
          // Error del lado del cliente
          errorMessage = `Error: ${error.error.message}`;
          this.errorService.navigateToError('default', errorMessage);
        } else {
          // Error del lado del servidor o de conexión
          const statusCode = error.status.toString();
          
          switch (error.status) {
            case 0:
              // Error de conexión - backend está apagado o no responde
              const connectionMessage = `No se puede conectar con el servidor.`;
              this.errorService.navigateToError('0', connectionMessage);
              break;
            case 401:
              // Verificar si el error viene del endpoint de login
              if (request.url.includes('/api/login')) {
                // Para errores de login, no redirigir automáticamente
                // Dejar que el componente de login maneje el error
                break;
              } else {
                // Para otros errores 401, redirigir a la página de error
                this.errorService.navigateToError('401');
              }
              break;
            case 403:
            case 404:
            case 500:
              // Para estos errores comunes, usamos el código tal cual
              this.errorService.navigateToError(statusCode);
              break;
            default:
              // Para otros errores, podríamos personalizar el mensaje
              errorMessage = error.message || 'Error desconocido';
              this.errorService.navigateToError(statusCode, errorMessage);
          }
        }
        
        // Devolvemos el error original para que otros interceptores o servicios puedan manejarlo
        return throwError(() => error);
      })
    );
  }
}
