/**
 * 
 */
package es.ubu.lsi.dao.chemistry;

import javax.persistence.EntityManager;

import es.ubu.lsi.dao.JpaDAO;
import es.ubu.lsi.model.chemistry.Moleculas;

/**
 * Clase MoleculaDao.
 * 
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

	/**
	 * Método que busca una molécula por su nombre.
	 * 
	 * @param nombre
	 *            String que representa el nombre
	 * @return Molecula buscada, null si no existe
	 */
	public Moleculas findMoleculaByNombre(String nombre) {
		try {
			return super.getEntityManager().createNamedQuery("Moleculas.findByNombre", Moleculas.class)
					.setParameter("nombre", nombre).getSingleResult();
		} catch (javax.persistence.NoResultException e) {
			return null;
		}
	}

	/**
	 * Método que busca una molécula por su fórmula.
	 * 
	 * @param nombre
	 *            String que representa la fórmula
	 * @return Molecula buscada, null si no existe
	 */
	public Moleculas findMoleculaByFormula(String formula) {
		try {
			return super.getEntityManager().createNamedQuery("Moleculas.findByFormula", Moleculas.class)
					.setParameter("formula", formula).getSingleResult();
		} catch (javax.persistence.NoResultException e) {
			return null;
		}
	}

}
