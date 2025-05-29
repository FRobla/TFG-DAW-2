import { Component } from '@angular/core';
import { AuthService } from '../../auth/auth.service';
import { Router } from '@angular/router';
import swal from 'sweetalert2';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-registro',
  standalone: false,
  templateUrl: './registro.component.html',
  styleUrls: ['./registro.component.css']
})
export class RegistroComponent {
  
  registroForm: FormGroup;
  errorMessage: string = '';
  showPassword: boolean = false;
  showConfirmPassword: boolean = false;
  isLoading: boolean = false;
  
  constructor(
    private authService: AuthService,
    private router: Router,
    private fb: FormBuilder
  ) {
    this.registroForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.minLength(2)]],
      lastName: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required]]
    }, { validators: this.passwordMatchValidator });
  }
  
  // Getter para fácil acceso a los campos del formulario
  get f() { return this.registroForm.controls; }
  
  // Validador personalizado para verificar que las contraseñas coincidan
  passwordMatchValidator(form: FormGroup) {
    const password = form.get('password');
    const confirmPassword = form.get('confirmPassword');
    
    if (password && confirmPassword && password.value !== confirmPassword.value) {
      confirmPassword.setErrors({ 'passwordMismatch': true });
      return { 'passwordMismatch': true };
    }
    
    return null;
  }
  
  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }
  
  toggleConfirmPasswordVisibility() {
    this.showConfirmPassword = !this.showConfirmPassword;
  }
  
  onSubmit() {
    // Si el formulario no es válido, marcar todos los campos como tocados para mostrar errores
    if (this.registroForm.invalid) {
      Object.keys(this.registroForm.controls).forEach(key => {
        const control = this.registroForm.get(key);
        control?.markAsTouched();
      });
      return;
    }
    
    this.isLoading = true;
    this.errorMessage = '';
    
    const datosRegistro = {
      firstName: this.registroForm.value.firstName,
      lastName: this.registroForm.value.lastName,
      email: this.registroForm.value.email,
      password: this.registroForm.value.password
    };
    
    this.authService.registro(datosRegistro).subscribe({
      next: (response) => {
        this.isLoading = false;
        
        swal(
          '¡Registro exitoso!',
          'Tu cuenta ha sido creada correctamente. Ahora puedes iniciar sesión.',
          'success'
        ).then(() => {
          // Redirigir al login después del registro exitoso
          this.router.navigate(['/login']);
        });
      },
      error: (error) => {
        this.isLoading = false;
        console.error('Error de registro:', error);
        
        // Extraer mensaje de error del backend
        this.errorMessage = error.error?.mensaje || 'Error al registrar el usuario';
        
        swal(
          'Error en el registro :(',
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
  
  // Método para navegar al login
  goToLogin() {
    this.router.navigate(['/login']);
  }
} 