export class Material {
    id: number = 0;
    nombre: string = "";
    descripcion: string = "";
    propiedades: string = "";
    color_disponibles: string = "";
    precio_kg: number = 0;
    activo: boolean = true;
    
    // Constructor para facilitar la transformación
    constructor(data?: Partial<Material>) {
        if (data) {
            Object.assign(this, data);
        }
    }
    
    // Método estático para convertir datos del backend
    static fromBackend(data: any): Material {
        return new Material({
            id: data.id,
            nombre: data.nombre,
            descripcion: data.descripcion,
            propiedades: data.propiedades,
            color_disponibles: data.color_disponibles,
            precio_kg: data.precio_kg,
            activo: data.activo
        });
    }
    
    // Método para convertir al formato esperado por el backend
    toBackend(): any {
        return {
            id: this.id,
            nombre: this.nombre,
            descripcion: this.descripcion,
            propiedades: this.propiedades,
            color_disponibles: this.color_disponibles,
            precio_kg: this.precio_kg,
            activo: this.activo
        };
    }

    // Método de utilidad para obtener precio formateado
    getPrecioFormateado(): string {
        return new Intl.NumberFormat('es-ES', {
            style: 'currency',
            currency: 'EUR'
        }).format(this.precio_kg);
    }

    // Método de utilidad para obtener colores como array
    getColoresArray(): string[] {
        if (!this.color_disponibles) return [];
        return this.color_disponibles.split(',').map(color => color.trim());
    }
} 