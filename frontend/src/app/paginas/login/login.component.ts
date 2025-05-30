import { Component } from '@angular/core';
import { AuthService } from '../../auth/auth.service';
import { Router } from '@angular/router';
import swal from 'sweetalert2';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  
  loginForm: FormGroup;
  email: string = '';
  password: string = '';
  errorMessage: string = '';
  showPassword: boolean = false;
  isLoading: boolean = false;
  
  constructor(
    private authService: AuthService,
    private router: Router,
    private fb: FormBuilder
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      rememberMe: [false]
    });
  }
  
  // Getter para fácil acceso a los campos del formulario
  get f() { return this.loginForm.controls; }
  
  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }
  
  onSubmit() {
    // Si el formulario no es válido, marcar todos los campos como tocados para mostrar errores
    if (this.loginForm.invalid) {
      Object.keys(this.loginForm.controls).forEach(key => {
        const control = this.loginForm.get(key);
        control?.markAsTouched();
      });
      return;
    }
    
    this.isLoading = true;
    this.errorMessage = '';
    
    this.authService.login({
      email: this.loginForm.value.email,
      password: this.loginForm.value.password
    }).subscribe({
      next: (usuario) => {
        this.isLoading = false;
        
        swal(
          `¡Bienvenido ${usuario.username}!`,
          '',
          'success'
        );
        
        // Redirigir según el rol
        switch(usuario.rol) {
          case 'ADMIN':
            this.router.navigate(['/admin']);
            break;
          case 'USER':
            this.router.navigate(['/servicios']);
            break;
          default:
            console.error('Rol no reconocido');
            this.router.navigate(['']);
        }
      },
      error: (error) => {
        this.isLoading = false;
        console.error('Error de inicio de sesión:', error);
        
        // Marcar todos los campos como tocados para mostrar los estilos de error
        Object.keys(this.loginForm.controls).forEach(key => {
          const control = this.loginForm.get(key);
          control?.markAsTouched();
        });
        
        // Almacenar el mensaje de error
        this.errorMessage = error.error?.mensaje || 'Credenciales inválidas';
        
        swal(
          'Error de inicio de sesión :(',
          this.errorMessage,
          'error'
        );
      }
    });
  }
  
  // Método para resetear errores cuando el usuario empieza a escribir
  onInputFocus() {
    this.errorMessage = '';
  }
}