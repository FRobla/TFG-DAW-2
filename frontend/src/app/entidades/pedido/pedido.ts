import { Usuario } from '../usuario/usuario';

// Clase para los detalles del pedido
export class DetallePedido {
    id: number = 0;
    cantidad: number = 1;
    precio_unitario: number = 0;
    subtotal: number = 0;
    especificaciones: string = "";
    notas: string = "";
    estado_item: string = "pendiente";
    anuncio?: any; // Se puede definir un modelo Anuncio si es necesario
    material?: any; // Se puede definir un modelo Material si es necesario
    
    constructor(data?: Partial<DetallePedido>) {
        if (data) {
            Object.assign(this, data);
        }
    }
    
    static fromBackend(data: any): DetallePedido {
        return new DetallePedido({
            id: data.id,
            cantidad: data.cantidad,
            precio_unitario: data.precio_unitario,
            subtotal: data.subtotal,
            especificaciones: data.especificaciones,
            notas: data.notas,
            estado_item: data.estado_item,
            anuncio: data.anuncio,
            material: data.material
        });
    }
    
    /**
     * Calcula el subtotal basado en cantidad y precio unitario
     */
    calcularSubtotal(): void {
        this.subtotal = this.cantidad * this.precio_unitario;
    }
    
    /**
     * Obtiene el título del anuncio asociado
     */
    getTituloAnuncio(): string {
        return this.anuncio ? this.anuncio.titulo : "";
    }
    
    /**
     * Obtiene el nombre del material seleccionado
     */
    getNombreMaterial(): string {
        return this.material ? this.material.nombre : "No especificado";
    }
}

export class Pedido {
    id: number = 0;
    numero_pedido: string = "";
    fecha_pedido: string = "";
    estado: string = "pendiente"; // (pendiente, en_proceso, completado, cancelado)
    total: number = 0;
    descuento: number = 0;
    impuestos: number = 0;
    subtotal: number = 0;
    metodo_pago: string = ""; // (paypal, tarjeta, transferencia, efectivo)
    referencia_pago: string = "";
    direccion_envio: string = "";
    codigo_postal: string = "";
    ciudad: string = "";
    provincia: string = "";
    notas_cliente: string = "";
    notas_internas: string = "";
    fecha_entrega_estimada: string = "";
    fecha_entrega_real: string = "";
    usuario?: Usuario;
    detallesPedido: DetallePedido[] = [];
    
    // Propiedad para acceder a la fecha como objeto Date
    get fechaPedido(): Date {
        return this.fecha_pedido ? new Date(this.fecha_pedido) : new Date();
    }
    
    set fechaPedido(fecha: Date) {
        if (fecha) {
            this.fecha_pedido = fecha.toISOString();
        }
    }
    
    get fechaEntregaEstimada(): Date | null {
        return this.fecha_entrega_estimada ? new Date(this.fecha_entrega_estimada) : null;
    }
    
    set fechaEntregaEstimada(fecha: Date | null) {
        if (fecha) {
            this.fecha_entrega_estimada = fecha.toISOString();
        } else {
            this.fecha_entrega_estimada = "";
        }
    }
    
    get fechaEntregaReal(): Date | null {
        return this.fecha_entrega_real ? new Date(this.fecha_entrega_real) : null;
    }
    
    set fechaEntregaReal(fecha: Date | null) {
        if (fecha) {
            this.fecha_entrega_real = fecha.toISOString();
        } else {
            this.fecha_entrega_real = "";
        }
    }
    
    // Constructor para facilitar la transformación
    constructor(data?: Partial<Pedido>) {
        if (data) {
            Object.assign(this, data);
        }
    }
    
    // Método estático para convertir datos del backend
    static fromBackend(data: any): Pedido {
        const pedido = new Pedido({
            id: data.id,
            numero_pedido: data.numero_pedido,
            estado: data.estado,
            total: data.total,
            descuento: data.descuento,
            impuestos: data.impuestos,
            subtotal: data.subtotal,
            metodo_pago: data.metodo_pago,
            referencia_pago: data.referencia_pago,
            direccion_envio: data.direccion_envio,
            codigo_postal: data.codigo_postal,
            ciudad: data.ciudad,
            provincia: data.provincia,
            notas_cliente: data.notas_cliente,
            notas_internas: data.notas_internas,
            usuario: data.usuario
        });
        
        // Manejar las fechas específicamente
        if (data.fecha_pedido) {
            pedido.fecha_pedido = data.fecha_pedido;
        }
        if (data.fecha_entrega_estimada) {
            pedido.fecha_entrega_estimada = data.fecha_entrega_estimada;
        }
        if (data.fecha_entrega_real) {
            pedido.fecha_entrega_real = data.fecha_entrega_real;
        }
        
        // Manejar detalles del pedido
        if (data.detallesPedido) {
            pedido.detallesPedido = data.detallesPedido.map((detalle: any) => DetallePedido.fromBackend(detalle));
        }
        
        return pedido;
    }
    
    // Método para convertir al formato esperado por el backend
    toBackend(): any {
        const backendData: any = {
            id: this.id,
            numero_pedido: this.numero_pedido,
            fecha_pedido: this.fecha_pedido,
            estado: this.estado,
            total: this.total,
            descuento: this.descuento,
            impuestos: this.impuestos,
            subtotal: this.subtotal,
            metodo_pago: this.metodo_pago,
            referencia_pago: this.referencia_pago,
            direccion_envio: this.direccion_envio,
            codigo_postal: this.codigo_postal,
            ciudad: this.ciudad,
            provincia: this.provincia,
            notas_cliente: this.notas_cliente,
            notas_internas: this.notas_internas,
            fecha_entrega_estimada: this.fecha_entrega_estimada,
            fecha_entrega_real: this.fecha_entrega_real
        };

        // Manejar el usuario - enviar solo el ID si existe
        if (this.usuario && this.usuario.id) {
            backendData.usuario = {
                id: this.usuario.id
            };
        } else {
            backendData.usuario = null;
        }

        return backendData;
    }

    // Métodos de utilidad
    
    /**
     * Obtiene el número total de artículos en el pedido
     */
    getTotalArticulos(): number {
        return this.detallesPedido ? 
                this.detallesPedido.reduce((total, detalle) => total + detalle.cantidad, 0) : 0;
    }

    /**
     * Verifica si el pedido puede ser cancelado
     */
    puedeSerCancelado(): boolean {
        return this.estado === 'pendiente' || this.estado === 'en_proceso';
    }

    /**
     * Verifica si el pedido está completado
     */
    estaCompletado(): boolean {
        return this.estado === 'completado';
    }

    /**
     * Obtiene el nombre completo del cliente
     */
    getNombreCliente(): string {
        return this.usuario ? `${this.usuario.nombre} ${this.usuario.apellido}` : "";
    }
    
    /**
     * Obtiene el texto del estado en español
     */
    getEstadoTexto(): string {
        const estados: { [key: string]: string } = {
            'pendiente': 'Pendiente de Pago',
            'en_proceso': 'En Proceso',
            'completado': 'Completado',
            'cancelado': 'Cancelado'
        };
        return estados[this.estado] || this.estado;
    }
    
    /**
     * Obtiene la clase CSS para el estado
     */
    getEstadoClase(): string {
        const clases: { [key: string]: string } = {
            'pendiente': 'estado-pendiente',
            'en_proceso': 'estado-proceso',
            'completado': 'estado-completado',
            'cancelado': 'estado-cancelado'
        };
        return clases[this.estado] || 'estado-default';
    }
    
    /**
     * Verifica si el pedido está pagado (en proceso o completado)
     */
    estaPagado(): boolean {
        return this.estado === 'en_proceso' || this.estado === 'completado';
    }

    /**
     * Verifica si el pedido está pendiente de pago
     */
    estaPendientePago(): boolean {
        return this.estado === 'pendiente';
    }
    
    /**
     * Obtiene el texto del método de pago en español
     */
    getMetodoPagoTexto(): string {
        const metodos: { [key: string]: string } = {
            'paypal': 'PayPal',
            'tarjeta': 'Tarjeta de Crédito',
            'transferencia': 'Transferencia Bancaria',
            'efectivo': 'Efectivo'
        };
        return metodos[this.metodo_pago] || this.metodo_pago;
    }
}
