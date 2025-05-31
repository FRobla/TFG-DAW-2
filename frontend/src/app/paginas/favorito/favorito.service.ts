import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';
import { Anuncio } from '../../entidades/anuncio/anuncio';

export interface Favorito {
  id: number;
  fechaMarcado: string;
  anuncio: Anuncio;
  usuario: any;
}

export interface FavoritoResponse {
  favoritos: Favorito[];
  currentPage: number;
  totalItems: number;
  totalPages: number;
  pageSize: number;
}

@Injectable({
  providedIn: 'root'
})
export class FavoritoService {
  private urlEndPoint: string = 'http://localhost:8080/api';

  private httpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });

  constructor(private http: HttpClient) { }

  /**
   * Obtiene todos los favoritos de un usuario con paginación
   */
  getFavoritosByUsuario(usuarioId: number, page: number = 0, size: number = 12): Observable<FavoritoResponse> {
    return this.http.get<FavoritoResponse>(`${this.urlEndPoint}/favoritos/usuario/${usuarioId}?page=${page}&size=${size}`)
      .pipe(
        map(response => {
          // Procesar los anuncios con el método fromBackend para asegurar el mapeo correcto
          if (response.favoritos) {
            response.favoritos = response.favoritos.map(favorito => ({
              ...favorito,
              anuncio: Anuncio.fromBackend(favorito.anuncio)
            }));
          }
          return response;
        })
      );
  }

  /**
   * Obtiene todos los favoritos de un usuario sin paginación
   */
  getFavoritosByUsuarioSimple(usuarioId: number): Observable<Favorito[]> {
    return this.http.get<any>(`${this.urlEndPoint}/favoritos/usuario/${usuarioId}/simple`)
      .pipe(
        map(response => response.favoritos || [])
      );
  }

  /**
   * Verifica si un anuncio es favorito de un usuario
   */
  checkFavorito(anuncioId: number, usuarioId: number): Observable<boolean> {
    return this.http.get<any>(`${this.urlEndPoint}/favoritos/check/${anuncioId}/${usuarioId}`)
      .pipe(
        map(response => response.esFavorito || false)
      );
  }

  /**
   * Marca/desmarca un anuncio como favorito (toggle)
   */
  toggleFavorito(anuncioId: number, usuarioId: number): Observable<any> {
    // Primero verificar si ya existe
    return this.checkFavorito(anuncioId, usuarioId).pipe(
      switchMap(esFavorito => {
        if (esFavorito) {
          // Si ya existe, eliminarlo (DELETE)
          return this.http.delete(`${this.urlEndPoint}/favorito/${anuncioId}/${usuarioId}`);
        } else {
          // Si no existe, crearlo (POST)
          return this.http.post(`${this.urlEndPoint}/favorito/${anuncioId}/${usuarioId}`, {}, { headers: this.httpHeaders });
        }
      })
    );
  }

  /**
   * Elimina un favorito
   */
  deleteFavorito(anuncioId: number, usuarioId: number): Observable<any> {
    return this.http.delete(`${this.urlEndPoint}/favorito/${anuncioId}/${usuarioId}`);
  }

  /**
   * Cuenta los favoritos de un usuario
   */
  countFavoritosByUsuario(usuarioId: number): Observable<number> {
    return this.http.get<any>(`${this.urlEndPoint}/favoritos/contar/usuario/${usuarioId}`)
      .pipe(
        map(response => response.count || 0)
      );
  }
} 