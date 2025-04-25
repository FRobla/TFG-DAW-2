import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-principal',
  standalone: false,
  templateUrl: './principal.component.html',
  styleUrl: './principal.component.css'
})
export class PrincipalComponent implements OnInit {
  // Variables para búsqueda y filtrado
  busquedaTexto: string = '';
  filtroCategoria: string = '';
  filtroUbicacion: string = '';
  filtroValoracion: string = '';
  
  // Mockup de servicios que estarían viniendo del backend
  servicios: any[] = [];
  serviciosFiltrados: any[] = [];
  
  // Mockup de fabricantes destacados
  fabricantesDestacados: any[] = [];
  
  constructor() { }

  ngOnInit(): void {
    // Inicializar datos para la demo
    this.inicializarDatos();
    
    // Establecer servicios filtrados inicialmente como todos los servicios
    this.serviciosFiltrados = [...this.servicios];
  }
  
  /**
   * Inicializa datos de prueba para la demostración
   */
  inicializarDatos(): void {
    // Datos de servicios populares
    this.servicios = [
      {
        id: 1,
        nombre: 'Impresión PLA de alta resolución',
        proveedor: 'TechPrint Madrid',
        valoracion: 5,
        numResenhas: 128,
        descripcion: 'Impresión de prototipos con alta precisión. Material PLA ecológico disponible en diferentes colores.',
        precio: 'Desde 15€',
        categoria: 'impresion-fdm',
        ubicacion: 'Madrid',
        destacado: 'Popular'
      },
      {
        id: 2,
        nombre: 'Modelado 3D profesional',
        proveedor: '3DDesign Barcelona',
        valoracion: 4,
        numResenhas: 96,
        descripcion: 'Diseño y modelado 3D para todo tipo de proyectos. Incluye 3 iteraciones de revisión.',
        precio: 'Desde 45€',
        categoria: 'modelado',
        ubicacion: 'Barcelona',
        destacado: 'Mejor valor'
      },
      {
        id: 3,
        nombre: 'Impresión SLA ultra detallada',
        proveedor: 'ResinMasters Valencia',
        valoracion: 5,
        numResenhas: 84,
        descripcion: 'Impresión de alta resolución con resina. Ideal para modelos que requieren alta precisión.',
        precio: 'Desde 30€',
        categoria: 'impresion-sla',
        ubicacion: 'Valencia',
        destacado: 'Rápido'
      },
      {
        id: 4,
        nombre: 'Producción en serie sostenible',
        proveedor: 'EcoPrint Sevilla',
        valoracion: 4,
        numResenhas: 62,
        descripcion: 'Producción de múltiples piezas con materiales biodegradables. Descuentos por volumen.',
        precio: 'Desde 12€/unidad',
        categoria: 'produccion',
        ubicacion: 'Sevilla',
        destacado: 'Eco'
      }
    ];
    
    // Datos de fabricantes destacados
    this.fabricantesDestacados = [
      {
        id: 1,
        nombre: 'TechPrint Madrid',
        valoracion: 5,
        numResenhas: 128,
        descripcion: 'Especialistas en impresión FDM y SLA con más de 5 años de experiencia en el sector.',
        tiempoMedio: '2-3 días',
        satisfaccion: '98%'
      },
      {
        id: 2,
        nombre: '3DDesign Barcelona',
        valoracion: 4,
        numResenhas: 96,
        descripcion: 'Expertos en modelado y diseño 3D para aplicaciones industriales y creativas.',
        tiempoMedio: '4-5 días',
        satisfaccion: '96%'
      },
      {
        id: 3,
        nombre: 'ResinMasters Valencia',
        valoracion: 5,
        numResenhas: 84,
        descripcion: 'Especialistas en impresión SLA de alta precisión para modelos detallados y maquetas.',
        tiempoMedio: '1-2 días',
        satisfaccion: '99%'
      }
    ];
  }
  
  /**
   * Realiza la búsqueda y filtrado de servicios
   */
  buscar(): void {
    // Filtrar por texto de búsqueda
    this.serviciosFiltrados = this.servicios.filter(servicio => {
      // Filtro de texto en nombre, proveedor o descripción
      const coincideTexto = this.busquedaTexto === '' || 
                           servicio.nombre.toLowerCase().includes(this.busquedaTexto.toLowerCase()) ||
                           servicio.proveedor.toLowerCase().includes(this.busquedaTexto.toLowerCase()) ||
                           servicio.descripcion.toLowerCase().includes(this.busquedaTexto.toLowerCase());
                           
      // Filtro de categoría
      const coincideCategoria = this.filtroCategoria === '' || 
                               servicio.categoria === this.filtroCategoria;
                               
      // Filtro de ubicación
      const coincideUbicacion = this.filtroUbicacion === '' || 
                              servicio.ubicacion === this.filtroUbicacion;
                              
      // Filtro de valoración
      const coincideValoracion = this.filtroValoracion === '' || 
                               this.filtroValoracion === '3+' && servicio.valoracion >= 3 ||
                               this.filtroValoracion === '4+' && servicio.valoracion >= 4 ||
                               this.filtroValoracion === '5' && servicio.valoracion === 5;
                               
      return coincideTexto && coincideCategoria && coincideUbicacion && coincideValoracion;
    });
  }
  
  /**
   * Actualiza el filtro de categoría
   * @param categoria Nueva categoría seleccionada
   */
  seleccionarCategoria(categoria: string): void {
    this.filtroCategoria = categoria;
    this.buscar();
  }
  
  /**
   * Actualiza el filtro de ubicación
   * @param ubicacion Nueva ubicación seleccionada
   */
  seleccionarUbicacion(ubicacion: string): void {
    this.filtroUbicacion = ubicacion;
    this.buscar();
  }
  
  /**
   * Actualiza el filtro de valoración
   * @param valoracion Nueva valoración seleccionada
   */
  seleccionarValoracion(valoracion: string): void {
    this.filtroValoracion = valoracion;
    this.buscar();
  }
  
  /**
   * Reinicia todos los filtros
   */
  limpiarFiltros(): void {
    this.busquedaTexto = '';
    this.filtroCategoria = '';
    this.filtroUbicacion = '';
    this.filtroValoracion = '';
    this.serviciosFiltrados = [...this.servicios];
  }
}
