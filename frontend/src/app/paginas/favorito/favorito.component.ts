import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FavoritoService, Favorito, FavoritoResponse } from './favorito.service';
import { Anuncio } from '../../entidades/anuncio/anuncio';
import { AuthService } from '../../auth/auth.service';

@Component({
  selector: 'app-favorito',
  standalone: false,
  templateUrl: './favorito.component.html',
  styleUrl: './favorito.component.css'
})
export class FavoritoComponent implements OnInit {
  
  // Variables para favoritos
  favoritos: Favorito[] = [];
  favoritosAnuncios: Anuncio[] = []; // Solo los anuncios para mostrar
  
  // Variables para paginación
  paginaActual: number = 0;
  totalItems: number = 0;
  totalPaginas: number = 0;
  tamañoPagina: number = 12;
  primeraPagina: boolean = true;
  ultimaPagina: boolean = true;
  
  // Variables de estado
  cargando: boolean = false;
  error: string = '';
  usuarioId: number = 0;
  
  // Variables para vista
  vistaActual: string = 'grid'; // 'grid' o 'list'
  
  constructor(
    private favoritoService: FavoritoService,
    public router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    console.log('FavoritoComponent iniciando...');
    // Temporalmente simplificado para debugging
    this.obtenerUsuarioId();
    // this.cargarFavoritos(); // Comentado temporalmente
  }

  /**
   * Obtiene el ID del usuario desde AuthService
   */
  private obtenerUsuarioId(): void {
    if (!this.authService.estaLogueado()) {
      console.warn('Usuario no autenticado, redirigiendo a login');
      this.router.navigate(['/login']);
      return;
    }

    this.usuarioId = this.authService.getCurrentUserId();
    console.log('Usuario ID obtenido:', this.usuarioId);
    
    if (!this.usuarioId || this.usuarioId <= 0) {
      console.error('ID de usuario inválido');
      this.router.navigate(['/login']);
    }
  }

  /**
   * Carga los favoritos del usuario con paginación
   */
  cargarFavoritos(): void {
    if (this.usuarioId <= 0) {
      return;
    }

    this.cargando = true;
    this.error = '';

    this.favoritoService.getFavoritosByUsuario(this.usuarioId, this.paginaActual, this.tamañoPagina)
      .subscribe({
        next: (response: FavoritoResponse) => {
          this.favoritos = response.favoritos || [];
          this.favoritosAnuncios = this.favoritos.map(fav => fav.anuncio);
          this.totalItems = response.totalItems || 0;
          this.totalPaginas = response.totalPages || 0;
          this.paginaActual = response.currentPage || 0;
          this.tamañoPagina = response.pageSize || 12;
          
          // Actualizar flags de paginación
          this.primeraPagina = this.paginaActual === 0;
          this.ultimaPagina = this.paginaActual >= this.totalPaginas - 1;
          
          this.cargando = false;
          console.log('Favoritos cargados:', this.favoritos);
        },
        error: (error) => {
          console.error('Error al cargar favoritos:', error);
          this.error = 'Error al cargar tus favoritos. Por favor, inténtalo de nuevo.';
          this.cargando = false;
        }
      });
  }

  /**
   * Elimina un anuncio de favoritos
   */
  eliminarFavorito(anuncio: Anuncio): void {
    if (!anuncio || !anuncio.id) {
      return;
    }

    this.favoritoService.deleteFavorito(anuncio.id, this.usuarioId)
      .subscribe({
        next: () => {
          // Recargar favoritos para actualizar la lista
          this.cargarFavoritos();
          console.log('Favorito eliminado correctamente');
        },
        error: (error) => {
          console.error('Error al eliminar favorito:', error);
          this.error = 'Error al eliminar el favorito. Por favor, inténtalo de nuevo.';
        }
      });
  }

  /**
   * Navega a los detalles de un anuncio
   */
  verDetalles(anuncio: Anuncio): void {
    if (anuncio && anuncio.id) {
      this.router.navigate(['/anuncio', anuncio.id]);
    }
  }

  /**
   * Cambia la vista entre grid y list
   */
  cambiarVista(vista: string): void {
    this.vistaActual = vista;
  }

  /**
   * Navega a la página anterior
   */
  paginaAnterior(): void {
    if (!this.primeraPagina) {
      this.paginaActual--;
      this.cargarFavoritos();
    }
  }

  /**
   * Navega a la página siguiente
   */
  paginaSiguiente(): void {
    if (!this.ultimaPagina) {
      this.paginaActual++;
      this.cargarFavoritos();
    }
  }

  /**
   * Navega a una página específica
   */
  cambiarPagina(pagina: number): void {
    if (pagina >= 0 && pagina < this.totalPaginas && pagina !== this.paginaActual) {
      this.paginaActual = pagina;
      this.cargarFavoritos();
    }
  }

  /**
   * Obtiene un array de números para la paginación
   */
  getPaginasArray(): number[] {
    const maxPaginas = 5;
    const inicio = Math.max(0, this.paginaActual - Math.floor(maxPaginas / 2));
    const fin = Math.min(this.totalPaginas, inicio + maxPaginas);
    
    const paginas: number[] = [];
    for (let i = inicio; i < fin; i++) {
      paginas.push(i);
    }
    return paginas;
  }

  /**
   * Formatea el precio de un anuncio
   */
  formatearPrecio(precio: number): string {
    return new Intl.NumberFormat('es-ES', {
      style: 'currency',
      currency: 'EUR'
    }).format(precio);
  }

  /**
   * Formatea la fecha de publicación
   */
  formatearFecha(fecha: string): string {
    if (!fecha) return '';
    const fechaObj = new Date(fecha);
    return fechaObj.toLocaleDateString('es-ES');
  }

  /**
   * Obtiene el nombre del usuario que publicó el anuncio
   */
  getNombreUsuario(anuncio: Anuncio): string {
    return anuncio.usuario?.nombre || 'Usuario';
  }

  /**
   * Obtiene las categorías del anuncio como string
   */
  getCategoriasString(anuncio: Anuncio): string {
    if (anuncio.categorias && anuncio.categorias.length > 0) {
      return anuncio.categorias.map(c => c.nombre).join(', ');
    }
    return '';
  }

  /**
   * Reinicia los filtros y recarga
   */
  recargar(): void {
    this.paginaActual = 0;
    this.cargarFavoritos();
  }

  /**
   * Navega a la página de búsqueda
   */
  navegarABusqueda(): void {
    this.router.navigate(['/resultados-busqueda']);
  }

  /**
   * Maneja errores de carga de imágenes
   */
  onImageError(event: any): void {
    if (event && event.target) {
      event.target.src = '/assets/img/default-service.jpg';
    }
  }
}
