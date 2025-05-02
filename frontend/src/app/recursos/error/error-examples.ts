/**
 * Ejemplos de uso del sistema de errores
 * Este archivo proporciona ejemplos para facilitar el manejo de errores en la aplicación
 */

import { ErrorService } from './error.service';

/**
 * Ejemplos de uso del ErrorService desde otros componentes
 */
export class ErrorExamples {

  /**
   * Ejemplo: Manejo de un error 404 - Recurso no encontrado
   * @param errorService Servicio de errores inyectado en el constructor del componente
   * @param resourceId ID del recurso no encontrado (opcional)
   */
  static handleNotFound(errorService: ErrorService, resourceId?: string): void {
    let message = 'Lo sentimos, el recurso solicitado no fue encontrado.';
    
    if (resourceId) {
      message = `El recurso con ID "${resourceId}" no existe o ha sido eliminado.`;
    }
    
    errorService.navigateToError('404', message);
  }

  /**
   * Ejemplo: Manejo de un error de conexión - Backend apagado
   * @param errorService Servicio de errores inyectado en el constructor del componente
   * @param endpoint Endpoint que se estaba intentando acceder (opcional)
   */
  static handleConnectionError(errorService: ErrorService, endpoint?: string): void {
    let message = 'No se puede conectar con el servidor. Verifica que el backend esté funcionando correctamente.';
    
    if (endpoint) {
      message = `No se pudo conectar con ${endpoint}. Verifica que el servidor esté funcionando correctamente.`;
    }
    
    errorService.navigateToError('0', message);
  }

  /**
   * Ejemplo: Manejo de un error 401 - No autorizado
   * @param errorService Servicio de errores inyectado en el constructor del componente
   */
  static handleUnauthorized(errorService: ErrorService): void {
    const message = 'Tu sesión ha expirado o no tienes acceso a este recurso. Por favor, inicia sesión de nuevo.';
    errorService.navigateToError('401', message);
  }

  /**
   * Ejemplo: Manejo de un error 403 - Prohibido
   * @param errorService Servicio de errores inyectado en el constructor del componente
   * @param resource Nombre del recurso (opcional)
   */
  static handleForbidden(errorService: ErrorService, resource?: string): void {
    let message = 'No tienes permisos suficientes para acceder a este recurso.';
    
    if (resource) {
      message = `No tienes permisos suficientes para acceder a ${resource}.`;
    }
    
    errorService.navigateToError('403', message);
  }

  /**
   * Ejemplo: Manejo de un error 500 - Error del servidor
   * @param errorService Servicio de errores inyectado en el constructor del componente
   * @param errorDetails Detalles del error (opcional)
   */
  static handleServerError(errorService: ErrorService, errorDetails?: string): void {
    let message = 'Ha ocurrido un error en el servidor. Por favor, inténtalo de nuevo más tarde.';
    
    if (errorDetails) {
      message = `Por favor, inténtalo de nuevo más tarde.`;
    }
    
    errorService.navigateToError('500', message);
  }

  /**
   * Ejemplo: Manejo de un error personalizado
   * @param errorService Servicio de errores inyectado en el constructor del componente
   * @param code Código de error personalizado
   * @param title Título personalizado
   * @param message Mensaje personalizado
   */
  static handleCustomError(errorService: ErrorService, code: string, title: string, message: string): void {
    errorService.navigateToError(code, message, title);
  }
}
