package es.ubu.lsi.model.chemistry;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the MOLECULAS database table.
 * 
 */
@Entity
@Table(name="MOLECULAS")
@NamedQuery(name="Molecula.findAll", query="SELECT m FROM Molecula m")
public class Molecula implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="MOLECULAS_ID_GENERATOR" )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MOLECULAS_ID_GENERATOR")
	private long id;

	private String formula;

	private String nombre;

	private BigDecimal pesomolecular;

	//bi-directional many-to-one association to Composicion
	@OneToMany(mappedBy="molecula")
	private List<Composicion> composicions;

	public Molecula() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFormula() {
		return this.formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public BigDecimal getPesomolecular() {
		return this.pesomolecular;
	}

	public void setPesomolecular(BigDecimal pesomolecular) {
		this.pesomolecular = pesomolecular;
	}

	public List<Composicion> getComposicions() {
		return this.composicions;
	}

	public void setComposicions(List<Composicion> composicions) {
		this.composicions = composicions;
	}

	public Composicion addComposicion(Composicion composicion) {
		getComposicions().add(composicion);
		composicion.setMolecula(this);

		return composicion;
	}

	public Composicion removeComposicion(Composicion composicion) {
		getComposicions().remove(composicion);
		composicion.setMolecula(null);

		return composicion;
	}

}