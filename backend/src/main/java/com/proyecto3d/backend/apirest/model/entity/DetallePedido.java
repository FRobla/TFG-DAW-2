package com.proyecto3d.backend.apirest.model.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa los detalles de un pedido
 * Cada registro representa un servicio/anuncio específico dentro de un pedido
 */
@Entity
@Table(name = "detalles_pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetallePedido implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Double precio_unitario;

    @Column(nullable = false)
    private Double subtotal;

    @Column(columnDefinition = "TEXT")
    private String especificaciones; // Detalles específicos del servicio (material, color, etc.)

    @Column(columnDefinition = "TEXT")
    private String notas; // Notas adicionales para este ítem específico

    @Column(length = 50)
    private String estado_item; // (pendiente, en_proceso, completado, cancelado)

    // Relaciones

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "anuncio_id", nullable = false)
    private Anuncio anuncio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "material_id")
    private Material material; // Material específico seleccionado para este ítem

    // Métodos de utilidad

    /**
     * Calcula el subtotal basado en cantidad y precio unitario
     */
    public void calcularSubtotal() {
        if (cantidad != null && precio_unitario != null) {
            this.subtotal = cantidad * precio_unitario;
        } else {
            this.subtotal = 0.0;
        }
    }

    /**
     * Obtiene el título del anuncio asociado
     */
    public String getTituloAnuncio() {
        return anuncio != null ? anuncio.getTitulo() : "";
    }

    /**
     * Obtiene el nombre del material seleccionado
     */
    public String getNombreMaterial() {
        return material != null ? material.getNombre() : "No especificado";
    }

    /**
     * Obtiene el proveedor del servicio
     */
    public String getNombreProveedor() {
        return anuncio != null && anuncio.getUsuario() != null ? 
               anuncio.getUsuario().getNombreCompleto() : "";
    }

    /**
     * Verifica si este ítem puede ser cancelado
     */
    public boolean puedeSerCancelado() {
        return "pendiente".equals(estado_item) || "en_proceso".equals(estado_item);
    }

    /**
     * Verifica si este ítem está completado
     */
    public boolean estaCompletado() {
        return "completado".equals(estado_item);
    }

    // Getters y Setters específicos

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
        calcularSubtotal(); // Recalcular automáticamente
    }

    public Double getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_unitario(Double precio_unitario) {
        this.precio_unitario = precio_unitario;
        calcularSubtotal(); // Recalcular automáticamente
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public String getEspecificaciones() {
        return especificaciones;
    }

    public void setEspecificaciones(String especificaciones) {
        this.especificaciones = especificaciones;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public String getEstado_item() {
        return estado_item;
    }

    public void setEstado_item(String estado_item) {
        this.estado_item = estado_item;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Anuncio getAnuncio() {
        return anuncio;
    }

    public void setAnuncio(Anuncio anuncio) {
        this.anuncio = anuncio;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
} 