import { Component, OnInit, ViewChild } from '@angular/core';
import { PedidoService } from './pedido.service';
import { Pedido } from './pedido';
import swal from 'sweetalert2';
import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';
import { formatDate } from '@angular/common';

@Component({
  selector: 'app-pedido',
  standalone: false,
  templateUrl: './pedido.component.html',
  styleUrls: ['./pedido.component.css']
})
export class PedidoComponent implements OnInit {

  // Lista de pedidos
  pedidos: Pedido[] = [];
  pedidosFiltrados: Pedido[] = [];
  pedidosPaginados: Pedido[] = [];
  
  // Pedido actual para crear/editar
  pedidoActual: Pedido = new Pedido();
  
  // Estado del modal
  modalVisible: boolean = false;
  modalDetallesVisible: boolean = false;
  modoEdicion: boolean = false;
  editando: boolean = false;
  
  // Estado de carga
  cargando: boolean = true;
  
  // Estado para el modal de confirmación
  modalConfirmacionVisible: boolean = false;
  tituloConfirmacion: string = '';
  mensajeConfirmacion: string = '';
  accionConfirmacion: Function = () => {};
  
  // Paginación
  paginaActual: number = 1;
  itemsPorPagina: number = 10;
  totalPaginas: number = 1;
  
  // Búsqueda y filtrado
  termino: string = '';
  filtroEstado: string = '';
  filtroUsuario: string = '';
  
  // Ordenación
  ordenarPor: string = 'fecha';
  ordenAscendente: boolean = false; // Más recientes primero
  
  // Estados y métodos de pago disponibles
  estadosDisponibles: string[] = [];
  metodosPagoDisponibles: string[] = [];
  
  @ViewChild('pedidoForm') pedidoForm!: NgForm;

  constructor(private pedidoService: PedidoService, private router: Router) { }

  ngOnInit(): void {
    this.cargarPedidos();
    this.estadosDisponibles = this.pedidoService.getEstadosDisponibles();
    this.metodosPagoDisponibles = this.pedidoService.getMetodosPagoDisponibles();
  }

  /**
   * Carga todos los pedidos desde el servicio
   */
  cargarPedidos(): void {
    this.cargando = true;
    this.pedidoService.getPedidos().subscribe({
      next: (pedidosData) => {
        this.pedidos = pedidosData;
        this.filtrarPedidos();
        this.calcularTotalPaginas();
        this.aplicarPaginacion();
        this.cargando = false;
      },
      error: (error) => {
        this.cargando = false;
        console.error('Error al cargar pedidos', error);
        swal('Error', 'Hubo un problema al cargar los pedidos', 'error');
      }
    });
  }

  /**
   * Filtra los pedidos según los criterios establecidos
   */
  filtrarPedidos(): void {
    let pedidosFiltrados = [...this.pedidos];
    
    // Aplicar filtro de búsqueda
    if (this.termino && this.termino.trim() !== '') {
      const terminoLower = this.termino.toLowerCase();
      pedidosFiltrados = pedidosFiltrados.filter(pedido => 
        pedido.numero_pedido.toLowerCase().includes(terminoLower) ||
        pedido.getNombreCliente().toLowerCase().includes(terminoLower) ||
        pedido.estado.toLowerCase().includes(terminoLower) ||
        pedido.direccion_envio.toLowerCase().includes(terminoLower) ||
        pedido.metodo_pago.toLowerCase().includes(terminoLower) ||
        (pedido.notas_cliente && pedido.notas_cliente.toLowerCase().includes(terminoLower)) ||
        (pedido.notas_internas && pedido.notas_internas.toLowerCase().includes(terminoLower))
      );
    }
    
    // Aplicar filtro por estado
    if (this.filtroEstado && this.filtroEstado.trim() !== '') {
      pedidosFiltrados = pedidosFiltrados.filter(pedido => 
        pedido.estado === this.filtroEstado
      );
    }
    
    // Aplicar filtro por usuario
    if (this.filtroUsuario && this.filtroUsuario.trim() !== '') {
      const filtroUsuarioLower = this.filtroUsuario.toLowerCase();
      pedidosFiltrados = pedidosFiltrados.filter(pedido => 
        pedido.getNombreCliente().toLowerCase().includes(filtroUsuarioLower)
      );
    }
    
    // Aplicar ordenación
    pedidosFiltrados.sort((a, b) => {
      let valorA, valorB;
      
      switch (this.ordenarPor) {
        case 'numero':
          valorA = a.numero_pedido.toLowerCase();
          valorB = b.numero_pedido.toLowerCase();
          break;
        case 'fecha':
          valorA = new Date(a.fechaPedido).getTime();
          valorB = new Date(b.fechaPedido).getTime();
          break;
        case 'estado':
          valorA = a.estado;
          valorB = b.estado;
          break;
        case 'total':
          valorA = a.total;
          valorB = b.total;
          break;
        case 'cliente':
          valorA = a.getNombreCliente().toLowerCase();
          valorB = b.getNombreCliente().toLowerCase();
          break;
        case 'notas':
          // Concatenar notas del cliente e internas para ordenación
          const notasA = ((a.notas_cliente || '') + ' ' + (a.notas_internas || '')).toLowerCase().trim();
          const notasB = ((b.notas_cliente || '') + ' ' + (b.notas_internas || '')).toLowerCase().trim();
          valorA = notasA || 'zzz'; // Los sin notas van al final
          valorB = notasB || 'zzz';
          break;
        default:
          valorA = a.id;
          valorB = b.id;
          break;
      }
      
      if (valorA < valorB) return this.ordenAscendente ? -1 : 1;
      if (valorA > valorB) return this.ordenAscendente ? 1 : -1;
      return 0;
    });
    
    this.pedidosFiltrados = pedidosFiltrados;
    this.calcularTotalPaginas();
    this.paginaActual = 1; // Reset página al filtrar
    this.aplicarPaginacion();
  }
  
  /**
   * Recibe el término de búsqueda desde el componente navbar-admin
   */
  aplicarFiltro(termino: string): void {
    this.termino = termino;
    this.filtrarPedidos();
  }

  /**
   * Calcula el número total de páginas
   */
  calcularTotalPaginas(): void {
    this.totalPaginas = Math.ceil(this.pedidosFiltrados.length / this.itemsPorPagina);
    if (this.totalPaginas === 0) this.totalPaginas = 1;
  }

  /**
   * Aplica la paginación
   */
  aplicarPaginacion(): void {
    const inicio = (this.paginaActual - 1) * this.itemsPorPagina;
    const fin = inicio + this.itemsPorPagina;
    this.pedidosPaginados = this.pedidosFiltrados.slice(inicio, fin);
  }
  
  /**
   * Obtiene los pedidos paginados para la página actual
   */
  getPedidosPaginados(): Pedido[] {
    return this.pedidosPaginados;
  }
  
  /**
   * Cambia el criterio de ordenación
   */
  cambiarOrden(campo: string): void {
    if (this.ordenarPor === campo) {
      this.ordenAscendente = !this.ordenAscendente;
    } else {
      this.ordenarPor = campo;
      this.ordenAscendente = true;
    }
    this.filtrarPedidos();
  }
  
  /**
   * Filtra los pedidos por estado
   */
  filtrarPorEstado(): void {
    this.filtrarPedidos();
  }

  /**
   * Cambia la página actual
   */
  cambiarPagina(pagina: number): void {
    if (pagina >= 1 && pagina <= this.totalPaginas) {
      this.paginaActual = pagina;
      this.aplicarPaginacion();
    }
  }
  
  /**
   * Cambia el número de items por página
   */
  cambiarItemsPorPagina(): void {
    this.paginaActual = 1;
    this.calcularTotalPaginas();
    this.aplicarPaginacion();
  }

  /**
   * Obtiene el conteo de pedidos por estado
   */
  getPedidosPorEstado(estado: string): number {
    return this.pedidos.filter(pedido => pedido.estado === estado).length;
  }

  /**
   * Obtiene el conteo de pedidos del mes actual
   */
  getPedidosEsteMes(): number {
    const hoy = new Date();
    const inicioMes = new Date(hoy.getFullYear(), hoy.getMonth(), 1);
    return this.pedidos.filter(pedido => 
      new Date(pedido.fechaPedido) >= inicioMes
    ).length;
  }

  /**
   * Obtiene las ventas totales del mes
   */
  getVentasTotalesMes(): number {
    const hoy = new Date();
    const inicioMes = new Date(hoy.getFullYear(), hoy.getMonth(), 1);
    return this.pedidos
      .filter(pedido => 
        new Date(pedido.fechaPedido) >= inicioMes && 
        pedido.estado !== 'cancelado'
      )
      .reduce((total, pedido) => total + pedido.total, 0);
  }

  /**
   * Abre el modal para crear un nuevo pedido
   */
  abrirModalCrearPedido(): void {
    this.pedidoActual = new Pedido();
    this.pedidoActual.numero_pedido = this.generarNumeroPedido();
    this.modoEdicion = false;
    this.modalVisible = true;
  }

  /**
   * Genera un número de pedido único
   */
  generarNumeroPedido(): string {
    const fecha = new Date();
    const year = fecha.getFullYear().toString().slice(-2);
    const month = (fecha.getMonth() + 1).toString().padStart(2, '0');
    const day = fecha.getDate().toString().padStart(2, '0');
    const random = Math.floor(Math.random() * 1000).toString().padStart(3, '0');
    return `PED${year}${month}${day}${random}`;
  }

  /**
   * Edita un pedido existente
   */
  editarPedido(pedido: Pedido): void {
    this.pedidoActual = Object.assign(new Pedido(), pedido);
    this.modoEdicion = true;
    this.modalVisible = true;
  }

  /**
   * Ver detalles de un pedido
   */
  verDetallesPedido(pedido: Pedido): void {
    this.pedidoActual = pedido;
    this.modalDetallesVisible = true;
  }

  /**
   * Guarda un pedido (crear o actualizar)
   */
  guardarPedido(): void {
    if (this.pedidoForm && !this.pedidoForm.valid) {
      swal('Error', 'Por favor, completa todos los campos requeridos', 'error');
      return;
    }

    if (this.modoEdicion) {
      this.actualizarPedido();
    } else {
      this.crearPedido();
    }
  }

  /**
   * Crea un nuevo pedido
   */
  crearPedido(): void {
    // Establecer fecha actual si no está establecida
    if (!this.pedidoActual.fecha_pedido) {
      this.pedidoActual.fechaPedido = new Date();
    }

    this.pedidoService.crearPedido(this.pedidoActual).subscribe({
      next: (pedidoCreado) => {
        this.pedidos.push(pedidoCreado);
        this.filtrarPedidos();
        this.cerrarModal();
        swal('Éxito', 'Pedido creado correctamente', 'success');
      },
      error: (error) => {
        console.error('Error al crear pedido', error);
        const mensaje = error.error?.mensaje || 'No se pudo crear el pedido';
        swal('Error', mensaje, 'error');
      }
    });
  }

  /**
   * Actualiza un pedido existente
   */
  actualizarPedido(): void {
    this.pedidoService.actualizarPedido(this.pedidoActual).subscribe({
      next: (pedidoActualizado) => {
        const index = this.pedidos.findIndex(p => p.id === pedidoActualizado.id);
        if (index !== -1) {
          this.pedidos[index] = pedidoActualizado;
        }
        this.filtrarPedidos();
        this.cerrarModal();
        swal('Éxito', 'Pedido actualizado correctamente', 'success');
      },
      error: (error) => {
        console.error('Error al actualizar pedido', error);
        const mensaje = error.error?.mensaje || 'No se pudo actualizar el pedido';
        swal('Error', mensaje, 'error');
      }
    });
  }

  /**
   * Actualiza el estado de un pedido
   */
  actualizarEstadoPedido(pedido: Pedido, nuevoEstado: string): void {
    swal({
      title: 'Confirmar cambio de estado',
      text: `¿Estás seguro de cambiar el estado a "${this.getEstadoTexto(nuevoEstado)}"?`,
      type: 'question',
      showCancelButton: true,
      confirmButtonText: 'Sí, cambiar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.value) {
        this.pedidoService.actualizarEstado(pedido.id, nuevoEstado).subscribe(
          pedidoActualizado => {
            const index = this.pedidos.findIndex(p => p.id === pedidoActualizado.id);
            if (index !== -1) {
              this.pedidos[index] = pedidoActualizado;
            }
            this.filtrarPedidos();
            swal('Éxito', 'Estado actualizado correctamente', 'success');
          },
          error => {
            console.error('Error al actualizar estado', error);
            swal('Error', 'No se pudo actualizar el estado', 'error');
          }
        );
      }
    });
  }

  /**
   * Completa un pedido
   */
  completarPedido(pedido: Pedido): void {
    this.pedidoService.completarPedido(pedido.id).subscribe(
      pedidoActualizado => {
        const index = this.pedidos.findIndex(p => p.id === pedidoActualizado.id);
        if (index !== -1) {
          this.pedidos[index] = pedidoActualizado;
        }
        this.filtrarPedidos();
        swal('Éxito', 'Pedido completado correctamente', 'success');
      },
      error => {
        console.error('Error al completar pedido', error);
        swal('Error', 'No se pudo completar el pedido', 'error');
      }
    );
  }

  /**
   * Cancela un pedido
   */
  cancelarPedido(pedido: Pedido): void {
    swal({
      title: 'Cancelar pedido',
      text: 'Motivo de cancelación (opcional):',
      input: 'textarea',
      inputPlaceholder: 'Escribe el motivo...',
      showCancelButton: true,
      confirmButtonText: 'Cancelar pedido',
      cancelButtonText: 'Cerrar'
    }).then((result) => {
      if (result.value !== undefined) {
        this.pedidoService.cancelarPedido(pedido.id, result.value || '').subscribe(
          pedidoActualizado => {
            const index = this.pedidos.findIndex(p => p.id === pedidoActualizado.id);
            if (index !== -1) {
              this.pedidos[index] = pedidoActualizado;
            }
            this.filtrarPedidos();
            swal('Éxito', 'Pedido cancelado correctamente', 'success');
          },
          error => {
            console.error('Error al cancelar pedido', error);
            swal('Error', 'No se pudo cancelar el pedido', 'error');
          }
        );
      }
    });
  }

  /**
   * Confirma la eliminación de un pedido
   */
  confirmarEliminar(pedido: Pedido): void {
    swal({
      title: '¿Estás seguro?',
      text: `Se eliminará el pedido ${pedido.numero_pedido}. Esta acción no se puede deshacer.`,
      type: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.value) {
        this.eliminarPedido(pedido);
      }
    });
  }

  /**
   * Elimina un pedido
   */
  eliminarPedido(pedido: Pedido): void {
    this.pedidoService.eliminarPedido(pedido.id).subscribe({
      next: () => {
        this.pedidos = this.pedidos.filter(p => p.id !== pedido.id);
        this.filtrarPedidos();
        swal('Eliminado', 'El pedido ha sido eliminado correctamente', 'success');
      },
      error: (error) => {
        console.error('Error al eliminar pedido', error);
        const mensaje = error.error?.mensaje || 'No se pudo eliminar el pedido';
        swal('Error', mensaje, 'error');
      }
    });
  }

  /**
   * Cierra el modal
   */
  cerrarModal(): void {
    this.modalVisible = false;
    this.modalDetallesVisible = false;
    this.pedidoActual = new Pedido();
    this.modoEdicion = false;
  }

  /**
   * Obtiene el texto del estado en español
   */
  getEstadoTexto(estado: string): string {
    const estados: { [key: string]: string } = {
      'pendiente': 'Pendiente de Pago',
      'en_proceso': 'En Proceso',
      'completado': 'Completado',
      'cancelado': 'Cancelado'
    };
    return estados[estado] || estado;
  }

  /**
   * Obtiene el texto del método de pago en español
   */
  getMetodoPagoTexto(metodo: string): string {
    const metodos: { [key: string]: string } = {
      'paypal': 'PayPal',
      'tarjeta': 'Tarjeta de Crédito',
      'transferencia': 'Transferencia Bancaria',
      'efectivo': 'Efectivo'
    };
    return metodos[metodo] || metodo;
  }

  /**
   * Confirma el pago de un pedido (cambio manual desde dashboard)
   */
  confirmarPago(pedido: Pedido): void {
    swal({
      title: 'Confirmar pago',
      text: `¿Estás seguro de confirmar el pago del pedido ${pedido.numero_pedido}?`,
      type: 'question',
      showCancelButton: true,
      confirmButtonText: 'Sí, confirmar pago',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.value) {
        this.pedidoService.confirmarPago(pedido.id).subscribe({
          next: (response) => {
            const index = this.pedidos.findIndex(p => p.id === response.pedido.id);
            if (index !== -1) {
              this.pedidos[index] = response.pedido;
            }
            this.filtrarPedidos();
            swal('Éxito', response.mensaje || 'Pago confirmado correctamente', 'success');
          },
          error: (error) => {
            console.error('Error al confirmar pago', error);
            const mensaje = error.error?.mensaje || 'No se pudo confirmar el pago';
            swal('Error', mensaje, 'error');
          }
        });
      }
    });
  }

  /**
   * Marca un pedido como completado (cambio manual desde dashboard)
   */
  marcarCompletado(pedido: Pedido): void {
    swal({
      title: 'Marcar como completado',
      text: `¿Estás seguro de marcar como completado el pedido ${pedido.numero_pedido}?`,
      type: 'question',
      showCancelButton: true,
      confirmButtonText: 'Sí, marcar completado',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.value) {
        this.pedidoService.marcarCompletado(pedido.id).subscribe({
          next: (response) => {
            const index = this.pedidos.findIndex(p => p.id === response.pedido.id);
            if (index !== -1) {
              this.pedidos[index] = response.pedido;
            }
            this.filtrarPedidos();
            swal('Éxito', response.mensaje || 'Pedido marcado como completado', 'success');
          },
          error: (error) => {
            console.error('Error al marcar como completado', error);
            const mensaje = error.error?.mensaje || 'No se pudo marcar como completado';
            swal('Error', mensaje, 'error');
          }
        });
      }
    });
  }

  /**
   * Alterna la expansión de las notas del cliente
   */
  toggleNotasCliente(pedido: Pedido): void {
    pedido.notasClienteExpandidas = !pedido.notasClienteExpandidas;
  }

  /**
   * Alterna la expansión de las notas internas
   */
  toggleNotasInternas(pedido: Pedido): void {
    pedido.notasInternasExpandidas = !pedido.notasInternasExpandidas;
  }
}
