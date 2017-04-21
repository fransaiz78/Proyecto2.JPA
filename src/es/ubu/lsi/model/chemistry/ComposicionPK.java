package es.ubu.lsi.model.chemistry;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the COMPOSICION database table.
 * 
 */
@Embeddable
public class ComposicionPK implements Serializable {


	
//	@Column(insertable=false, updatable=false)
	private String simbolo;

//	@Column(insertable=false, updatable=false)
	private Integer idMolecula;

	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	
	public ComposicionPK() {
		super();
	}
	
	public ComposicionPK(String simbolo, Integer idMolecula) {
		this.simbolo = simbolo;
		this.idMolecula = idMolecula;
	}
	
	public String getSimbolo() {
		return this.simbolo;
	}
	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
	}
	public long getIdMolecula() {
		return this.idMolecula;
	}
	public void setIdMolecula(Integer idMolecula) {
		this.idMolecula = idMolecula;
	}

//	public boolean equals(Object other) {
//		if (this == other) {
//			return true;
//		}
//		if (!(other instanceof ComposicionPK)) {
//			return false;
//		}
//		ComposicionPK castOther = (ComposicionPK)other;
//		return 
//			this.simbolo.equals(castOther.simbolo)
//			&& (this.idMolecula == castOther.idMolecula);
//	}
//
//	public int hashCode() {
//		final int prime = 31;
//		int hash = 17;
//		hash = hash * prime + this.simbolo.hashCode();
//		hash = hash * prime + ((int) (this.idMolecula ^ (this.idMolecula >>> 32)));
//		
//		return hash;
//	}
	/*
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object o) {
		return ((o instanceof ComposicionPK) && simbolo.equals(((ComposicionPK)o).getSimbolo()) &&
				idMolecula == ((ComposicionPK)o).getIdMolecula());
	}

	/*
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return simbolo.hashCode() + idMolecula;
	}
}