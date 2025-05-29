import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { AuthService } from './auth.service';
import swal from 'sweetalert2';

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean {
    
    // Verificar si está logueado
    if (!this.authService.estaLogueado()) {
      console.log('No autenticado, redirigiendo a login');
      swal(
        'Acceso denegado',
        'Debe iniciar sesión para acceder a esta página',
        'warning'
      );
      this.router.navigate(['/login']);
      return false;
    }

    // Verificar si es administrador
    if (!this.authService.esAdmin) {
      console.log('Usuario no es administrador, acceso denegado');
      swal(
        'Acceso restringido',
        'No tiene permisos de administrador para acceder a esta página',
        'error'
      );
      this.router.navigate(['/servicios']); // Redirigir a la página principal para usuarios
      return false;
    }

    return true;
  }
} 