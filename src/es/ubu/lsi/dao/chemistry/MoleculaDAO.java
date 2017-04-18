/**
 * 
 */
package es.ubu.lsi.dao.chemistry;

import javax.persistence.EntityManager;

import es.ubu.lsi.dao.JpaDAO;
import es.ubu.lsi.model.chemistry.Molecula;

/**
 * @author Mario Santamaria
 * @author Francisco Saiz
 *
 */
public class MoleculaDAO extends JpaDAO<Molecula, Integer> {

	/**
	 * Constructor de la clase.
	 */
	public MoleculaDAO(EntityManager em) {
		super(em);
	}

}
