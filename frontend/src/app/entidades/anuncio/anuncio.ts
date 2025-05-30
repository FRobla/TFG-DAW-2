import { Categoria } from "../categoria/categoria";
import { Usuario } from "../usuario/usuario";
import { Impresora } from "../impresora/impresora";
import { Material } from "../material/material";

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
    valoracionMedia: number = 0;
    
    // Propiedades de relaciones
    usuario?: Usuario;
    categorias?: Categoria[];
    impresora?: Impresora;
    materiales?: Material[];

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
            vistas: data.vistas || 0,
            valoracionMedia: data.valoracion_media || 0
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
            anuncio.impresora = Impresora.fromBackendImpresora(data.impresora);
        }
        if (data.materiales && data.materiales.length > 0) {
            anuncio.materiales = data.materiales.map((material: any) => Material.fromBackend(material));
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

    // Métodos de utilidad para impresora
    getNombreImpresora(): string {
        return this.impresora ? `${this.impresora.marca} ${this.impresora.modelo}` : '';
    }

    getTecnologiaImpresora(): string {
        return this.impresora?.tecnologia || '';
    }

    getVolumenMaximoImpresion(): string {
        if (this.impresora?.volumen_impresion_x && this.impresora?.volumen_impresion_y && this.impresora?.volumen_impresion_z) {
            return `${this.impresora.volumen_impresion_x}mm x ${this.impresora.volumen_impresion_y}mm x ${this.impresora.volumen_impresion_z}mm`;
        }
        return '';
    }

    getPrecisionImpresora(): string {
        return this.impresora?.precision_valor ? `${this.impresora.precision_valor}mm` : '';
    }

    // Métodos de utilidad para materiales
    getNombresMateriales(): string[] {
        return this.materiales ? this.materiales.map(m => m.nombre) : [];
    }

    getColoresDisponibles(): string[] {
        if (!this.materiales) return [];
        const colores = new Set<string>();
        this.materiales.forEach(material => {
            material.getColoresArray().forEach(color => colores.add(color));
        });
        return Array.from(colores);
    }

    getPropsiedadesMateriales(): string[] {
        if (!this.materiales) return [];
        return this.materiales
            .map(m => m.propiedades)
            .filter(prop => prop && prop.trim() !== '')
            .flatMap(prop => prop.split(',').map(p => p.trim()));
    }

    // Método para obtener el material más económico
    getMaterialMasEconomico(): Material | undefined {
        if (!this.materiales || this.materiales.length === 0) return undefined;
        return this.materiales.reduce((prev, current) => 
            (prev.precio_kg < current.precio_kg) ? prev : current
        );
    }

    // Método para obtener información de valoración formateada
    getEstrellas(): string {
        const puntuacion = Math.round(this.valoracionMedia);
        let estrellas = '';
        for (let i = 1; i <= 5; i++) {
            estrellas += i <= puntuacion ? '★' : '☆';
        }
        return estrellas;
    }
}
