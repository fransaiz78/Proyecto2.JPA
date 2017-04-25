package es.ubu.lsi.model.chemistry;

import java.io.Serializable;
import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The persistent class for the MOLECULAS database table.
 * 
 */
@Entity

@NamedQueries({ @NamedQuery(name = "Moleculas.findAll", query = "SELECT m FROM Moleculas m"),
		@NamedQuery(name = "Moleculas.findByNombre", query = "SELECT m FROM Moleculas m WHERE m.nombre = :nombre"),
		@NamedQuery(name = "Moleculas.findByFormula", query = "SELECT m FROM Moleculas m WHERE m.formula = :formula"), 
		})

public class Moleculas implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@SequenceGenerator(name = "moleculasId_SEQ", sequenceName = "moleculasId_SEQ", allocationSize = 1)
	@Id
	@GeneratedValue(generator = "moleculasId_SEQ")
	private Integer id;
	private String nombre;
	private Integer pesomolecular;
	private String formula;

	// bi-directional many-to-one association to Composicion
	@OneToMany(mappedBy = "molecula")
	private List<Composicion> composicions;

	public Moleculas() {
		super();
		composicions = new ArrayList<Composicion>();
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
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

	public Integer getPesoMolecular() {
		return this.pesomolecular;
	}

	public void setPesoMolecular(Integer pesomolecular) {
		this.pesomolecular = pesomolecular;
	}

	public List<Composicion> getComposicions() {
		return this.composicions;
	}

//	public void setComposicions(List<Composicion> composicions) {
//		this.composicions = composicions;
//	}

	public Composicion addComposicion(Composicion composicion) {
		getComposicions().add(composicion);
		composicion.setMolecula(this);

		return composicion;
	}

//	public Composicion removeComposicion(Composicion composicion) {
//		getComposicions().remove(composicion);
//		composicion.setMolecula(null);
//
//		return composicion;
//	}

}