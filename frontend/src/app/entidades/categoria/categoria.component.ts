import { Component, OnInit, ViewChild } from '@angular/core';
import { trigger, transition, style, animate } from '@angular/animations';
import { Categoria } from './categoria';
import { CategoriaService } from './categoria.service';
import swal from 'sweetalert2';
import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';
import { formatDate } from '@angular/common';

@Component({
  selector: 'app-categoria',
  standalone: false,
  templateUrl: './categoria.component.html',
  styleUrls: ['./categoria.component.css'],
  animations: [
    trigger('viewTransition', [
      transition(':enter', [
        style({ opacity: 0, transform: 'translateY(20px)' }),
        animate('300ms ease-in-out', style({ opacity: 1, transform: 'translateY(0)' }))
      ]),
      transition(':leave', [
        animate('300ms ease-in-out', style({ opacity: 0, transform: 'translateY(-20px)' }))
      ])
    ])
  ]
})
export class CategoriasComponent implements OnInit {

  // Lista de categorías
  categorias: Categoria[] = [];
  categoriasFiltradas: Categoria[] = [];
  categoriasPaginadas: Categoria[] = [];
  
  // Categoría actual para crear/editar
  categoriaActual: Categoria = new Categoria();
  
  // Estado del modal
  modalVisible: boolean = false;
  modoEdicion: boolean = false;
  editando: boolean = false;
  
  // Estado para el modal de confirmación
  modalConfirmacionVisible: boolean = false;
  
  // Estado de carga
  cargando: boolean = false;
  tituloConfirmacion: string = '';
  mensajeConfirmacion: string = '';
  accionConfirmacion: Function = () => {};
  
  // Paginación
  paginaActual: number = 1;
  itemsPorPagina: number = 10;
  totalPaginas: number = 1;
  
  // Búsqueda y filtrado
  termino: string = '';
  busquedaNombre: string = '';
  
  // Ordenación
  ordenarPor: string = 'nombre';
  ordenAscendente: boolean = true;
  
  // Datos de servicios (simulados)
  serviciosPorCategoria: Map<number, number> = new Map();
  // Datos reales de conteo de anuncios por categoría
  conteoAnunciosPorCategoria: Map<number, number> = new Map();
  
  @ViewChild('categoriaForm') categoriaForm!: NgForm;

  constructor(private categoriaService: CategoriaService, private router: Router) { }

  ngOnInit(): void {
    this.cargarCategorias();
    this.cargarConteoAnuncios();
  }

  /**
   * Carga todas las categorías desde el servicio
   */
  cargarCategorias(): void {
    // No activamos el estado de carga para permitir view-transitions fluidas
    this.categoriaService.getCategorias().subscribe(
      categorias => {
        this.categorias = categorias;
        this.filtrarCategorias();
        this.calcularTotalPaginas();
        this.aplicarPaginacion();
        
        // Aseguramos que cargando esté desactivado para no interferir con las transiciones
        this.cargando = false;
      },
      error => {
        this.cargando = false;
        console.error('Error al cargar categorías', error);
        swal('Error', 'Hubo un problema al cargar las categorías', 'error');
      }
    );
  }

  /**
   * Carga el conteo real de anuncios por categoría desde el backend
   */
  cargarConteoAnuncios(): void {
    this.categoriaService.getCategoriasConConteoAnuncios().subscribe(
      categoriasConConteo => {
        console.log('Datos recibidos del backend:', categoriasConConteo);
        
        // Limpiamos el mapa anterior
        this.conteoAnunciosPorCategoria.clear();
        
        // Llenamos el mapa con los datos reales
        categoriasConConteo.forEach(categoria => {
          this.conteoAnunciosPorCategoria.set(categoria.id, categoria.cantidad);
          console.log(`Categoría ${categoria.nombre} (ID: ${categoria.id}): ${categoria.cantidad} anuncios`);
        });
        
        console.log('Mapa de conteo actualizado:', this.conteoAnunciosPorCategoria);
      },
      error => {
        console.error('Error al cargar conteo de anuncios', error);
        // En caso de error, mantenemos la funcionalidad con datos simulados
      }
    );
  }

  /**
   * Filtra las categorías según el término de búsqueda
   */
  filtrarCategorias(): void {
    let categoriasFiltradas = [...this.categorias];
    
    // Aplicar filtro de búsqueda general
    if (this.termino && this.termino.trim() !== '') {
      const terminoLower = this.termino.toLowerCase();
      categoriasFiltradas = categoriasFiltradas.filter(categoria => 
        categoria.nombre.toLowerCase().includes(terminoLower) ||
        categoria.descripcion.toLowerCase().includes(terminoLower)
      );
    }
    
    // Aplicar filtro por nombre específico
    if (this.busquedaNombre && this.busquedaNombre.trim() !== '') {
      const nombreLower = this.busquedaNombre.toLowerCase();
      categoriasFiltradas = categoriasFiltradas.filter(categoria => 
        categoria.nombre.toLowerCase().includes(nombreLower)
      );
    }
    
    // Aplicar ordenación
    categoriasFiltradas.sort((a, b) => {
      let valorA, valorB;
      
      switch (this.ordenarPor) {
        case 'nombre':
          valorA = a.nombre.toLowerCase();
          valorB = b.nombre.toLowerCase();
          break;
        case 'id':
        default:
          valorA = a.id;
          valorB = b.id;
          break;
      }
      
      if (valorA < valorB) return this.ordenAscendente ? -1 : 1;
      if (valorA > valorB) return this.ordenAscendente ? 1 : -1;
      return 0;
    });
    
    this.categoriasFiltradas = categoriasFiltradas;
    this.calcularTotalPaginas();
    this.aplicarPaginacion();
  }
  
  /**
   * Recibe el término de búsqueda desde el componente navbar-admin
   */
  aplicarFiltro(termino: string): void {
    this.termino = termino;
    this.filtrarCategorias();
  }

  /**
   * Calcula el número total de páginas basado en el número de categorías y el tamaño de página
   */
  calcularTotalPaginas(): void {
    this.totalPaginas = Math.ceil(this.categoriasFiltradas.length / this.itemsPorPagina);
    if (this.totalPaginas === 0) this.totalPaginas = 1;
  }

  /**
   * Aplica la paginación a la lista filtrada de categorías
   */
  aplicarPaginacion(): void {
    const inicio = (this.paginaActual - 1) * this.itemsPorPagina;
    const fin = inicio + this.itemsPorPagina;
    this.categoriasPaginadas = this.categoriasFiltradas.slice(inicio, fin);
  }
  
  /**
   * Obtiene las categorías paginadas para la página actual
   */
  getCategoriasPaginadas(): Categoria[] {
    return this.categoriasPaginadas;
  }
  
  /**
   * Cambia el criterio de ordenación de las categorías
   */
  cambiarOrden(campo: string): void {
    if (this.ordenarPor === campo) {
      this.ordenAscendente = !this.ordenAscendente;
    } else {
      this.ordenarPor = campo;
      this.ordenAscendente = true;
    }
    this.filtrarCategorias();
  }
  
  /**
   * Filtra las categorías por nombre
   */
  filtrarPorNombre(): void {
    this.filtrarCategorias();
  }
  
  /**
   * Obtiene el número de categorías activas
   * (En este caso, todas las categorías se consideran activas)
   */
  getCategoriasActivas(): number {
    return this.categorias.length;
  }
  
  /**
   * Obtiene el número total de servicios asociados a todas las categorías
   */
  getServiciosAsociados(): number {
    let total = 0;
    // Usar datos reales si están disponibles
    if (this.conteoAnunciosPorCategoria.size > 0) {
      for (const cantidad of this.conteoAnunciosPorCategoria.values()) {
        total += cantidad;
      }
      return total;
    }
    
    // Fallback a datos simulados si no hay datos reales
    for (const cantidad of this.serviciosPorCategoria.values()) {
      total += cantidad;
    }
    return total || this.categorias.length * 3; // Si no hay datos, simulamos datos
  }
  
  /**
   * Obtiene el número de categorías creadas en los últimos 30 días
   * (Simulado ya que no tenemos fecha de creación en el modelo)
   */
  getCategoriasRecientes(): number {
    // Simulamos que el 30% de las categorías son recientes
    return Math.round(this.categorias.length * 0.3);
  }
  
  /**
   * Obtiene el icono de Font Awesome para la categoría
   */
  getIconoCategoria(categoria: Categoria): string {
    // Si el modelo tiene icono, lo usamos; de lo contrario asignamos uno según el ID
    if (categoria.icono) {
      return categoria.icono;
    }
    
    // Asignamos un icono según el ID para simular diferentes iconos
    const iconos = ['fa-tag', 'fa-cube', 'fa-box', 'fa-cog', 'fa-print', 'fa-tools', 'fa-puzzle-piece'];
    return iconos[categoria.id % iconos.length];
  }
  
  /**
   * Obtiene el número de servicios asociados a una categoría
   */
  getNumeroServicios(categoria: Categoria): number {
    // Usar datos reales si están disponibles
    if (this.conteoAnunciosPorCategoria.has(categoria.id)) {
      const cantidad = this.conteoAnunciosPorCategoria.get(categoria.id) || 0;
      console.log(`Conteo REAL para categoría ${categoria.nombre} (ID: ${categoria.id}): ${cantidad}`);
      return cantidad;
    }
    
    // Fallback a datos simulados
    if (this.serviciosPorCategoria.has(categoria.id)) {
      const cantidad = this.serviciosPorCategoria.get(categoria.id) || 0;
      console.log(`Conteo SIMULADO (cached) para categoría ${categoria.nombre} (ID: ${categoria.id}): ${cantidad}`);
      return cantidad;
    }
    
    // Simulamos datos solo si no tenemos datos reales
    const servicio = Math.floor(Math.random() * 10) + 1;
    this.serviciosPorCategoria.set(categoria.id, servicio);
    console.log(`Conteo SIMULADO (nuevo) para categoría ${categoria.nombre} (ID: ${categoria.id}): ${servicio}`);
    return servicio;
  }
  
  /**
   * Muestra los detalles de una categoría
   */
  verDetallesCategoria(categoria: Categoria): void {
    // Mostramos los detalles con SweetAlert
    swal({
      title: `Categoría: ${categoria.nombre}`,
      html: `
        <div class="detalles-categoria">
          <p><strong>ID:</strong> ${categoria.id}</p>
          <p><strong>Nombre:</strong> ${categoria.nombre}</p>
          <p><strong>Descripción:</strong> ${categoria.descripcion}</p>
          <p><strong>Servicios asociados:</strong> ${this.getNumeroServicios(categoria)}</p>
        </div>
      `,
      type: 'info',
      confirmButtonText: 'Cerrar'
    });
  }

  /**
   * Cambia la página actual con transición suave
   * @param pagina Número de página a cambiar
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
    this.cargarCategorias();
  }

  /**
   * Abre el modal para crear una nueva categoría
   */
  abrirModalCrearCategoria(): void {
    this.categoriaActual = new Categoria();
    this.modoEdicion = false;
    this.modalVisible = true;
  }

  /**
   * Abre el modal para editar una categoría existente
   */
  editarCategoria(categoria: Categoria): void {
    // Creamos una nueva instancia para evitar modificar el original por referencia
    this.categoriaActual = new Categoria();
    Object.assign(this.categoriaActual, categoria);
    this.modoEdicion = true;
    this.modalVisible = true;
  }

  /**
   * Cierra el modal y limpia el formulario
   */
  cerrarModal(): void {
    this.modalVisible = false;
    this.categoriaActual = new Categoria();
    if (this.categoriaForm) {
      this.categoriaForm.resetForm();
    }
  }

  /**
   * Guarda la categoría actual (creación o edición)
   */
  guardarCategoria(): void {
    if (this.modoEdicion) {
      this.categoriaService.updateCategoria(this.categoriaActual.id, this.categoriaActual).subscribe(
        response => {
          this.cerrarModal();
          this.cargarCategorias();
          this.cargarConteoAnuncios(); // Recargar conteo después de actualizar
          swal('¡Actualizada!', `La categoría ha sido actualizada con éxito`, 'success');
        },
        error => {
          console.error('Error al actualizar la categoría', error);
          swal('Error', 'Hubo un problema al actualizar la categoría', 'error');
        }
      );
    } else {
      this.categoriaService.createCategoria(this.categoriaActual).subscribe(
        response => {
          this.cerrarModal();
          this.cargarCategorias();
          this.cargarConteoAnuncios(); // Recargar conteo después de crear
          swal('¡Creada!', `La categoría ha sido creada con éxito`, 'success');
        },
        error => {
          console.error('Error al crear la categoría', error);
          swal('Error', 'Hubo un problema al crear la categoría', 'error');
        }
      );
    }
  }

  /**
   * Muestra un diálogo de confirmación para eliminar una categoría
   */
  confirmarEliminar(categoria: Categoria): void {
    this.tituloConfirmacion = 'Confirmar eliminación';
    this.mensajeConfirmacion = `¿Estás seguro de eliminar la categoría "${categoria.nombre}"?`;
    this.accionConfirmacion = () => {
      this.eliminarCategoria(categoria);
    };
    this.modalConfirmacionVisible = true;
  }

  /**
   * Muestra un diálogo de confirmación para eliminar todas las categorías
   */
  confirmarEliminarTodos(): void {
    this.tituloConfirmacion = 'Confirmar eliminación masiva';
    this.mensajeConfirmacion = '¿Estás seguro de eliminar TODAS las categorías? Esta acción no se puede deshacer.';
    this.accionConfirmacion = () => {
      this.eliminarTodos();
    };
    this.modalConfirmacionVisible = true;
  }

  /**
   * Ejecuta la acción de confirmación definida previamente
   */
  ejecutarAccionConfirmacion(): void {
    this.accionConfirmacion();
    this.modalConfirmacionVisible = false;
  }

  /**
   * Acepta la acción de confirmación y cierra el modal
   */
  aceptarConfirmacion(): void {
    this.ejecutarAccionConfirmacion();
  }

  /**
   * Cancela la acción de confirmación y cierra el modal
   */
  cancelarConfirmacion(): void {
    this.modalConfirmacionVisible = false;
    this.tituloConfirmacion = '';
    this.mensajeConfirmacion = '';
    this.accionConfirmacion = () => {};
  }

  /**
   * Elimina una categoría específica
   */
  eliminarCategoria(categoria: Categoria): void {
    this.categoriaService.deleteCategoria(categoria.id).subscribe(
      response => {
        this.cargarCategorias();
        this.cargarConteoAnuncios(); // Recargar conteo después de eliminar
        swal(
          '¡Eliminada!',
          `La categoría "${categoria.nombre}" ha sido eliminada con éxito`,
          'success'
        );
      },
      error => {
        console.error('Error al eliminar la categoría', error);
        swal(
          'Error',
          'Hubo un problema al eliminar la categoría',
          'error'
        );
      }
    );
  }

  /**
   * Elimina todas las categorías
   */
  eliminarTodos(): void {
    this.categoriaService.deleteAllCategorias().subscribe(
      response => {
        this.categorias = [];
        this.categoriasFiltradas = [];
        this.conteoAnunciosPorCategoria.clear(); // Limpiar conteo al eliminar todas
        swal(
          '¡Eliminadas!',
          'Todas las categorías han sido eliminadas con éxito',
          'success'
        );
      },
      error => {
        console.error('Error al eliminar todas las categorías', error);
        swal(
          'Error',
          'Hubo un problema al eliminar todas las categorías',
          'error'
        );
      }
    );
  }

  /**
   * Navega a la página de servicios filtrados por categoría
   */
  verServiciosCategoria(categoria: Categoria): void {
    this.router.navigate(['/servicios'], {
      queryParams: {
        categoriaId: categoria.id,
        page: 0,
        size: 8
      }
    });
  }

  /**
   * Exporta las categorías a un archivo CSV
   */
  exportarCategorias(): void {
    // Cabeceras del CSV
    const cabeceras = 'ID,Nombre,Descripción,Servicios Asociados\n';
    
    // Datos de las categorías
    const datos = this.categorias.map(categoria => {
      return `${categoria.id},"${categoria.nombre}","${categoria.descripcion}","${this.getNumeroServicios(categoria)}"`;
    }).join('\n');
    
    // Archivo completo
    const csv = cabeceras + datos;
    
    // Crear el archivo y descargarlo
    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.setAttribute('href', url);
    link.setAttribute('download', 'categorias.csv');
    link.style.visibility = 'hidden';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }
}
