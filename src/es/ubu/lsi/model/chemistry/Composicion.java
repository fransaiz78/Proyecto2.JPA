package es.ubu.lsi.model.chemistry;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the COMPOSICION database table.
 * 
 */
@Entity
@NamedQueries({ 
	@NamedQuery(name="Composicion.findAll", query="SELECT c FROM Composicion c"),
	@NamedQuery(name = "Composicion.findByIdMolecula", query = "SELECT m FROM Composicion m WHERE m.id = :idMolecula"),
	})
public class Composicion implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ComposicionPK id;

	private Integer nroAtomos;

	//bi-directional many-to-one association to Elemento
	@ManyToOne
	@JoinColumn(name="simbolo")
	private Elementos elemento;

	//bi-directional many-to-one association to Molecula
	@ManyToOne
	@JoinColumn(name="idMolecula")
	private Moleculas molecula;

	public Composicion() {
	}

	public ComposicionPK getId() {
		return this.id;
	}

	public void setId(ComposicionPK id) {
		this.id = id;
	}

	public Integer getNroAtomos() {
		return this.nroAtomos;
	}

	public void setNroAtomos(Integer nroatomos) {
		this.nroAtomos = nroatomos;
	}

	public Elementos getElemento() {
		return this.elemento;
	}

	public void setElemento(Elementos elemento) {
		this.elemento = elemento;
	}

	public Moleculas getMolecula() {
		return this.molecula;
	}

	public void setMolecula(Moleculas molecula) {
		this.molecula = molecula;
	}


}