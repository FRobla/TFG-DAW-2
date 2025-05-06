import { Injectable, OnInit } from '@angular/core';
import { Observable, catchError, tap, throwError } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import swal from 'sweetalert2';
import { Impresora } from './impresora';

@Injectable({
  providedIn: 'root'
})
export class ImpresoraService implements OnInit {

  constructor(private http: HttpClient, private router: Router) { }

  ngOnInit(): void {
    
  }

  private urlEndPoint: string = 'http://localhost:8080/api/impresoras';
  private urlEndPoint1: string = 'http://localhost:8080/api/impresora';

  /**
   * Obtiene todas las impresoras
   */
  getImpresoras(): Observable<Impresora[]> {
    return this.http.get<Impresora[]>(this.urlEndPoint).pipe(
      catchError(e => {
        this.router.navigate(['/impresoras']);
        console.log(e.error.mensaje);
        swal('Error al obtener las impresoras', e.error.mensaje, 'error');
        return throwError(() => e);
      })
    );
  }

  /**
   * Obtiene una impresora por su ID
   * @param id ID de la impresora
   */
  getImpresora(id: number): Observable<Impresora> {
    return this.http.get<Impresora>(`${this.urlEndPoint1}/${id}`).pipe(
      catchError(e => {
        this.router.navigate(['/impresoras']);
        console.log(e.error.mensaje);
        swal('Error al obtener la impresora', e.error.mensaje, 'error');
        return throwError(() => e);
      })
    );
  }

  /**
   * Crea una nueva impresora
   * @param impresora Impresora a crear
   */
  createImpresora(impresora: Impresora): Observable<any> {
    return this.http.post<any>(this.urlEndPoint1, impresora.toBackend()).pipe(
      catchError(e => {
        if (e.status == 400) {
          return throwError(() => e);
        }

        this.router.navigate(['/impresoras']);
        console.log(e.error.mensaje);
        swal(e.error.mensaje, e.error.error, 'error');
        return throwError(() => e);
      })
    );
  }

  /**
   * Actualiza una impresora existente
   * @param id ID de la impresora a actualizar
   * @param impresora Impresora con los datos actualizados
   */
  updateImpresora(id: number, impresora: Impresora): Observable<any> {
    return this.http.put<any>(`${this.urlEndPoint1}/${id}`, impresora.toBackend()).pipe(
      catchError(e => {
        if (e.status == 400) {
          return throwError(() => e);
        }

        this.router.navigate(['/impresoras']);
        console.log(e.error.mensaje);
        swal(e.error.mensaje, e.error.error, 'error');
        return throwError(() => e);
      })
    );
  }

  /**
   * Elimina una impresora
   * @param id ID de la impresora a eliminar
   */
  deleteImpresora(id: number): Observable<any> {
    return this.http.delete<Impresora>(`${this.urlEndPoint1}/${id}`).pipe(
      catchError(e => {
        this.router.navigate(['/impresoras']);
        console.log(e.error.mensaje);
        swal(e.error.mensaje, e.error.error, 'error');
        return throwError(() => e);
      }),
      tap(() => {
        this.getImpresoras().subscribe();  // Refrescamos la lista
      })
    );
  }

  /**
   * Elimina todas las impresoras
   */
  deleteAllImpresoras(): Observable<any> {
    return this.http.delete<any>(this.urlEndPoint);
  }
}
