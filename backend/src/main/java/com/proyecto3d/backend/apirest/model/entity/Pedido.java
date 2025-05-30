package com.proyecto3d.backend.apirest.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa un pedido en el sistema
 * Contiene información sobre el pedido realizado por un usuario
 */
@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String numero_pedido;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date fecha_pedido;

    @Column(nullable = false)
    private String estado; // (pendiente, en_proceso, completado, cancelado, enviado)

    @Column(nullable = false)
    private Double total;

    @Column
    private Double descuento;

    @Column
    private Double impuestos;

    @Column(nullable = false)
    private Double subtotal;

    @Column(length = 50)
    private String metodo_pago; // (paypal, tarjeta, transferencia, efectivo)

    @Column(length = 100)
    private String referencia_pago; // ID de transacción de PayPal u otro método

    @Column(nullable = false)
    private String direccion_envio;

    @Column(length = 20)
    private String codigo_postal;

    @Column(length = 100)
    private String ciudad;

    @Column(length = 100)
    private String provincia;

    @Column(columnDefinition = "TEXT")
    private String notas_cliente;

    @Column(columnDefinition = "TEXT")
    private String notas_internas;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date fecha_entrega_estimada;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date fecha_entrega_real;

    // Relaciones

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetallePedido> detallesPedido = new ArrayList<>();

    // Métodos de utilidad

    @PrePersist
    protected void onCreate() {
        if (fecha_pedido == null) {
            fecha_pedido = new Date();
        }
        if (numero_pedido == null) {
            numero_pedido = generarNumeroPedido();
        }
        if (estado == null) {
            estado = "pendiente";
        }
    }

    /**
     * Genera un número de pedido único basado en timestamp
     */
    private String generarNumeroPedido() {
        return "PED" + System.currentTimeMillis();
    }

    /**
     * Calcula el total del pedido basado en los detalles
     */
    public void calcularTotal() {
        if (detallesPedido != null && !detallesPedido.isEmpty()) {
            this.subtotal = detallesPedido.stream()
                    .mapToDouble(DetallePedido::getSubtotal)
                    .sum();
            
            // Aplicar descuento si existe
            double totalConDescuento = this.subtotal;
            if (descuento != null && descuento > 0) {
                totalConDescuento = this.subtotal - descuento;
            }
            
            // Aplicar impuestos si existen
            if (impuestos != null && impuestos > 0) {
                totalConDescuento += impuestos;
            }
            
            this.total = totalConDescuento;
        } else {
            this.subtotal = 0.0;
            this.total = 0.0;
        }
    }

    /**
     * Obtiene el número total de artículos en el pedido
     */
    public Integer getTotalArticulos() {
        return detallesPedido != null ? 
                detallesPedido.stream()
                        .mapToInt(DetallePedido::getCantidad)
                        .sum() : 0;
    }

    /**
     * Verifica si el pedido puede ser cancelado
     */
    public boolean puedeSerCancelado() {
        return "pendiente".equals(estado) || "en_proceso".equals(estado);
    }

    /**
     * Verifica si el pedido está completado
     */
    public boolean estaCompletado() {
        return "completado".equals(estado);
    }

    /**
     * Obtiene el nombre completo del cliente
     */
    public String getNombreCliente() {
        return usuario != null ? usuario.getNombreCompleto() : "";
    }

    // Getters y Setters específicos

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero_pedido() {
        return numero_pedido;
    }

    public void setNumero_pedido(String numero_pedido) {
        this.numero_pedido = numero_pedido;
    }

    public Date getFecha_pedido() {
        return fecha_pedido;
    }

    public void setFecha_pedido(Date fecha_pedido) {
        this.fecha_pedido = fecha_pedido;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getDescuento() {
        return descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    public Double getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(Double impuestos) {
        this.impuestos = impuestos;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public String getMetodo_pago() {
        return metodo_pago;
    }

    public void setMetodo_pago(String metodo_pago) {
        this.metodo_pago = metodo_pago;
    }

    public String getReferencia_pago() {
        return referencia_pago;
    }

    public void setReferencia_pago(String referencia_pago) {
        this.referencia_pago = referencia_pago;
    }

    public String getDireccion_envio() {
        return direccion_envio;
    }

    public void setDireccion_envio(String direccion_envio) {
        this.direccion_envio = direccion_envio;
    }

    public String getCodigo_postal() {
        return codigo_postal;
    }

    public void setCodigo_postal(String codigo_postal) {
        this.codigo_postal = codigo_postal;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getNotas_cliente() {
        return notas_cliente;
    }

    public void setNotas_cliente(String notas_cliente) {
        this.notas_cliente = notas_cliente;
    }

    public String getNotas_internas() {
        return notas_internas;
    }

    public void setNotas_internas(String notas_internas) {
        this.notas_internas = notas_internas;
    }

    public Date getFecha_entrega_estimada() {
        return fecha_entrega_estimada;
    }

    public void setFecha_entrega_estimada(Date fecha_entrega_estimada) {
        this.fecha_entrega_estimada = fecha_entrega_estimada;
    }

    public Date getFecha_entrega_real() {
        return fecha_entrega_real;
    }

    public void setFecha_entrega_real(Date fecha_entrega_real) {
        this.fecha_entrega_real = fecha_entrega_real;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<DetallePedido> getDetallesPedido() {
        return detallesPedido;
    }

    public void setDetallesPedido(List<DetallePedido> detallesPedido) {
        this.detallesPedido = detallesPedido;
    }
} 