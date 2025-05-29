import { Categoria } from "../../entidades/categoria/categoria";
import { Anuncio } from "../../entidades/anuncio/anuncio";

export class ResultadoBusqueda {
    categorias: Categoria[] = []
}

export interface ResultadoBusquedaPaginada {
    content: Anuncio[];
    totalElements: number;
    totalPages: number;
    currentPage: number;
    size: number;
    numberOfElements: number;
    first: boolean;
    last: boolean;
}

export interface ParametrosBusqueda {
    q?: string;
    categoria?: string;
    ubicacion?: string;
    valoracion?: string;
    precioMin?: number;
    precioMax?: number;
    material?: string;
    tiempoEntrega?: string;
    page?: number;
    size?: number;
    orden?: string;
}