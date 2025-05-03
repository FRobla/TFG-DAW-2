import { Injectable, OnInit } from '@angular/core';
import { catchError, Observable, of } from 'rxjs';
import { Anuncio } from './anuncio';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import swal from 'sweetalert2';

@Injectable({
  providedIn: 'root'
})
export class AnuncioService implements OnInit{

  constructor(private http: HttpClient, private router: Router) { }

  ngOnInit(): void {
    
  }

  private url = 'http://localhost:8080/api/anuncios';


  findAll(): Observable<Anuncio[]> {
    return this.http.get<Anuncio[]>(this.url).pipe(
      catchError((error) => {
        this.router.navigate(['/error']);
        return of([]);
      })
    );
  }

  findById(id: number): Observable<Anuncio | null> {
    return this.http.get<Anuncio>(`${this.url}/${id}`).pipe(
      catchError((error) => {
        swal('Error al obtener el anuncio', error.error.mensaje, 'error');
        return of(null);
      })
    );
  }

  save(anuncio: Anuncio): Observable<Anuncio | null> {
    return this.http.post<Anuncio>(this.url, anuncio).pipe(
      catchError((error) => {
        swal('Error al guardar el anuncio', error.error.mensaje, 'error');
        return of(null);
      })
    );
  }

  update(anuncio: Anuncio): Observable<Anuncio | null> {
    return this.http.put<Anuncio>(`${this.url}/${anuncio.id}`, anuncio).pipe(
      catchError((error) => {
        swal('Error al actualizar el anuncio', error.error.mensaje, 'error');
        return of(null);
      })
    );
  }

  delete(id: number): Observable<void | null> {
    return this.http.delete<void>(`${this.url}/${id}`).pipe(
      catchError((error) => {
        swal('Error al eliminar el anuncio', error.error.mensaje, 'error');
        return of(null);
      })
    );
  }

  deleteAll(): Observable<void | null> {
    return this.http.delete<void>(this.url).pipe(
      catchError((error) => {
        swal('Error al eliminar todos los anuncios', error.error.mensaje, 'error');
        return of(null);
      })
    );
  }
}
