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
    public authService: AuthService
  ) {}

  ngOnInit(): void {
    console.log('=== DEBUG FAVORITOS: Componente iniciando ===');
    this.obtenerUsuarioId();
    this.cargarFavoritos(); // Descomentado para que funcione
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
    console.log('=== DEBUG FAVORITOS: Usuario ID obtenido ===', this.usuarioId);
    
    if (!this.usuarioId || this.usuarioId <= 0) {
      console.error('ID de usuario inválido');
      this.router.navigate(['/login']);
    }
  }

  /**
   * Carga los favoritos del usuario con paginación
   */
  cargarFavoritos(): void {
    console.log('=== DEBUG FAVORITOS: Iniciando carga de favoritos para usuario ===', this.usuarioId);
    
    if (this.usuarioId <= 0) {
      console.error('=== DEBUG FAVORITOS: Usuario ID inválido ===', this.usuarioId);
      return;
    }

    this.cargando = true;
    this.error = '';

    this.favoritoService.getFavoritosByUsuario(this.usuarioId, this.paginaActual, this.tamañoPagina)
      .subscribe({
        next: (response: FavoritoResponse) => {
          console.log('=== DEBUG FAVORITOS: Respuesta del backend ===', response);
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
          console.log('=== DEBUG FAVORITOS: Favoritos cargados ===', {
            total: this.favoritos.length,
            favoritos: this.favoritos,
            anuncios: this.favoritosAnuncios
          });
          
          // Debug específico para precios
          this.favoritosAnuncios.forEach((anuncio, index) => {
            console.log(`=== DEBUG FAVORITO ${index}: Precio ===`, {
              titulo: anuncio.titulo,
              precio: anuncio.precio,
              tipoPrecio: typeof anuncio.precio,
              anuncioCompleto: anuncio
            });
          });
        },
        error: (error) => {
          console.error('=== DEBUG FAVORITOS: Error al cargar favoritos ===', error);
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

    // Confirmar eliminación
    if (confirm(`¿Estás seguro de que quieres eliminar "${anuncio.titulo}" de tus favoritos?`)) {
      console.log('=== DEBUG FAVORITOS: Eliminando favorito ===', {
        anuncioId: anuncio.id,
        usuarioId: this.usuarioId,
        titulo: anuncio.titulo
      });

      this.favoritoService.deleteFavorito(anuncio.id, this.usuarioId)
        .subscribe({
          next: (response) => {
            console.log('=== DEBUG FAVORITOS: Favorito eliminado ===', response);
            // Recargar favoritos para actualizar la lista
            this.cargarFavoritos();
          },
          error: (error) => {
            console.error('=== DEBUG FAVORITOS: Error al eliminar favorito ===', error);
            
            let mensaje = 'Error al eliminar el favorito. Por favor, inténtalo de nuevo.';
            if (error.status === 404) {
              mensaje = 'El favorito ya había sido eliminado.';
              // Recargar para actualizar la vista
              this.cargarFavoritos();
            } else if (error.status === 500) {
              mensaje = 'Error del servidor. Por favor, inténtalo más tarde.';
            }
            
            this.error = mensaje;
            
            // Limpiar error después de 5 segundos
            setTimeout(() => {
              this.error = '';
            }, 5000);
          }
        });
    }
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
    // Verificar si el precio es válido
    if (precio === null || precio === undefined || isNaN(precio) || precio < 0) {
      return 'Precio no disponible';
    }
    
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
    event.target.src = 'https://placehold.co/300x200/2a2a2a/ffffff?text=Servicio+3D';
  }

  /**
   * Muestra información del usuario (método de debug)
   */
  mostrarInfoUsuario(): void {
    const usuario = this.authService.getCurrentUser();
    console.log('=== DEBUG FAVORITOS: Información del usuario ===', usuario);
    
    alert(`
      Información del Usuario (DEBUG):
      - ID: ${this.authService.getCurrentUserId()}
      - Email: ${this.authService.getCurrentUserEmail()}
      - Token presente: ${this.authService.getToken() ? 'Sí' : 'No'}
      - Usuario completo: ${JSON.stringify(usuario, null, 2)}
      - LocalStorage: ${localStorage.getItem('usuario') || 'No encontrado'}
    `);
  }
}
