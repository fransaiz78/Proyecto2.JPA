/**
 * 
 */
package es.ubu.lsi.dao.chemistry;

import javax.persistence.EntityManager;

import es.ubu.lsi.dao.JpaDAO;
import es.ubu.lsi.model.chemistry.Elementos;

/**
 * @author Mario Santamaria
 * @author Francisco Saiz
 *
 */
public class ElementoDAO extends JpaDAO<Elementos, String>{

	public ElementoDAO(EntityManager em) {
		super(em);
	}
	
}
