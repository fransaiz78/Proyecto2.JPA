package es.ubu.lsi.model.chemistry;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the COMPOSICION database table.
 * 
 */
@Embeddable
public class ComposicionPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(insertable=false, updatable=false)
	private String simbolo;

	@Column(insertable=false, updatable=false)
	private long idmolecula;

	public ComposicionPK() {
	}
	public String getSimbolo() {
		return this.simbolo;
	}
	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
	}
	public long getIdmolecula() {
		return this.idmolecula;
	}
	public void setIdmolecula(long idmolecula) {
		this.idmolecula = idmolecula;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ComposicionPK)) {
			return false;
		}
		ComposicionPK castOther = (ComposicionPK)other;
		return 
			this.simbolo.equals(castOther.simbolo)
			&& (this.idmolecula == castOther.idmolecula);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.simbolo.hashCode();
		hash = hash * prime + ((int) (this.idmolecula ^ (this.idmolecula >>> 32)));
		
		return hash;
	}
}