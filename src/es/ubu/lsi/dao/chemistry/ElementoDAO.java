/**
 * 
 */
package es.ubu.lsi.dao.chemistry;

import javax.persistence.EntityManager;

import es.ubu.lsi.dao.JpaDAO;
import es.ubu.lsi.model.chemistry.Elemento;

/**
 * Clase ElementoDAO.
 * 
 * @author Mario Santamaria
 * @author Francisco Saiz
 *
 */
public class ElementoDAO extends JpaDAO<Elemento, String> {

	/**
	 * Contructor de la clase.
	 * 
	 * @param em
	 *            EntityManager
	 */
	public ElementoDAO(EntityManager em) {
		super(em);
	}

}
