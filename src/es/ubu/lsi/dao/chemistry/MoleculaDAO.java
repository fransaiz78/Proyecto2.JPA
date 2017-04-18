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

	public Molecula findMoleculaByNombre(String nombre) {
		try {
			return super.getEntityManager().createNamedQuery("molecula.findByNombre", Molecula.class)
					.setParameter("nombre", nombre).getSingleResult();
		} catch (javax.persistence.NoResultException e) {
			return null;
		}
	}

	public Molecula findMoleculaByFormula(String formula) {
		try {
			return super.getEntityManager().createNamedQuery("molecula.findByFormula", Molecula.class)
					.setParameter("formula", formula).getSingleResult();
		} catch (javax.persistence.NoResultException e) {
			return null;
		}
	}

}
