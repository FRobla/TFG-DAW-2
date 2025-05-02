import { Injectable, OnInit } from '@angular/core';
import { BehaviorSubject, catchError, map, Observable, of, tap, throwError } from 'rxjs';
import { HttpClient} from '@angular/common/http';
import { Categoria} from './categoria';
import { Router } from '@angular/router';
import swal from 'sweetalert2';
//import swal from 'sweetalert2';

@Injectable({
  providedIn: 'root'  //disponible a nivel global
})
export class CategoriaService implements OnInit{

  constructor(private http: HttpClient, private router: Router) { }
  ngOnInit(): void {}

  private urlEndPoint: string = 'http://localhost:8080/api/categorias';
  private urlEndPoint1: string = 'http://localhost:8080/api/categoria'; 

  // Obtener todas las categorías
  getCategorias(): Observable<Categoria[]> {
    return this.http.get<Categoria[]>(this.urlEndPoint).pipe(
      catchError(e => {
        this.router.navigate(['/categorias']);
        console.log(e.error.mensaje);
        swal('Error al obtener los categorias', e.error.mensaje, 'error');
        return throwError(e);
      })
    );
  }
  
  // Obtener una categoría por ID
  getCategoria(id: number): Observable<Categoria> {
    return this.http.get<Categoria>(`${this.urlEndPoint1}/${id}`).pipe(
      catchError(e => {
        this.router.navigate(['/categorias']);
        console.log(e.error.mensaje);
        swal('Error al obtener la categoria', e.error.mensaje, 'error');
        return throwError(e);
      })
    );
  }

  // Crear una nueva categoría
  createCategoria(categoria: Categoria): Observable<any> {
    return this.http.post<any>(this.urlEndPoint1, categoria).pipe(
      catchError(e => {
        if (e.status == 400) {
          return throwError(e);
        }

        this.router.navigate(['/categorias']);
        console.log(e.error.mensaje);
        swal(e.error.mensaje, e.error.error, 'error');
        return throwError(e);
      })
    );
  }
  
  // Actualizar una categoría
  updateCategoria(id: number, categoria: Categoria): Observable<any> {
    return this.http.put<any>(`${this.urlEndPoint1}/${id}`, categoria).pipe(
      catchError(e => {
        if (e.status == 400) {
          return throwError(e);
        }

        this.router.navigate(['/categorias']);
        console.log(e.error.mensaje);
        swal(e.error.mensaje, e.error.error, 'error');
        return throwError(e);
      })
    );
  }
  
  // Eliminar una categoría por ID
  deleteCategoria(id: number): Observable<any> {
    return this.http.delete<Categoria>(`${this.urlEndPoint}/${id}`).pipe(
      catchError(e => {
        this.router.navigate(['/categorias']);
        console.log(e.error.mensaje);
        swal(e.error.mensaje, e.error.error, 'error');
        return throwError(e);
      }),
      tap(() => {
        this.getCategorias().subscribe();  // Refrescamos la lista
      })
    );
  }
  
  // Eliminar todas las categorias
  deleteAllCategorias(): Observable<any> {
    return this.http.delete<any>(this.urlEndPoint);
  }

}
