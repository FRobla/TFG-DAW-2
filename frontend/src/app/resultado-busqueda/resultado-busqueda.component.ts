import { Component, OnInit } from "@angular/core"
import { ActivatedRoute, Router } from "@angular/router"
import { ResultadoBusquedaService } from "./resultado-busqueda.service"
import { Categoria } from "../categoria/categoria"

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
  categoriaSeleccionadas: number[] = [] // Array para almacenar IDs de categorías seleccionadas
  mostrarTodasCategorias = false // Flag para controlar si se muestran todas las categorías

  // Variables para los resultados
  resultados: any[] = []
  totalResultados = 48
  paginaActual = 1
  resultadosPorPagina = 9
  terminoBusqueda = ""

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
    // Cargar categorías
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
      this.filtroUbicacion = params["ubicacion"] || ""
      this.filtroValoracion = params["valoracion"] || ""

      // Si hay categorías en los parámetros, convertirlas a array de IDs
      if (params["categoria"] && params["categoria"].trim() !== '') {
        this.categoriaSeleccionadas = params["categoria"].split(',').map((id: string) => Number(id));
      } else {
        this.categoriaSeleccionadas = [];
      }

      // Obtener filtros de precio con valores por defecto
      this.filtroPrecioMin = params["precioMin"] ? Number.parseInt(params["precioMin"]) : 10
      this.filtroPrecioMax = params["precioMax"] ? Number.parseInt(params["precioMax"]) : 500

      // Obtener filtros de arrays
      if (params["material"] && params["material"].trim() !== '') {
        this.filtroMaterial = params["material"].split(",")
      } else {
        this.filtroMaterial = []
      }

      if (params["tiempoEntrega"] && params["tiempoEntrega"].trim() !== '') {
        this.filtroTiempoEntrega = params["tiempoEntrega"].split(",")
      } else {
        this.filtroTiempoEntrega = []
      }

      console.log("Parámetros de búsqueda recibidos:", params)
      
      // Cargar resultados basados en los filtros
      this.cargarResultados()
    })
  }

  cargarResultados(): void {
    // Aquí iría la lógica para cargar los resultados desde el backend
    // Por ahora, simulamos que ya tenemos los resultados cargados
    console.log("Cargando resultados con los siguientes filtros:")
    console.log("Búsqueda:", this.busquedaTexto)
    console.log("Categoría:", this.filtroCategoria)
    console.log("Ubicación:", this.filtroUbicacion)
    console.log("Valoración:", this.filtroValoracion)
    console.log("Precio Min:", this.filtroPrecioMin)
    console.log("Precio Max:", this.filtroPrecioMax)
    console.log("Material:", this.filtroMaterial)
    console.log("Tiempo de entrega:", this.filtroTiempoEntrega)
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
    
    if (this.filtroUbicacion) queryParams.ubicacion = this.filtroUbicacion
    if (this.filtroValoracion) queryParams.valoracion = this.filtroValoracion

    // Añadir filtros de precio si son diferentes a los valores por defecto
    if (this.filtroPrecioMin !== 10) queryParams.precioMin = this.filtroPrecioMin
    if (this.filtroPrecioMax !== 500) queryParams.precioMax = this.filtroPrecioMax

    // Añadir filtros de arrays solo si tienen elementos
    if (this.filtroMaterial.length > 0) queryParams.material = this.filtroMaterial.join(",")
    if (this.filtroTiempoEntrega.length > 0) queryParams.tiempoEntrega = this.filtroTiempoEntrega.join(",")

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
    this.filtroUbicacion = ""
    this.filtroValoracion = ""
    this.filtroPrecioMin = 10
    this.filtroPrecioMax = 500
    this.filtroMaterial = []
    this.filtroTiempoEntrega = []

    // Limpiar URL y cargar resultados sin filtros
    this.router.navigate(["resultados-busqueda"], {replaceUrl: true});
    this.cargarResultados();
  }

  cambiarOrden(orden: string): void {
    this.ordenActual = orden
    this.cargarResultados()
  }

  cambiarVista(vista: string): void {
    this.vistaActual = vista
  }

  cambiarPagina(pagina: number): void {
    this.paginaActual = pagina
    this.cargarResultados()
  }

  /**
   * Actualiza el filtro de material
   * @param material Material a añadir o quitar
   * @param checked Si está seleccionado o no
   */
  toggleMaterial(material: string, checked: boolean): void {
    if (checked) {
      if (!this.filtroMaterial.includes(material)) {
        this.filtroMaterial.push(material)
      }
    } else {
      this.filtroMaterial = this.filtroMaterial.filter((m) => m !== material)
    }
  }

  /**
   * Actualiza el filtro de tiempo de entrega
   * @param tiempo Tiempo a añadir o quitar
   * @param checked Si está seleccionado o no
   */
  toggleTiempoEntrega(tiempo: string, checked: boolean): void {
    if (checked) {
      if (!this.filtroTiempoEntrega.includes(tiempo)) {
        this.filtroTiempoEntrega.push(tiempo)
      }
    } else {
      this.filtroTiempoEntrega = this.filtroTiempoEntrega.filter((t) => t !== tiempo)
    }
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
    const categoria = this.categorias.find(cat => cat.id === categoriaId);
    return categoria ? categoria.nombre : '';
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
        this.filtroUbicacion = ""
        break
      case "valoracion":
        this.filtroValoracion = ""
        break
      case "material":
        this.filtroMaterial = []
        break
      case "tiempoEntrega":
        this.filtroTiempoEntrega = []
        break
    }
  }

  iniciarSesion(): void {
    // Redirigir a la página de inicio de sesión
    this.router.navigate(["/login"])
  }

  /**
   * Navega a la página de detalles del anuncio
   * @param id Identificador del anuncio
   */
  verDetalles(id: number): void {
    // Redirigir a la página de detalles del anuncio
    this.router.navigate(["/detalles-anuncio", id])
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
