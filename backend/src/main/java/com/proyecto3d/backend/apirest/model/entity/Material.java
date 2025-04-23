package com.proyecto3d.backend.apirest.model.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

	@Column(nullable = false)
	private String nombre;

	@Column(nullable = false)
	private String fabricante;

    @Column(nullable = false)
	private String color;

	@Column(nullable = false)
	private String tipo;

	@Column(nullable = false)
	private Double precioKg;

	@Column(nullable = false)
	private Integer stock;

    // Relaciones
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "anuncio_material", joinColumns = @JoinColumn(name = "material_id"), inverseJoinColumns = @JoinColumn(name = "anuncio_id"))
    private final Set<Anuncio> anuncios = new HashSet<>();
    
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

	public String getFabricante() {
		return fabricante;
	}

	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Double getPrecioKg() {
		return precioKg;
	}

	public void setPrecioKg(Double precioKg) {
		this.precioKg = precioKg;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Set<Anuncio> getAnuncios() {
		return anuncios;
	}
    
    
}
