import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CarritoService, CarritoElemento, CarritoResponse } from './carrito.service';

@Component({
  selector: 'app-carrito',
  standalone: false,
  templateUrl: './carrito.component.html',
  styleUrl: './carrito.component.css'
})
export class CarritoComponent implements OnInit {
  
  // Datos del carrito
  elementosCarrito: CarritoElemento[] = [];
  totalElementos: number = 0;
  totalPrecio: number = 0;
  
  // Estado de carga
  cargando: boolean = true;
  procesando: boolean = false;
  
  // ID del usuario (simulado - en el futuro vendrá del servicio de autenticación)
  usuarioId: number = 1;
  
  // Estados de interacción
  mostrarConfirmacionVaciar: boolean = false;
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
  actualizarCantidad(elemento: CarritoElemento, event: any): void {
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
  toggleServicioAdicional(elemento: CarritoElemento, servicio: string): void {
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
  eliminarElemento(elemento: CarritoElemento): void {
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
   * Vacía todo el carrito
   */
  confirmarVaciarCarrito(): void {
    this.mostrarConfirmacionVaciar = true;
  }

  /**
   * Ejecuta el vaciado del carrito
   */
  vaciarCarrito(): void {
    this.procesando = true;
    this.mostrarConfirmacionVaciar = false;
    
    this.carritoService.vaciarCarrito(this.usuarioId).subscribe({
      next: (response) => {
        this.cargarCarrito();
        this.procesando = false;
      },
      error: (error) => {
        console.error('Error al vaciar carrito:', error);
        this.procesando = false;
      }
    });
  }

  /**
   * Cancela el vaciado del carrito
   */
  cancelarVaciarCarrito(): void {
    this.mostrarConfirmacionVaciar = false;
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
   * Calcula el tiempo estimado de entrega más alto
   */
  calcularTiempoEntregaMaximo(): string {
    // Por ahora retorna un valor fijo, se puede mejorar con datos reales
    return '5-7 días laborables';
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
