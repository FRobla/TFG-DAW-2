import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  errorMessage: string = '';
  isLoading: boolean = false;
  showPassword: boolean = false;
  
  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) { }
  
  ngOnInit(): void {
    // Redirigir si ya está autenticado
    if (this.authService.isAuthenticated()) {
      this.router.navigate(['/servicios']);
      return;
    }
    
    // Inicializar formulario
    this.loginForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      rememberMe: [false]
    });
  }
  
  /**
   * Maneja el envío del formulario de login
   */
  onSubmit(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }
    
    this.isLoading = true;
    this.errorMessage = '';
    
    const credentials = {
      username: this.loginForm.value.username,
      password: this.loginForm.value.password
    };
    
    this.authService.login(credentials).subscribe({
      next: (response) => {
        this.isLoading = false;
        // Recordar credenciales si está marcada la opción
        if (this.loginForm.value.rememberMe) {
          localStorage.setItem('rememberedUser', this.loginForm.value.username);
        } else {
          localStorage.removeItem('rememberedUser');
        }
        
        // Redirigir según el rol
        if (response.rol === 'ROLE_ADMIN') {
          this.router.navigate(['/admin']);
        } else {
          this.router.navigate(['/servicios']);
        }
      },
      error: (error) => {
        this.isLoading = false;
        this.errorMessage = 'Usuario o contraseña incorrectos';
        console.error('Error de login:', error);
      }
    });
  }
  
  /**
   * Alterna la visibilidad de la contraseña
   */
  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }
}
