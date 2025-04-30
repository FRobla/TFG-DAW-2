import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Categoria } from '../categoria/categoria';

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
}
