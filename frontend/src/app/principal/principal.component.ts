import { Component, OnInit } from "@angular/core"
import { Router } from "@angular/router"

@Component({
  selector: "app-principal",
  standalone: false,
  templateUrl: "./principal.component.html",
  styleUrls: ["./principal.component.css"],
})
export class PrincipalComponent implements OnInit {
  // Variables para búsqueda y filtrado
  busquedaTexto = ""

  // Filtros disponibles
  filtroCategoria = ""
  filtroUbicacion = ""
  filtroValoracion = ""
  filtroPrecioMin = 10
  filtroPrecioMax = 500
  filtroMaterial: string[] = []
  filtroTiempoEntrega: string[] = []

  // Mockup de servicios que estarían viniendo del backend
  servicios: any[] = []
  serviciosFiltrados: any[] = []

  // Mockup de fabricantes destacados
  fabricantesDestacados: any[] = []

  constructor(private router: Router) {}

  ngOnInit(): void {
    // Inicializar datos para la demo
    this.inicializarDatos()

    // Establecer servicios filtrados inicialmente como todos los servicios
    this.serviciosFiltrados = [...this.servicios]
  }

  /**
   * Inicializa datos de prueba para la demostración
   */
  inicializarDatos(): void {
    // Datos de servicios populares
    this.servicios = [
      {
        id: 1,
        nombre: "Impresión PLA de alta resolución",
        proveedor: "TechPrint Madrid",
        valoracion: 5,
        numResenhas: 128,
        descripcion:
          "Impresión de prototipos con alta precisión. Material PLA ecológico disponible en diferentes colores.",
        precio: "Desde 15€",
        categoria: "impresion-fdm",
        ubicacion: "Madrid",
        destacado: "Popular",
      },
      {
        id: 2,
        nombre: "Modelado 3D profesional",
        proveedor: "3DDesign Barcelona",
        valoracion: 4,
        numResenhas: 96,
        descripcion: "Diseño y modelado 3D para todo tipo de proyectos. Incluye 3 iteraciones de revisión.",
        precio: "Desde 45€",
        categoria: "modelado",
        ubicacion: "Barcelona",
        destacado: "Mejor valor",
      },
      {
        id: 3,
        nombre: "Impresión SLA ultra detallada",
        proveedor: "ResinMasters Valencia",
        valoracion: 5,
        numResenhas: 84,
        descripcion: "Impresión de alta resolución con resina. Ideal para modelos que requieren alta precisión.",
        precio: "Desde 30€",
        categoria: "impresion-sla",
        ubicacion: "Valencia",
        destacado: "Rápido",
      },
      {
        id: 4,
        nombre: "Producción en serie sostenible",
        proveedor: "EcoPrint Sevilla",
        valoracion: 4,
        numResenhas: 62,
        descripcion: "Producción de múltiples piezas con materiales biodegradables. Descuentos por volumen.",
        precio: "Desde 12€/unidad",
        categoria: "produccion",
        ubicacion: "Sevilla",
        destacado: "Eco",
      },
    ]

    // Datos de fabricantes destacados
    this.fabricantesDestacados = [
      {
        id: 1,
        nombre: "TechPrint Madrid",
        valoracion: 5,
        numResenhas: 128,
        descripcion: "Especialistas en impresión FDM y SLA con más de 5 años de experiencia en el sector.",
        tiempoMedio: "2-3 días",
        satisfaccion: "98%",
      },
      {
        id: 2,
        nombre: "3DDesign Barcelona",
        valoracion: 4,
        numResenhas: 96,
        descripcion: "Expertos en modelado y diseño 3D para aplicaciones industriales y creativas.",
        tiempoMedio: "4-5 días",
        satisfaccion: "96%",
      },
      {
        id: 3,
        nombre: "ResinMasters Valencia",
        valoracion: 5,
        numResenhas: 84,
        descripcion: "Especialistas en impresión SLA de alta precisión para modelos detallados y maquetas.",
        tiempoMedio: "1-2 días",
        satisfaccion: "99%",
      },
    ]
  }

  /**
   * Realiza la búsqueda y filtrado de servicios
   * Solo se ejecuta cuando el usuario hace clic en el botón de buscar
   */
  buscar(): void {
    // No proceder si todos los filtros están vacíos
    if (!this.busquedaTexto && !this.filtroCategoria && !this.filtroUbicacion && !this.filtroValoracion &&
        this.filtroPrecioMin === 10 && this.filtroPrecioMax === 500 && 
        this.filtroMaterial.length === 0 && this.filtroTiempoEntrega.length === 0) {
      console.log("No hay criterios de búsqueda especificados")
      return
    }
    
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

    // Redirigir a la página de resultados de búsqueda con todos los parámetros
    console.log("Redirigiendo a resultados de búsqueda con parámetros:", queryParams)
    this.router.navigate(["resultados-busqueda"], {
      queryParams: queryParams
    })
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

  /**
   * Reinicia todos los filtros
   */
  limpiarFiltros(): void {
    this.busquedaTexto = ""
    this.filtroCategoria = ""
    this.filtroUbicacion = ""
    this.filtroValoracion = ""
    this.filtroPrecioMin = 10
    this.filtroPrecioMax = 500
    this.filtroMaterial = []
    this.filtroTiempoEntrega = []
    
    // Si estamos en la página de resultados, hacer una búsqueda vacía
    // para refrescar la página con los filtros limpios
    if (this.router.url.includes('resultados-busqueda')) {
      this.router.navigate(['resultados-busqueda']);
    }
  }

  /**
   * Redirige al usuario a la página de inicio de sesión
   */
  iniciarSesion(): void {
    // Aquí se añadiría la navegación a la página de login
    console.log("Redirigiendo a página de inicio de sesión")
    this.router.navigate(["/login"])
  }
}
