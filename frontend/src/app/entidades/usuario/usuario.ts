export class Usuario {
    id: number = 0;
    nombre: string = "";
    apellido: string = "";
    direccion: string = "";
    email: string = "";
    password: string = "";
    rol: string = "";
    fotoPerfil: string = "";
    fecha_registro: string = ""; // Cambiado de fechaRegistro a fecha_registro para coincidir con el backend
    
    // Propiedad calculada para acceder a la fecha como objeto Date
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
            fotoPerfil: data.foto
        });
        
        // Manejar la fecha específicamente
        if (data.fecha_registro) {
            usuario.fecha_registro = data.fecha_registro;
        }
        
        return usuario;
    }
    
    // Método para convertir al formato esperado por el backend
    toBackend(): any {
        return {
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
    }
}