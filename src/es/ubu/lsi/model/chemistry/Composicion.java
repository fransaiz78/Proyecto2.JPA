package es.ubu.lsi.model.chemistry;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the COMPOSICION database table.
 * 
 * @author Mario Santamar�a Arias
 * @author Francisco Saiz G�emes
 */
@Entity
@NamedQuery(name = "Composicion.findAll", query = "SELECT c FROM Composicion c")
public class Composicion implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ComposicionPK id;

	private Integer nroatomos;

	// bi-directional many-to-one association to Elemento
	@MapsId("simbolo") // Terminamos de indicar que ese campo es el mismo.
	@ManyToOne
	@JoinColumn(name = "SIMBOLO")
	private Elemento elemento;

	// bi-directional many-to-one association to Molecula
	@MapsId("idMolecula") // Terminamos de indicar que ese campo es el mismo.
	@ManyToOne
	@JoinColumn(name = "IDMOLECULA")
	private Moleculas molecula;

	public Composicion() {
		super();
	}

	public ComposicionPK getId() {
		return this.id;
	}

	public void setId(ComposicionPK id) {
		this.id = id;
	}

	public Integer getNroAtomos() {
		return this.nroatomos;
	}

	public void setNroAtomos(Integer nroatomos) {
		this.nroatomos = nroatomos;
	}

	public Elemento getElemento() {
		return this.elemento;
	}

	public void setElemento(Elemento elemento) {
		this.elemento = elemento;
	}

	public Moleculas getMolecula() {
		return this.molecula;
	}

	public void setMolecula(Moleculas molecula) {
		this.molecula = molecula;
	}

}