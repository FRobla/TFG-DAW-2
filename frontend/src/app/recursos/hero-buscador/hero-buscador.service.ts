import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Categoria } from '../../entidades/categoria/categoria';
import { Ubicacion } from '../../entidades/ubicacion/ubicacion';

@Injectable({
  providedIn: 'root'
})
export class HeroBuscadorService {
  private apiUrl = 'http://localhost:8080/api'; // URL base del backend

  constructor(private http: HttpClient) { }

  /**
   * Obtiene todas las categorías disponibles del backend
   */
  getCategorias(): Observable<Categoria[]> {
    return this.http.get<Categoria[]>(`${this.apiUrl}/categorias`);
  }

  /**
   * Obtiene todas las ubicaciones activas del backend
   */
  getUbicaciones(): Observable<Ubicacion[]> {
    return this.http.get<Ubicacion[]>(`${this.apiUrl}/ubicaciones/activas`);
  }

  /**
   * Obtiene solo las ubicaciones que tienen usuarios con anuncios publicados
   */
  getUbicacionesConAnuncios(): Observable<Ubicacion[]> {
    return this.http.get<Ubicacion[]>(`${this.apiUrl}/ubicaciones/con-anuncios`);
  }
}
