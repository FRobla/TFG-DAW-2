import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { trigger, state, style, transition, animate } from '@angular/animations';
import { CarritoService, CarritoElemento, CarritoResponse } from './carrito.service';
import { AuthService } from '../../auth/auth.service';
import swal from 'sweetalert2';

// Extendemos la interfaz para incluir la propiedad showServices
interface CarritoElementoExtendido extends CarritoElemento {
  showServices?: boolean;
}

@Component({
  selector: 'app-carrito',
  standalone: false,
  templateUrl: './carrito.component.html',
  styleUrl: './carrito.component.css',
  animations: [
    trigger('slideInOut', [
      state('in', style({
        height: '*',
        opacity: 1,
        overflow: 'hidden'
      })),
      state('out', style({
        height: '0px',
        opacity: 0,
        overflow: 'hidden'
      })),
      transition('in => out', animate('300ms ease-in-out')),
      transition('out => in', animate('300ms ease-in-out'))
    ])
  ]
})
export class CarritoComponent implements OnInit {
  
  // Datos del carrito
  elementosCarrito: CarritoElementoExtendido[] = [];
  totalElementos: number = 0;
  totalPrecio: number = 0;
  
  // Estado de carga
  cargando: boolean = true;
  procesando: boolean = false;
  
  // ID del usuario obtenido del servicio de autenticación
  usuarioId: number = 0;
  
  // Estados de interacción
  elementoEliminando: number | null = null;
  elementoActualizando: number | null = null;

  // Notas del cliente para el pedido
  notasCliente: string = '';

  constructor(
    private carritoService: CarritoService,
    private router: Router,
    public authService: AuthService
  ) { }

  ngOnInit(): void {
    // Obtener el ID del usuario autenticado
    this.usuarioId = this.authService.getCurrentUserId();
    console.log('=== DEBUG CARRITO: Usuario ID obtenido ===', this.usuarioId);
    
    // Verificar que tenemos un usuario válido antes de cargar el carrito
    if (this.usuarioId && this.usuarioId > 0) {
      console.log('=== DEBUG CARRITO: Usuario válido, cargando carrito ===');
      this.cargarCarrito();
    } else {
      console.error('No se pudo obtener un ID de usuario válido');
      this.cargando = false;
      swal({
        title: 'Error de autenticación',
        text: 'No se pudo identificar al usuario. Por favor, inicia sesión nuevamente.',
        type: 'error',
        confirmButtonColor: '#dc3545',
        confirmButtonText: 'Aceptar'
      }).then(() => {
        this.router.navigate(['/login']);
      });
    }

    // Suscribirse a cambios en la autenticación del usuario
    this.authService.usuarioActual.subscribe(usuario => {
      if (!usuario && !this.authService.estaHaciendoLogout) {
        // Si el usuario se desautentica y no es durante un logout manual
        console.warn('Usuario desautenticado durante el uso del carrito');
        this.router.navigate(['/login']);
      }
    });
  }

  /**
   * Carga el carrito del usuario desde el backend
   */
  cargarCarrito(): void {
    this.cargando = true;
    console.log('=== DEBUG CARRITO: Iniciando carga de carrito para usuario ===', this.usuarioId);
    
    this.carritoService.obtenerCarritoUsuario(this.usuarioId).subscribe({
      next: (response: CarritoResponse) => {
        console.log('=== DEBUG CARRITO: Respuesta del backend ===', response);
        this.elementosCarrito = response.carrito;
        this.totalElementos = response.totalElementos;
        this.totalPrecio = response.totalPrecio;
        this.cargando = false;
        console.log('=== DEBUG CARRITO: Elementos cargados ===', this.elementosCarrito.length);
      },
      error: (error) => {
        console.error('Error al cargar el carrito:', error);
        this.cargando = false;
        
        // Mostrar un mensaje más específico basado en el tipo de error
        if (error.status === 401) {
          swal({
            title: 'Sesión expirada',
            text: 'Tu sesión ha expirado. Por favor, inicia sesión nuevamente.',
            type: 'warning',
            confirmButtonColor: '#ffc107',
            confirmButtonText: 'Iniciar sesión'
          }).then(() => {
            this.router.navigate(['/login']);
          });
        } else if (error.status === 404) {
          // Si el carrito no existe, simplemente mostrar carrito vacío
          this.elementosCarrito = [];
          this.totalElementos = 0;
          this.totalPrecio = 0;
        } else {
          swal({
            title: 'Error al cargar carrito',
            text: 'Ha ocurrido un error al cargar tu carrito. Por favor, inténtalo de nuevo.',
            type: 'error',
            confirmButtonColor: '#dc3545',
            confirmButtonText: 'Aceptar'
          });
        }
      }
    });
  }

  /**
   * Actualiza la cantidad de un elemento del carrito
   */
  actualizarCantidad(elemento: CarritoElementoExtendido, event: any): void {
    const nuevaCantidad = parseInt(event.target.value);
    
    if (nuevaCantidad <= 0 || nuevaCantidad > 99) {
      event.target.value = elemento.cantidad;
      return;
    }

    this.elementoActualizando = elemento.id;
    
    this.carritoService.actualizarCantidad(elemento.id, nuevaCantidad).subscribe({
      next: (response: any) => {
        // Actualizar solo el elemento específico en lugar de recargar todo
        const elementoActualizado = response.elemento;
        const index = this.elementosCarrito.findIndex(item => item.id === elemento.id);
        if (index !== -1) {
          // Preservar el estado de showServices
          elementoActualizado.showServices = this.elementosCarrito[index].showServices;
          this.elementosCarrito[index] = elementoActualizado;
          this.recalcularTotales();
        }
        this.elementoActualizando = null;
      },
      error: (error) => {
        console.error('Error al actualizar cantidad:', error);
        event.target.value = elemento.cantidad; // Revertir cambio
        this.elementoActualizando = null;
      }
    });
  }

  /**
   * Recalcula los totales del carrito basándose en los elementos actuales
   */
  private recalcularTotales(): void {
    // Suma las cantidades individuales de cada elemento, no solo el número de elementos
    this.totalElementos = this.elementosCarrito.reduce((total, elemento) => total + elemento.cantidad, 0);
    this.totalPrecio = this.elementosCarrito.reduce((total, elemento) => total + elemento.precioTotal, 0);
  }

  /**
   * Alterna un servicio adicional para un elemento del carrito
   */
  toggleServicioAdicional(elemento: CarritoElementoExtendido, servicio: string): void {
    this.elementoActualizando = elemento.id;
    
    const servicios = {
      acabadoPremium: elemento.acabadoPremium,
      urgente: elemento.urgente,
      envioGratis: elemento.envioGratis
    };

    // Alternar el servicio específico
    switch(servicio) {
      case 'acabadoPremium':
        servicios.acabadoPremium = !servicios.acabadoPremium;
        break;
      case 'urgente':
        servicios.urgente = !servicios.urgente;
        break;
      case 'envioGratis':
        servicios.envioGratis = !servicios.envioGratis;
        break;
    }

    this.carritoService.actualizarServiciosAdicionales(elemento.id, servicios).subscribe({
      next: (response: any) => {
        // Actualizar solo el elemento específico en lugar de recargar todo
        const elementoActualizado = response.elemento;
        const index = this.elementosCarrito.findIndex(item => item.id === elemento.id);
        if (index !== -1) {
          // Preservar el estado de showServices
          elementoActualizado.showServices = this.elementosCarrito[index].showServices;
          this.elementosCarrito[index] = elementoActualizado;
          this.recalcularTotales();
        }
        this.elementoActualizando = null;
      },
      error: (error) => {
        console.error('Error al actualizar servicios adicionales:', error);
        this.elementoActualizando = null;
      }
    });
  }

  /**
   * Elimina un elemento del carrito
   */
  eliminarElemento(elemento: CarritoElementoExtendido): void {
    this.elementoEliminando = elemento.id;
    
    this.carritoService.eliminarElementoCarrito(elemento.id).subscribe({
      next: (response) => {
        // Eliminar solo el elemento específico del array local
        this.elementosCarrito = this.elementosCarrito.filter(item => item.id !== elemento.id);
        this.recalcularTotales();
        this.elementoEliminando = null;
      },
      error: (error) => {
        console.error('Error al eliminar elemento:', error);
        this.elementoEliminando = null;
      }
    });
  }

  /**
   * Vacía todo el carrito con confirmación SweetAlert
   */
  confirmarVaciarCarrito(): void {
    swal({
      title: '¿Vaciar carrito?',
      text: 'Esta acción eliminará todos los artículos del carrito y no se puede deshacer.',
      type: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#dc3545',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Sí, vaciar carrito',
      cancelButtonText: 'Cancelar',
      reverseButtons: true
    }).then((result: any) => {
      if (result.value) {
        this.vaciarCarrito();
      }
    });
  }

  /**
   * Ejecuta el vaciado del carrito
   */
  vaciarCarrito(): void {
    this.procesando = true;
    
    this.carritoService.vaciarCarrito(this.usuarioId).subscribe({
      next: (response) => {
        this.cargarCarrito();
        this.procesando = false;
        
        // Mostrar mensaje de éxito
        swal({
          title: '¡Carrito vaciado!',
          text: 'Todos los artículos han sido eliminados del carrito.',
          type: 'success',
          confirmButtonColor: '#28a745',
          confirmButtonText: 'Aceptar'
        });
      },
      error: (error) => {
        console.error('Error al vaciar carrito:', error);
        this.procesando = false;
        
        // Mostrar mensaje de error
        swal({
          title: 'Error',
          text: 'Ha ocurrido un error al vaciar el carrito. Por favor, inténtalo de nuevo.',
          type: 'error',
          confirmButtonColor: '#dc3545',
          confirmButtonText: 'Aceptar'
        });
      }
    });
  }

  /**
   * Cancela el vaciado del carrito
   */
  cancelarVaciarCarrito(): void {
    // No se necesita implementar esta función
  }

  /**
   * Navega a los detalles de un anuncio
   */
  verDetallesAnuncio(anuncioId: number): void {
    this.router.navigate(['/detalles-anuncio', anuncioId]);
  }

  /**
   * Procede al checkout (por implementar)
   */
  procederAlCheckout(): void {
    this.iniciarCheckout();
  }

  /**
   * Continúa comprando - navega a resultados de búsqueda
   */
  continuarComprando(): void {
    this.router.navigate(['/resultados-busqueda']);
  }

  /**
   * Formatea el precio para mostrar
   */
  formatearPrecio(precio: number): string {
    return precio.toFixed(2);
  }

  /**
   * Verifica si el carrito está vacío
   */
  carritoVacio(): boolean {
    return this.elementosCarrito.length === 0;
  }

  trackByElementoId(index: number, elemento: any): any {
    return elemento.id || index;
  }

  /**
   * Muestra el componente de PayPal para procesar el pago
   */
  mostrarPayPal: boolean = false;

  /**
   * Inicia el proceso de checkout mostrando PayPal
   */
  iniciarCheckout(): void {
    if (this.elementosCarrito.length === 0) {
      swal({
        title: 'Carrito vacío',
        text: 'Agrega productos al carrito antes de proceder al pago.',
        type: 'warning',
        confirmButtonColor: '#ffc107',
        confirmButtonText: 'Aceptar'
      });
      return;
    }

    this.mostrarPayPal = true;
  }

  /**
   * Cancela el proceso de checkout
   */
  cancelarCheckout(): void {
    this.mostrarPayPal = false;
  }

  /**
   * Maneja el evento de pago completado exitosamente
   */
  onPagoCompletado(checkoutData: any): void {
    this.procesando = true;
    
    // Añadir las notas del cliente a los datos del checkout
    const checkoutDataConNotas = {
      ...checkoutData,
      notasCliente: this.notasCliente.trim() || null
    };
    
    this.carritoService.procesarCheckout(checkoutDataConNotas).subscribe({
      next: (response) => {
        this.procesando = false;
        this.mostrarPayPal = false;
        
        // Limpiar las notas después del pago exitoso
        this.notasCliente = '';
        
        // Mostrar mensaje de éxito
        swal({
          title: '¡Pago realizado!',
          text: `Tu pedido ${response.pedido.numero_pedido} ha sido procesado exitosamente.`,
          type: 'success',
          confirmButtonColor: '#28a745',
          confirmButtonText: 'Aceptar'
        }).then(() => {
          // Recargar carrito (debería estar vacío)
          this.cargarCarrito();
        });
      },
      error: (error) => {
        this.procesando = false;
        console.error('Error al procesar pago:', error);
        
        swal({
          title: 'Error en el pago',
          text: 'Ha ocurrido un error al procesar tu pago. Por favor, inténtalo de nuevo.',
          type: 'error',
          confirmButtonColor: '#dc3545',
          confirmButtonText: 'Aceptar'
        });
      }
    });
  }

  /**
   * Maneja el evento de pago cancelado
   */
  onPagoCancelado(checkoutData: any): void {
    this.procesando = true;
    
    // Añadir las notas del cliente a los datos del checkout incluso en cancelación
    const checkoutDataConNotas = {
      ...checkoutData,
      notasCliente: this.notasCliente.trim() || null
    };
    
    this.carritoService.procesarCheckout(checkoutDataConNotas).subscribe({
      next: (response) => {
        this.procesando = false;
        this.mostrarPayPal = false;
        
        swal({
          title: 'Pago cancelado',
          text: 'Has cancelado el pago. Puedes intentar de nuevo cuando lo desees.',
          type: 'info',
          confirmButtonColor: '#17a2b8',
          confirmButtonText: 'Aceptar'
        });
      },
      error: (error) => {
        this.procesando = false;
        console.error('Error al registrar cancelación:', error);
      }
    });
  }

  /**
   * Maneja errores en el pago
   */
  onPagoError(error: any): void {
    console.error('Error en PayPal:', error);
    
    swal({
      title: 'Error en el pago',
      text: 'Ha ocurrido un error técnico durante el pago. Por favor, inténtalo de nuevo.',
      type: 'error',
      confirmButtonColor: '#dc3545',
      confirmButtonText: 'Aceptar'
    });
  }

  /**
   * Recarga el carrito manualmente (método de debug)
   */
  recargarCarrito(): void {
    console.log('=== DEBUG: Recargando carrito manualmente ===');
    this.cargarCarrito();
  }

  /**
   * Muestra información del usuario (método de debug)
   */
  mostrarInfoUsuario(): void {
    const usuario = this.authService.getCurrentUser();
    console.log('=== DEBUG: Información del usuario ===', usuario);
    
    swal({
      title: 'Información del Usuario',
      html: `
        <div style="text-align: left;">
          <p><strong>ID:</strong> ${this.authService.getCurrentUserId()}</p>
          <p><strong>Email:</strong> ${this.authService.getCurrentUserEmail()}</p>
          <p><strong>Usuario Completo:</strong></p>
          <pre style="background: #f5f5f5; padding: 10px; border-radius: 4px; font-size: 12px; text-align: left;">${JSON.stringify(usuario, null, 2)}</pre>
          <p><strong>Token:</strong> ${this.authService.getToken() ? 'Presente' : 'No presente'}</p>
          <p><strong>LocalStorage Usuario:</strong></p>
          <pre style="background: #f5f5f5; padding: 10px; border-radius: 4px; font-size: 12px; text-align: left;">${localStorage.getItem('usuario') || 'No encontrado'}</pre>
        </div>
      `,
      confirmButtonText: 'Cerrar',
      confirmButtonColor: '#5d4fff'
    });
  }
}
