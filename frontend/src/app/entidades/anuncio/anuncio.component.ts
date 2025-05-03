import { Component, OnInit } from '@angular/core';
import swal from 'sweetalert2';
import { Anuncio } from './anuncio';
import { AnuncioService } from './anuncio.service';

@Component({
  selector: 'app-anuncio',
  standalone: false,
  templateUrl: './anuncio.component.html',
  styleUrl: './anuncio.component.css'
})
export class AnuncioComponent implements OnInit {

  constructor(private anuncioService: AnuncioService) { }

  ngOnInit(): void {
    this.cargarAnuncios();
  }

  // Lista de anuncios
  anuncios: Anuncio[] = [];
  anunciosFiltrados: Anuncio[] = [];
  
  // Anuncio actual para crear/editar
  anuncioActual: Anuncio = new Anuncio();
  
  // Estado del modal
  modalVisible: boolean = false;
  modoEdicion: boolean = false;
  
  // Estado para el modal de confirmación
  modalConfirmacionVisible: boolean = false;
  tituloConfirmacion: string = '';
  mensajeConfirmacion: string = '';
  accionConfirmacion: Function = () => {};
  
  // Paginación
  paginaActual: number = 1;
  itemsPorPagina: number = 10;
  totalPaginas: number = 1;
  
  // Búsqueda
  termino: string = '';

  // Formulario
  anuncioForm: any;

  cargarAnuncios(): void {
    this.anuncioService.findAll().subscribe(
      (anuncios) => {
        this.anuncios = anuncios;
      },
      (error) => {
        swal('Error al cargar los anuncios', error.error.mensaje, 'error');
      }
    );
  }

  crearAnuncio(): void {
    this.anuncioService.save(this.anuncioActual).subscribe(
      (anuncio) => {
        this.anuncios.push(this.anuncioActual);
        this.anuncioActual = new Anuncio();
        this.modalVisible = false;
      },
      (error) => {
        swal('Error al crear el anuncio', error.error.mensaje, 'error');
      }
    );
  }

  editarAnuncio(): void {
    this.anuncioService.update(this.anuncioActual).subscribe(
      (anuncio) => {
        const index = this.anuncios.findIndex((a) => a.id === this.anuncioActual.id);
        this.anuncios[index] = this.anuncioActual;
        this.anuncioActual = new Anuncio();
        this.modalVisible = false;
      },
      (error) => {
        swal('Error al editar el anuncio', error.error.mensaje, 'error');
      }
    );
  }

  eliminarAnuncio(id: number): void {
    this.anuncioService.delete(id).subscribe(
      () => {
        this.cargarAnuncios();
      },
      (error) => {
        swal('Error al eliminar el anuncio', error.error.mensaje, 'error');
      }
    );
  }

  eliminarTodosAnuncios(): void {
    this.anuncioService.deleteAll().subscribe(
      () => {
        this.cargarAnuncios();
      },
      (error) => {
        swal('Error al eliminar todos los anuncios', error.error.mensaje, 'error');
      }
    );
  }

  cerrarModal(): void {
    this.modalVisible = false;
    this.anuncioActual = new Anuncio();
    this.anuncioForm.reset();
  }
}
