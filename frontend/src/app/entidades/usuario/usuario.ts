import { Ubicacion } from '../ubicacion/ubicacion';

export class Usuario {
    id: number = 0;
    nombre: string = "";
    apellido: string = "";
    direccion: string = "";
    email: string = "";
    password: string = "";
    rol: string = "";
    fotoPerfil: string = "";
    fecha_registro: string = "";
    ubicacion?: Ubicacion;
    
    // Propiedad para acceder a la fecha como objeto Date
    get fechaRegistro(): Date {
        return this.fecha_registro ? new Date(this.fecha_registro) : new Date();
    }
    
    set fechaRegistro(fecha: Date) {
        if (fecha) {
            this.fecha_registro = fecha.toISOString();
        }
    }
    
    // Constructor para facilitar la transformación
    constructor(data?: Partial<Usuario>) {
        if (data) {
            Object.assign(this, data);
        }
    }
    
    // Método estático para convertir datos del backend
    static fromBackend(data: any): Usuario {
        const usuario = new Usuario({
            id: data.id,
            nombre: data.nombre,
            apellido: data.apellido,
            direccion: data.direccion,
            email: data.email,
            password: data.password,
            rol: data.rol,
            fotoPerfil: data.foto,
            ubicacion: data.ubicacion
        });
        
        // Manejar la fecha específicamente
        if (data.fecha_registro) {
            usuario.fecha_registro = data.fecha_registro;
        }
        
        return usuario;
    }
    
    // Método para convertir al formato esperado por el backend
    toBackend(): any {
        const backendData: any = {
            id: this.id,
            nombre: this.nombre,
            apellido: this.apellido,
            direccion: this.direccion,
            email: this.email,
            password: this.password,
            rol: this.rol,
            foto: this.fotoPerfil,
            fecha_registro: this.fecha_registro
        };

        // Manejar la ubicación - enviar solo el ID si existe
        if (this.ubicacion && this.ubicacion.id) {
            backendData.ubicacion = {
                id: this.ubicacion.id
            };
        } else {
            backendData.ubicacion = null;
        }

        return backendData;
    }

    // Método de utilidad para obtener el nombre de la ubicación
    getNombreUbicacion(): string {
        return this.ubicacion ? this.ubicacion.nombre : 'No especificada';
    }
}