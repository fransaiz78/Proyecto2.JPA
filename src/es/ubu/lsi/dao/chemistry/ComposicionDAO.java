/**
 * 
 */
package es.ubu.lsi.dao.chemistry;

import javax.persistence.EntityManager;

import es.ubu.lsi.dao.JpaDAO;
import es.ubu.lsi.model.chemistry.Composicion;
import es.ubu.lsi.model.chemistry.ComposicionPK;

/**
 * @author Mario Santamaria
 * @author Francisco Saiz
 *
 */
public class ComposicionDAO extends JpaDAO<Composicion, ComposicionPK> {

	public ComposicionDAO(EntityManager em) {
		super(em);
		// TODO Auto-generated constructor stub
	}
}
