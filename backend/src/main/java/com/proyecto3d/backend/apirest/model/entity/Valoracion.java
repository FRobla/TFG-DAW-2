package com.proyecto3d.backend.apirest.model.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

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
 * Entidad que representa las valoraciones de los anuncios por parte de los usuarios
 */
@Entity
@Table(name = "valoraciones")
public class Valoracion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer puntuacion; // De 1 a 5 estrellas

    @Column(nullable = true, columnDefinition = "TEXT")
    private String comentario;

    @Column(name = "fecha_valoracion", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonProperty("fecha_valoracion")
    private Date fechaValoracion;

    @Column(name = "respuesta_proveedor", nullable = true, columnDefinition = "TEXT")
    @JsonProperty("respuesta_proveedor")
    private String respuestaProveedor; // Respuesta del proveedor del servicio

    @Column(name = "fecha_respuesta", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonProperty("fecha_respuesta")
    private Date fechaRespuesta;

    @Column(nullable = false)
    private Boolean verificado = false; // Si la valoración está verificada (usuario compró el servicio)

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anuncio_id", nullable = false)
    private Anuncio anuncio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Constructores
    public Valoracion() {}

    public Valoracion(Long id, Integer puntuacion, String comentario, Date fechaValoracion, 
                     String respuestaProveedor, Date fechaRespuesta, Boolean verificado, 
                     Anuncio anuncio, Usuario usuario) {
        this.id = id;
        this.puntuacion = puntuacion;
        this.comentario = comentario;
        this.fechaValoracion = fechaValoracion;
        this.respuestaProveedor = respuestaProveedor;
        this.fechaRespuesta = fechaRespuesta;
        this.verificado = verificado;
        this.anuncio = anuncio;
        this.usuario = usuario;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Integer puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Date getFechaValoracion() {
        return fechaValoracion;
    }

    public void setFechaValoracion(Date fechaValoracion) {
        this.fechaValoracion = fechaValoracion;
    }

    public String getRespuestaProveedor() {
        return respuestaProveedor;
    }

    public void setRespuestaProveedor(String respuestaProveedor) {
        this.respuestaProveedor = respuestaProveedor;
    }

    public Date getFechaRespuesta() {
        return fechaRespuesta;
    }

    public void setFechaRespuesta(Date fechaRespuesta) {
        this.fechaRespuesta = fechaRespuesta;
    }

    public Boolean getVerificado() {
        return verificado;
    }

    public void setVerificado(Boolean verificado) {
        this.verificado = verificado;
    }

    public Anuncio getAnuncio() {
        return anuncio;
    }

    public void setAnuncio(Anuncio anuncio) {
        this.anuncio = anuncio;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
} 