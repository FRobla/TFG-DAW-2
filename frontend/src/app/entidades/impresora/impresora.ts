export class Impresora {
    id: number = 0;
    modelo: string = "";
    marca: string = "";
    tecnologia: string = "";
    volumen_impresion_x: number = 0;
    volumen_impresion_y: number = 0;
    volumen_impresion_z: number = 0;
    precision_valor: number = 0;
    descripcion: string = "";
    imagen: string = "";
    
    // Propiedades adicionales para compatibilidad con el frontend
    precio: number = 0;
    urlImagen: string = "";
    tecnologiaImpresion: string = "";
    volumenImpresion: string = "";
    categoriaId: number = 0;
    enStock: boolean = true;
    fechaFabricacion: string = "";
    
    // Constructor para facilitar la transformación
    constructor(data?: Partial<Impresora>) {
        if (data) {
            Object.assign(this, data);
        }
    }
    
    // Propiedad para acceder a la fecha como objeto Date
    get fechaFormateada(): Date {
        return this.fechaFabricacion ? new Date(this.fechaFabricacion) : new Date();
    }
    
    set fechaFormateada(fecha: Date) {
        if (fecha) {
            this.fechaFabricacion = fecha.toISOString().split('T')[0];
        }
    }
    
    // Método estático para convertir datos del backend
    static fromBackend(data: any): Impresora {
        const impresora = new Impresora({
            id: data.id,
            modelo: data.modelo,
            marca: data.marca,
            tecnologia: data.tecnologia,
            volumen_impresion_x: data.volumen_impresion_x,
            volumen_impresion_y: data.volumen_impresion_y,
            volumen_impresion_z: data.volumen_impresion_z,
            precision_valor: data.precision_valor,
            descripcion: data.descripcion,
            imagen: data.imagen
        });
        
        // Mapear propiedades adicionales para compatibilidad
        impresora.urlImagen = data.imagen || "";
        impresora.tecnologiaImpresion = data.tecnologia || "";
        impresora.precio = data.precio || 0;
        impresora.categoriaId = data.categoriaId || 0;
        impresora.enStock = data.enStock !== undefined ? data.enStock : true;
        impresora.fechaFabricacion = data.fechaFabricacion || "";
        
        // Generar volumenImpresion como string
        if (data.volumen_impresion_x && data.volumen_impresion_y && data.volumen_impresion_z) {
            impresora.volumenImpresion = `${data.volumen_impresion_x}x${data.volumen_impresion_y}x${data.volumen_impresion_z}mm`;
        }
        
        return impresora;
    }

    // Método específico para mapear desde backend (para compatibilidad)
    static fromBackendImpresora(data: any): Impresora {
        return Impresora.fromBackend(data);
    }
    
    // Método para convertir al formato esperado por el backend
    toBackend(): any {
        return {
            id: this.id,
            modelo: this.modelo,
            marca: this.marca,
            tecnologia: this.tecnologiaImpresion || this.tecnologia,
            volumen_impresion_x: this.volumen_impresion_x,
            volumen_impresion_y: this.volumen_impresion_y,
            volumen_impresion_z: this.volumen_impresion_z,
            precision_valor: this.precision_valor,
            descripcion: this.descripcion,
            imagen: this.urlImagen || this.imagen,
            precio: this.precio,
            categoriaId: this.categoriaId,
            enStock: this.enStock,
            fechaFabricacion: this.fechaFabricacion
        };
    }

    // Métodos de utilidad
    getNombreCompleto(): string {
        return `${this.marca} ${this.modelo}`;
    }

    getVolumenFormateado(): string {
        return `${this.volumen_impresion_x}mm x ${this.volumen_impresion_y}mm x ${this.volumen_impresion_z}mm`;
    }

    getPrecisionFormateada(): string {
        return `${this.precision_valor}mm`;
    }
    
    // Método de utilidad para obtener precio formateado
    getPrecioFormateado(): string {
        return new Intl.NumberFormat('es-ES', {
            style: 'currency',
            currency: 'EUR'
        }).format(this.precio);
    }
}
