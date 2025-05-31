package com.proyecto3d.backend.apirest.model.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * DTO para la respuesta del checkout
 * Evita problemas de serialización con proxies de Hibernate
 */
public class CheckoutResponseDto {

    private Long id;
    private String numero_pedido;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date fecha_pedido;
    
    private String estado;
    private Double total;
    private Double subtotal;
    private String metodo_pago;
    private String referencia_pago;
    private String direccion_envio;
    private String codigo_postal;
    private String ciudad;
    private String provincia;
    private String notas_cliente;
    
    // Información del usuario simplificada
    private UsuarioSimpleDto usuario;
    
    // Detalles del pedido simplificados
    private List<DetallePedidoSimpleDto> detallesPedido;

    // Constructores
    public CheckoutResponseDto() {}

    public CheckoutResponseDto(Long id, String numero_pedido, Date fecha_pedido, String estado, 
                               Double total, Double subtotal, String metodo_pago, String referencia_pago,
                               String direccion_envio, String codigo_postal, String ciudad, String provincia,
                               String notas_cliente, UsuarioSimpleDto usuario, List<DetallePedidoSimpleDto> detallesPedido) {
        this.id = id;
        this.numero_pedido = numero_pedido;
        this.fecha_pedido = fecha_pedido;
        this.estado = estado;
        this.total = total;
        this.subtotal = subtotal;
        this.metodo_pago = metodo_pago;
        this.referencia_pago = referencia_pago;
        this.direccion_envio = direccion_envio;
        this.codigo_postal = codigo_postal;
        this.ciudad = ciudad;
        this.provincia = provincia;
        this.notas_cliente = notas_cliente;
        this.usuario = usuario;
        this.detallesPedido = detallesPedido;
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumero_pedido() { return numero_pedido; }
    public void setNumero_pedido(String numero_pedido) { this.numero_pedido = numero_pedido; }

    public Date getFecha_pedido() { return fecha_pedido; }
    public void setFecha_pedido(Date fecha_pedido) { this.fecha_pedido = fecha_pedido; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }

    public String getMetodo_pago() { return metodo_pago; }
    public void setMetodo_pago(String metodo_pago) { this.metodo_pago = metodo_pago; }

    public String getReferencia_pago() { return referencia_pago; }
    public void setReferencia_pago(String referencia_pago) { this.referencia_pago = referencia_pago; }

    public String getDireccion_envio() { return direccion_envio; }
    public void setDireccion_envio(String direccion_envio) { this.direccion_envio = direccion_envio; }

    public String getCodigo_postal() { return codigo_postal; }
    public void setCodigo_postal(String codigo_postal) { this.codigo_postal = codigo_postal; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }

    public String getNotas_cliente() { return notas_cliente; }
    public void setNotas_cliente(String notas_cliente) { this.notas_cliente = notas_cliente; }

    public UsuarioSimpleDto getUsuario() { return usuario; }
    public void setUsuario(UsuarioSimpleDto usuario) { this.usuario = usuario; }

    public List<DetallePedidoSimpleDto> getDetallesPedido() { return detallesPedido; }
    public void setDetallesPedido(List<DetallePedidoSimpleDto> detallesPedido) { this.detallesPedido = detallesPedido; }
    
    /**
     * DTO simplificado para información de usuario
     */
    public static class UsuarioSimpleDto {
        private Long id;
        private String nombre;
        private String email;

        public UsuarioSimpleDto() {}

        public UsuarioSimpleDto(Long id, String nombre, String email) {
            this.id = id;
            this.nombre = nombre;
            this.email = email;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
    
    /**
     * DTO simplificado para detalles de pedido
     */
    public static class DetallePedidoSimpleDto {
        private Long id;
        private Integer cantidad;
        private Double precio_unitario;
        private Double subtotal;
        private String especificaciones;
        private String tituloAnuncio;
        private String nombreProveedor;

        public DetallePedidoSimpleDto() {}

        public DetallePedidoSimpleDto(Long id, Integer cantidad, Double precio_unitario, Double subtotal,
                                      String especificaciones, String tituloAnuncio, String nombreProveedor) {
            this.id = id;
            this.cantidad = cantidad;
            this.precio_unitario = precio_unitario;
            this.subtotal = subtotal;
            this.especificaciones = especificaciones;
            this.tituloAnuncio = tituloAnuncio;
            this.nombreProveedor = nombreProveedor;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

        public Double getPrecio_unitario() { return precio_unitario; }
        public void setPrecio_unitario(Double precio_unitario) { this.precio_unitario = precio_unitario; }

        public Double getSubtotal() { return subtotal; }
        public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }

        public String getEspecificaciones() { return especificaciones; }
        public void setEspecificaciones(String especificaciones) { this.especificaciones = especificaciones; }

        public String getTituloAnuncio() { return tituloAnuncio; }
        public void setTituloAnuncio(String tituloAnuncio) { this.tituloAnuncio = tituloAnuncio; }

        public String getNombreProveedor() { return nombreProveedor; }
        public void setNombreProveedor(String nombreProveedor) { this.nombreProveedor = nombreProveedor; }
    }
} 