import { Component, OnInit, OnDestroy } from '@angular/core';
import { Usuario } from '../../entidades/usuario/usuario';
import { UsuarioService } from '../../entidades/usuario/usuario.service';
import { AuthService } from '../../auth/auth.service';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import swal from 'sweetalert2';

@Component({
  selector: 'app-perfil',
  standalone: false,
  templateUrl: './perfil.component.html',
  styleUrl: './perfil.component.css'
})
export class PerfilComponent implements OnInit, OnDestroy {

  usuario: Usuario = new Usuario();
  usuarioEditado: Usuario = new Usuario();
  cargando: boolean = false;
  private usuarioSubscription: Subscription = new Subscription();
  private componenteActivo: boolean = true;
  
  // Variables para controlar modales
  modalDatosPersonalesVisible: boolean = false;
  modalContactoVisible: boolean = false;
  modalSeguridadVisible: boolean = false;
  modalFotoVisible: boolean = false;
  
  // Variables para validaciones
  passwordActual: string = '';
  passwordNueva: string = '';
  passwordConfirmar: string = '';
  
  // Variables para la foto de perfil
  archivoSeleccionado: File | null = null;
  imagenPreview: string | null = null;

  constructor(
    private usuarioService: UsuarioService,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.cargarDatosUsuario();
  }

  ngOnDestroy(): void {
    // Marcar que el componente ya no está activo
    this.componenteActivo = false;
    // Desuscribirse para evitar memory leaks
    if (this.usuarioSubscription) {
      this.usuarioSubscription.unsubscribe();
    }
  }

  cargarDatosUsuario(): void {
    this.cargando = true;
    
    // Cancelar suscripción anterior si existe
    if (this.usuarioSubscription) {
      this.usuarioSubscription.unsubscribe();
    }
    
    // Obtener el usuario actual del servicio de autenticación
    this.usuarioSubscription = this.authService.usuarioActual.subscribe(usuarioAuth => {
      if (usuarioAuth && usuarioAuth.id) {
        this.usuarioService.getUsuario(usuarioAuth.id).subscribe({
          next: (usuario) => {
            this.usuario = Usuario.fromBackend(usuario);
            this.cargando = false;
          },
          error: (error) => {
            console.error('Error al cargar datos del usuario:', error);
            this.cargando = false;
            if (this.componenteActivo) {
              swal('Error', 'No se pudieron cargar los datos del usuario', 'error');
            }
          }
        });
      } else {
        this.cargando = false;
        // Solo mostrar la alerta si el componente está activo, no estamos en proceso de logout, y estamos en la ruta de perfil
        if (this.componenteActivo && !this.authService.estaHaciendoLogout && this.router.url.includes('/perfil')) {
          swal('Error', 'No se encontró información del usuario autenticado', 'error').then(() => {
            this.router.navigate(['/login']);
          });
        }
      }
    });
  }

  // Métodos para abrir modales
  abrirModalDatosPersonales(): void {
    this.usuarioEditado = new Usuario(this.usuario);
    this.modalDatosPersonalesVisible = true;
  }

  abrirModalContacto(): void {
    this.usuarioEditado = new Usuario(this.usuario);
    this.modalContactoVisible = true;
  }

  abrirModalSeguridad(): void {
    this.passwordActual = '';
    this.passwordNueva = '';
    this.passwordConfirmar = '';
    this.modalSeguridadVisible = true;
  }

  abrirModalFoto(): void {
    this.archivoSeleccionado = null;
    this.imagenPreview = null;
    this.modalFotoVisible = true;
  }

  // Métodos para cerrar modales
  cerrarModalDatosPersonales(): void {
    this.modalDatosPersonalesVisible = false;
    this.usuarioEditado = new Usuario();
  }

  cerrarModalContacto(): void {
    this.modalContactoVisible = false;
    this.usuarioEditado = new Usuario();
  }

  cerrarModalSeguridad(): void {
    this.modalSeguridadVisible = false;
    this.passwordActual = '';
    this.passwordNueva = '';
    this.passwordConfirmar = '';
  }

  cerrarModalFoto(): void {
    this.modalFotoVisible = false;
    this.archivoSeleccionado = null;
    this.imagenPreview = null;
  }

  // Métodos para guardar cambios
  guardarDatosPersonales(): void {
    if (!this.usuarioEditado.nombre || !this.usuarioEditado.apellido) {
      swal('Error', 'El nombre y apellido son obligatorios', 'error');
      return;
    }

    this.usuario.nombre = this.usuarioEditado.nombre;
    this.usuario.apellido = this.usuarioEditado.apellido;

    this.actualizarUsuario();
    this.cerrarModalDatosPersonales();
  }

  guardarContacto(): void {
    if (!this.usuarioEditado.email) {
      swal('Error', 'El email es obligatorio', 'error');
      return;
    }

    if (!this.validarEmail(this.usuarioEditado.email)) {
      swal('Error', 'El formato del email no es válido', 'error');
      return;
    }

    this.usuario.email = this.usuarioEditado.email;
    this.usuario.direccion = this.usuarioEditado.direccion;

    this.actualizarUsuario();
    this.cerrarModalContacto();
  }

  cambiarPassword(): void {
    // Validar que todos los campos estén completos
    if (!this.passwordActual || !this.passwordNueva || !this.passwordConfirmar) {
      swal('Error', 'Todos los campos de contraseña son obligatorios', 'error');
      return;
    }

    // Validar que la nueva contraseña y la confirmación coincidan
    if (this.passwordNueva !== this.passwordConfirmar) {
      swal('Error', 'La nueva contraseña y la confirmación no coinciden', 'error');
      return;
    }

    // Validar longitud mínima de la nueva contraseña
    if (this.passwordNueva.length < 6) {
      swal('Error', 'La nueva contraseña debe tener al menos 6 caracteres', 'error');
      return;
    }

    // Validar la contraseña actual contra el backend
    this.usuarioService.validarPasswordActual(this.usuario.id, this.passwordActual).subscribe({
      next: (response) => {
        if (response.valida) {
          // La contraseña actual es correcta, proceder con el cambio usando el endpoint específico
          this.usuarioService.cambiarPassword(this.usuario.id, this.passwordNueva).subscribe({
            next: (response) => {
              this.cerrarModalSeguridad();
              swal('Éxito', 'Contraseña cambiada correctamente', 'success');
            },
            error: (error) => {
              console.error('Error al cambiar contraseña:', error);
              swal('Error', 'No se pudo cambiar la contraseña. Intenta de nuevo.', 'error');
            }
          });
        } else {
          // La contraseña actual es incorrecta
          swal('Error', 'La contraseña actual no es correcta', 'error');
        }
      },
      error: (error) => {
        console.error('Error al validar contraseña:', error);
        swal('Error', 'No se pudo validar la contraseña actual. Intenta de nuevo.', 'error');
      }
    });
  }

  // Método auxiliar para actualizar usuario
  private actualizarUsuario(mostrarAlerta: boolean = true): void {
    this.usuarioService.updateUsuario(this.usuario.id, this.usuario).subscribe({
      next: (response) => {
        if (mostrarAlerta) {
          swal('Éxito', 'Datos actualizados correctamente', 'success');
        }
        this.cargarDatosUsuario(); // Recargar datos
      },
      error: (error) => {
        console.error('Error al actualizar usuario:', error);
        swal('Error', 'No se pudieron actualizar los datos', 'error');
      }
    });
  }

  // Métodos para manejo de archivos
  onFileSelected(event: any): void {
    const archivo = event.target.files[0];
    if (archivo) {
      if (archivo.type.startsWith('image/')) {
        // Validar tamaño máximo de archivo (por ejemplo 5MB)
        const maxSize = 5 * 1024 * 1024; // 5MB en bytes
        if (archivo.size > maxSize) {
          swal('Error', 'La imagen es demasiado grande. El tamaño máximo permitido es 5MB', 'error');
          return;
        }

        this.archivoSeleccionado = archivo;
        
        // Crear preview de la imagen
        const reader = new FileReader();
        reader.onload = (e: any) => {
          this.imagenPreview = e.target.result;
        };
        reader.readAsDataURL(archivo);
      } else {
        swal('Error', 'Por favor selecciona un archivo de imagen válido', 'error');
      }
    }
  }

  guardarFotoPerfil(): void {
    if (!this.archivoSeleccionado) {
      swal('Error', 'Selecciona una imagen primero', 'error');
      return;
    }

    // Convertir imagen a base64 y enviar al backend
    const reader = new FileReader();
    reader.onload = (e: any) => {
      const imagenBase64 = e.target.result;
      
      // Enviar al backend usando el método específico para foto de perfil
      this.usuarioService.actualizarFotoPerfil(this.usuario.id, imagenBase64).subscribe({
        next: (response: any) => {
          // Actualizar el usuario local con la nueva foto
          this.usuario.fotoPerfil = imagenBase64;
          swal('Éxito', 'Foto de perfil actualizada correctamente', 'success');
          this.cerrarModalFoto();
          // Recargar datos del usuario para obtener la foto actualizada
          this.cargarDatosUsuario();
        },
        error: (error: any) => {
          console.error('Error al actualizar foto de perfil:', error);
          swal('Error', 'No se pudo actualizar la foto de perfil. Intenta de nuevo.', 'error');
        }
      });
    };
    
    reader.onerror = (error: any) => {
      console.error('Error al leer el archivo:', error);
      swal('Error', 'No se pudo procesar la imagen. Intenta de nuevo.', 'error');
    };
    
    reader.readAsDataURL(this.archivoSeleccionado);
  }

  // Métodos de validación
  private validarEmail(email: string): boolean {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  }

  // Método para obtener iniciales del usuario
  getIniciales(): string {
    if (this.usuario.nombre && this.usuario.apellido) {
      return (this.usuario.nombre.charAt(0) + this.usuario.apellido.charAt(0)).toUpperCase();
    }
    return this.usuario.nombre ? this.usuario.nombre.charAt(0).toUpperCase() : 'U';
  }

  // Método para formatear fecha de registro
  getFechaRegistroFormateada(): string {
    if (this.usuario.fecha_registro) {
      return new Date(this.usuario.fecha_registro).toLocaleDateString('es-ES', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
      });
    }
    return 'No disponible';
  }
}
