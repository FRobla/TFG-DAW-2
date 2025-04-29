import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-detalles-anuncio',
  standalone: false,
  templateUrl: './detalles-anuncio.component.html',
  styleUrl: './detalles-anuncio.component.css'
})
export class DetallesAnuncioComponent implements OnInit {
  // ID del anuncio
  anuncioId!: number;
  
  // Datos del anuncio (simulados)
  anuncio: any = {};
  
  // Valoraciones (simuladas)
  valoraciones: any[] = [];
  
  // Estado de carga
  cargando: boolean = true;
  
  // Material seleccionado y cantidad
  materialSeleccionado: string = '';
  cantidad: number = 1;
  
  // Servicios adicionales seleccionados
  serviciosAdicionales: {[key: string]: boolean} = {
    acabadoPremium: false,
    urgente: false,
    envioGratis: false
  };
  
  // Precio calculado
  precioBase: number = 0;
  precioTotal: number = 0;

  constructor(
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    // Obtener el ID del anuncio de la URL
    this.route.params.subscribe(params => {
      this.anuncioId = +params['id']; // Convertir a número
      
      // Cargar los detalles del anuncio
      this.cargarDetallesAnuncio();
    });
  }

  /**
   * Carga los detalles del anuncio desde el backend
   * Por ahora simulamos la carga con datos estáticos
   */
  cargarDetallesAnuncio(): void {
    // Simulamos una carga desde el backend
    setTimeout(() => {
      // Datos simulados según el ID
      this.cargarDatosSimulados();
      
      // Calcular precio inicial
      this.calcularPrecio();
      
      // Marcar como cargado
      this.cargando = false;
    }, 800); // Simulamos un tiempo de carga
  }

  /**
   * Carga datos simulados según el ID del anuncio
   */
  cargarDatosSimulados(): void {
    // Proveedores simulados
    const proveedores = [
      'TechPrint Madrid',
      '3DExpress Madrid',
      'EcoMakers Madrid',
      'ColorPrint Madrid',
      'ProMakers Madrid',
      'TechSolutions Madrid',
      'PrintStarters Madrid',
      'IndustrialPrint Madrid',
      'NylonTech Madrid',
    ];
    
    // Títulos simulados
    const titulos = [
      'Impresión PLA de alta resolución',
      'Impresión FDM con acabado premium',
      'Impresión PLA biodegradable',
      'Impresión FDM multicolor',
      'Impresión FDM industrial',
      'Impresión FDM con PETG',
      'Pack impresión FDM básica',
      'Impresión FDM con ABS',
      'Impresión FDM con Nylon',
    ];
    
    // Precios base simulados
    const precios = [15, 20, 18, 25, 40, 22, 12, 24, 28];
    
    // Valoraciones simuladas (entre 3 y 5 estrellas, entre 30 y 150 valoraciones)
    const estrellas = Math.floor(Math.random() * 2) + 3; // 3, 4 o 5 estrellas
    const numValoraciones = Math.floor(Math.random() * 120) + 30;
    
    // Configuramos el anuncio con datos simulados
    this.anuncio = {
      id: this.anuncioId,
      titulo: titulos[this.anuncioId - 1] || 'Servicio de impresión 3D',
      proveedor: proveedores[this.anuncioId - 1] || 'Proveedor de servicios 3D',
      ubicacion: 'Madrid',
      valoracion: estrellas,
      numValoraciones: numValoraciones,
      descripcion: 'Servicio profesional de impresión 3D con la más alta calidad. ' +
                  'Utilizamos tecnología de última generación para garantizar resultados ' +
                  'precisos y acabados impecables. Ideal para prototipos, ' +
                  'maquetas, piezas funcionales y proyectos creativos.',
      descripcionLarga: 'Ofrecemos soluciones de impresión 3D personalizadas para satisfacer sus necesidades específicas. ' +
                        'Nuestro equipo de expertos trabajará con usted para entender sus requisitos y proporcionar la mejor ' +
                        'solución posible. Utilizamos materiales de alta calidad y equipos de última generación para garantizar ' +
                        'la mejor calidad en cada impresión.\n\n' +
                        'Nuestro proceso incluye una revisión detallada de su modelo 3D para asegurar su imprimibilidad, ' +
                        'optimización para reducir costos sin comprometer la calidad, y un control de calidad riguroso ' +
                        'después de la impresión. Todo esto para garantizar su completa satisfacción con el producto final.',
      tiempoEntrega: '3-5 días laborables',
      materiales: ['PLA', 'PETG', 'ABS', 'TPU', 'Nylon'],
      colores: ['Blanco', 'Negro', 'Gris', 'Rojo', 'Azul', 'Verde', 'Amarillo'],
      tamanoMaximo: '250mm x 250mm x 300mm',
      precioBase: precios[this.anuncioId - 1] || 20,
      serviciosAdicionales: [
        { id: 'acabadoPremium', nombre: 'Acabado Premium', descripcion: 'Tratamiento de superficie para un acabado profesional', precio: 10 },
        { id: 'urgente', nombre: 'Entrega Urgente (24h)', descripcion: 'Impresión y entrega en 24 horas', precio: 15 },
        { id: 'envioGratis', nombre: 'Envío Gratuito', descripcion: 'Envío gratuito a cualquier punto de España', precio: 5 }
      ],
      imagenes: [
        'https://ejemplo.com/imagen1.jpg',
        'https://ejemplo.com/imagen2.jpg',
        'https://ejemplo.com/imagen3.jpg'
      ],
      politicasCancelacion: 'Cancelación gratuita hasta 24 horas después de realizar el pedido. ' +
                           'Para cancelaciones posteriores se retendrá el 20% del valor del pedido.',
      metodoPago: 'Tarjeta de crédito, PayPal, transferencia bancaria',
      informacionAdicional: 'Para pedidos especiales o consultas, contáctenos directamente.'
    };
    
    // Precios base y material seleccionado por defecto
    this.precioBase = this.anuncio.precioBase;
    this.materialSeleccionado = this.anuncio.materiales[0];
    
    // Generar valoraciones simuladas
    this.generarValoracionesSimuladas();
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
    for (const servicio of this.anuncio.serviciosAdicionales) {
      if (this.serviciosAdicionales[servicio.id]) {
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
   * Actualiza los servicios adicionales seleccionados
   */
  toggleServicioAdicional(servicioId: string): void {
    this.serviciosAdicionales[servicioId] = !this.serviciosAdicionales[servicioId];
    this.calcularPrecio();
  }
  
  /**
   * Añade el servicio al carrito
   */
  anadirAlCarrito(): void {
    alert('Servicio añadido al carrito');
    // Aquí iría la lógica para añadir al carrito
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
}
