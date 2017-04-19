/**
 * 
 */
package es.ubu.lsi.dao.chemistry;

import javax.persistence.EntityManager;

import es.ubu.lsi.dao.JpaDAO;
import es.ubu.lsi.model.chemistry.Moleculas;

/**
 * @author Mario Santamaria
 * @author Francisco Saiz
 *
 */
public class MoleculaDAO extends JpaDAO<Moleculas, Integer> {

	/**
	 * Constructor de la clase.
	 */
	public MoleculaDAO(EntityManager em) {
		super(em);
	}

	public Moleculas findMoleculaByNombre(String nombre) {
		try {
			return super.getEntityManager().createNamedQuery("Moleculas.findByNombre", Moleculas.class)
					.setParameter("nombre", nombre).getSingleResult();
		} catch (javax.persistence.NoResultException e) {
			return null;
		}
	}

	public Moleculas findMoleculaByFormula(String formula) {
		try {
			return super.getEntityManager().createNamedQuery("Moleculas.findByFormula", Moleculas.class)
					.setParameter("formula", formula).getSingleResult();
		} catch (javax.persistence.NoResultException e) {
			return null;
		}
	}

}
