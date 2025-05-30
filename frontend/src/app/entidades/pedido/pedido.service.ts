import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, map, catchError, throwError } from 'rxjs';
import { Pedido } from './pedido';

@Injectable({
  providedIn: 'root'
})
export class PedidoService {

  constructor(private http: HttpClient, private router: Router) { }

  private urlEndPoint: string = 'http://localhost:8080/api/pedidos';
  private urlEndPointSingle: string = 'http://localhost:8080/api/pedido';

  /**
   * Obtiene todos los pedidos
   */
  getPedidos(): Observable<Pedido[]> {
    return this.http.get<any[]>(this.urlEndPoint).pipe(
      map(response => response.map(data => Pedido.fromBackend(data))),
      catchError(this.handleError)
    );
  }

  /**
   * Obtiene pedidos con paginación
   */
  getPedidosPage(page: number, size: number = 10): Observable<any> {
    return this.http.get<any>(`${this.urlEndPoint}/page/${page}/size/${size}`).pipe(
      map(response => ({
        ...response,
        content: response.content.map((data: any) => Pedido.fromBackend(data))
      })),
      catchError(this.handleError)
    );
  }

  /**
   * Obtiene un pedido por ID
   */
  getPedido(id: number): Observable<Pedido> {
    return this.http.get<any>(`${this.urlEndPointSingle}/${id}`).pipe(
      map(response => Pedido.fromBackend(response)),
      catchError(this.handleError)
    );
  }

  /**
   * Busca un pedido por número de pedido
   */
  getPedidoByNumero(numeroPedido: string): Observable<Pedido> {
    return this.http.get<any>(`${this.urlEndPointSingle}/numero/${numeroPedido}`).pipe(
      map(response => Pedido.fromBackend(response)),
      catchError(this.handleError)
    );
  }

  /**
   * Obtiene pedidos por usuario
   */
  getPedidosByUsuario(usuarioId: number): Observable<Pedido[]> {
    return this.http.get<any>(`${this.urlEndPoint}/usuario/${usuarioId}`).pipe(
      map(response => response.pedidos.map((data: any) => Pedido.fromBackend(data))),
      catchError(this.handleError)
    );
  }

  /**
   * Obtiene pedidos por usuario con paginación
   */
  getPedidosByUsuarioPage(usuarioId: number, page: number, size: number = 10): Observable<any> {
    return this.http.get<any>(`${this.urlEndPoint}/usuario/${usuarioId}/page/${page}/size/${size}`).pipe(
      map(response => ({
        ...response,
        content: response.content.map((data: any) => Pedido.fromBackend(data))
      })),
      catchError(this.handleError)
    );
  }

  /**
   * Obtiene pedidos por estado
   */
  getPedidosByEstado(estado: string): Observable<Pedido[]> {
    return this.http.get<any>(`${this.urlEndPoint}/estado/${estado}`).pipe(
      map(response => response.pedidos.map((data: any) => Pedido.fromBackend(data))),
      catchError(this.handleError)
    );
  }

  /**
   * Obtiene pedidos por estado con paginación
   */
  getPedidosByEstadoPage(estado: string, page: number, size: number = 10): Observable<any> {
    return this.http.get<any>(`${this.urlEndPoint}/estado/${estado}/page/${page}/size/${size}`).pipe(
      map(response => ({
        ...response,
        content: response.content.map((data: any) => Pedido.fromBackend(data))
      })),
      catchError(this.handleError)
    );
  }

  /**
   * Busca pedidos con filtros combinados
   */
  getPedidosWithFilters(
    usuarioId?: number,
    estado?: string,
    fechaInicio?: string,
    fechaFin?: string,
    page: number = 0,
    size: number = 10
  ): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (usuarioId) {
      params = params.set('usuarioId', usuarioId.toString());
    }
    if (estado) {
      params = params.set('estado', estado);
    }
    if (fechaInicio) {
      params = params.set('fechaInicio', fechaInicio);
    }
    if (fechaFin) {
      params = params.set('fechaFin', fechaFin);
    }

    return this.http.get<any>(`${this.urlEndPoint}/filtros`, { params }).pipe(
      map(response => ({
        ...response,
        content: response.content.map((data: any) => Pedido.fromBackend(data))
      })),
      catchError(this.handleError)
    );
  }

  /**
   * Crea un nuevo pedido
   */
  crearPedido(pedido: Pedido): Observable<Pedido> {
    return this.http.post<any>(this.urlEndPointSingle, pedido.toBackend()).pipe(
      map(response => Pedido.fromBackend(response.pedido)),
      catchError(this.handleError)
    );
  }

  /**
   * Actualiza un pedido existente
   */
  actualizarPedido(pedido: Pedido): Observable<Pedido> {
    return this.http.put<any>(`${this.urlEndPointSingle}/${pedido.id}`, pedido.toBackend()).pipe(
      map(response => Pedido.fromBackend(response.pedido)),
      catchError(this.handleError)
    );
  }

  /**
   * Actualiza el estado de un pedido
   */
  actualizarEstado(id: number, estado: string): Observable<Pedido> {
    return this.http.put<any>(`${this.urlEndPointSingle}/${id}/estado/${estado}`, {}).pipe(
      map(response => Pedido.fromBackend(response.pedido)),
      catchError(this.handleError)
    );
  }

  /**
   * Procesa el pago de un pedido
   */
  procesarPago(id: number, metodoPago: string, referenciaPago?: string): Observable<Pedido> {
    const pagoData = {
      metodoPago: metodoPago,
      referenciaPago: referenciaPago || ''
    };
    return this.http.put<any>(`${this.urlEndPointSingle}/${id}/pago`, pagoData).pipe(
      map(response => Pedido.fromBackend(response.pedido)),
      catchError(this.handleError)
    );
  }

  /**
   * Completa un pedido
   */
  completarPedido(id: number): Observable<Pedido> {
    return this.http.put<any>(`${this.urlEndPointSingle}/${id}/completar`, {}).pipe(
      map(response => Pedido.fromBackend(response.pedido)),
      catchError(this.handleError)
    );
  }

  /**
   * Cancela un pedido
   */
  cancelarPedido(id: number, motivo?: string): Observable<Pedido> {
    const cancelData = motivo ? { motivo } : {};
    return this.http.put<any>(`${this.urlEndPointSingle}/${id}/cancelar`, cancelData).pipe(
      map(response => Pedido.fromBackend(response.pedido)),
      catchError(this.handleError)
    );
  }

  /**
   * Elimina un pedido
   */
  eliminarPedido(id: number): Observable<any> {
    return this.http.delete<any>(`${this.urlEndPointSingle}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Obtiene estadísticas de pedidos por estado
   */
  getEstadisticasPorEstado(): Observable<any[]> {
    return this.http.get<any>(`${this.urlEndPoint}/estadisticas/estados`).pipe(
      map(response => response.estadisticas),
      catchError(this.handleError)
    );
  }

  /**
   * Obtiene estadísticas de ventas por mes
   */
  getVentasPorMes(): Observable<any[]> {
    return this.http.get<any>(`${this.urlEndPoint}/estadisticas/ventas-mes`).pipe(
      map(response => response.ventas),
      catchError(this.handleError)
    );
  }

  /**
   * Obtiene pedidos pendientes
   */
  getPedidosPendientes(): Observable<Pedido[]> {
    return this.http.get<any>(`${this.urlEndPoint}/pendientes`).pipe(
      map(response => response.pedidos.map((data: any) => Pedido.fromBackend(data))),
      catchError(this.handleError)
    );
  }

  /**
   * Actualiza notas del cliente
   */
  actualizarNotasCliente(id: number, notas: string): Observable<Pedido> {
    return this.http.put<any>(`${this.urlEndPointSingle}/${id}/notas-cliente`, { notas }).pipe(
      map(response => Pedido.fromBackend(response.pedido)),
      catchError(this.handleError)
    );
  }

  /**
   * Actualiza notas internas (solo admin)
   */
  actualizarNotasInternas(id: number, notas: string): Observable<Pedido> {
    return this.http.put<any>(`${this.urlEndPointSingle}/${id}/notas-internas`, { notas }).pipe(
      map(response => Pedido.fromBackend(response.pedido)),
      catchError(this.handleError)
    );
  }

  /**
   * Manejo de errores
   */
  private handleError(error: any): Observable<never> {
    console.error('Error en PedidoService:', error);
    return throwError(() => error);
  }

  /**
   * Exporta pedidos a CSV
   */
  exportarPedidos(): Observable<Blob> {
    return this.http.get(`${this.urlEndPoint}/export`, { 
      responseType: 'blob' 
    }).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Obtiene los estados disponibles para pedidos
   */
  getEstadosDisponibles(): string[] {
    return ['pendiente', 'en_proceso', 'completado', 'cancelado', 'enviado'];
  }

  /**
   * Obtiene los métodos de pago disponibles
   */
  getMetodosPagoDisponibles(): string[] {
    return ['paypal', 'tarjeta', 'transferencia', 'efectivo'];
  }
}
