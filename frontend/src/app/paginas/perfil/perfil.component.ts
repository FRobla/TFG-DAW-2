import { Component, OnInit, OnDestroy } from '@angular/core';
import { Usuario } from '../../entidades/usuario/usuario';
import { Ubicacion } from '../../entidades/ubicacion/ubicacion';
import { Pedido } from '../../entidades/pedido/pedido';
import { UsuarioService } from '../../entidades/usuario/usuario.service';
import { PedidoService } from '../../entidades/pedido/pedido.service';
import { AuthService } from '../../auth/auth.service';
import { HeroBuscadorService } from '../../recursos/hero-buscador/hero-buscador.service';
import { Router, ActivatedRoute } from '@angular/router';
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
  
  // Variable para determinar si es el perfil propio o de otro usuario
  esMiPerfil: boolean = true;
  usuarioIdPerfil: number | null = null;
  esAdmin: boolean = false; // Variable para detectar si el usuario actual es admin
  puedeEditar: boolean = false; // Variable combinada para permisos de edición
  
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

  // Variables para ubicaciones
  ubicaciones: Ubicacion[] = [];
  ubicacionSeleccionadaId: number | string = '';

  // Variables para los pedidos
  pedidosUsuario: Pedido[] = [];
  cargandoPedidos: boolean = false;
  
  // Paginación de pedidos
  paginaPedidos: number = 0;
  tamañoPagina: number = 10;
  totalPedidos: number = 0;
  totalPaginasPedidos: number = 0;

  // Variables para navegación entre secciones
  seccionActiva: string = 'informacion-personal';

  constructor(
    private usuarioService: UsuarioService,
    private pedidoService: PedidoService,
    private authService: AuthService,
    private heroBuscadorService: HeroBuscadorService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.usuarioIdPerfil = params['id'];
      this.esMiPerfil = !this.usuarioIdPerfil;
      this.verificarPermisosUsuario();
      this.cargarDatosUsuario();
      this.cargarUbicaciones();
    });
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
    
    if (this.esMiPerfil) {
      // Cargar perfil propio - necesita autenticación
      this.usuarioSubscription = this.authService.usuarioActual.subscribe(usuarioAuth => {
        if (usuarioAuth && usuarioAuth.id) {
          this.usuarioService.getUsuario(usuarioAuth.id).subscribe({
            next: (usuario) => {
              this.usuario = Usuario.fromBackend(usuario);
              this.cargando = false;
              // Cargar pedidos solo si esa sección está activa
              if (this.componenteActivo && this.esSeccionActiva('mis-pedidos')) {
                this.cargarPedidosUsuario();
              }
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
    } else {
      // Cargar perfil de otro usuario - usar ID de la URL
      this.usuarioService.getUsuario(this.usuarioIdPerfil!).subscribe({
        next: (usuario) => {
          this.usuario = Usuario.fromBackend(usuario);
          this.cargando = false;
          // Cargar pedidos solo si esa sección está activa
          if (this.componenteActivo && this.esSeccionActiva('mis-pedidos')) {
            this.cargarPedidosUsuario();
          }
        },
        error: (error) => {
          console.error('Error al cargar datos del usuario:', error);
          this.cargando = false;
          if (this.componenteActivo) {
            swal('Error', 'No se pudo cargar el perfil del usuario', 'error').then(() => {
              this.router.navigate(['/servicios']);
            });
          }
        }
      });
    }
  }

  // Métodos para abrir modales
  abrirModalDatosPersonales(): void {
    this.usuarioEditado = new Usuario(this.usuario);
    this.modalDatosPersonalesVisible = true;
  }

  abrirModalContacto(): void {
    this.usuarioEditado = new Usuario(this.usuario);
    // Establecer la ubicación seleccionada actual
    this.ubicacionSeleccionadaId = this.usuario.ubicacion ? this.usuario.ubicacion.id : '';
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
    this.ubicacionSeleccionadaId = '';
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

    // Manejar la ubicación seleccionada
    if (this.ubicacionSeleccionadaId) {
      const ubicacionSeleccionada = this.ubicaciones.find(u => u.id == this.ubicacionSeleccionadaId);
      this.usuario.ubicacion = ubicacionSeleccionada;
    } else {
      this.usuario.ubicacion = undefined;
    }

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

  cargarUbicaciones(): void {
    this.heroBuscadorService.getUbicaciones().subscribe({
      next: (ubicaciones) => {
        this.ubicaciones = ubicaciones;
      },
      error: (error) => {
        console.error('Error al cargar ubicaciones:', error);
        swal('Error', 'No se pudieron cargar las ubicaciones', 'error');
      }
    });
  }

  verificarPermisosUsuario(): void {
    // Verificar permisos del usuario actual
    this.authService.usuarioActual.subscribe(usuarioAuth => {
      if (usuarioAuth && usuarioAuth.id) {
        this.esAdmin = usuarioAuth.rol === 'ADMIN';
        // Se puede editar si es el perfil propio O si el usuario actual es admin
        this.puedeEditar = this.esMiPerfil || this.esAdmin;
      } else {
        this.esAdmin = false;
        this.puedeEditar = false;
      }
    });
  }

  /**
   * Carga los pedidos del usuario visualizado
   */
  cargarPedidosUsuario(): void {
    const usuarioId = this.esMiPerfil ? 
      this.authService.getCurrentUser()?.id : 
      this.usuarioIdPerfil;

    if (!usuarioId) {
      console.warn('No se pudo obtener el ID del usuario para cargar pedidos');
      this.pedidosUsuario = [];
      this.totalPedidos = 0;
      this.totalPaginasPedidos = 0;
      this.cargandoPedidos = false;
      return;
    }

    // Verificar que el componente esté activo antes de hacer la petición
    if (!this.componenteActivo) {
      return;
    }

    this.cargandoPedidos = true;
    
    // Timeout de seguridad para evitar carga infinita
    const timeoutId = setTimeout(() => {
      if (this.cargandoPedidos) {
        this.cargandoPedidos = false;
        console.warn('Timeout al cargar pedidos del usuario');
      }
    }, 10000); // 10 segundos de timeout

    this.pedidoService.getPedidosByUsuarioPage(usuarioId, this.paginaPedidos, this.tamañoPagina).subscribe({
      next: (response) => {
        clearTimeout(timeoutId);
        if (this.componenteActivo) {
          this.pedidosUsuario = response?.content || [];
          this.totalPedidos = response?.totalElements || 0;
          this.totalPaginasPedidos = response?.totalPages || 0;
          this.cargandoPedidos = false;
        }
      },
      error: (error) => {
        clearTimeout(timeoutId);
        console.error('Error al cargar pedidos del usuario:', error);
        this.cargandoPedidos = false;
        
        // Inicializar valores por defecto en caso de error
        this.pedidosUsuario = [];
        this.totalPedidos = 0;
        this.totalPaginasPedidos = 0;
        
        if (this.componenteActivo) {
          // Solo mostrar el error si es relevante para el usuario
          if (error.status !== 404) { // Si no es un 404 (usuario sin pedidos)
            console.warn('Error al cargar pedidos. El servicio podría no estar disponible.');
            // No mostrar alert para evitar interrumpir la experiencia del usuario
          }
        }
      }
    });
  }

  /**
   * Cambia la página de pedidos
   */
  cambiarPaginaPedidos(nuevaPagina: number): void {
    if (!this.componenteActivo || this.cargandoPedidos) {
      return;
    }
    
    if (nuevaPagina >= 0 && nuevaPagina < this.totalPaginasPedidos && nuevaPagina !== this.paginaPedidos) {
      this.paginaPedidos = nuevaPagina;
      this.cargarPedidosUsuario();
    }
  }

  /**
   * Obtiene el estado del pedido con formato legible
   */
  getEstadoPedidoTexto(estado: string): string {
    const estados: { [key: string]: string } = {
      'pendiente': 'Pendiente',
      'en_proceso': 'En Proceso',
      'completado': 'Completado',
      'cancelado': 'Cancelado'
    };
    return estados[estado] || estado;
  }

  /**
   * Obtiene la clase CSS para el estado del pedido
   */
  getEstadoPedidoClase(estado: string): string {
    const clases: { [key: string]: string } = {
      'pendiente': 'estado-pendiente',
      'en_proceso': 'estado-proceso',
      'completado': 'estado-completado',
      'cancelado': 'estado-cancelado'
    };
    return clases[estado] || '';
  }

  /**
   * Formatea el precio con símbolo de moneda
   */
  formatearPrecio(precio: number): string {
    return new Intl.NumberFormat('es-ES', {
      style: 'currency',
      currency: 'EUR'
    }).format(precio);
  }

  /**
   * Formatea la fecha para mostrar
   */
  formatearFecha(fecha: string): string {
    if (!fecha) return 'No especificada';
    
    return new Date(fecha).toLocaleDateString('es-ES', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  /**
   * Navegar a los detalles del pedido
   */
  verDetallesPedido(pedido: Pedido): void {
    if (!pedido || !pedido.numero_pedido) {
      console.warn('Pedido inválido para mostrar detalles');
      return;
    }

    // Se puede implementar navegación a una página de detalles del pedido
    const numeroDisplayed = pedido.numero_pedido || 'N/A';
    const estadoDisplayed = this.getEstadoPedidoTexto(pedido.estado || 'desconocido');
    const totalDisplayed = pedido.total ? this.formatearPrecio(pedido.total) : 'N/A';
    const fechaDisplayed = this.formatearFecha(pedido.fecha_pedido || '');
    const metodoPagoDisplayed = pedido.metodo_pago || 'No especificado';
    const direccionDisplayed = pedido.direccion_envio || 'No especificada';

    swal('Detalles del Pedido', `
      <div style="text-align: left;">
        <p><strong>Número:</strong> ${numeroDisplayed}</p>
        <p><strong>Estado:</strong> ${estadoDisplayed}</p>
        <p><strong>Total:</strong> ${totalDisplayed}</p>
        <p><strong>Fecha:</strong> ${fechaDisplayed}</p>
        <p><strong>Método de Pago:</strong> ${metodoPagoDisplayed}</p>
        <p><strong>Dirección de Envío:</strong> ${direccionDisplayed}</p>
        ${pedido.notas_cliente ? `<p><strong>Notas:</strong> ${pedido.notas_cliente}</p>` : ''}
      </div>
    `, 'info');
  }

  /**
   * Cambia la sección activa del perfil
   */
  cambiarSeccion(seccion: string): void {
    this.seccionActiva = seccion;
    
    // Si se selecciona la sección de pedidos y aún no se han cargado
    if (seccion === 'mis-pedidos' && this.pedidosUsuario.length === 0 && !this.cargandoPedidos) {
      this.cargarPedidosUsuario();
    }
  }

  /**
   * Verifica si una sección está activa
   */
  esSeccionActiva(seccion: string): boolean {
    return this.seccionActiva === seccion;
  }
}
