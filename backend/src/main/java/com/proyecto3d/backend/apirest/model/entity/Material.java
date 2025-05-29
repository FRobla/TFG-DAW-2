package com.proyecto3d.backend.apirest.model.entity;

import java.io.Serializable;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa los diferentes materiales de impresión 3D disponibles
 */
@Entity
@Table(name = "materiales")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Material implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = true)
    private String descripcion;

    @Column(nullable = true)
    private String propiedades; // Características del material (flexibilidad, resistencia, etc.)

    @Column(nullable = true)
    private String color_disponibles; // Colores disponibles separados por comas

    @Column(nullable = true)
    private Double precio_kg; // Precio por kilogramo del material

    @Column(nullable = false)
    private Boolean activo = true; // Si el material está disponible

    // Relaciones
    @ManyToMany(mappedBy = "materiales")
    @JsonIgnore
    private Set<Anuncio> anuncios;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPropiedades() {
        return propiedades;
    }

    public void setPropiedades(String propiedades) {
        this.propiedades = propiedades;
    }

    public String getColor_disponibles() {
        return color_disponibles;
    }

    public void setColor_disponibles(String color_disponibles) {
        this.color_disponibles = color_disponibles;
    }

    public Double getPrecio_kg() {
        return precio_kg;
    }

    public void setPrecio_kg(Double precio_kg) {
        this.precio_kg = precio_kg;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Set<Anuncio> getAnuncios() {
        return anuncios;
    }

    public void setAnuncios(Set<Anuncio> anuncios) {
        this.anuncios = anuncios;
    }
} 