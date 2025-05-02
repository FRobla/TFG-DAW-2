import { Categoria } from "../categoria/categoria";
import { Usuario } from "../usuario/usuario";

export class Anuncio {
    id: number = 0;
    titulo: string = "";
    descripcion: string = "";
    estado: string = "";
    imagen: string = "";
    precio_base: number = 0;
    tiempo_estimado: string = "";
    fechaPublicacion: Date = new Date();
    vistas: number = 0;
    valoracionMedia: number = 0;

    usuario: Usuario = new Usuario();
    // impresora: Impresora = new Impresora();
    categorias: Categoria[] = [];
    // material: Material[] = [];
    // valoraciones: Valoracion[] = [];
}
