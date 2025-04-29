import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { Router } from '@angular/router';

export interface AuthResponse {
  token: string;
  refreshToken: string;
  userId: number;
  username: string;
  nombre: string;
  apellidos: string;
  email: string;
  rol: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegistroRequest {
  username: string;
  password: string;
  email: string;
  nombre: string;
  apellidos: string;
  direccion?: string;
  telefono?: string;
  ubicacion?: string;
  bio?: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = 'http://localhost:8080/api/auth';
  private currentUserSubject = new BehaviorSubject<AuthResponse | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();
  
  constructor(private http: HttpClient, private router: Router) {
    // Recuperar sesión del usuario del localStorage al iniciar
    const userData = localStorage.getItem('currentUser');
    if (userData) {
      this.currentUserSubject.next(JSON.parse(userData));
    }
  }
  
  /**
   * Inicia sesión con las credenciales proporcionadas
   * @param credentials Credenciales (usuario y contraseña)
   * @returns Observable con los datos de autenticación
   */
  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/login`, credentials)
      .pipe(
        tap(response => {
          // Almacenar datos del usuario y token
          localStorage.setItem('currentUser', JSON.stringify(response));
          localStorage.setItem('token', response.token);
          this.currentUserSubject.next(response);
        }),
        catchError(error => {
          return throwError(() => new Error('Credenciales incorrectas'));
        })
      );
  }
  
  /**
   * Registra un nuevo usuario
   * @param userData Datos del nuevo usuario
   * @returns Observable con los datos de autenticación
   */
  register(userData: RegistroRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/registro`, userData)
      .pipe(
        tap(response => {
          // Almacenar datos del usuario y token
          localStorage.setItem('currentUser', JSON.stringify(response));
          localStorage.setItem('token', response.token);
          this.currentUserSubject.next(response);
        }),
        catchError(error => {
          return throwError(() => new Error('Error en el registro'));
        })
      );
  }
  
  /**
   * Cierra la sesión del usuario actual
   */
  logout(): void {
    // Eliminar datos de sesión del localStorage
    localStorage.removeItem('currentUser');
    localStorage.removeItem('token');
    this.currentUserSubject.next(null);
    
    // Redirigir a la página principal
    this.router.navigate(['/']);
  }
  
  /**
   * Obtiene el token actual del usuario
   * @returns Token JWT o null si no hay sesión
   */
  getToken(): string | null {
    return localStorage.getItem('token');
  }
  
  /**
   * Verifica si hay un usuario autenticado
   * @returns true si hay un usuario autenticado
   */
  isAuthenticated(): boolean {
    return this.currentUserSubject.value !== null;
  }
  
  /**
   * Obtiene el usuario actualmente autenticado
   * @returns Datos del usuario actual o null
   */
  getCurrentUser(): AuthResponse | null {
    return this.currentUserSubject.value;
  }
  
  /**
   * Verifica si el usuario actual tiene rol de administrador
   * @returns true si el usuario es administrador
   */
  isAdmin(): boolean {
    const user = this.currentUserSubject.value;
    return user !== null && user.rol === 'ROLE_ADMIN';
  }
} 