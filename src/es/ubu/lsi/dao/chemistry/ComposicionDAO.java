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
 * @author Mario Santamaria
 * @author Francisco Saiz
 *
 */
public class ComposicionDAO extends JpaDAO<Composicion, ComposicionPK> {

	public ComposicionDAO(EntityManager em) {
		super(em);
		// TODO Auto-generated constructor stub
	}

	public Object findById(int idMolecula) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Composicion findMoleculaByNombre(Integer idMolecula) {
		try {
			return super.getEntityManager().createNamedQuery("Composicion.findByIdMolecula", Composicion.class)
					.setParameter("idMolecula", idMolecula).getSingleResult();
		} catch (javax.persistence.NoResultException e) {
			return null;
		}
	}
}

