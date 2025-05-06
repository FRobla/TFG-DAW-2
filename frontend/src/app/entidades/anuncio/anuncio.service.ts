import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, catchError, map, tap, throwError } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { Anuncio } from './anuncio';
import swal from 'sweetalert2';

@Injectable({
  providedIn: 'root'
})
export class AnuncioService {
  private baseUrl: string = 'http://localhost:8080/api';
  private httpHeaders = new HttpHeaders({'Content-Type': 'application/json'});
  
  private anunciosSubject = new BehaviorSubject<Anuncio[]>([]);
  anuncios$ = this.anunciosSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) {
    this.cargarAnuncios();
  }

  private cargarAnuncios() {
    this.getAnuncios().subscribe(
      anuncios => this.anunciosSubject.next(anuncios)
    );
  }

  getAnuncios(): Observable<Anuncio[]> {
    return this.http.get<any[]>(`${this.baseUrl}/anuncios`).pipe(
      map(response => response.map(item => Anuncio.fromBackend(item))),
      catchError(e => {
        console.error(e);
        return throwError(() => e);
      })
    );
  }

  getAnuncio(id: number): Observable<Anuncio> {
    return this.http.get<any>(`${this.baseUrl}/anuncios/${id}`).pipe(
      map(response => Anuncio.fromBackend(response)),
      catchError(e => {
        this.router.navigate(['/anuncios']);
        console.error(e);
        return throwError(() => e);
      })
    );
  }

  create(anuncio: Anuncio): Observable<Anuncio> {
    return this.http.post<any>(
      `${this.baseUrl}/anuncios`,
      anuncio.toBackend(),
      { headers: this.httpHeaders }
    ).pipe(
      map(response => Anuncio.fromBackend(response)),
      tap(nuevoAnuncio => {
        const anuncios = this.anunciosSubject.value;
        this.anunciosSubject.next([...anuncios, nuevoAnuncio]);
        swal('Nuevo anuncio', `El anuncio ${nuevoAnuncio.titulo} ha sido creado con éxito`, 'success');
      }),
      catchError(e => {
        console.error(e);
        swal('Error al crear', e.error.mensaje, 'error');
        return throwError(() => e);
      })
    );
  }

  update(anuncio: Anuncio): Observable<Anuncio> {
    return this.http.put<any>(
      `${this.baseUrl}/anuncios/${anuncio.id}`,
      anuncio.toBackend(),
      { headers: this.httpHeaders }
    ).pipe(
      map(response => Anuncio.fromBackend(response)),
      tap(anuncioActualizado => {
        const anuncios = this.anunciosSubject.value;
        const index = anuncios.findIndex(a => a.id === anuncioActualizado.id);
        if (index !== -1) {
          anuncios[index] = anuncioActualizado;
          this.anunciosSubject.next([...anuncios]);
        }
        swal('Anuncio Actualizado', `El anuncio ${anuncioActualizado.titulo} ha sido actualizado con éxito`, 'success');
      }),
      catchError(e => {
        console.error(e);
        swal('Error al actualizar', e.error.mensaje, 'error');
        return throwError(() => e);
      })
    );
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/anuncios/${id}`, { headers: this.httpHeaders }).pipe(
      tap(() => {
        const anuncios = this.anunciosSubject.value.filter(anuncio => anuncio.id !== id);
        this.anunciosSubject.next(anuncios);
        swal('Anuncio Eliminado', 'El anuncio ha sido eliminado con éxito', 'success');
      }),
      catchError(e => {
        console.error(e);
        swal('Error al eliminar', e.error.mensaje, 'error');
        return throwError(() => e);
      })
    );
  }

  getAnunciosPorCategoria(categoriaId: number): Observable<Anuncio[]> {
    return this.http.get<any[]>(`${this.baseUrl}/anuncios/categoria/${categoriaId}`).pipe(
      map(response => response.map(item => Anuncio.fromBackend(item))),
      catchError(e => {
        console.error(e);
        return throwError(() => e);
      })
    );
  }

  getAnunciosPorUsuario(usuarioId: number): Observable<Anuncio[]> {
    return this.http.get<any[]>(`${this.baseUrl}/anuncios/usuario/${usuarioId}`).pipe(
      map(response => response.map(item => Anuncio.fromBackend(item))),
      catchError(e => {
        console.error(e);
        return throwError(() => e);
      })
    );
  }

  deleteAll() {
    return this.http.delete(`${this.baseUrl}/anuncios`).pipe(
      map(() => {
        this.anunciosSubject.next([]);
        return true;
      }),
      catchError(e => {
        if (e.status === 400) {
          return throwError(() => e);
        }
        console.error(e.error.mensaje);
        return throwError(() => e);
      })
    );
  }
}