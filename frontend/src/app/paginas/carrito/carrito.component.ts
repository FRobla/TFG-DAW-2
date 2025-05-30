import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { trigger, state, style, transition, animate } from '@angular/animations';
import { CarritoService, CarritoElemento, CarritoResponse } from './carrito.service';
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
  
  // ID del usuario (simulado - en el futuro vendrá del servicio de autenticación)
  usuarioId: number = 1;
  
  // Estados de interacción
  elementoEliminando: number | null = null;
  elementoActualizando: number | null = null;

  constructor(
    private carritoService: CarritoService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.cargarCarrito();
  }

  /**
   * Carga el carrito del usuario desde el backend
   */
  cargarCarrito(): void {
    this.cargando = true;
    
    this.carritoService.obtenerCarritoUsuario(this.usuarioId).subscribe({
      next: (response: CarritoResponse) => {
        this.elementosCarrito = response.carrito;
        this.totalElementos = response.totalElementos;
        this.totalPrecio = response.totalPrecio;
        this.cargando = false;
      },
      error: (error) => {
        console.error('Error al cargar el carrito:', error);
        this.cargando = false;
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
    this.totalElementos = this.elementosCarrito.length;
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
    // TODO: Implementar navegación al checkout/pago
    console.log('Proceder al checkout...');
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
}
