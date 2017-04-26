package es.ubu.lsi.model.chemistry;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * The persistent class for the ELEMENTOS database table.
 * 
 * @author Mario Santamaría Arias
 * @author Francisco Saiz Güemes
 */
@Entity
@Table(name = "ELEMENTOS") // Indicar que en el sql la tabla se llama Elementos.
@NamedQuery(name = "Elemento.findAll", query = "SELECT e FROM Elemento e")
public class Elemento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String simbolo;

	private String nombre;

	private Integer pesoatomico;

	// bi-directional many-to-one association to Composicion
	@OneToMany(mappedBy = "elemento")
	private List<Composicion> composicions;

	public Elemento() {
	}

	public String getSimbolo() {
		return this.simbolo;
	}

	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getPesoAtomico() {
		return this.pesoatomico;
	}

	public void setPesoAtomico(Integer pesoatomico) {
		this.pesoatomico = pesoatomico;
	}

	public List<Composicion> getComposicions() {
		return this.composicions;
	}

	public void setComposicions(List<Composicion> composicions) {
		this.composicions = composicions;
	}

	public Composicion addComposicion(Composicion composicion) {
		getComposicions().add(composicion);
		composicion.setElemento(this);

		return composicion;
	}

	public Composicion removeComposicion(Composicion composicion) {
		getComposicions().remove(composicion);
		composicion.setElemento(null);

		return composicion;
	}

}