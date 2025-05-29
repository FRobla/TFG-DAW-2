import { Component, OnInit } from "@angular/core"
import { ActivatedRoute, Router } from "@angular/router"
import { ResultadoBusquedaService } from "./resultado-busqueda.service"
import { Categoria } from "../../entidades/categoria/categoria"
import { Anuncio } from "../../entidades/anuncio/anuncio"
import { ParametrosBusqueda, ResultadoBusquedaPaginada } from "./resultado-busqueda"

@Component({
  selector: "app-resultado-busqueda",
  standalone: false,
  templateUrl: "./resultado-busqueda.component.html",
  styleUrls: ["./resultado-busqueda.component.css"],
})
export class ResultadoBusquedaComponent implements OnInit {
  // Variables para la búsqueda
  busquedaTexto = ""
  filtroCategoria = ""
  filtroUbicacion = ""
  filtroValoracion = ""
  filtroPrecioMin = 10
  filtroPrecioMax = 500
  filtroMaterial: string[] = []
  filtroTiempoEntrega: string[] = []
  categorias: Categoria[] = []
  categoriasConConteo: any[] = [] // Para almacenar categorías con conteo real
  categoriaSeleccionadas: number[] = [] // Array para almacenar IDs de categorías seleccionadas
  mostrarTodasCategorias = false // Flag para controlar si se muestran todas las categorías
  
  // Hacer Math disponible en el template
  Math = Math
  
  // Nuevos arrays para gestionar los filtros
  ubicaciones: {id: number, nombre: string, cantidad: number}[] = [
    {id: 1, nombre: 'Madrid', cantidad: 18},
    {id: 2, nombre: 'Barcelona', cantidad: 14},
    {id: 3, nombre: 'Valencia', cantidad: 9},
    {id: 4, nombre: 'Sevilla', cantidad: 7},
    {id: 5, nombre: 'Valladolid', cantidad: 8},
    {id: 6, nombre: 'Bilbao', cantidad: 6},
    {id: 7, nombre: 'Zaragoza', cantidad: 5},
    {id: 8, nombre: 'Málaga', cantidad: 4}
  ]
  ubicacionesSeleccionadas: number[] = [] // IDs de ubicaciones seleccionadas
  mostrarTodasUbicaciones = false

  valoraciones: {id: number, estrellas: number, cantidad: number}[] = [
    {id: 1, estrellas: 5, cantidad: 15},
    {id: 2, estrellas: 4, cantidad: 22},
    {id: 3, estrellas: 3, cantidad: 11},
    {id: 4, estrellas: 2, cantidad: 7},
    {id: 5, estrellas: 1, cantidad: 3}
  ]
  valoracionesSeleccionadas: number[] = [] // IDs de valoraciones seleccionadas

  materiales: {id: number, nombre: string, cantidad: number}[] = [
    {id: 1, nombre: 'PLA', cantidad: 20},
    {id: 2, nombre: 'ABS', cantidad: 14},
    {id: 3, nombre: 'PETG', cantidad: 12},
    {id: 4, nombre: 'Resina', cantidad: 16},
    {id: 5, nombre: 'Nylon', cantidad: 8},
    {id: 6, nombre: 'TPU', cantidad: 6},
    {id: 7, nombre: 'Fibra de carbono', cantidad: 4}
  ]
  materialesSeleccionados: number[] = []
  mostrarTodosMateriales = false

  tiemposEntrega: {id: number, nombre: string, cantidad: number}[] = [
    {id: 1, nombre: '24 horas', cantidad: 6},
    {id: 2, nombre: '2-3 días', cantidad: 18},
    {id: 3, nombre: '4-7 días', cantidad: 24},
    {id: 4, nombre: 'Más de 7 días', cantidad: 10}
  ]
  tiemposEntregaSeleccionados: number[] = []
  mostrarTodosTiempos = false

  // Variables para los resultados - ahora usando datos reales del backend
  resultados: Anuncio[] = []
  totalResultados = 0
  totalPaginas = 0
  paginaActual = 1
  resultadosPorPagina = 9
  terminoBusqueda = ""
  cargando = false
  primeraPagina = true
  ultimaPagina = false

  // Variables para la ordenación
  ordenActual = "relevancia"

  // Variables para la vista
  vistaActual = "grid"

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private resultadoBusquedaService: ResultadoBusquedaService
  ) {}

  ngOnInit(): void {
    // Cargar categorías con conteo
    this.resultadoBusquedaService.cargarCategoriasConConteo().subscribe(
      categoriasConConteo => {
        this.categoriasConConteo = categoriasConConteo;
        console.log('Categorías con conteo cargadas:', this.categoriasConConteo);
      },
      error => {
        console.error('Error al cargar categorías con conteo:', error);
      }
    );

    // También cargar categorías normales para compatibilidad
    this.resultadoBusquedaService.cargarCategorias().subscribe(
      categorias => {
        this.categorias = categorias;
        console.log('Categorías cargadas:', this.categorias);
      },
      error => {
        console.error('Error al cargar categorías:', error);
      }
    );
    
    // Obtener parámetros de la URL
    this.route.queryParams.subscribe((params) => {
      // Valores por defecto si no se especifican en la URL
      this.busquedaTexto = params["q"] || ""
      this.terminoBusqueda = this.busquedaTexto // Para mostrar en la UI

      // Si hay categorías en los parámetros, convertirlas a array de IDs
      if (params["categoria"] && params["categoria"].trim() !== '') {
        this.categoriaSeleccionadas = params["categoria"].split(',').map((id: string) => Number(id));
      } else {
        this.categoriaSeleccionadas = [];
      }
      
      // Cargar ubicaciones seleccionadas
      if (params["ubicacion"] && params["ubicacion"].trim() !== '') {
        this.ubicacionesSeleccionadas = params["ubicacion"].split(',').map((id: string) => Number(id));
        this.filtroUbicacion = params["ubicacion"];
      } else {
        this.ubicacionesSeleccionadas = [];
        this.filtroUbicacion = "";
      }
      
      // Cargar valoraciones seleccionadas
      if (params["valoracion"] && params["valoracion"].trim() !== '') {
        this.valoracionesSeleccionadas = params["valoracion"].split(',').map((id: string) => Number(id));
        this.filtroValoracion = params["valoracion"];
      } else {
        this.valoracionesSeleccionadas = [];
        this.filtroValoracion = "";
      }

      // Obtener filtros de precio con valores por defecto
      this.filtroPrecioMin = params["precioMin"] ? Number.parseInt(params["precioMin"]) : 10
      this.filtroPrecioMax = params["precioMax"] ? Number.parseInt(params["precioMax"]) : 500

      // Cargar materiales seleccionados
      if (params["material"] && params["material"].trim() !== '') {
        this.materialesSeleccionados = params["material"].split(',').map((id: string) => Number(id));
        this.filtroMaterial = this.materialesSeleccionados.map(id => 
          this.materiales.find(m => m.id === id)?.nombre || ''
        ).filter(nombre => nombre !== '');
      } else {
        this.materialesSeleccionados = [];
        this.filtroMaterial = [];
      }

      // Cargar tiempos de entrega seleccionados
      if (params["tiempoEntrega"] && params["tiempoEntrega"].trim() !== '') {
        this.tiemposEntregaSeleccionados = params["tiempoEntrega"].split(',').map((id: string) => Number(id));
        this.filtroTiempoEntrega = this.tiemposEntregaSeleccionados.map(id => 
          this.tiemposEntrega.find(t => t.id === id)?.nombre || ''
        ).filter(nombre => nombre !== '');
      } else {
        this.tiemposEntregaSeleccionados = [];
        this.filtroTiempoEntrega = [];
      }

      console.log("Parámetros de búsqueda recibidos:", params)
      
      // Cargar resultados basados en los filtros
      this.cargarResultados()
    })
  }

  cargarResultados(): void {
    this.cargando = true;
    
    // Preparar parámetros de búsqueda
    const parametros: ParametrosBusqueda = {
      q: this.busquedaTexto || undefined,
      categoria: this.categoriaSeleccionadas.length > 0 ? this.categoriaSeleccionadas.join(',') : undefined,
      ubicacion: this.ubicacionesSeleccionadas.length > 0 ? this.ubicacionesSeleccionadas.join(',') : undefined,
      valoracion: this.valoracionesSeleccionadas.length > 0 ? this.valoracionesSeleccionadas.join(',') : undefined,
      precioMin: this.filtroPrecioMin !== 10 ? this.filtroPrecioMin : undefined,
      precioMax: this.filtroPrecioMax !== 500 ? this.filtroPrecioMax : undefined,
      material: this.materialesSeleccionados.length > 0 ? this.materialesSeleccionados.join(',') : undefined,
      tiempoEntrega: this.tiemposEntregaSeleccionados.length > 0 ? this.tiemposEntregaSeleccionados.join(',') : undefined,
      page: this.paginaActual - 1, // El backend usa páginas basadas en 0
      size: this.resultadosPorPagina,
      orden: this.ordenActual
    };

    console.log("Cargando resultados con parámetros:", parametros);

    this.resultadoBusquedaService.buscarAnunciosAvanzada(parametros).subscribe({
      next: (respuesta: ResultadoBusquedaPaginada) => {
        this.resultados = respuesta.content;
        this.totalResultados = respuesta.totalElements;
        this.totalPaginas = respuesta.totalPages;
        this.paginaActual = respuesta.currentPage + 1; // Convertir a base 1 para la UI
        this.primeraPagina = respuesta.first;
        this.ultimaPagina = respuesta.last;
        this.cargando = false;
        
        console.log("Resultados cargados:", this.resultados);
        console.log("Total de resultados:", this.totalResultados);
      },
      error: (error) => {
        console.error("Error al cargar resultados:", error);
        this.resultados = [];
        this.totalResultados = 0;
        this.totalPaginas = 0;
        this.cargando = false;
        
        // Mostrar mensaje de error al usuario (opcional)
        // Aquí podrías añadir un servicio de notificaciones
      }
    });
  }

  buscar(): void {
    // Crear un objeto con todos los filtros seleccionados
    const queryParams: any = {}

    // Añadir filtros solo si tienen valor
    if (this.busquedaTexto && this.busquedaTexto.trim() !== '') queryParams.q = this.busquedaTexto.trim()
    
    // Añadir categorías seleccionadas como string separado por comas
    if (this.categoriaSeleccionadas.length > 0) {
      queryParams.categoria = this.categoriaSeleccionadas.join(',');
    }
    
    // Añadir ubicaciones seleccionadas
    if (this.ubicacionesSeleccionadas.length > 0) {
      queryParams.ubicacion = this.ubicacionesSeleccionadas.join(',');
    }
    
    // Añadir valoraciones seleccionadas
    if (this.valoracionesSeleccionadas.length > 0) {
      queryParams.valoracion = this.valoracionesSeleccionadas.join(',');
    }

    // Añadir filtros de precio si son diferentes a los valores por defecto
    if (this.filtroPrecioMin !== 10) queryParams.precioMin = this.filtroPrecioMin
    if (this.filtroPrecioMax !== 500) queryParams.precioMax = this.filtroPrecioMax

    // Añadir materiales seleccionados
    if (this.materialesSeleccionados.length > 0) {
      queryParams.material = this.materialesSeleccionados.join(',');
    }
    
    // Añadir tiempos de entrega seleccionados
    if (this.tiemposEntregaSeleccionados.length > 0) {
      queryParams.tiempoEntrega = this.tiemposEntregaSeleccionados.join(',');
    }

    console.log("Actualizando búsqueda con parámetros:", queryParams)
    
    // Actualizar la URL con los parámetros de búsqueda
    this.router.navigate(["resultados-busqueda"], {
      queryParams: queryParams,
      replaceUrl: true // Reemplazar URL actual para evitar historial innecesario
    })
  }

  limpiarFiltros(): void {
    this.busquedaTexto = ""
    this.terminoBusqueda = ""
    this.categoriaSeleccionadas = []
    this.ubicacionesSeleccionadas = []
    this.valoracionesSeleccionadas = []
    this.filtroPrecioMin = 10
    this.filtroPrecioMax = 500
    this.materialesSeleccionados = []
    this.tiemposEntregaSeleccionados = []
    this.filtroUbicacion = ""
    this.filtroValoracion = ""
    this.filtroMaterial = []
    this.filtroTiempoEntrega = []

    // Limpiar URL y cargar resultados sin filtros
    this.router.navigate(["resultados-busqueda"], {replaceUrl: true});
    this.cargarResultados();
  }

  cambiarOrden(orden: string): void {
    this.ordenActual = orden
    this.paginaActual = 1 // Resetear a la primera página al cambiar el orden
    this.cargarResultados()
  }

  // Método para obtener el texto legible del orden actual
  getTextoOrden(): string {
    switch (this.ordenActual) {
      case 'precio_asc':
        return 'Precio: menor a mayor';
      case 'precio_desc':
        return 'Precio: mayor a menor';
      case 'fecha_desc':
        return 'Más recientes';
      case 'fecha_asc':
        return 'Más antiguos';
      case 'titulo':
        return 'Título A-Z';
      default:
        return 'Relevancia';
    }
  }

  cambiarVista(vista: string): void {
    this.vistaActual = vista
  }

  cambiarPagina(pagina: number): void {
    if (pagina >= 1 && pagina <= this.totalPaginas) {
      this.paginaActual = pagina
      this.cargarResultados()
    }
  }

  // Métodos de utilidad para la paginación
  getPaginasArray(): number[] {
    const paginas: number[] = [];
    const inicio = Math.max(1, this.paginaActual - 2);
    const fin = Math.min(this.totalPaginas, this.paginaActual + 2);
    
    for (let i = inicio; i <= fin; i++) {
      paginas.push(i);
    }
    
    return paginas;
  }

  paginaAnterior(): void {
    if (this.paginaActual > 1) {
      this.cambiarPagina(this.paginaActual - 1);
    }
  }

  paginaSiguiente(): void {
    if (this.paginaActual < this.totalPaginas) {
      this.cambiarPagina(this.paginaActual + 1);
    }
  }

  // Métodos para el filtro de material
  toggleMaterial(materialId: number): void {
    const index = this.materialesSeleccionados.indexOf(materialId);
    if (index === -1) {
      // Si no está seleccionado, añadirlo
      this.materialesSeleccionados.push(materialId);
    } else {
      // Si ya está seleccionado, quitarlo
      this.materialesSeleccionados.splice(index, 1);
    }
    
    // Actualizar array de nombres de materiales para la URL
    this.filtroMaterial = this.materialesSeleccionados.map(id => 
      this.materiales.find(m => m.id === id)?.nombre || ''
    ).filter(nombre => nombre !== '');
  }

  isMaterialSeleccionado(materialId: number): boolean {
    return this.materialesSeleccionados.includes(materialId);
  }

  getNombreMaterial(materialId: number): string {
    const material = this.materiales.find(m => m.id === materialId);
    return material ? material.nombre : '';
  }

  eliminarFiltroMaterial(materialId: number): void {
    const index = this.materialesSeleccionados.indexOf(materialId);
    if (index !== -1) {
      this.materialesSeleccionados.splice(index, 1);
      this.filtroMaterial = this.materialesSeleccionados.map(id => 
        this.materiales.find(m => m.id === id)?.nombre || ''
      ).filter(nombre => nombre !== '');
      this.buscar();
    }
  }

  verMasMateriales(): void {
    this.mostrarTodosMateriales = true;
  }

  verMenosMateriales(): void {
    this.mostrarTodosMateriales = false;
  }

  // Métodos para el filtro de ubicación
  toggleUbicacion(ubicacionId: number): void {
    const index = this.ubicacionesSeleccionadas.indexOf(ubicacionId);
    if (index === -1) {
      this.ubicacionesSeleccionadas.push(ubicacionId);
    } else {
      this.ubicacionesSeleccionadas.splice(index, 1);
    }
    this.filtroUbicacion = this.ubicacionesSeleccionadas.join(',');
  }

  isUbicacionSeleccionada(ubicacionId: number): boolean {
    return this.ubicacionesSeleccionadas.includes(ubicacionId);
  }

  getNombreUbicacion(ubicacionId: number): string {
    const ubicacion = this.ubicaciones.find(u => u.id === ubicacionId);
    return ubicacion ? ubicacion.nombre : '';
  }

  eliminarFiltroUbicacion(ubicacionId: number): void {
    const index = this.ubicacionesSeleccionadas.indexOf(ubicacionId);
    if (index !== -1) {
      this.ubicacionesSeleccionadas.splice(index, 1);
      this.filtroUbicacion = this.ubicacionesSeleccionadas.join(',');
      this.buscar();
    }
  }

  verMasUbicaciones(): void {
    this.mostrarTodasUbicaciones = true;
  }

  verMenosUbicaciones(): void {
    this.mostrarTodasUbicaciones = false;
  }

  // Métodos para el filtro de valoración
  toggleValoracion(valoracionId: number): void {
    const index = this.valoracionesSeleccionadas.indexOf(valoracionId);
    if (index === -1) {
      this.valoracionesSeleccionadas.push(valoracionId);
    } else {
      this.valoracionesSeleccionadas.splice(index, 1);
    }
    this.filtroValoracion = this.valoracionesSeleccionadas.join(',');
  }

  isValoracionSeleccionada(valoracionId: number): boolean {
    return this.valoracionesSeleccionadas.includes(valoracionId);
  }

  getValoracionEstrellas(valoracionId: number): number {
    const valoracion = this.valoraciones.find(v => v.id === valoracionId);
    return valoracion ? valoracion.estrellas : 0;
  }

  eliminarFiltroValoracion(valoracionId: number): void {
    const index = this.valoracionesSeleccionadas.indexOf(valoracionId);
    if (index !== -1) {
      this.valoracionesSeleccionadas.splice(index, 1);
      this.filtroValoracion = this.valoracionesSeleccionadas.join(',');
      this.buscar();
    }
  }

  // Métodos para el filtro de tiempo de entrega
  toggleTiempoEntrega(tiempoId: number): void {
    const index = this.tiemposEntregaSeleccionados.indexOf(tiempoId);
    if (index === -1) {
      this.tiemposEntregaSeleccionados.push(tiempoId);
    } else {
      this.tiemposEntregaSeleccionados.splice(index, 1);
    }
    
    this.filtroTiempoEntrega = this.tiemposEntregaSeleccionados.map(id => 
      this.tiemposEntrega.find(t => t.id === id)?.nombre || ''
    ).filter(nombre => nombre !== '');
  }

  isTiempoEntregaSeleccionado(tiempoId: number): boolean {
    return this.tiemposEntregaSeleccionados.includes(tiempoId);
  }

  getNombreTiempoEntrega(tiempoId: number): string {
    const tiempo = this.tiemposEntrega.find(t => t.id === tiempoId);
    return tiempo ? tiempo.nombre : '';
  }

  eliminarFiltroTiempoEntrega(tiempoId: number): void {
    const index = this.tiemposEntregaSeleccionados.indexOf(tiempoId);
    if (index !== -1) {
      this.tiemposEntregaSeleccionados.splice(index, 1);
      this.filtroTiempoEntrega = this.tiemposEntregaSeleccionados.map(id => 
        this.tiemposEntrega.find(t => t.id === id)?.nombre || ''
      ).filter(nombre => nombre !== '');
      this.buscar();
    }
  }

  verMasTiempos(): void {
    this.mostrarTodosTiempos = true;
  }

  verMenosTiempos(): void {
    this.mostrarTodosTiempos = false;
  }

  // Método para manejar la selección/deselección de categorías
  toggleCategoria(categoriaId: number): void {
    const index = this.categoriaSeleccionadas.indexOf(categoriaId);
    if (index === -1) {
      // Si no está seleccionada, añadirla
      this.categoriaSeleccionadas.push(categoriaId);
    } else {
      // Si ya está seleccionada, quitarla
      this.categoriaSeleccionadas.splice(index, 1);
    }
    // Actualizar el filtro de categoría para la URL
    this.filtroCategoria = this.categoriaSeleccionadas.join(',');
  }

  // Método para obtener el nombre de una categoría por su ID
  getNombreCategoria(categoriaId: number): string {
    // Buscar primero en categorías con conteo
    const categoriaConConteo = this.categoriasConConteo.find(cat => cat.id === categoriaId);
    if (categoriaConConteo) {
      return categoriaConConteo.nombre;
    }
    
    // Fallback a categorías normales
    const categoria = this.categorias.find(cat => cat.id === categoriaId);
    return categoria ? categoria.nombre : '';
  }

  // Método para obtener el conteo de anuncios de una categoría
  getConteoCategoria(categoriaId: number): number {
    const categoriaConConteo = this.categoriasConConteo.find(cat => cat.id === categoriaId);
    return categoriaConConteo ? categoriaConConteo.cantidad : 0;
  }

  // Método para verificar si una categoría está seleccionada
  isCategoriaSeleccionada(categoriaId: number): boolean {
    return this.categoriaSeleccionadas.includes(categoriaId);
  }

  // Método para eliminar una categoría de los filtros seleccionados
  eliminarFiltroCategoria(categoriaId: number): void {
    const index = this.categoriaSeleccionadas.indexOf(categoriaId);
    if (index !== -1) {
      this.categoriaSeleccionadas.splice(index, 1);
      this.filtroCategoria = this.categoriaSeleccionadas.join(',');
      this.buscar(); // Actualizar la búsqueda con el filtro eliminado
    }
  }

  eliminarFiltro(tipo: string, valor?: string | number): void {
    switch (tipo) {
      case "categoria":
        if (typeof valor === 'number') {
          this.eliminarFiltroCategoria(valor);
        } else if (typeof valor === 'string') {
          const categoriaId = Number(valor);
          if (!isNaN(categoriaId)) {
            this.eliminarFiltroCategoria(categoriaId);
          } else {
            this.categoriaSeleccionadas = [];
            this.filtroCategoria = "";
            this.buscar();
          }
        } else {
          this.categoriaSeleccionadas = [];
          this.filtroCategoria = "";
          this.buscar();
        }
        break;
      case "ubicacion":
        if (typeof valor === 'number') {
          this.eliminarFiltroUbicacion(valor);
        } else {
          this.ubicacionesSeleccionadas = [];
          this.filtroUbicacion = "";
          this.buscar();
        }
        break;
      case "valoracion":
        if (typeof valor === 'number') {
          this.eliminarFiltroValoracion(valor);
        } else {
          this.valoracionesSeleccionadas = [];
          this.filtroValoracion = "";
          this.buscar();
        }
        break;
      case "material":
        if (typeof valor === 'number') {
          this.eliminarFiltroMaterial(valor);
        } else if (typeof valor === 'string') {
          const material = this.materiales.find(m => m.nombre === valor);
          if (material) {
            this.eliminarFiltroMaterial(material.id);
          } else {
            this.materialesSeleccionados = [];
            this.filtroMaterial = [];
            this.buscar();
          }
        } else {
          this.materialesSeleccionados = [];
          this.filtroMaterial = [];
          this.buscar();
        }
        break;
      case "tiempoEntrega":
        if (typeof valor === 'number') {
          this.eliminarFiltroTiempoEntrega(valor);
        } else if (typeof valor === 'string') {
          const tiempo = this.tiemposEntrega.find(t => t.nombre === valor);
          if (tiempo) {
            this.eliminarFiltroTiempoEntrega(tiempo.id);
          } else {
            this.tiemposEntregaSeleccionados = [];
            this.filtroTiempoEntrega = [];
            this.buscar();
          }
        } else {
          this.tiemposEntregaSeleccionados = [];
          this.filtroTiempoEntrega = [];
          this.buscar();
        }
        break;
    }
  }

  /**
   * Navega a la página de detalles del anuncio con transición visual
   * @param id Identificador del anuncio
   */
  verDetalles(id: number): void {
    // Verificar si el navegador soporta View Transitions API
    if (document.startViewTransition) {
      // Aplicar transición visual
      document.startViewTransition(() => {
        // Preparar elementos para la transición
        const imagen = document.querySelector(`[data-view-id="anuncio-img-${id}"]`);
        const titulo = document.querySelector(`[data-view-id="anuncio-title-${id}"]`);
        
        // Asegurar que los elementos mantengan su estilo durante la transición
        if (imagen) imagen.setAttribute('style', 'view-transition-name: image;');
        if (titulo) titulo.setAttribute('style', 'view-transition-name: header;');
        
        // Redirigir a la página de detalles del anuncio
        this.router.navigate(["/detalles-anuncio", id]);
        
        return new Promise<void>(resolve => {
          setTimeout(() => {
            // Limpiar los estilos después de la transición
            if (imagen) imagen.removeAttribute('style');
            if (titulo) titulo.removeAttribute('style');
            resolve();
          }, 300); // Duración aproximada de la transición
        });
      });
    } else {
      // Fallback para navegadores que no soportan View Transitions API
      this.router.navigate(["/detalles-anuncio", id]);
    }
  }

  // Método para mostrar todas las categorías
  verMasCategorias(): void {
    this.mostrarTodasCategorias = true;
  }

  // Método para ocultar categorías adicionales
  verMenosCategorias(): void {
    this.mostrarTodasCategorias = false;
  }
}
