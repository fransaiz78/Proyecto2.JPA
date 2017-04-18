package es.ubu.lsi.model.chemistry;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the COMPOSICION database table.
 * 
 */
@Entity
@NamedQuery(name="Composicion.findAll", query="SELECT c FROM Composicion c")
public class Composicion implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ComposicionPK id;

	private BigDecimal nroatomos;

	//bi-directional many-to-one association to Elemento
	@ManyToOne
	@JoinColumn(name="SIMBOLO")
	private Elemento elemento;

	//bi-directional many-to-one association to Molecula
	@ManyToOne
	@JoinColumn(name="IDMOLECULA")
	private Molecula molecula;

	public Composicion() {
	}

	public ComposicionPK getId() {
		return this.id;
	}

	public void setId(ComposicionPK id) {
		this.id = id;
	}

	public BigDecimal getNroatomos() {
		return this.nroatomos;
	}

	public void setNroatomos(BigDecimal nroatomos) {
		this.nroatomos = nroatomos;
	}

	public Elemento getElemento() {
		return this.elemento;
	}

	public void setElemento(Elemento elemento) {
		this.elemento = elemento;
	}

	public Molecula getMolecula() {
		return this.molecula;
	}

	public void setMolecula(Molecula molecula) {
		this.molecula = molecula;
	}

}