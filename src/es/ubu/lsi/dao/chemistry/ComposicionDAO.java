/**
 * 
 */
package es.ubu.lsi.dao.chemistry;

import javax.persistence.EntityManager;

import es.ubu.lsi.dao.JpaDAO;
import es.ubu.lsi.model.chemistry.Composicion;
import es.ubu.lsi.model.chemistry.ComposicionPK;
import es.ubu.lsi.model.chemistry.Moleculas;

/**
 * Clase ComposicionDAO.
 * 
 * @author Mario Santamaría Arias
 * @author Francisco Saiz Güemes
 *
 */
public class ComposicionDAO extends JpaDAO<Composicion, ComposicionPK> {

	/**
	 * Constructor de la clase.
	 * 
	 * @param em
	 *            EntityManager
	 */
	public ComposicionDAO(EntityManager em) {
		super(em);
		// TODO Auto-generated constructor stub
	}

}
