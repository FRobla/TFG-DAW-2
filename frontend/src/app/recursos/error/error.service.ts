import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

export interface ErrorData {
  code: string;
  title: string;
  message: string;
}

@Injectable({
  providedIn: 'root'
})
export class ErrorService {
  
  // Definición de errores comunes con sus mensajes correspondientes
  private errorTypes: { [key: string]: ErrorData } = {
    '0': {
      code: 'CONN_ERROR',
      title: 'Error de conexión',
      message: 'No se pudo conectar con el servidor. Por favor, verifica que el servidor esté en funcionamiento e inténtalo de nuevo.'
    },
    '400': {
      code: '400',
      title: 'Solicitud incorrecta',
      message: 'La solicitud enviada contiene errores o datos incorrectos.'
    },
    '401': {
      code: '401',
      title: 'No autorizado',
      message: 'Necesitas iniciar sesión para acceder a este recurso.'
    },
    '403': {
      code: '403',
      title: 'Acceso prohibido',
      message: 'No tienes permiso para acceder a este recurso.'
    },
    '404': {
      code: '404',
      title: 'Página no encontrada',
      message: 'Lo sentimos, la página que buscas no existe o ha sido movida.'
    },
    '500': {
      code: '500',
      title: 'Error del servidor',
      message: 'Ha ocurrido un error interno en el servidor. Por favor, inténtalo más tarde.'
    },
    '503': {
      code: '503',
      title: 'Servicio no disponible',
      message: 'El servicio no está disponible en este momento. Por favor, inténtalo más tarde.'
    },
    'default': {
      code: 'Error',
      title: 'Ha ocurrido un error',
      message: 'Se ha producido un error inesperado. Por favor, inténtalo de nuevo.'
    }
  };

  constructor(private router: Router) { }

  /**
   * Navega a la página de error con el código específico
   * @param errorCode Código de error (400, 401, 403, 404, 500, etc.)
   * @param customMessage Mensaje personalizado opcional
   * @param customTitle Título personalizado opcional
   */
  navigateToError(errorCode: string, customMessage?: string, customTitle?: string): void {
    const errorData = this.getErrorData(errorCode);
    
    this.router.navigate(['/error'], { 
      queryParams: { 
        code: errorData.code,
        title: customTitle || errorData.title,
        message: customMessage || errorData.message
      }
    });
  }

  /**
   * Obtiene los datos de error según el código
   * @param errorCode Código de error
   * @returns Datos de error (código, título, mensaje)
   */
  getErrorData(errorCode: string): ErrorData {
    return this.errorTypes[errorCode] || this.errorTypes['default'];
  }
}
