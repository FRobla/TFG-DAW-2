import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Categoria } from '../../entidades/categoria/categoria';
import { Ubicacion } from '../../entidades/ubicacion/ubicacion';
import { HeroBuscadorService } from './hero-buscador.service';

@Component({
  selector: 'app-hero-buscador',
  standalone: false,
  templateUrl: './hero-buscador.component.html',
  styleUrls: ['./hero-buscador.component.css']
})
export class HeroBuscadorComponent implements OnInit {
  busquedaTexto: string = '';
  filtroCategoria: string = '';
  filtroUbicacion: string = '';
  filtroValoracion: string = '';
  filtroPrecioMin = 10;
  filtroPrecioMax = 500;
  filtroMaterial: string[] = [];
  filtroTiempoEntrega: string[] = [];
  categorias: Categoria[] = [];
  ubicaciones: Ubicacion[] = [];

  constructor(
    private heroBuscadorService: HeroBuscadorService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.cargarCategorias();
    this.cargarUbicaciones();
  }

  cargarCategorias(): void {
    this.heroBuscadorService.getCategorias().subscribe({
      next: (categorias) => {
        this.categorias = categorias;
      },
      error: (error) => {
        console.error('Error al cargar categorías:', error);
      }
    });
  }

  cargarUbicaciones(): void {
    this.heroBuscadorService.getUbicacionesConAnuncios().subscribe({
      next: (ubicaciones) => {
        this.ubicaciones = ubicaciones;
      },
      error: (error) => {
        console.error('Error al cargar ubicaciones:', error);
      }
    });
  }

  buscar(): void {
    // Crear un objeto con todos los filtros seleccionados
    const queryParams: any = {}

    // Verificar si hay algún criterio de búsqueda
    const hayTerminoBusqueda = this.busquedaTexto && this.busquedaTexto.trim() !== '';
    const hayFiltroCategoria = this.filtroCategoria !== '';
    const hayFiltroUbicacion = this.filtroUbicacion !== '';
    const hayFiltroValoracion = this.filtroValoracion !== '';
    const hayFiltroPrecio = this.filtroPrecioMin !== 10 || this.filtroPrecioMax !== 500;
    const hayFiltroMaterial = this.filtroMaterial.length > 0;
    const hayFiltroTiempoEntrega = this.filtroTiempoEntrega.length > 0;

    const hayCriteriosBusqueda = hayTerminoBusqueda || hayFiltroCategoria || hayFiltroUbicacion || 
                                hayFiltroValoracion || hayFiltroPrecio || hayFiltroMaterial || hayFiltroTiempoEntrega;

    // Si no hay criterios de búsqueda, redirigir a resultados-busqueda sin parámetros
    if (!hayCriteriosBusqueda) {
      console.log("No hay criterios de búsqueda especificados, redirigiendo a resultados vacíos");
      this.router.navigate(["/resultados-busqueda"]);
      return;
    }

    // Añadir filtros solo si tienen valor
    if (hayTerminoBusqueda) queryParams.q = this.busquedaTexto.trim();
    if (hayFiltroCategoria) queryParams.categoria = this.filtroCategoria;
    if (hayFiltroUbicacion) queryParams.ubicacion = this.filtroUbicacion;
    if (hayFiltroValoracion) queryParams.valoracion = this.filtroValoracion;

    // Añadir filtros de precio si son diferentes a los valores por defecto
    if (this.filtroPrecioMin !== 10) queryParams.precioMin = this.filtroPrecioMin;
    if (this.filtroPrecioMax !== 500) queryParams.precioMax = this.filtroPrecioMax;

    // Añadir filtros de arrays solo si tienen elementos
    if (hayFiltroMaterial) queryParams.material = this.filtroMaterial.join(",");
    if (hayFiltroTiempoEntrega) queryParams.tiempoEntrega = this.filtroTiempoEntrega.join(",");

    // Redirigir a la página de resultados de búsqueda con todos los parámetros
    console.log("Redirigiendo a resultados de búsqueda con parámetros:", queryParams);
    this.router.navigate(["/resultados-busqueda"], {
      queryParams: queryParams
    });
  }

  limpiarFiltros(): void {
    this.busquedaTexto = '';
    this.filtroCategoria = '';
    this.filtroUbicacion = '';
    this.filtroValoracion = '';
  }
}
