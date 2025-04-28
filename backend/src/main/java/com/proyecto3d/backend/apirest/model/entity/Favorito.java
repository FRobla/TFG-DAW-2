package com.proyecto3d.backend.apirest.model.entity;
 
import java.io.Serializable;
import java.util.Date;
 
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
 
@Entity
@Table(name = "favoritos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Favorito implements Serializable {
 
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "anuncio_id", nullable = false)
    private Anuncio anuncio;

    @Column(nullable = false)
    private Date fechaAgregado;

    //Getter y Setter

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
 
	public Date getFechaAgregado() {
		return fechaAgregado;
	}
 
	public void setFechaAgregado(Date fechaAgregado) {
		this.fechaAgregado = fechaAgregado;
	}
 
	public Long getId() {
		return id;
	}
}