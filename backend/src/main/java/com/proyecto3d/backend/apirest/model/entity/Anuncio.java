package com.proyecto3d.backend.apirest.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import jakarta.persistence.ManyToOne;
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

	@Column(name = "valoracion_media", nullable = true)
	private Double valoracion_media;

	// Relaciones

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "impresora_id")
	private Impresora impresora;

	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "anuncio_categoria", joinColumns = @JoinColumn(name = "anuncio_id"), inverseJoinColumns = @JoinColumn(name = "categoria_id"))
	private Set<Categoria> categorias;

	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "anuncio_material", joinColumns = @JoinColumn(name = "anuncio_id"), inverseJoinColumns = @JoinColumn(name = "material_id"))
	private Set<Material> materiales;

	@OneToMany(mappedBy = "anuncio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Valoracion> valoraciones = new ArrayList<>();

	@OneToMany(mappedBy = "anuncio", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Favorito> favoritos = new ArrayList<>();

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

	public void setFavoritos(List<Favorito> favoritos) {
		this.favoritos = favoritos;
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

	public Usuario getUsuario() {
		return usuario;
	}

	public Impresora getImpresora() {
		return impresora;
	}

	public Set<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(Set<Categoria> categorias) {
		this.categorias = categorias;
	}

	public Set<Material> getMateriales() {
		return materiales;
	}

	public void setMateriales(Set<Material> materiales) {
		this.materiales = materiales;
	}

	public List<Valoracion> getValoraciones() {
		return valoraciones;
	}

	public void setValoraciones(List<Valoracion> valoraciones) {
		this.valoraciones = valoraciones;
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

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public void setImpresora(Impresora impresora) {
		this.impresora = impresora;
	} 

	// Valoracion media
	public Double getValoracion_media() {
		if (valoraciones == null || valoraciones.isEmpty()) {
			this.valoracion_media = 0.0;
			return this.valoracion_media;
		}
		// Calcular la media de las valoraciones
		double suma = valoraciones.stream()
				.mapToDouble(Valoracion::getPuntuacion)
				.sum();
		this.valoracion_media = suma / valoraciones.size();
		return valoracion_media;
	}

	public void setValoracion_media(Double valoracion_media) {
		this.valoracion_media = valoracion_media;
	}

	/**
	 * Método de utilidad para obtener el número de valoraciones
	 */
	public Integer getNumero_valoraciones() {
		return valoraciones != null ? valoraciones.size() : 0;
	}

	/**
	 * Método de utilidad para verificar si está marcado como favorito por un usuario
	 */
	public boolean esFavorito(Long usuarioId) {
		if (favoritos == null || usuarioId == null) {
			return false;
		}
		return favoritos.stream()
				.anyMatch(favorito -> favorito.getUsuario().getId().equals(usuarioId));
	}
}
