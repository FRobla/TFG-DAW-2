import { Component, OnInit, ViewChild } from '@angular/core';
import { ImpresoraService } from './impresora.service';
import { Impresora } from './impresora';
import swal from 'sweetalert2';
import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';
import { formatDate } from '@angular/common';
import { CategoriaService } from '../categoria/categoria.service';
import { Categoria } from '../categoria/categoria';

@Component({
  selector: 'app-impresora',
  standalone: false,
  templateUrl: './impresora.component.html',
  styleUrl: './impresora.component.css'
})
export class ImpresoraComponent implements OnInit {

  // Lista de impresoras
  impresoras: Impresora[] = [];
  impresorasFiltradas: Impresora[] = [];
  
  // Control de edición y vista
  editando: boolean = false;
  
  // Lista de categorías para el selector
  categorias: Categoria[] = [];
  
  // Impresora actual para crear/editar
  impresoraActual: Impresora = new Impresora();
  
  // Estado del modal
  modalVisible: boolean = false;
  modoEdicion: boolean = false;
  
  // Estado para el modal de confirmación
  modalConfirmacionVisible: boolean = false;
  tituloConfirmacion: string = '';
  mensajeConfirmacion: string = '';
  accionConfirmacion: Function = () => {};
  
  // Paginación
  paginaActual: number = 1;
  itemsPorPagina: number = 10;
  totalPaginas: number = 1;
  
  // Búsqueda y filtros
  termino: string = '';
  filtroCategoria: number = 0;
  
  // Ordenación
  ordenarPor: string = 'modelo';
  ordenAscendente: boolean = true;
  
  // Estado de carga
  cargando: boolean = false;
  
  @ViewChild('impresoraForm') impresoraForm!: NgForm;

  constructor(
    private impresoraService: ImpresoraService, 
    private categoriaService: CategoriaService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.cargarImpresoras();
    this.cargarCategorias();
  }

  /**
   * Carga todas las impresoras desde el servicio
   */
  cargarImpresoras(): void {
    // No activamos el estado de carga para permitir view-transitions fluidas
    // this.cargando = true;
    this.impresoraService.getImpresoras().subscribe(
      impresorasData => {
        // Transformamos los datos del backend a objetos Impresora
        this.impresoras = impresorasData.map(data => Impresora.fromBackend(data));
        this.filtrarImpresoras();
        this.calcularTotalPaginas();
        this.aplicarPaginacion();
        
        // Aseguramos que cargando está desactivado para no interferir con las transiciones
        this.cargando = false;
      },
      error => {
        this.cargando = false;
        console.error('Error al cargar impresoras', error);
        swal('Error', 'Hubo un problema al cargar las impresoras', 'error');
      }
    );
  }
  
  /**
   * Carga todas las categorías para el selector
   */
  cargarCategorias(): void {
    this.categoriaService.getCategorias().subscribe(
      categoriasData => {
        this.categorias = categoriasData;
      },
      error => {
        console.error('Error al cargar categorías', error);
        swal('Error', 'Hubo un problema al cargar las categorías', 'error');
      }
    );
  }

  /**
   * Filtra las impresoras según el término de búsqueda
   */
  filtrarImpresoras(): void {
    // Primero aplicar filtro por texto de búsqueda
    let resultado = [...this.impresoras];
    
    if (this.termino && this.termino.trim() !== '') {
      const terminoLower = this.termino.toLowerCase();
      resultado = resultado.filter(impresora => 
        impresora.modelo.toLowerCase().includes(terminoLower) ||
        impresora.marca.toLowerCase().includes(terminoLower) ||
        impresora.tecnologiaImpresion?.toLowerCase().includes(terminoLower) ||
        impresora.descripcion?.toLowerCase().includes(terminoLower)
      );
    }
    
    // Luego aplicar filtro por categoría si está seleccionada
    if (this.filtroCategoria > 0) {
      resultado = resultado.filter(impresora => impresora.categoriaId === this.filtroCategoria);
    }
    
    // Aplicar ordenación
    resultado = this.ordenarImpresoras(resultado);
    
    this.impresorasFiltradas = resultado;
    this.calcularTotalPaginas();
    this.aplicarPaginacion();
  }
  
  /**
   * Recibe el término de búsqueda desde el componente navbar-admin
   */
  aplicarFiltro(termino: string): void {
    this.termino = termino;
    this.filtrarImpresoras();
  }

  /**
   * Filtra las impresoras por categoría
   */
  filtrarPorCategoria(): void {
    this.paginaActual = 1;
    this.filtrarImpresoras();
  }
  
  /**
   * Ordena las impresoras según el criterio seleccionado
   */
  ordenarImpresoras(impresoras: Impresora[]): Impresora[] {
    return [...impresoras].sort((a, b) => {
      let valorA, valorB;
      
      switch (this.ordenarPor) {
        case 'modelo':
          valorA = a.modelo.toLowerCase();
          valorB = b.modelo.toLowerCase();
          break;
        case 'precio':
          valorA = a.precio || 0;
          valorB = b.precio || 0;
          break;
        case 'marca':
          valorA = a.marca.toLowerCase();
          valorB = b.marca.toLowerCase();
          break;
        default:
          valorA = a.id;
          valorB = b.id;
      }
      
      if (valorA < valorB) return this.ordenAscendente ? -1 : 1;
      if (valorA > valorB) return this.ordenAscendente ? 1 : -1;
      return 0;
    });
  }
  
  /**
   * Cambia el criterio de ordenación, si es el mismo invierte el orden
   */
  cambiarOrden(criterio: string): void {
    if (this.ordenarPor === criterio) {
      this.ordenAscendente = !this.ordenAscendente;
    } else {
      this.ordenarPor = criterio;
      this.ordenAscendente = true;
    }
    this.filtrarImpresoras();
  }
  
  /**
   * Calcula el número total de páginas basado en el número de impresoras y el tamaño de página
   */
  calcularTotalPaginas(): void {
    this.totalPaginas = Math.ceil(this.impresorasFiltradas.length / this.itemsPorPagina);
    if (this.totalPaginas === 0) this.totalPaginas = 1;
  }

  /**
   * Aplica la paginación a la lista filtrada de impresoras
   */
  aplicarPaginacion(): void {
    const inicio = (this.paginaActual - 1) * this.itemsPorPagina;
    const fin = inicio + this.itemsPorPagina;
    this.impresorasFiltradas = this.impresorasFiltradas.slice(inicio, fin);
  }

  /**
   * Cambia la página actual
   */
  cambiarPagina(pagina: number): void {
    this.paginaActual = pagina;
    this.cargarImpresoras();
  }

  /**
   * Cambia el número de items por página
   */
  cambiarItemsPorPagina(): void {
    this.paginaActual = 1;
    this.calcularTotalPaginas();
    this.cargarImpresoras();
  }

  /**
   * Abre el modal para crear una nueva impresora
   */
  abrirModalCrearImpresora(): void {
    this.impresoraActual = new Impresora();
    this.modoEdicion = false;
    this.modalVisible = true;
  }

  /**
   * Abre el modal para editar una impresora existente
   */
  editarImpresora(impresora: Impresora): void {
    // Creamos una nueva instancia para evitar modificar el original por referencia
    this.impresoraActual = new Impresora({...impresora});
    this.modoEdicion = true;
    this.modalVisible = true;
  }

  /**
   * Cierra el modal y limpia el formulario
   */
  cerrarModal(): void {
    this.modalVisible = false;
    this.impresoraActual = new Impresora();
    if (this.impresoraForm) {
      this.impresoraForm.resetForm();
    }
  }

  /**
   * Guarda la impresora actual (creación o edición)
   */
  guardarImpresora(): void {
    if (this.modoEdicion) {
      this.impresoraService.updateImpresora(this.impresoraActual.id, this.impresoraActual).subscribe(
        response => {
          this.cerrarModal();
          this.cargarImpresoras();
          swal('¡Actualizada!', `La impresora ha sido actualizada con éxito`, 'success');
        },
        error => {
          console.error('Error al actualizar la impresora', error);
          swal('Error', 'Hubo un problema al actualizar la impresora', 'error');
        }
      );
    } else {
      this.impresoraService.createImpresora(this.impresoraActual).subscribe(
        response => {
          this.cerrarModal();
          this.cargarImpresoras();
          swal('¡Creada!', `La impresora ha sido creada con éxito`, 'success');
        },
        error => {
          console.error('Error al crear la impresora', error);
          swal('Error', 'Hubo un problema al crear la impresora', 'error');
        }
      );
    }
  }

  /**
   * Muestra un diálogo de confirmación para eliminar una impresora
   */
  confirmarEliminar(impresora: Impresora): void {
    this.tituloConfirmacion = '¿Eliminar impresora?';
    this.mensajeConfirmacion = `¿Está seguro que desea eliminar la impresora ${impresora.modelo}?`;
    this.accionConfirmacion = () => {
      this.eliminarImpresora(impresora);
    };
    this.modalConfirmacionVisible = true;
  }

  /**
   * Muestra un diálogo de confirmación para eliminar todas las impresoras
   */
  confirmarEliminarTodos(): void {
    this.tituloConfirmacion = '¿Eliminar todas las impresoras?';
    this.mensajeConfirmacion = '¿Está seguro que desea eliminar todas las impresoras? Esta acción no se puede deshacer.';
    this.accionConfirmacion = () => {
      this.eliminarTodos();
    };
    this.modalConfirmacionVisible = true;
  }

  /**
   * Ejecuta la acción de confirmación actual
   */
  ejecutarAccionConfirmacion(): void {
    this.accionConfirmacion();
    this.modalConfirmacionVisible = false;
  }

  /**
   * Cancela la acción de confirmación
   */
  cancelarConfirmacion(): void {
    this.modalConfirmacionVisible = false;
  }

  /**
   * Elimina una impresora
   */
  eliminarImpresora(impresora: Impresora): void {
    this.impresoraService.deleteImpresora(impresora.id).subscribe(
      response => {
        // Eliminar la impresora de la lista local
        this.impresoras = this.impresoras.filter(i => i.id !== impresora.id);
        this.filtrarImpresoras();
        this.calcularTotalPaginas();
        this.aplicarPaginacion();
        
        swal('Impresora eliminada', `La impresora ${impresora.modelo} ha sido eliminada con éxito`, 'success');
      },
      error => {
        console.error('Error al eliminar la impresora', error);
        swal('Error', 'No se pudo eliminar la impresora', 'error');
      }
    );
  }

  /**
   * Elimina todas las impresoras
   */
  eliminarTodos(): void {
    this.impresoraService.deleteAllImpresoras().subscribe(
      response => {
        this.impresoras = [];
        this.impresorasFiltradas = [];
        this.calcularTotalPaginas();
        
        swal('Impresoras eliminadas', 'Todas las impresoras han sido eliminadas con éxito', 'success');
      },
      error => {
        console.error('Error al eliminar las impresoras', error);
        swal('Error', 'No se pudieron eliminar las impresoras', 'error');
      }
    );
  }

  /**
   * Navega a la vista de detalles de la impresora
   */
  verDetallesImpresora(impresora: Impresora): void {
    this.router.navigate(['/impresora', impresora.id]);
  }

  /**
   * Obtiene el recuento de impresoras por marca
   */
  getImpresorasPorMarca(marca: string): number {
    return this.impresoras.filter(impresora => impresora.marca === marca).length;
  }

  /**
   * Obtiene el precio medio de las impresoras
   */
  getPrecioMedio(): number {
    if (this.impresoras.length === 0) return 0;
    const suma = this.impresoras.reduce((total, impresora) => total + impresora.precio, 0);
    return Math.round((suma / this.impresoras.length) * 100) / 100;
  }

  /**
   * Obtiene el recuento de impresoras en stock
   */
  getImpresorasEnStock(): number {
    return this.impresoras.filter(impresora => impresora.enStock).length;
  }

  /**
   * Exporta las impresoras a un archivo CSV
   */
  exportarImpresoras(): void {
    // Cabecera del CSV
    let csv = 'ID,Modelo,Marca,Fecha Fabricación,Precio,Volumen Impresión,Tecnología,En Stock\n';
    
    // Datos
    this.impresoras.forEach(impresora => {
      csv += `${impresora.id},${impresora.modelo},${impresora.marca},${impresora.fechaFabricacion},${impresora.precio},${impresora.volumenImpresion || ''},${impresora.tecnologiaImpresion || ''},${impresora.enStock}\n`;
    });
    
    // Crear y descargar el archivo
    const blob = new Blob([csv], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'impresoras.csv';
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    window.URL.revokeObjectURL(url);
  }

  /**
   * Devuelve el nombre de una categoría dada su ID
   */
  getNombreCategoria(categoriaId: number): string {
    const categoria = this.categorias.find(c => c.id === categoriaId);
    return categoria ? categoria.nombre : 'Sin categoría';
  }
}
