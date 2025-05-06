export class Impresora {
    id: number = 0;
    modelo: string = "";
    marca: string = "";
    fechaFabricacion: string = "";
    precio: number = 0;
    volumenImpresion: string = "";
    descripcion: string = "";
    tecnologiaImpresion: string = "";
    urlImagen: string = "";
    enStock: boolean = true;
    categoriaId: number = 0;
    
    // Propiedad para acceder a la fecha como objeto Date
    get fechaFormateada(): Date {
        return this.fechaFabricacion ? new Date(this.fechaFabricacion) : new Date();
    }
    
    set fechaFormateada(fecha: Date) {
        if (fecha) {
            this.fechaFabricacion = fecha.toISOString();
        }
    }
    
    // Constructor para facilitar la transformación
    constructor(data?: Partial<Impresora>) {
        if (data) {
            Object.assign(this, data);
        }
    }
    
    // Método estático para convertir datos del backend
    static fromBackend(data: any): Impresora {
        const impresora = new Impresora({
            id: data.id,
            modelo: data.modelo,
            marca: data.marca,
            precio: data.precio,
            volumenImpresion: data.volumenImpresion,
            descripcion: data.descripcion,
            tecnologiaImpresion: data.tecnologiaImpresion,
            urlImagen: data.urlImagen,
            enStock: data.enStock
        });
        
        // Manejar la fecha específicamente
        if (data.fechaFabricacion) {
            impresora.fechaFabricacion = data.fechaFabricacion;
        }
        
        // Manejar la categoría
        if (data.categoria) {
            impresora.categoriaId = data.categoria.id;
        }
        
        return impresora;
    }
    
    // Método para convertir al formato esperado por el backend
    toBackend(): any {
        return {
            id: this.id,
            modelo: this.modelo,
            marca: this.marca,
            fechaFabricacion: this.fechaFabricacion,
            precio: this.precio,
            volumenImpresion: this.volumenImpresion,
            descripcion: this.descripcion,
            tecnologiaImpresion: this.tecnologiaImpresion,
            urlImagen: this.urlImagen,
            enStock: this.enStock,
            categoria: {
                id: this.categoriaId
            }
        };
    }
}
