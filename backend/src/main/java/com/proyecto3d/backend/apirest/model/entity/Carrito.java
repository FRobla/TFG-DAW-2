package com.proyecto3d.backend.apirest.model.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 * Entidad que representa un elemento del carrito de compras
 */
@Entity
@Table(name = "carrito")
public class Carrito implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anuncio_id", nullable = false)
    private Anuncio anuncio;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "material_seleccionado", nullable = false)
    private String materialSeleccionado;

    @Column(name = "color_seleccionado")
    private String colorSeleccionado;

    @Column(name = "acabado_premium")
    private Boolean acabadoPremium = false;

    @Column(name = "urgente")
    private Boolean urgente = false;

    @Column(name = "envio_gratis")
    private Boolean envioGratis = false;

    @Column(name = "precio_unitario", nullable = false)
    private Double precioUnitario;

    @Column(name = "precio_total", nullable = false)
    private Double precioTotal;

    @Column(name = "fecha_agregado", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fechaAgregado;

    /**
     * Calcula el precio total basado en la cantidad y servicios adicionales
     */
    public void calcularPrecioTotal() {
        double total = this.precioUnitario * this.cantidad;
        
        // Aplicar recargos por servicios adicionales
        if (Boolean.TRUE.equals(this.acabadoPremium)) {
            total += total * 0.15; // 15% adicional por acabado premium
        }
        
        if (Boolean.TRUE.equals(this.urgente)) {
            total += total * 0.25; // 25% adicional por urgente
        }
        
        // El envío gratis no afecta el precio, es un beneficio
        
        this.precioTotal = total;
    }

    /**
     * Constructor sin parámetros requerido por JPA
     */
    public Carrito() {
        // Constructor vacío
    }

    /**
     * Constructor con parámetros principales
     */
    public Carrito(Usuario usuario, Anuncio anuncio, Integer cantidad, String materialSeleccionado, Double precioUnitario) {
        this.usuario = usuario;
        this.anuncio = anuncio;
        this.cantidad = cantidad;
        this.materialSeleccionado = materialSeleccionado;
        this.precioUnitario = precioUnitario;
        this.fechaAgregado = new Date();
        this.acabadoPremium = false;
        this.urgente = false;
        this.envioGratis = false;
        calcularPrecioTotal();
    }

    // Getters y Setters manuales
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Anuncio getAnuncio() {
        return anuncio;
    }

    public void setAnuncio(Anuncio anuncio) {
        this.anuncio = anuncio;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getMaterialSeleccionado() {
        return materialSeleccionado;
    }

    public void setMaterialSeleccionado(String materialSeleccionado) {
        this.materialSeleccionado = materialSeleccionado;
    }

    public String getColorSeleccionado() {
        return colorSeleccionado;
    }

    public void setColorSeleccionado(String colorSeleccionado) {
        this.colorSeleccionado = colorSeleccionado;
    }

    public Boolean getAcabadoPremium() {
        return acabadoPremium;
    }

    public void setAcabadoPremium(Boolean acabadoPremium) {
        this.acabadoPremium = acabadoPremium;
    }

    public Boolean getUrgente() {
        return urgente;
    }

    public void setUrgente(Boolean urgente) {
        this.urgente = urgente;
    }

    public Boolean getEnvioGratis() {
        return envioGratis;
    }

    public void setEnvioGratis(Boolean envioGratis) {
        this.envioGratis = envioGratis;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(Double precioTotal) {
        this.precioTotal = precioTotal;
    }

    public Date getFechaAgregado() {
        return fechaAgregado;
    }

    public void setFechaAgregado(Date fechaAgregado) {
        this.fechaAgregado = fechaAgregado;
    }

    // Método builder estático
    public static CarritoBuilder builder() {
        return new CarritoBuilder();
    }

    // Clase Builder interna
    public static class CarritoBuilder {
        private Long id;
        private Usuario usuario;
        private Anuncio anuncio;
        private Integer cantidad;
        private String materialSeleccionado;
        private String colorSeleccionado;
        private Boolean acabadoPremium = false;
        private Boolean urgente = false;
        private Boolean envioGratis = false;
        private Double precioUnitario;
        private Double precioTotal;
        private Date fechaAgregado;

        public CarritoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CarritoBuilder usuario(Usuario usuario) {
            this.usuario = usuario;
            return this;
        }

        public CarritoBuilder anuncio(Anuncio anuncio) {
            this.anuncio = anuncio;
            return this;
        }

        public CarritoBuilder cantidad(Integer cantidad) {
            this.cantidad = cantidad;
            return this;
        }

        public CarritoBuilder materialSeleccionado(String materialSeleccionado) {
            this.materialSeleccionado = materialSeleccionado;
            return this;
        }

        public CarritoBuilder colorSeleccionado(String colorSeleccionado) {
            this.colorSeleccionado = colorSeleccionado;
            return this;
        }

        public CarritoBuilder acabadoPremium(Boolean acabadoPremium) {
            this.acabadoPremium = acabadoPremium;
            return this;
        }

        public CarritoBuilder urgente(Boolean urgente) {
            this.urgente = urgente;
            return this;
        }

        public CarritoBuilder envioGratis(Boolean envioGratis) {
            this.envioGratis = envioGratis;
            return this;
        }

        public CarritoBuilder precioUnitario(Double precioUnitario) {
            this.precioUnitario = precioUnitario;
            return this;
        }

        public CarritoBuilder precioTotal(Double precioTotal) {
            this.precioTotal = precioTotal;
            return this;
        }

        public CarritoBuilder fechaAgregado(Date fechaAgregado) {
            this.fechaAgregado = fechaAgregado;
            return this;
        }

        public Carrito build() {
            Carrito carrito = new Carrito();
            carrito.setId(this.id);
            carrito.setUsuario(this.usuario);
            carrito.setAnuncio(this.anuncio);
            carrito.setCantidad(this.cantidad);
            carrito.setMaterialSeleccionado(this.materialSeleccionado);
            carrito.setColorSeleccionado(this.colorSeleccionado);
            carrito.setAcabadoPremium(this.acabadoPremium);
            carrito.setUrgente(this.urgente);
            carrito.setEnvioGratis(this.envioGratis);
            carrito.setPrecioUnitario(this.precioUnitario);
            carrito.setPrecioTotal(this.precioTotal);
            carrito.setFechaAgregado(this.fechaAgregado);
            return carrito;
        }
    }
} 