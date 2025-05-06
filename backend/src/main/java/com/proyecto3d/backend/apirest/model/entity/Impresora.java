package com.proyecto3d.backend.apirest.model.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa una impresora 3D en el sistema
 */
@Entity
@Table(name = "impresoras")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Impresora implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String modelo;

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private String tecnologia; // FDM, SLA, SLS, etc.

    @Column(nullable = true)
    private Double volumen_impresion_x;

    @Column(nullable = true)
    private Double volumen_impresion_y;

    @Column(nullable = true)
    private Double volumen_impresion_z;

    @Column(nullable = true, name = "valor_precision")
    private Double precision_valor; // precisión en mm

    @Column(nullable = true)
    private String descripcion;

    @Column(nullable = true, columnDefinition = "LONGTEXT")
    private String imagen;

    // Getters y Setters
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getTecnologia() {
		return tecnologia;
	}

	public void setTecnologia(String tecnologia) {
		this.tecnologia = tecnologia;
	}

	public Double getVolumen_impresion_x() {
		return volumen_impresion_x;
	}

	public void setVolumen_impresion_x(Double volumen_impresion_x) {
		this.volumen_impresion_x = volumen_impresion_x;
	}

	public Double getVolumen_impresion_y() {
		return volumen_impresion_y;
	}

	public void setVolumen_impresion_y(Double volumen_impresion_y) {
		this.volumen_impresion_y = volumen_impresion_y;
	}

	public Double getVolumen_impresion_z() {
		return volumen_impresion_z;
	}

	public void setVolumen_impresion_z(Double volumen_impresion_z) {
		this.volumen_impresion_z = volumen_impresion_z;
	}

	public Double getPrecision_valor() {
		return precision_valor;
	}

	public void setPrecision_valor(Double precision_valor) {
		this.precision_valor = precision_valor;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
    
    
} 