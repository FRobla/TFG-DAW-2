import { Component, OnInit } from "@angular/core"
import { ActivatedRoute, Router } from "@angular/router"

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
  ) {}

  ngOnInit(): void {
    // Obtener parámetros de la URL
    this.route.queryParams.subscribe((params) => {
      // Valores por defecto si no se especifican en la URL
      this.busquedaTexto = params["q"] || ""
      this.terminoBusqueda = this.busquedaTexto // Para mostrar en la UI
      this.filtroCategoria = params["categoria"] || ""
      this.filtroUbicacion = params["ubicacion"] || ""
      this.filtroValoracion = params["valoracion"] || ""

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
    if (this.filtroCategoria) queryParams.categoria = this.filtroCategoria
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
    this.filtroCategoria = ""
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

  eliminarFiltro(tipo: string, valor?: string): void {
    switch (tipo) {
      case "categoria":
        this.filtroCategoria = ""
        break
      case "ubicacion":
        this.filtroUbicacion = ""
        break
      case "valoracion":
        this.filtroValoracion = ""
        break
      case "material":
        if (valor && this.filtroMaterial.includes(valor)) {
          this.filtroMaterial = this.filtroMaterial.filter((m) => m !== valor)
        }
        break
      case "tiempoEntrega":
        if (valor && this.filtroTiempoEntrega.includes(valor)) {
          this.filtroTiempoEntrega = this.filtroTiempoEntrega.filter((t) => t !== valor)
        }
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
}
