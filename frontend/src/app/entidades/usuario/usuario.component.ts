import { Component, OnInit, ViewChild } from '@angular/core';
import { UsuarioService } from './usuario.service';
import { Usuario } from './usuario';
import swal from 'sweetalert2';
import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';
import { formatDate } from '@angular/common';
// No necesitamos importaciones adicionales para la exportación

@Component({
  selector: 'app-usuario',
  standalone: false,
  templateUrl: './usuario.component.html',
  styleUrls: ['./usuario.component.css']
})
export class UsuarioComponent implements OnInit {

  // Lista de usuarios
  usuarios: Usuario[] = [];
  usuariosFiltrados: Usuario[] = [];
  usuariosPaginados: Usuario[] = [];
  
  // Usuario actual para crear/editar
  usuarioActual: Usuario = new Usuario();
  
  // Estado del modal
  modalVisible: boolean = false;
  modoEdicion: boolean = false;
  editando: boolean = false;
  
  // Estado de carga
  cargando: boolean = false;
  
  // Estado para el modal de confirmación
  modalConfirmacionVisible: boolean = false;
  tituloConfirmacion: string = '';
  mensajeConfirmacion: string = '';
  accionConfirmacion: Function = () => {};
  
  // Paginación
  paginaActual: number = 1;
  itemsPorPagina: number = 10;
  totalPaginas: number = 1;
  
  // Búsqueda y filtrado
  termino: string = '';
  filtroRol: string = '';
  
  // Ordenación
  ordenarPor: string = 'nombre';
  ordenAscendente: boolean = true;
  
  @ViewChild('usuarioForm') usuarioForm!: NgForm;

  constructor(private usuarioService: UsuarioService, private router: Router) { }

  ngOnInit(): void {
    this.cargarUsuarios();
  }

  /**
   * Carga todos los usuarios desde el servicio
   */
  cargarUsuarios(): void {
    // No activamos el estado de carga para permitir las view-transitions fluidas
    // this.cargando = true; 
    this.usuarioService.getUsuarios().subscribe(
      usuariosData => {
        // Transformamos los datos del backend a objetos Usuario
        this.usuarios = usuariosData.map(data => Usuario.fromBackend(data));
        this.filtrarUsuarios();
        this.calcularTotalPaginas();
        this.aplicarPaginacion();
        
        // Aseguramos que cargando esté desactivado para que no interfiera con las transiciones
        this.cargando = false;
      },
      error => {
        this.cargando = false;
        console.error('Error al cargar usuarios', error);
        swal('Error', 'Hubo un problema al cargar los usuarios', 'error');
      }
    );
  }

  /**
   * Filtra los usuarios según el término de búsqueda
   */
  filtrarUsuarios(): void {
    let usuariosFiltrados = [...this.usuarios];
    
    // Aplicar filtro de búsqueda
    if (this.termino && this.termino.trim() !== '') {
      const terminoLower = this.termino.toLowerCase();
      usuariosFiltrados = usuariosFiltrados.filter(usuario => 
        usuario.nombre.toLowerCase().includes(terminoLower) ||
        usuario.apellido.toLowerCase().includes(terminoLower) ||
        usuario.email.toLowerCase().includes(terminoLower) ||
        usuario.rol.toLowerCase().includes(terminoLower)
      );
    }
    
    // Aplicar filtro por rol
    if (this.filtroRol && this.filtroRol.trim() !== '') {
      usuariosFiltrados = usuariosFiltrados.filter(usuario => 
        usuario.rol === this.filtroRol
      );
    }
    
    // Aplicar ordenación
    usuariosFiltrados.sort((a, b) => {
      let valorA, valorB;
      
      switch (this.ordenarPor) {
        case 'nombre':
          valorA = a.nombre.toLowerCase();
          valorB = b.nombre.toLowerCase();
          break;
        case 'fecha':
          valorA = new Date(a.fechaRegistro).getTime();
          valorB = new Date(b.fechaRegistro).getTime();
          break;
        case 'rol':
          valorA = a.rol;
          valorB = b.rol;
          break;
        default:
          valorA = a.id;
          valorB = b.id;
          break;
      }
      
      if (valorA < valorB) return this.ordenAscendente ? -1 : 1;
      if (valorA > valorB) return this.ordenAscendente ? 1 : -1;
      return 0;
    });
    
    this.usuariosFiltrados = usuariosFiltrados;
    this.calcularTotalPaginas();
    this.aplicarPaginacion();
  }
  
  /**
   * Recibe el término de búsqueda desde el componente navbar-admin
   */
  aplicarFiltro(termino: string): void {
    this.termino = termino;
    this.filtrarUsuarios();
  }

  /**
   * Calcula el número total de páginas basado en el número de usuarios y el tamaño de página
   */
  calcularTotalPaginas(): void {
    this.totalPaginas = Math.ceil(this.usuariosFiltrados.length / this.itemsPorPagina);
    if (this.totalPaginas === 0) this.totalPaginas = 1;
  }

  /**
   * Aplica la paginación a la lista filtrada de usuarios
   */
  aplicarPaginacion(): void {
    const inicio = (this.paginaActual - 1) * this.itemsPorPagina;
    const fin = inicio + this.itemsPorPagina;
    this.usuariosPaginados = this.usuariosFiltrados.slice(inicio, fin);
  }
  
  /**
   * Obtiene los usuarios paginados para la página actual
   */
  getUsuariosPaginados(): Usuario[] {
    return this.usuariosPaginados;
  }
  
  /**
   * Cambia el criterio de ordenación de los usuarios
   */
  cambiarOrden(campo: string): void {
    if (this.ordenarPor === campo) {
      this.ordenAscendente = !this.ordenAscendente;
    } else {
      this.ordenarPor = campo;
      this.ordenAscendente = true;
    }
    this.filtrarUsuarios();
  }
  
  /**
   * Filtra los usuarios por rol
   */
  filtrarPorRol(): void {
    this.filtrarUsuarios();
  }

  /**
   * Cambia la página actual con transición suave
   * @param pagina Número de página a cambiar
   */
  cambiarPagina(pagina: number): void {
    if (pagina >= 1 && pagina <= this.totalPaginas) {
      this.paginaActual = pagina;
      this.aplicarPaginacion();
    }
  }

  /**
   * Cambia el número de items por página
   */
  cambiarItemsPorPagina(): void {
    this.paginaActual = 1;
    this.calcularTotalPaginas();
    this.cargarUsuarios();
  }

  /**
   * Abre el modal para crear un nuevo usuario
   */
  abrirModalCrearUsuario(): void {
    this.usuarioActual = new Usuario();
    this.modoEdicion = false;
    this.modalVisible = true;
  }

  /**
   * Abre el modal para editar un usuario existente
   */
  editarUsuario(usuario: Usuario): void {
    // Creamos una nueva instancia para evitar modificar el original por referencia
    this.usuarioActual = new Usuario({...usuario});
    this.modoEdicion = true;
    this.modalVisible = true;
  }

  /**
   * Cierra el modal y limpia el formulario
   */
  cerrarModal(): void {
    this.modalVisible = false;
    this.usuarioActual = new Usuario();
    if (this.usuarioForm) {
      this.usuarioForm.resetForm();
    }
  }

  /**
   * Guarda el usuario actual (creación o edición)
   */
  guardarUsuario(): void {
    if (this.modoEdicion) {
      this.usuarioService.updateUsuario(this.usuarioActual.id, this.usuarioActual.toBackend()).subscribe(
        response => {
          this.cerrarModal();
          this.cargarUsuarios();
          swal('¡Actualizado!', `El usuario ha sido actualizado con éxito`, 'success');
        },
        error => {
          console.error('Error al actualizar el usuario', error);
          swal('Error', 'Hubo un problema al actualizar el usuario', 'error');
        }
      );
    } else {
      // Para nuevo usuario, añadimos la fecha de registro
      // Usamos el setter que convertirá la fecha a formato ISO
      this.usuarioActual.fechaRegistro = new Date();
      
      this.usuarioService.createUsuario(this.usuarioActual.toBackend()).subscribe(
        response => {
          this.cerrarModal();
          this.cargarUsuarios();
          swal('¡Creado!', `El usuario ha sido creado con éxito`, 'success');
        },
        error => {
          console.error('Error al crear el usuario', error);
          swal('Error', 'Hubo un problema al crear el usuario', 'error');
        }
      );
    }
  }

  /**
   * Muestra un diálogo de confirmación para eliminar un usuario
   */
  confirmarEliminar(usuario: Usuario): void {
    this.tituloConfirmacion = 'Confirmar eliminación';
    this.mensajeConfirmacion = `¿Estás seguro de eliminar al usuario "${usuario.nombre} ${usuario.apellido}"?`;
    this.accionConfirmacion = () => {
      this.eliminarUsuario(usuario);
    };
    this.modalConfirmacionVisible = true;
  }

  /**
   * Muestra un diálogo de confirmación para eliminar todos los usuarios
   */
  confirmarEliminarTodos(): void {
    this.tituloConfirmacion = 'Confirmar eliminación masiva';
    this.mensajeConfirmacion = '¿Estás seguro de eliminar TODOS los usuarios? Esta acción no se puede deshacer.';
    this.accionConfirmacion = () => {
      this.eliminarTodos();
    };
    this.modalConfirmacionVisible = true;
  }

  /**
   * Ejecuta la acción de confirmación definida previamente
   */
  ejecutarAccionConfirmacion(): void {
    this.accionConfirmacion();
    this.modalConfirmacionVisible = false;
  }

  /**
   * Acepta la acción de confirmación y cierra el modal
   */
  aceptarConfirmacion(): void {
    this.ejecutarAccionConfirmacion();
  }

  /**
   * Cancela la acción de confirmación y cierra el modal
   */
  cancelarConfirmacion(): void {
    this.modalConfirmacionVisible = false;
    this.tituloConfirmacion = '';
    this.mensajeConfirmacion = '';
    this.accionConfirmacion = () => {};
  }

  /**
   * Elimina un usuario específico
   */
  eliminarUsuario(usuario: Usuario): void {
    this.usuarioService.deleteUsuario(usuario.id).subscribe(
      response => {
        this.cargarUsuarios();
        swal(
          '¡Eliminado!',
          `El usuario "${usuario.nombre}" ha sido eliminado con éxito`,
          'success'
        );
      },
      error => {
        console.error('Error al eliminar el usuario', error);
        swal(
          'Error',
          'Hubo un problema al eliminar el usuario',
          'error'
        );
      }
    );
  }

  /**
   * Elimina todos los usuarios
   */
  eliminarTodos(): void {
    this.usuarioService.deleteAllUsuarios().subscribe(
      response => {
        this.usuarios = [];
        this.usuariosFiltrados = [];
        swal(
          '¡Eliminados!',
          'Todos los usuarios han sido eliminados con éxito',
          'success'
        );
      },
      error => {
        console.error('Error al eliminar todos los usuarios', error);
        swal(
          'Error',
          'Hubo un problema al eliminar todos los usuarios',
          'error'
        );
      }
    );
  }

  /**
   * Navega a la página de detalles de un usuario
   */
  verDetallesUsuario(usuario: Usuario): void {
    this.router.navigate(['/perfil', usuario.id]);
  }

  /**
   * Obtiene el número de usuarios con un rol específico
   */
  getUsuariosPorRol(rol: string): number {
    return this.usuarios.filter(u => u.rol === rol).length;
  }

  /**
   * Obtiene el número de usuarios registrados en los últimos 30 días
   */
  getUsuariosRecientes(): number {
    const fechaLimite = new Date();
    fechaLimite.setDate(fechaLimite.getDate() - 7);
    
    return this.usuarios.filter(usuario => {
      if (!usuario.fechaRegistro) return false;
      const fechaRegistro = new Date(usuario.fechaRegistro);
      return fechaRegistro >= fechaLimite;
    }).length;
  }

  /**
   * Exporta los usuarios a un archivo CSV
   */
  exportarUsuarios(): void {
    const usuariosExport = this.usuarios.map(usuario => ({
      ID: usuario.id,
      Nombre: usuario.nombre,
      Apellido: usuario.apellido,
      Email: usuario.email,
      Rol: usuario.rol,
      Dirección: usuario.direccion || '',
      'Fecha Registro': new Date(usuario.fechaRegistro).toLocaleDateString('es-ES'),
      Ubicación: usuario.getNombreUbicacion()
    }));

    // Convertir a CSV
    const headers = Object.keys(usuariosExport[0]);
    const csvContent = [
      headers.join(','),
      ...usuariosExport.map(row => 
        headers.map(header => {
          const value = row[header as keyof typeof row] || '';
          // Escape commas and quotes in CSV values
          return typeof value === 'string' && (value.includes(',') || value.includes('"')) 
            ? `"${value.replace(/"/g, '""')}"` 
            : value;
        }).join(',')
      )
    ].join('\n');

    // Crear y descargar el archivo CSV
    const BOM = '\uFEFF'; // UTF-8 BOM para caracteres especiales
    const blob = new Blob([BOM + csvContent], { type: 'text/csv;charset=utf-8;' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `usuarios_export_${new Date().toISOString().split('T')[0]}.csv`;
    document.body.appendChild(a);
    a.click();
    
    // Limpiar recursos
    window.URL.revokeObjectURL(url);
    document.body.removeChild(a);
  }
}
