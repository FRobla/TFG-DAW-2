import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface CarritoElemento {
  id: number;
  usuarioId: number;
  anuncioId: number;
  tituloAnuncio: string;
  descripcionAnuncio: string;
  imagenAnuncio: string;
  nombreProveedor: string;
  cantidad: number;
  materialSeleccionado: string;
  colorSeleccionado?: string;
  acabadoPremium: boolean;
  urgente: boolean;
  envioGratis: boolean;
  precioUnitario: number;
  precioTotal: number;
  fechaAgregado: string;
}

export interface CarritoResponse {
  carrito: CarritoElemento[];
  totalElementos: number;
  totalPrecio: number;
}

export interface AnadirCarritoRequest {
  usuarioId: number;
  anuncioId: number;
  cantidad: number;
  materialSeleccionado: string;
  colorSeleccionado?: string;
  acabadoPremium: boolean;
  urgente: boolean;
  envioGratis: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class CarritoService {

  private apiUrl = 'http://localhost:8080/api/carrito';

  private httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };

  constructor(private http: HttpClient) { }

  /**
   * Añade un elemento al carrito
   */
  anadirAlCarrito(request: AnadirCarritoRequest): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/anadir`, request, this.httpOptions);
  }

  /**
   * Obtiene el carrito de un usuario
   */
  obtenerCarritoUsuario(usuarioId: number): Observable<CarritoResponse> {
    return this.http.get<CarritoResponse>(`${this.apiUrl}/usuario/${usuarioId}`);
  }

  /**
   * Actualiza la cantidad de un elemento del carrito
   */
  actualizarCantidad(carritoId: number, nuevaCantidad: number): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${carritoId}/cantidad?nuevaCantidad=${nuevaCantidad}`, {});
  }

  /**
   * Actualiza los servicios adicionales de un elemento del carrito
   */
  actualizarServiciosAdicionales(carritoId: number, servicios: {
    acabadoPremium: boolean;
    urgente: boolean;
    envioGratis: boolean;
  }): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${carritoId}/servicios`, servicios, this.httpOptions);
  }

  /**
   * Elimina un elemento del carrito
   */
  eliminarElementoCarrito(carritoId: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/${carritoId}`);
  }

  /**
   * Vacía todo el carrito de un usuario
   */
  vaciarCarrito(usuarioId: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/vaciar/${usuarioId}`);
  }

  /**
   * Obtiene el número de elementos en el carrito de un usuario
   */
  contarElementosCarrito(usuarioId: number): Observable<{count: number}> {
    return this.http.get<{count: number}>(`${this.apiUrl}/count/${usuarioId}`);
  }

  /**
   * Calcula el precio total del carrito de un usuario
   */
  calcularTotalCarrito(usuarioId: number): Observable<{total: number}> {
    return this.http.get<{total: number}>(`${this.apiUrl}/total/${usuarioId}`);
  }
} 