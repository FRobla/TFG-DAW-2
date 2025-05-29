import { Injectable, OnInit } from '@angular/core';
import { Observable, catchError, throwError, tap } from 'rxjs';
import swal from 'sweetalert2';
import { Usuario } from './usuario';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService implements OnInit{

  constructor(private http: HttpClient, private router: Router) { }

  ngOnInit(): void {
    
  }

  private urlEndPoint: string = 'http://localhost:8080/api/usuarios';
  private urlEndPoint1: string = 'http://localhost:8080/api/usuario';

  getUsuarios(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(this.urlEndPoint).pipe(
      catchError(e => {
        this.router.navigate(['/usuarios']);
        console.log(e.error.mensaje);
        swal('Error al obtener los usuarios', e.error.mensaje, 'error');
        return throwError(e);
      })
    );
  }

  getUsuario(id: number): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.urlEndPoint1}/${id}`).pipe(
      catchError(e => {
        this.router.navigate(['/usuarios']);
        console.log(e.error.mensaje);
        swal('Error al obtener el usuario', e.error.mensaje, 'error');
        return throwError(e);
      })
    );
  }

  createUsuario(usuario: Usuario): Observable<any> {
    return this.http.post<any>(this.urlEndPoint1, usuario).pipe(
      catchError(e => {
        if (e.status == 400) {
          return throwError(e);
        }

        this.router.navigate(['/usuarios']);
        console.log(e.error.mensaje);
        swal(e.error.mensaje, e.error.error, 'error');
        return throwError(e);
      })
    );
  }

  updateUsuario(id: number, usuario: Usuario): Observable<any> {
    return this.http.put<any>(`${this.urlEndPoint1}/${id}`, usuario).pipe(
      catchError(e => {
        if (e.status == 400) {
          return throwError(e);
        }

        this.router.navigate(['/usuarios']);
        console.log(e.error.mensaje);
        swal(e.error.mensaje, e.error.error, 'error');
        return throwError(e);
      })
    );
  }

  deleteUsuario(id: number): Observable<any> {
    return this.http.delete<Usuario>(`${this.urlEndPoint}/${id}`).pipe(
      catchError(e => {
        this.router.navigate(['/usuarios']);
        console.log(e.error.mensaje);
        swal(e.error.mensaje, e.error.error, 'error');
        return throwError(e);
      }),
      tap(() => {
        this.getUsuarios().subscribe();  // Refrescamos la lista
      })
    );
  }

  deleteAllUsuarios(): Observable<any> {
    return this.http.delete<any>(this.urlEndPoint);
  }

  // Validar contraseña actual del usuario
  validarPasswordActual(id: number, password: string): Observable<any> {
    return this.http.post<any>(`${this.urlEndPoint1}/${id}/validar-password`, { password }).pipe(
      catchError(e => {
        console.log(e.error.mensaje);
        return throwError(e);
      })
    );
  }

  // Cambiar contraseña del usuario
  cambiarPassword(id: number, nuevaPassword: string): Observable<any> {
    return this.http.put<any>(`${this.urlEndPoint1}/${id}/cambiar-password`, { password: nuevaPassword }).pipe(
      catchError(e => {
        console.log(e.error.mensaje);
        return throwError(e);
      })
    );
  }

  // Actualizar foto de perfil del usuario
  actualizarFotoPerfil(id: number, fotoBase64: string): Observable<any> {
    return this.http.put<any>(`${this.urlEndPoint1}/${id}/foto`, { foto: fotoBase64 }).pipe(
      catchError(e => {
        console.log('Error al actualizar foto:', e.error?.mensaje || e.message);
        return throwError(e);
      })
    );
  }
}
