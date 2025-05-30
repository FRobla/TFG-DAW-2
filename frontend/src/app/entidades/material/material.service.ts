import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, map, catchError, of } from 'rxjs';
import { Material } from './material';

@Injectable({
  providedIn: 'root'
})
export class MaterialService {

  private urlEndPoint: string = 'http://localhost:8080/api/materiales';

  private httpHeaders = new HttpHeaders({'Content-Type': 'application/json'});

  constructor(private http: HttpClient) { }

  // Obtener todos los materiales
  getMateriales(): Observable<Material[]> {
    return this.http.get<any[]>(this.urlEndPoint).pipe(
      map(response => response.map(data => Material.fromBackend(data))),
      catchError(e => {
        console.error('Error al obtener materiales:', e);
        return of([]);
      })
    );
  }

  // Obtener material por ID
  getMaterial(id: number): Observable<Material> {
    return this.http.get<any>(`${this.urlEndPoint}/${id}`).pipe(
      map(response => Material.fromBackend(response)),
      catchError(e => {
        console.error(`Error al obtener material con ID ${id}:`, e);
        return of(new Material());
      })
    );
  }

  // Obtener solo materiales activos
  getMaterialesActivos(): Observable<Material[]> {
    return this.http.get<any[]>(`${this.urlEndPoint}/activos`).pipe(
      map(response => response.map(data => Material.fromBackend(data))),
      catchError(e => {
        console.error('Error al obtener materiales activos:', e);
        return of([]);
      })
    );
  }

  // Crear material
  create(material: Material): Observable<any> {
    return this.http.post<any>(this.urlEndPoint, material.toBackend(), {headers: this.httpHeaders}).pipe(
      catchError(e => {
        console.error('Error al crear material:', e);
        throw e;
      })
    );
  }

  // Actualizar material
  update(material: Material): Observable<any> {
    return this.http.put<any>(`${this.urlEndPoint}/${material.id}`, material.toBackend(), {headers: this.httpHeaders}).pipe(
      catchError(e => {
        console.error('Error al actualizar material:', e);
        throw e;
      })
    );
  }

  // Eliminar material
  delete(id: number): Observable<any> {
    return this.http.delete<any>(`${this.urlEndPoint}/${id}`, {headers: this.httpHeaders}).pipe(
      catchError(e => {
        console.error('Error al eliminar material:', e);
        throw e;
      })
    );
  }
} 