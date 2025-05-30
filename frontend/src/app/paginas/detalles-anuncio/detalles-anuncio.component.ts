import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FavoritoService } from '../favorito/favorito.service';
import { AnuncioService } from '../../entidades/anuncio/anuncio.service';
import { CarritoService, AnadirCarritoRequest } from '../carrito/carrito.service';
import { Anuncio } from '../../entidades/anuncio/anuncio';
import { Material } from '../../entidades/material/material';

@Component({
  selector: 'app-detalles-anuncio',
  standalone: false,
  templateUrl: './detalles-anuncio.component.html',
  styleUrl: './detalles-anuncio.component.css'
})
export class DetallesAnuncioComponent implements OnInit {
  // ID del anuncio
  anuncioId!: number;
  
  // Datos del anuncio desde el backend
  anuncio!: Anuncio;
  
  // Propiedades para el template
  proveedor: string = '';
  ubicacion: string = '';
  valoracion: number = 0;
  numValoraciones: number = 0;
  descripcionLarga: string = '';
  tiempoEntrega: string = '';
  materiales: string[] = [];
  colores: string[] = [];
  tamanoMaximo: string = '';
  serviciosAdicionales: any[] = [];
  metodoPago: string = '';
  politicasCancelacion: string = '';
  
  // Información de la impresora
  nombreImpresora: string = '';
  tecnologiaImpresora: string = '';
  precisionImpresora: string = '';
  
  // Valoraciones (simuladas por ahora - se puede implementar más adelante)
  valoraciones: any[] = [];
  
  // Estado de carga
  cargando: boolean = true;
  
  // Estado de favoritos
  esFavorito: boolean = false;
  cargandoFavorito: boolean = false;
  
  // Material seleccionado y cantidad
  materialSeleccionado: string = '';
  colorSeleccionado: string = '';
  cantidad: number = 1;
  
  // Servicios adicionales seleccionados
  serviciosAdicionalesSeleccionados: {[key: string]: boolean} = {
    acabadoPremium: false,
    urgente: false,
    envioGratis: true
  };
  
  // Precio calculado
  precioBase: number = 0;
  precioTotal: number = 0;

  // Estado del carrito
  agregandoAlCarrito: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private favoritoService: FavoritoService,
    private anuncioService: AnuncioService,
    private carritoService: CarritoService
  ) { }

  ngOnInit(): void {
    // Obtener el ID del anuncio de la URL
    this.route.params.subscribe(params => {
      this.anuncioId = +params['id']; // Convertir a número
      
      // Cargar los detalles del anuncio desde el backend
      this.cargarDetallesAnuncio();
    });
  }

  /**
   * Carga los detalles del anuncio desde el backend
   */
  cargarDetallesAnuncio(): void {
    this.cargando = true;
    
    this.anuncioService.getAnuncio(this.anuncioId).subscribe({
      next: (anuncio) => {
        this.anuncio = anuncio;
        
        // Configurar valores por defecto basados en los datos del backend
        this.precioBase = this.anuncio.precio;
        
        // Configurar datos basados en la información real del anuncio del backend
        this.configurarDatosDelBackend();
        
        // Calcular precio inicial
        this.calcularPrecio();
        
        // Verificar estado de favorito
        this.verificarEstadoFavorito();
        
        // Generar valoraciones simuladas (por ahora)
        this.generarValoracionesSimuladas();
        
        this.cargando = false;
      },
      error: (error) => {
        console.error('Error al cargar el anuncio:', error);
        this.cargando = false;
        // Redirigir a resultados de búsqueda si hay error
        this.router.navigate(['/resultado-busqueda']);
      }
    });
  }

  /**
   * Configura datos basados en la información real del anuncio del backend
   */
  configurarDatosDelBackend(): void {
    // Usar el nombre del usuario como proveedor si está disponible
    this.proveedor = this.anuncio.getNombreUsuario() || 'Proveedor de servicios 3D';
    
    // Ubicación simulada (esto podría venir del usuario en el futuro)
    this.ubicacion = 'Madrid';
    
    // Valoración del backend o simulada
    this.valoracion = this.anuncio.valoracionMedia > 0 ? Math.round(this.anuncio.valoracionMedia) : Math.floor(Math.random() * 3) + 3;
    this.numValoraciones = Math.floor(Math.random() * 120) + 30;
    
    // Descripción larga mejorada
    this.descripcionLarga = 'Ofrecemos soluciones de impresión 3D personalizadas para satisfacer sus necesidades específicas. ' +
                           'Nuestro equipo de expertos trabajará con usted para entender sus requisitos y proporcionar la mejor ' +
                           'solución posible. Utilizamos materiales de alta calidad y equipos de última generación para garantizar ' +
                           'la mejor calidad en cada impresión.';
    
    // Tiempo de entrega del backend
    this.tiempoEntrega = this.anuncio.tiempoEstimado || '3-5 días laborables';
    
    // Materiales del backend
    if (this.anuncio.materiales && this.anuncio.materiales.length > 0) {
      this.materiales = this.anuncio.getNombresMateriales();
      this.materialSeleccionado = this.materiales[0];
      
      // Colores disponibles de los materiales
      this.colores = this.anuncio.getColoresDisponibles();
      this.colorSeleccionado = this.colores.length > 0 ? this.colores[0] : '';
    } else {
      // Fallback a materiales simulados si no hay datos del backend
      this.materiales = ['PLA', 'PETG', 'ABS', 'TPU', 'Nylon'];
      this.materialSeleccionado = this.materiales[0];
      this.colores = ['Blanco', 'Negro', 'Gris', 'Rojo', 'Azul', 'Verde', 'Amarillo'];
      this.colorSeleccionado = this.colores[0];
    }
    
    // Información de la impresora del backend
    if (this.anuncio.impresora) {
      this.nombreImpresora = this.anuncio.getNombreImpresora();
      this.tecnologiaImpresora = this.anuncio.getTecnologiaImpresora();
      this.tamanoMaximo = this.anuncio.getVolumenMaximoImpresion();
      this.precisionImpresora = this.anuncio.getPrecisionImpresora();
    } else {
      // Fallback a valores simulados si no hay impresora
      this.nombreImpresora = 'Impresora 3D profesional';
      this.tecnologiaImpresora = 'FDM';
      this.tamanoMaximo = '250mm x 250mm x 300mm';
      this.precisionImpresora = '0.1mm';
    }
    
    // Servicios adicionales (estos pueden seguir siendo simulados o configurables)
    this.serviciosAdicionales = [
      { id: 'acabadoPremium', nombre: 'Acabado Premium', descripcion: 'Tratamiento de superficie para un acabado profesional', precio: 10 },
      { id: 'urgente', nombre: 'Entrega Urgente (24h)', descripcion: 'Impresión y entrega en 24 horas', precio: 15 },
      { id: 'envioGratis', nombre: 'Envío gratuito', descripcion: 'Entrega en una dirección de su elección gratuitamente', precio: 0 }
    ];
    
    // Métodos de pago
    this.metodoPago = 'Tarjeta de crédito, PayPal, transferencia bancaria';
    
    // Políticas de cancelación
    this.politicasCancelacion = 'Cancelación gratuita hasta 24 horas después de realizar el pedido. ' +
                              'Para cancelaciones posteriores se retendrá el 20% del valor del pedido.';
  }

  /**
   * Genera valoraciones simuladas para el anuncio
   */
  generarValoracionesSimuladas(): void {
    // Nombres simulados para las valoraciones
    const nombres = ['María L.', 'Carlos S.', 'Laura G.', 'Juan P.', 'Ana M.', 'Roberto T.', 'Elena R.', 'Luis V.', 'Clara N.', 'Diego F.'];
    // Comentarios simulados
    const comentarios = [
      'Excelente servicio, muy profesional y rápido.',
      'La calidad de impresión es impresionante, superó mis expectativas.',
      'Muy buena atención al cliente y el resultado final es perfecto.',
      'Buen servicio aunque tardó un poco más de lo esperado.',
      'Excelente relación calidad-precio, recomendado al 100%.',
      'El acabado es muy profesional, definitivamente volveré a contratar.',
      'La pieza llegó con un pequeño defecto pero lo solucionaron rápidamente.',
      'Comunicación clara y resultado exactamente como lo necesitaba.',
      'Gran profesionalidad y atención personalizada.',
      'Cumplieron con los plazos y la calidad es muy buena.'
    ];
    
    // Generar entre 5 y 10 valoraciones aleatorias
    const numValoraciones = Math.floor(Math.random() * 6) + 5;
    
    this.valoraciones = [];
    for (let i = 0; i < numValoraciones; i++) {
      const estrellas = Math.floor(Math.random() * 3) + 3; // 3, 4 o 5 estrellas
      const nombreIndex = Math.floor(Math.random() * nombres.length);
      const comentarioIndex = Math.floor(Math.random() * comentarios.length);
      
      // Fecha aleatoria de los últimos 60 días
      const fecha = new Date();
      fecha.setDate(fecha.getDate() - Math.floor(Math.random() * 60));
      
      this.valoraciones.push({
        id: i + 1,
        nombre: nombres[nombreIndex],
        estrellas: estrellas,
        comentario: comentarios[comentarioIndex],
        fecha: fecha,
        respuesta: Math.random() > 0.7 ? {
          texto: 'Gracias por tu valoración. Nos alegra que hayas quedado satisfecho con nuestro servicio.',
          fecha: new Date()
        } : null
      });
    }
    
    // Ordenar valoraciones por fecha (más recientes primero)
    this.valoraciones.sort((a, b) => b.fecha.getTime() - a.fecha.getTime());
  }
  
  /**
   * Calcula el precio total en base a las selecciones
   */
  calcularPrecio(): void {
    // Precio base
    let precio = this.precioBase;
    
    // Multiplicar por cantidad
    precio = precio * this.cantidad;
    
    // Añadir servicios adicionales
    for (const servicio of this.serviciosAdicionales) {
      if (this.serviciosAdicionalesSeleccionados[servicio.id]) {
        precio += servicio.precio;
      }
    }
    
    // Actualizar precio total
    this.precioTotal = precio;
  }
  
  /**
   * Actualiza la cantidad de unidades
   */
  actualizarCantidad(cantidad: number): void {
    this.cantidad = cantidad;
    this.calcularPrecio();
  }
  
  /**
   * Actualiza el material seleccionado
   */
  actualizarMaterial(material: string): void {
    this.materialSeleccionado = material;
    // Podríamos ajustar el precio según el material
    this.calcularPrecio();
  }
  
  /**
   * Actualiza el color seleccionado
   */
  actualizarColor(color: string): void {
    this.colorSeleccionado = color;
    // Recalcular precio en caso de que el color afecte al precio en el futuro
    this.calcularPrecio();
  }
  
  /**
   * Actualiza los servicios adicionales seleccionados
   */
  toggleServicioAdicional(servicioId: string): void {
    this.serviciosAdicionalesSeleccionados[servicioId] = !this.serviciosAdicionalesSeleccionados[servicioId];
    this.calcularPrecio();
  }
  
  /**
   * Añade el elemento al carrito
   */
  anadirAlCarrito(): void {
    if (!this.materialSeleccionado) {
      alert('Por favor, selecciona un material antes de añadir al carrito');
      return;
    }

    if (this.colores.length > 0 && !this.colorSeleccionado) {
      alert('Por favor, selecciona un color antes de añadir al carrito');
      return;
    }

    if (this.cantidad <= 0) {
      alert('La cantidad debe ser mayor a 0');
      return;
    }

    this.agregandoAlCarrito = true;

    const request: AnadirCarritoRequest = {
      usuarioId: 1, // Usuario simulado - en el futuro vendrá del servicio de autenticación
      anuncioId: this.anuncioId,
      cantidad: this.cantidad,
      materialSeleccionado: this.materialSeleccionado,
      colorSeleccionado: this.colorSeleccionado || undefined,
      acabadoPremium: this.serviciosAdicionalesSeleccionados['acabadoPremium'],
      urgente: this.serviciosAdicionalesSeleccionados['urgente'],
      envioGratis: this.serviciosAdicionalesSeleccionados['envioGratis']
    };

    this.carritoService.anadirAlCarrito(request).subscribe({
      next: (response) => {
        console.log('Elemento añadido al carrito exitosamente:', response);
        this.agregandoAlCarrito = false;
        
        // Mostrar mensaje de éxito
        this.mostrarMensajeExito('Elemento añadido al carrito exitosamente');
        
        // Opcional: Redirigir al carrito
        // this.router.navigate(['/carrito']);
      },
      error: (error) => {
        console.error('Error al añadir al carrito:', error);
        this.agregandoAlCarrito = false;
      }
    });
  }

  /**
   * Muestra un mensaje de éxito temporal
   */
  private mostrarMensajeExito(mensaje: string): void {
    // Crear elemento de mensaje
    const messageElement = document.createElement('div');
    messageElement.innerHTML = `
      <div style="
        position: fixed;
        top: 20px;
        right: 20px;
        background-color: #4caf50;
        color: white;
        padding: 1rem 1.5rem;
        border-radius: 8px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.3);
        z-index: 10000;
        font-family: Inter, sans-serif;
        font-weight: 500;
      ">
        <i class="fas fa-check-circle" style="margin-right: 0.5rem;"></i>
        ${mensaje}
      </div>
    `;
    
    document.body.appendChild(messageElement);
    
    // Eliminar después de 3 segundos
    setTimeout(() => {
      if (messageElement.parentNode) {
        messageElement.parentNode.removeChild(messageElement);
      }
    }, 3000);
  }
  
  /**
   * Vuelve a la página anterior
   */
  volver(): void {
    this.router.navigate(['/resultados-busqueda']);
  }
  
  /**
   * Contacta con el proveedor
   */
  contactarProveedor(): void {
    alert('Función de contacto con el proveedor');
    // Aquí iría la lógica para contactar con el proveedor
  }
  
  /**
   * Redirige a la página de inicio de sesión
   */
  iniciarSesion(): void {
    // Redirigir a la página de inicio de sesión
    this.router.navigate(['/login']);
  }
  
  /**
   * Navega de vuelta a la página de resultados de búsqueda con transición visual
   * @param event Evento del clic
   */
  volverAResultados(event: Event): void {
    event.preventDefault();
    
    // Verificar si el navegador soporta View Transitions API
    if (document.startViewTransition) {
      // Aplicar transición visual
      document.startViewTransition(() => {
        // Preparar elementos para la transición
        const titulo = document.querySelector(`[data-view-id="anuncio-title-${this.anuncioId}"]`);
        const imagen = document.querySelector(`[data-view-id="anuncio-img-${this.anuncioId}"]`);
        
        // Asegurar que los elementos mantengan su estilo durante la transición
        if (titulo) titulo.setAttribute('style', 'view-transition-name: header;');
        if (imagen) imagen.setAttribute('style', 'view-transition-name: image;');
        
        // Redirigir a la página de resultados de búsqueda
        this.router.navigate(["/resultados-busqueda"]);
        
        return new Promise<void>(resolve => {
          setTimeout(() => {
            // Limpiar los estilos después de la transición
            if (titulo) titulo.removeAttribute('style');
            if (imagen) imagen.removeAttribute('style');
            resolve();
          }, 300); // Duración aproximada de la transición
        });
      });
    } else {
      // Fallback para navegadores que no soportan View Transitions API
      this.router.navigate(["/resultados-busqueda"]);
    }
  }

  /**
   * Verifica si el anuncio está marcado como favorito por el usuario actual
   */
  verificarEstadoFavorito(): void {
    // Simulamos que tenemos un usuario logueado con ID 1
    const usuarioId = 1;
    
    this.favoritoService.checkFavorito(this.anuncioId, usuarioId).subscribe({
      next: (esFavorito) => {
        this.esFavorito = esFavorito;
      },
      error: (error) => {
        console.error('Error verificando estado de favorito:', error);
        // En caso de error, asumimos que no es favorito
        this.esFavorito = false;
      }
    });
  }

  /**
   * Alterna el estado de favorito del anuncio
   */
  toggleFavorito(): void {
    // Simulamos que tenemos un usuario logueado con ID 1
    const usuarioId = 1;
    
    this.cargandoFavorito = true;
    
    this.favoritoService.toggleFavorito(this.anuncioId, usuarioId).subscribe({
      next: (response) => {
        this.esFavorito = !this.esFavorito;
        this.cargandoFavorito = false;
        
        // Mostrar mensaje de confirmación
        if (this.esFavorito) {
          console.log('Anuncio añadido a favoritos');
        } else {
          console.log('Anuncio eliminado de favoritos');
        }
      },
      error: (error) => {
        console.error('Error al cambiar estado de favorito:', error);
        this.cargandoFavorito = false;
      }
    });
  }
}
