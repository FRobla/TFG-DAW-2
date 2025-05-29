import { Injectable, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Categoria } from '../../entidades/categoria/categoria';
import { Anuncio } from '../../entidades/anuncio/anuncio';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Router } from '@angular/router';
import { ResultadoBusquedaPaginada, ParametrosBusqueda } from './resultado-busqueda';

@Injectable({
  providedIn: 'root'
})
export class ResultadoBusquedaService implements OnInit{

  constructor(private http: HttpClient, private router: Router) { }

  ngOnInit(): void {
    
  }

  private urlEndPoint: string = 'http://localhost:8080/api';
  
  cargarCategorias(): Observable<Categoria[]> {
    return this.http.get<Categoria[]>(`${this.urlEndPoint}/categorias`)
  }

  /**
   * Cargar categorías con conteo de anuncios
   */
  cargarCategoriasConConteo(): Observable<any[]> {
    return this.http.get<any[]>(`${this.urlEndPoint}/categorias/con-conteo`);
  }

  /**
   * Realizar búsqueda avanzada de anuncios
   */
  buscarAnunciosAvanzada(parametros: ParametrosBusqueda): Observable<ResultadoBusquedaPaginada> {
    let params = new HttpParams();

    // Añadir parámetros solo si tienen valor
    if (parametros.q && parametros.q.trim() !== '') {
      params = params.set('q', parametros.q.trim());
    }
    if (parametros.categoria && parametros.categoria.trim() !== '') {
      params = params.set('categoria', parametros.categoria);
    }
    if (parametros.ubicacion && parametros.ubicacion.trim() !== '') {
      params = params.set('ubicacion', parametros.ubicacion);
    }
    if (parametros.valoracion && parametros.valoracion.trim() !== '') {
      params = params.set('valoracion', parametros.valoracion);
    }
    if (parametros.precioMin !== undefined && parametros.precioMin !== null) {
      params = params.set('precioMin', parametros.precioMin.toString());
    }
    if (parametros.precioMax !== undefined && parametros.precioMax !== null) {
      params = params.set('precioMax', parametros.precioMax.toString());
    }
    if (parametros.material && parametros.material.trim() !== '') {
      params = params.set('material', parametros.material);
    }
    if (parametros.tiempoEntrega && parametros.tiempoEntrega.trim() !== '') {
      params = params.set('tiempoEntrega', parametros.tiempoEntrega);
    }
    if (parametros.page !== undefined && parametros.page !== null) {
      params = params.set('page', parametros.page.toString());
    }
    if (parametros.size !== undefined && parametros.size !== null) {
      params = params.set('size', parametros.size.toString());
    }
    if (parametros.orden && parametros.orden.trim() !== '') {
      params = params.set('orden', parametros.orden);
    }

    return this.http.get<any>(`${this.urlEndPoint}/search/anuncios/avanzada`, { params })
      .pipe(
        map(response => {
          // Convertir los anuncios del backend usando el método fromBackend
          const anunciosConvertidos = response.content.map((anuncioData: any) => 
            Anuncio.fromBackend(anuncioData)
          );

          return {
            content: anunciosConvertidos,
            totalElements: response.totalElements,
            totalPages: response.totalPages,
            currentPage: response.currentPage,
            size: response.size,
            numberOfElements: response.numberOfElements,
            first: response.first,
            last: response.last
          };
        })
      );
  }

  /**
   * Búsqueda simple de anuncios
   */
  buscarAnuncios(query: string): Observable<Anuncio[]> {
    const params = new HttpParams().set('query', query);
    
    return this.http.get<any>(`${this.urlEndPoint}/search`, { params })
      .pipe(
        map(response => {
          if (response.anuncios) {
            return response.anuncios.map((anuncioData: any) => 
              Anuncio.fromBackend(anuncioData)
            );
          }
          return [];
        })
      );
  }
}
