package com.proyecto3d.backend.apirest.model.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * DTO para transferir datos del carrito entre el backend y frontend
 */
public class CarritoDTO {

    private Long id;
    private Long usuarioId;
    private Long anuncioId;
    private String tituloAnuncio;
    private String descripcionAnuncio;
    private String imagenAnuncio;
    private String nombreProveedor;
    private Integer cantidad;
    private String materialSeleccionado;
    private String colorSeleccionado;
    private Boolean acabadoPremium;
    private Boolean urgente;
    private Boolean envioGratis;
    private Double precioUnitario;
    private Double precioTotal;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fechaAgregado;

    // Constructor sin parámetros
    public CarritoDTO() {
    }

    // Constructor con todos los parámetros
    public CarritoDTO(Long id, Long usuarioId, Long anuncioId, String tituloAnuncio, 
                     String descripcionAnuncio, String imagenAnuncio, String nombreProveedor,
                     Integer cantidad, String materialSeleccionado, String colorSeleccionado,
                     Boolean acabadoPremium, Boolean urgente, Boolean envioGratis,
                     Double precioUnitario, Double precioTotal, Date fechaAgregado) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.anuncioId = anuncioId;
        this.tituloAnuncio = tituloAnuncio;
        this.descripcionAnuncio = descripcionAnuncio;
        this.imagenAnuncio = imagenAnuncio;
        this.nombreProveedor = nombreProveedor;
        this.cantidad = cantidad;
        this.materialSeleccionado = materialSeleccionado;
        this.colorSeleccionado = colorSeleccionado;
        this.acabadoPremium = acabadoPremium;
        this.urgente = urgente;
        this.envioGratis = envioGratis;
        this.precioUnitario = precioUnitario;
        this.precioTotal = precioTotal;
        this.fechaAgregado = fechaAgregado;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getAnuncioId() {
        return anuncioId;
    }

    public void setAnuncioId(Long anuncioId) {
        this.anuncioId = anuncioId;
    }

    public String getTituloAnuncio() {
        return tituloAnuncio;
    }

    public void setTituloAnuncio(String tituloAnuncio) {
        this.tituloAnuncio = tituloAnuncio;
    }

    public String getDescripcionAnuncio() {
        return descripcionAnuncio;
    }

    public void setDescripcionAnuncio(String descripcionAnuncio) {
        this.descripcionAnuncio = descripcionAnuncio;
    }

    public String getImagenAnuncio() {
        return imagenAnuncio;
    }

    public void setImagenAnuncio(String imagenAnuncio) {
        this.imagenAnuncio = imagenAnuncio;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
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
} 