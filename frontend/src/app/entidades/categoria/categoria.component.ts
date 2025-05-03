import { Component, OnInit, ViewChild } from '@angular/core';
import { Categoria } from './categoria';
import { CategoriaService } from './categoria.service';
import swal from 'sweetalert2';
import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-categoria',
  standalone: false,
  templateUrl: './categoria.component.html',
  styleUrls: ['./categoria.component.css']
})
export class CategoriasComponent implements OnInit {

  // Lista de categorías
  categorias: Categoria[] = [];
  categoriasFiltradas: Categoria[] = [];
  
  // Categoría actual para crear/editar
  categoriaActual: Categoria = new Categoria();
  
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
  
  // Búsqueda
  termino: string = '';
  
  @ViewChild('categoriaForm') categoriaForm!: NgForm;

  constructor(private categoriaService: CategoriaService, private router: Router) { }

  ngOnInit(): void {
    this.cargarCategorias();
  }

  /**
   * Carga todas las categorías desde el servicio
   */
  cargarCategorias(): void {
    this.categoriaService.getCategorias().subscribe(
      categorias => {
        this.categorias = categorias;
        this.filtrarCategorias();
        this.calcularTotalPaginas();
        this.aplicarPaginacion();
      },
      error => {
        console.error('Error al cargar categorías', error);
        swal('Error', 'Hubo un problema al cargar las categorías', 'error');
      }
    );
  }

  /**
   * Filtra las categorías según el término de búsqueda
   */
  filtrarCategorias(): void {
    if (!this.termino || this.termino.trim() === '') {
      this.categoriasFiltradas = [...this.categorias];
      return;
    }

    const terminoLower = this.termino.toLowerCase();
    this.categoriasFiltradas = this.categorias.filter(categoria => 
      categoria.nombre.toLowerCase().includes(terminoLower) ||
      categoria.descripcion.toLowerCase().includes(terminoLower)
    );
    
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
    this.categoriasFiltradas = this.categorias.slice(inicio, fin);
  }

  /**
   * Cambia la página actual
   */
  cambiarPagina(pagina: number): void {
    this.paginaActual = pagina;
    this.cargarCategorias();
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
    const cabeceras = 'ID,Nombre,Descripción\n';
    
    // Datos de las categorías
    const datos = this.categorias.map(categoria => {
      return `${categoria.id},"${categoria.nombre}","${categoria.descripcion}"`;
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
