import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Anuncio } from './anuncio';
import { AnuncioService } from './anuncio.service';
import swal from 'sweetalert2';
import { CategoriaService } from '../categoria/categoria.service';
import { ImpresoraService } from '../impresora/impresora.service';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-anuncio',
  standalone: false,
  templateUrl: './anuncio.component.html',
  styleUrl: './anuncio.component.css'
})
export class AnuncioComponent implements OnInit {
  anuncios: Anuncio[] = [];
  anuncioForm: FormGroup;
  editando = false;
  cargando = true;
  categorias: any[] = [];
  impresoras: any[] = [];
  filtroCategoria: number = 0;
  ordenarPor: string = 'fecha';
  ordenAscendente: boolean = false;
  
  // Propiedades para paginación
  paginaActual: number = 1;
  totalPaginas: number = 1;
  itemsPorPagina: number = 10;
  anunciosFiltrados: Anuncio[] = [];
  
  // Propiedades para modal
  modalVisible: boolean = false;
  modoEdicion: boolean = false;
  modalConfirmacionVisible: boolean = false;
  tituloConfirmacion: string = 'Confirmar eliminación';
  mensajeConfirmacion: string = '¿Estás seguro de que deseas eliminar todos los anuncios? Esta acción no se puede deshacer.';
  accionConfirmacion: string = '';
  
  // Filtro de búsqueda
  terminoBusqueda: string = '';

  constructor(
    private anuncioService: AnuncioService,
    private categoriaService: CategoriaService,
    private impresoraService: ImpresoraService,
    private formBuilder: FormBuilder,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.anuncioForm = this.formBuilder.group({
      id: [0],
      titulo: ['', [Validators.required, Validators.minLength(3)]],
      descripcion: ['', [Validators.required, Validators.minLength(10)]],
      precio: [0, [Validators.required, Validators.min(0)]],
      estado: ['activo'],
      urlImagen: [''],
      categoriaId: [0, Validators.required],
      impresoraId: [0, Validators.required]
    });
  }

  ngOnInit(): void {
    this.cargarDatos();
    
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.editando = true;
        this.cargarAnuncio(params['id']);
      }
    });

    this.anuncioService.anuncios$.subscribe({
      next: (anuncios) => {
        this.anuncios = this.ordenarAnuncios(anuncios);
        this.anunciosFiltrados = this.anuncios.slice();
        this.calcularTotalPaginas();
        this.cargando = false;
      },
      error: (error) => {
        console.error('Error al cargar anuncios:', error);
        this.cargando = false;
      },
      complete: () => {
        this.cargando = false;
      }
    });
  }

  async cargarDatos() {
    this.cargando = true;
    
    try {
      // Cargar datos en paralelo
      const [categorias, impresoras] = await Promise.all([
        firstValueFrom(this.categoriaService.getCategorias()),
        firstValueFrom(this.impresoraService.getImpresoras())
      ]);
      
      this.categorias = categorias || [];
      this.impresoras = impresoras || [];
      
      // Iniciar la carga de anuncios (la suscripción a anuncios$ establecerá cargando a false)
      this.anuncioService.getAnuncios().subscribe({
        error: (error) => {
          console.error('Error al cargar anuncios:', error);
          this.cargando = false;
        }
      });
    } catch (error) {
      console.error('Error al cargar datos:', error);
      this.cargando = false;
    }
  }

  cargarAnuncio(id: number) {
    this.anuncioService.getAnuncio(id).subscribe(
      anuncio => {
        this.anuncioForm.patchValue(anuncio);
      }
    );
  }

  onSubmit() {
    if (this.anuncioForm.valid) {
      const anuncio = new Anuncio(this.anuncioForm.value);
      this.cargando = true;
      
      if (this.editando) {
        this.anuncioService.update(anuncio).subscribe({
          next: () => {
            this.cargando = false;
            this.router.navigate(['/anuncios']);
          },
          error: error => {
            console.error('Error al actualizar:', error);
            this.cargando = false;
          }
        });
      } else {
        this.anuncioService.create(anuncio).subscribe({
          next: () => {
            this.cargando = false;
            this.router.navigate(['/anuncios']);
          },
          error: error => {
            console.error('Error al crear:', error);
            this.cargando = false;
          }
        });
      }
    } else {
      Object.keys(this.anuncioForm.controls).forEach(key => {
        const control = this.anuncioForm.get(key);
        if (control?.invalid) {
          control.markAsTouched();
        }
      });
    }
  }
  
  // Método para guardar anuncio desde el modal
  guardarAnuncio() {
    this.onSubmit();
    this.modalVisible = false;
  }
  
  // Método para ejecutar acción de confirmación
  ejecutarAccionConfirmacion() {
    switch(this.accionConfirmacion) {
      case 'eliminarTodos':
        this.eliminarTodosAnuncios();
        break;
      default:
        this.modalConfirmacionVisible = false;
    }
  }

  eliminarAnuncio(anuncio: Anuncio) {
    swal({
      title: '¿Estás seguro?',
      text: `¿Deseas eliminar el anuncio ${anuncio.titulo}?`,
      type: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then((result: any) => {
      if (result.value) {
        this.anuncioService.delete(anuncio.id).subscribe();
      }
    });
  }

  filtrarPorCategoria() {
    if (this.filtroCategoria === 0) {
      this.anuncioService.getAnuncios().subscribe();
    } else {
      this.anuncioService.getAnunciosPorCategoria(this.filtroCategoria).subscribe(
        anuncios => {
          this.anuncios = this.ordenarAnuncios(anuncios);
        }
      );
    }
  }

  ordenarAnuncios(anuncios: Anuncio[]): Anuncio[] {
    return anuncios.sort((a, b) => {
      let valorA: any;
      let valorB: any;

      switch (this.ordenarPor) {
        case 'fecha':
          valorA = new Date(a.fechaPublicacion).getTime();
          valorB = new Date(b.fechaPublicacion).getTime();
          break;
        case 'precio':
          valorA = a.precio;
          valorB = b.precio;
          break;
        case 'titulo':
          valorA = a.titulo.toLowerCase();
          valorB = b.titulo.toLowerCase();
          break;
        default:
          return 0;
      }

      if (this.ordenAscendente) {
        return valorA > valorB ? 1 : -1;
      } else {
        return valorA < valorB ? 1 : -1;
      }
    });
  }

  cambiarOrden(criterio: string) {
    if (this.ordenarPor === criterio) {
      this.ordenAscendente = !this.ordenAscendente;
    } else {
      this.ordenarPor = criterio;
      this.ordenAscendente = true;
    }
    this.anuncios = this.ordenarAnuncios([...this.anuncios]);
    this.calcularTotalPaginas();
  }
  
  // Métodos para estadísticas del dashboard
  getPrecioMedio(): number {
    if (this.anuncios.length === 0) return 0;
    const total = this.anuncios.reduce((sum, anuncio) => sum + anuncio.precio, 0);
    return Math.round((total / this.anuncios.length) * 100) / 100;
  }
  
  getCategorias(): any[] {
    // Obtener categorías únicas usadas en anuncios
    const categoriaIds = new Set(this.anuncios.map(a => a.categoriaId));
    return this.categorias.filter(c => categoriaIds.has(c.id));
  }
  
  getAnunciosRecientes(): number {
    const fechaLimite = new Date();
    fechaLimite.setDate(fechaLimite.getDate() - 30);
    return this.anuncios.filter(a => new Date(a.fechaPublicacion) >= fechaLimite).length;
  }
  
  // Método para obtener categoría por ID
  getCategoriaById(id: number): any {
    return this.categorias.find(c => c.id === id);
  }

  // Métodos para paginación
  calcularTotalPaginas(): void {
    this.totalPaginas = Math.ceil(this.anunciosFiltrados.length / this.itemsPorPagina);
    if (this.paginaActual > this.totalPaginas && this.totalPaginas > 0) {
      this.paginaActual = this.totalPaginas;
    }
  }
  
  /**
   * Cambia la página actual con transición suave
   * @param pagina Número de página a cambiar
   */
  cambiarPagina(pagina: number): void {
    if (pagina >= 1 && pagina <= this.totalPaginas) {
      // Activar estado de carga para evitar parpadeos
      this.cargando = true;
      
      // Usar setTimeout para permitir actualizar la UI antes del cambio
      setTimeout(() => {
        this.paginaActual = pagina;
        
        // Un breve retraso adicional para completar la transición
        setTimeout(() => {
          this.cargando = false;
        }, 100);
      }, 50);
    }
  }
  
  cambiarItemsPorPagina(): void {
    this.paginaActual = 1;
    this.calcularTotalPaginas();
  }
  
  // Método para obtener anuncios de la página actual
  getAnunciosPaginados(): Anuncio[] {
    const indiceInicio = (this.paginaActual - 1) * this.itemsPorPagina;
    const indiceFin = indiceInicio + this.itemsPorPagina;
    return this.anunciosFiltrados.slice(indiceInicio, indiceFin);
  }
  
  // Métodos para modales
  abrirModalCrearAnuncio(): void {
    this.modoEdicion = false;
    this.anuncioForm.reset({
      id: 0,
      titulo: '',
      descripcion: '',
      precio: 0,
      estado: 'activo',
      urlImagen: '',
      categoriaId: 0,
      impresoraId: 0
    });
    this.modalVisible = true;
  }
  
  editarAnuncio(anuncio: Anuncio): void {
    this.modoEdicion = true;
    this.anuncioForm.patchValue(anuncio);
    this.modalVisible = true;
  }
  
  cerrarModal(): void {
    this.modalVisible = false;
  }
  
  verDetallesAnuncio(anuncio: Anuncio): void {
    this.router.navigate(['/anuncios', anuncio.id]);
  }
  
  // Métodos para eliminación múltiple
  confirmarEliminarTodos(): void {
    this.tituloConfirmacion = 'Confirmar eliminación';
    this.mensajeConfirmacion = '¿Estás seguro de que deseas eliminar todos los anuncios? Esta acción no se puede deshacer.';
    this.accionConfirmacion = 'eliminarTodos';
    this.modalConfirmacionVisible = true;
  }
  
  cancelarEliminarTodos(): void {
    this.modalConfirmacionVisible = false;
    this.accionConfirmacion = '';
  }
  
  eliminarTodosAnuncios(): void {
    this.cargando = true;
    this.anuncioService.deleteAll().subscribe({
      next: () => {
        this.modalConfirmacionVisible = false;
        swal('Eliminados', 'Todos los anuncios han sido eliminados', 'success');
        this.cargando = false;
      },
      error: (error) => {
        console.error('Error al eliminar anuncios:', error);
        swal('Error', 'No se pudieron eliminar los anuncios', 'error');
        this.cargando = false;
      }
    });
  }
  
  // Método para filtrar por término de búsqueda
  aplicarFiltro(termino: string): void {
    this.terminoBusqueda = termino.toLowerCase();
    if (!this.terminoBusqueda) {
      this.anunciosFiltrados = this.anuncios;
    } else {
      this.anunciosFiltrados = this.anuncios.filter(anuncio => 
        anuncio.titulo.toLowerCase().includes(this.terminoBusqueda) ||
        anuncio.descripcion.toLowerCase().includes(this.terminoBusqueda) ||
        anuncio.precio.toString().includes(this.terminoBusqueda)
      );
    }
    this.paginaActual = 1;
    this.calcularTotalPaginas();
  }
  
  // Método para exportar anuncios
  exportarAnuncios(): void {
    const anunciosExport = this.anuncios.map(anuncio => {
      const categoria = this.getCategoriaById(anuncio.categoriaId);
      const impresora = this.impresoras.find(i => i.id === anuncio.impresoraId);
      return {
        ID: anuncio.id,
        Título: anuncio.titulo,
        Descripción: anuncio.descripcion,
        'Precio (€)': anuncio.precio.toFixed(2),
        Categoría: categoria ? categoria.nombre : 'Sin categoría',
        Impresora: impresora ? impresora.modelo : 'Sin impresora',
        'Fecha Publicación': new Date(anuncio.fechaPublicacion).toLocaleDateString('es-ES'),
        Estado: anuncio.estado,
        Vistas: anuncio.vistas,
        'Valoración Media': anuncio.valoracionMedia.toFixed(1),
        'Tiempo Estimado': anuncio.tiempoEstimado || 'No especificado'
      };
    });

    // Convertir a CSV
    const headers = Object.keys(anunciosExport[0]);
    const csvContent = [
      headers.join(','),
      ...anunciosExport.map(row => 
        headers.map(header => {
          const value = row[header as keyof typeof row] || '';
          // Escape commas and quotes in CSV values
          return typeof value === 'string' && (value.includes(',') || value.includes('"')) 
            ? `"${value.replace(/"/g, '""')}"` 
            : value;
        }).join(',')
      )
    ].join('\n');

    // Crear y descargar el archivo CSV
    const BOM = '\uFEFF'; // UTF-8 BOM para caracteres especiales
    const blob = new Blob([BOM + csvContent], { type: 'text/csv;charset=utf-8;' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `anuncios_export_${new Date().toISOString().split('T')[0]}.csv`;
    document.body.appendChild(a);
    a.click();
    
    // Limpiar recursos
    window.URL.revokeObjectURL(url);
    document.body.removeChild(a);
  }
}
