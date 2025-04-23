package com.evidenlibrary.backend.apirest.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "anuncios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Anuncio implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String titulo;

	@Column(nullable = false)
	private String descripcion;

	@Column(nullable = false)
	private String estado; // (activo, reservado, vendido)

	@Column(nullable = true, columnDefinition = "LONGTEXT")
	private String imagen;

	@Column(nullable = false)
	private Double precio_base;

	@Column(nullable = false)
	private String tiempo_estimado;

	@Column(nullable = false)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date fecha_publicacion;

	@Column(nullable = true)
	private Integer vistas;

	// Relaciones

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "anuncio_categoria", joinColumns = @JoinColumn(name = "anuncio_id"), inverseJoinColumns = @JoinColumn(name = "categoria_id"))
	private final Set<Categoria> categorias = new HashSet<>();

	@OneToMany(mappedBy = "anuncio", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private final List<Usuario> usuario = new ArrayList<>();

	@OneToMany(mappedBy = "anuncio", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private final List<Impresora> impresora = new ArrayList<>();

	@OneToMany(mappedBy = "anuncio", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private final List<Valoracion> valoraciones = new ArrayList<>();

	@OneToMany(mappedBy = "anuncio", fetch = FetchType.EAGER)
	@JsonIgnore
	private final List<Favorito> favoritos = new ArrayList<>();

	@Column(name = "valoracion_media", nullable = true)
	private Double valoracionMedia;

	// Getter/Setter

	public String getDescripcion() {
		return descripcion;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public List<Favorito> getFavoritos() {
		return favoritos;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Double getPrecioBase() {
		return precio_base;
	}

	public void setPrecioBase(Double precio_base) {
		this.precio_base = precio_base;
	}

	public List<Usuario> getUsuario() {
		return usuario;
	}

	public Set<Categoria> getCategorias() {
		return categorias;
	}

	public List<Valoracion> getValoraciones() {
		return valoraciones;
	}

	public Long getId() {
		return id;
	}

	public Double getPrecio_base() {
		return precio_base;
	}

	public void setPrecio_base(Double precio_base) {
		this.precio_base = precio_base;
	}

	public String getTiempo_estimado() {
		return tiempo_estimado;
	}

	public void setTiempo_estimado(String tiempo_estimado) {
		this.tiempo_estimado = tiempo_estimado;
	}

	public Date getFecha_publicacion() {
		return fecha_publicacion;
	}

	public void setFecha_publicacion(Date fecha_publicacion) {
		this.fecha_publicacion = fecha_publicacion;
	}

	public Integer getVistas() {
		return vistas;
	}

	public void setVistas(Integer vistas) {
		this.vistas = vistas;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public List<Impresora> getImpresora() {
		return impresora;
	}

	// Valoracion media
	public Double getValoracionMedia() {
		if (valoraciones.isEmpty()) { // No hay valoraciones
			this.valoracionMedia = 0.0;
			return this.valoracionMedia;
		}
		// Calcular la media
		/*
		 * double suma = 0.0;
		 * for (Valoracion v : valoraciones) {
		 * suma += v.getPuntuacion();
		 * }
		 */
		double suma = valoraciones.stream()
				.mapToDouble(Valoracion::getPuntuacion)
				.sum();
		this.valoracionMedia = suma / valoraciones.size();
		return valoracionMedia;
	}

	public void setValoracionMedia(Double valoracionMedia) {
		this.valoracionMedia = valoracionMedia;
	}

}
