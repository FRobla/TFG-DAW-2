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
            precio: data.precio,
            estado: data.estado,
            urlImagen: data.urlImagen
        });
        
        // Manejar la fecha específicamente
        if (data.fechaPublicacion) {
            anuncio.fechaPublicacion = data.fechaPublicacion;
        }
        
        // Manejar las relaciones
        if (data.usuario) {
            anuncio.usuarioId = data.usuario.id;
        }
        if (data.categoria) {
            anuncio.categoriaId = data.categoria.id;
        }
        if (data.impresora) {
            anuncio.impresoraId = data.impresora.id;
        }
        
        return anuncio;
    }
    
    // Método para convertir al formato esperado por el backend
    toBackend(): any {
        return {
            id: this.id,
            titulo: this.titulo,
            descripcion: this.descripcion,
            precio: this.precio,
            fechaPublicacion: this.fechaPublicacion,
            estado: this.estado,
            urlImagen: this.urlImagen,
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
}
