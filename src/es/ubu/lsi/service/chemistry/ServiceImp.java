/**
 * 
 */
package es.ubu.lsi.service.chemistry;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ubu.lsi.dao.chemistry.ComposicionDAO;
import es.ubu.lsi.dao.chemistry.MoleculaDAO;
import es.ubu.lsi.model.chemistry.Composicion;
import es.ubu.lsi.model.chemistry.Moleculas;
import es.ubu.lsi.service.PersistenceException;
import es.ubu.lsi.service.PersistenceService;

/**
 * @author Mario Santamaria
 * @author Francisco Saiz
 *
 */
public class ServiceImp extends PersistenceService implements Service {

	// Inicializacion del Logger
	private static Logger logger = LoggerFactory.getLogger(ServiceImp.class);

	@Override
	public void insertarMolecula(String nombre, String[] simbolos, int[] numeros) throws PersistenceException {
		
	}

	@Override
	public void actualizarMolecula(String nombreMol, String simbolo, int numero) throws PersistenceException {

	}

	@Override
	public void actualizarMolecula(int id, String simbolo, int numero) throws PersistenceException {
		// TODO Auto-generated method stub

	}

	@Override
	public void borrarMolecula(String nombre) throws PersistenceException {
		// TODO Auto-generated method stub

	}

	@Override
	public void borrarMolecula(int idMolecula) throws PersistenceException {

		EntityManager em = null;
//		EntityManager em = createSession();

//		System.out.println("Vamos a borrar la molecula. ");
		try {
			em = createSession();
			System.out.println("Furrula.");
			logger.info("Comenzando la transaccion.");
			beginTransaction(em);

			// Comprobamos que el idMolecula corresponda a una molecula.
			Moleculas molecula = null;
//			Composicion composicion = null;
			MoleculaDAO moleculasDAO = new MoleculaDAO(em);
//			ComposicionDAO composicionDAO = new ComposicionDAO(em);

			//sacamos la molecula correspondiente al id. 
			molecula = moleculasDAO.findById(idMolecula);
			
//			composicion = (Composicion) composicionDAO.findById(idMolecula);
			
			// moleculaComp = composicionDAO.findById(idMolecula);
			
			// Comprobamos que la molecula correspondiente a ese id es == null
			if (molecula == null) {
//				System.out.println("Esa molecula no existe. \n");
				logger.error("La molecula no existe. Realizando rollback...");
				rollbackTransaction(em);
				throw (new ChemistryException(ChemistryError.NO_EXISTE_MOLECULA));
			} else {
				logger.info("Comenzando la transaccion.");

				moleculasDAO.remove(molecula);
//				composicionDAO.remove(composicion);
//				moleculasDAO.persist(molecula);
				
				//No se eliminarlo de composicion.
//				composicionDAO.remove(molecula);
				

				logger.info("Transaccion correcta. Realizando commit.");
				commitTransaction(em);
			}

		} catch (EntityExistsException e) {
			System.out.println("Casca");
			// En el caso de que la molecula ya existe
			logger.error("La molecula ya existe.");
			rollbackTransaction(em);
			throw (new ChemistryException(ChemistryError.MOLECULA_YA_EXISTENTE));

		} finally {
			// Cerrando recursos
			logger.info("Cerrando recursos.");
			close(em);
		}
	}

}
