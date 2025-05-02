import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Categoria } from '../../entidades/categoria/categoria';
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

  constructor(
    private heroBuscadorService: HeroBuscadorService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.cargarCategorias();
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

  limpiarFiltros(): void {
    this.busquedaTexto = '';
    this.filtroCategoria = '';
    this.filtroUbicacion = '';
    this.filtroValoracion = '';
  }
}
