import { Categoria } from "../categoria/categoria";
import { Usuario } from "../usuario/usuario";
import { Impresora } from "../impresora/impresora";

export class Anuncio {
    id: number = 0;
    titulo: string = "";
    descripcion: string = "";
    precio: number = 0;
    fechaPublicacion: string = "";
    estado: string = "ACTIVO";
    urlImagen: string = "";
    usuarioId: number = 0;
    categoriaId: number = 0;
    impresoraId: number = 0;
    tiempoEstimado: string = "";
    vistas: number = 0;
    
    // Propiedades de relaciones
    usuario?: Usuario;
    categorias?: Categoria[];
    impresora?: Impresora;

    // Propiedad para acceder a la fecha como objeto Date
    get fechaFormateada(): Date {
        return this.fechaPublicacion ? new Date(this.fechaPublicacion) : new Date();
    }
    
    set fechaFormateada(fecha: Date) {
        if (fecha) {
            this.fechaPublicacion = fecha.toISOString();
        }
    }
    
    // Constructor para facilitar la transformación
    constructor(data?: Partial<Anuncio>) {
        if (data) {
            Object.assign(this, data);
        }
    }
    
    // Método estático para convertir datos del backend
    static fromBackend(data: any): Anuncio {
        const anuncio = new Anuncio({
            id: data.id,
            titulo: data.titulo,
            descripcion: data.descripcion,
            precio: data.precio_base, // Mapear precio_base a precio
            estado: data.estado,
            urlImagen: data.imagen || data.urlImagen, // Considerar ambos posibles nombres
            tiempoEstimado: data.tiempo_estimado || "",
            vistas: data.vistas || 0
        });
        
        // Manejar la fecha específicamente
        if (data.fecha_publicacion) {
            anuncio.fechaPublicacion = data.fecha_publicacion;
        } else if (data.fechaPublicacion) {
            anuncio.fechaPublicacion = data.fechaPublicacion;
        }
        
        // Manejar las relaciones
        if (data.usuario) {
            anuncio.usuarioId = data.usuario.id;
            anuncio.usuario = data.usuario;
        }
        if (data.categoria) {
            anuncio.categoriaId = data.categoria.id;
        } else if (data.categorias && data.categorias.length > 0) {
            anuncio.categoriaId = data.categorias[0].id;
            anuncio.categorias = data.categorias;
        }
        if (data.impresora) {
            anuncio.impresoraId = data.impresora.id;
            anuncio.impresora = data.impresora;
        }
        
        return anuncio;
    }
    
    // Método para convertir al formato esperado por el backend
    toBackend(): any {
        return {
            id: this.id,
            titulo: this.titulo,
            descripcion: this.descripcion,
            precio_base: this.precio, // Mapear precio a precio_base
            fechaPublicacion: this.fechaPublicacion,
            estado: this.estado,
            imagen: this.urlImagen,
            tiempo_estimado: this.tiempoEstimado,
            vistas: this.vistas,
            usuario: {
                id: this.usuarioId
            },
            categoria: {
                id: this.categoriaId
            },
            impresora: {
                id: this.impresoraId
            }
        };
    }

    // Métodos de utilidad para obtener información formateada
    getPrecioFormateado(): string {
        return new Intl.NumberFormat('es-ES', {
            style: 'currency',
            currency: 'EUR'
        }).format(this.precio);
    }

    getFechaFormateada(): string {
        if (!this.fechaPublicacion) return '';
        const fecha = new Date(this.fechaPublicacion);
        return fecha.toLocaleDateString('es-ES');
    }

    getNombreUsuario(): string {
        return this.usuario?.nombre || 'Usuario';
    }

    getCategoriasNombres(): string {
        if (this.categorias && this.categorias.length > 0) {
            return this.categorias.map(c => c.nombre).join(', ');
        }
        return '';
    }
}
