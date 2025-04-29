package com.proyecto3d.backend.apirest.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "autores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Impresora implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min=5, max=50, message="debe tener entre 5 y 50 caracteres")
    @NotEmpty(message = "no puede estar vacío")
    private String nombre;

    @Column(nullable = false)
    @Size(min=2, max=250, message="debe tener entre 2 y 250 caracteres")
    @NotEmpty(message = "no puede estar vacío")
    private String marca;

    @Column(nullable = false)
    @Size(min=2, max=250, message="debe tener entre 2 y 250 caracteres")
    @NotEmpty(message = "no puede estar vacío")
    private String modelo;

    @Column(nullable = false)
	private String tipo; // (resina, filamento, laser)

    @Column(nullable = false)
	private Integer volumen_impresion_x;

    @Column(nullable = false)
	private Integer volumen_impresion_y;

    @Column(nullable = false)
	private Integer volumen_impresion_z;

    // Relación OneToMany: una impresora puede tener varios anuncios asociados
    @OneToMany(mappedBy = "impresora", cascade = CascadeType.ALL)
    private List<Anuncio> anuncios;

    @JsonProperty("anuncios")
    public List<AnuncioSimple> getAnunciosSimple() {
        List<AnuncioSimple> anunciosSimple = new ArrayList<>();
        for (Anuncio anuncio : anuncios) {
            anunciosSimple.add(new AnuncioSimple(anuncio.getId(), anuncio.getTitulo(), anuncio.getPrecioBase(), anuncio.getDescripcion()));
        }
        return anunciosSimple;
    }

    public static class AnuncioSimple {
        private Long id;
        private String titulo;
        private Double precio;
        private String descripcion;

        public AnuncioSimple(Long id, String titulo, Double precio, String descripcion) {
            this.id = id;
            this.titulo = titulo;
            this.precio = precio;
            this.descripcion = descripcion;
        }

        // Getters
        public Long getId() { return id; }
        public String getTitulo() { return titulo; }
        public Double getPrecio() { return precio; }
        public String getDescripcion() { return descripcion; }

        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public void setPrecio(Double precio) {
            this.precio = precio;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }
    }
    
    // Getters and setters

    public Long getId() {
        return id;
    }
    
    
    public void setId(Long id) {
        this.id = id;
    }


    public String getNombre() {
        return nombre;
    }

    public String getModelo() {
		return modelo;
	}


	public void setModelo(String modelo) {
		this.modelo = modelo;
	}


	public String getTipo() {
		return tipo;
	}


	public void setTipo(String tipo) {
		this.tipo = tipo;
	}


	public Integer getVolumen_impresion_x() {
		return volumen_impresion_x;
	}


	public void setVolumen_impresion_x(Integer volumen_impresion_x) {
		this.volumen_impresion_x = volumen_impresion_x;
	}


	public Integer getVolumen_impresion_y() {
		return volumen_impresion_y;
	}


	public void setVolumen_impresion_y(Integer volumen_impresion_y) {
		this.volumen_impresion_y = volumen_impresion_y;
	}


	public Integer getVolumen_impresion_z() {
		return volumen_impresion_z;
	}


	public void setVolumen_impresion_z(Integer volumen_impresion_z) {
		this.volumen_impresion_z = volumen_impresion_z;
	}


	public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public List<Anuncio> getAnuncios() {
        return anuncios;
    }

    public void setAnuncios(List<Anuncio> anuncios) {
        this.anuncios = anuncios;
    }
}