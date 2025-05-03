import { Component, OnInit } from '@angular/core';
import swal from 'sweetalert2';
import { Router } from '@angular/router';
import { Categoria } from '../../entidades/categoria/categoria';
import { CategoriaService } from '../../entidades/categoria/categoria.service';

@Component({
  selector: 'app-selector-categoria',
  standalone: false,
  templateUrl: './selector-categoria.component.html',
  styleUrl: './selector-categoria.component.css'
})
export class SelectorCategoriaComponent implements OnInit{
  categorias : Categoria[]= [];
  constructor(private categoriaService: CategoriaService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.categoriaService.getCategorias().subscribe(
      (categorias: Categoria[]) => {
        this.categorias = categorias;
      }
    );
    // Obtener categorias al cargar el componente
    this.categoriaService.getCategorias().subscribe();
  }

  // Eliminar categoria con confirmación
  delete(categoria: Categoria): void {
    // Mensaje de confirmación de eliminación
    swal({
      title: `¿Estás seguro de eliminar el categoria "${categoria.nombre}"?`,
      text: "¡Esta operación no es reversible!",
      type: "warning",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
      confirmButtonText: "¡Sí, eliminalo!",
      cancelButtonText: "No, cancelar",
      buttonsStyling: true,
      reverseButtons: true
    }).then((result) => {
      if (result.value) {
        this.categoriaService.deleteCategoria(categoria.id).subscribe(
          response => {
            // Cuando la eliminación es exitosa, actualizamos la lista
            this.categorias = this.categorias.filter(a => a.id !== categoria.id);
            swal(
              '¡Eliminado!',
              `La categoria "${categoria.nombre}" ha sido eliminada con éxito`,
              'success'
            );
          },
          error => {
            console.error('Error al eliminar la categoria', error);
            swal(
              'Error',
              'Hubo un problema al eliminar el categoria',
              'error'
            );
          }
        );
      } else if (result.dismiss === swal.DismissReason.cancel) {
        swal(
          'Cancelado',
          'Tu categoria está a salvo :)',
          'error'
        );
      }
    });
  }

  deleteAll(): void {
    // Mensaje confirmacion eliminar todos
    swal({
      title: '¿Estás seguro de eliminar todos los categoriaes?',
      text: "¡Esta operación no es reversible!",
      type: "warning",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
      confirmButtonText: "¡Sí, eliminar todos!",
      cancelButtonText: "No, cancelar",
      buttonsStyling: true,
      reverseButtons: true
    }).then((result) => {
      if (result.value) {
        this.categoriaService.deleteAllCategorias().subscribe(
          response => {
            // Vaciamos el array de categorias
            this.categorias = [];
  
            swal(
              '¡Eliminados!',
              'Todos las categorias han sido eliminados :(',
              'success'
            );
          }
        );
      } else if (result.dismiss === swal.DismissReason.cancel) {
        swal(
          'Cancelado',
          'Tus categorias están a salvo :)',
          'error'
        )
      }
    });
  }
    
  verServiciosCategoria(categoriaId: number): void {
    this.router.navigate(['/servicios'], {
      queryParams: {
        categoriaId: categoriaId,
        page: 0,
        size: 8
      }
    });
  }
}
