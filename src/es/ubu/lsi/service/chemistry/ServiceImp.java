/**
 * 
 */
package es.ubu.lsi.service.chemistry;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ubu.lsi.dao.chemistry.ComposicionDAO;
import es.ubu.lsi.dao.chemistry.ElementoDAO;
import es.ubu.lsi.dao.chemistry.MoleculaDAO;
import es.ubu.lsi.model.chemistry.Composicion;
import es.ubu.lsi.model.chemistry.ComposicionPK;
import es.ubu.lsi.model.chemistry.Elemento;
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

		EntityManager em = null;

		try {
			em = createSession();
			logger.info("Comenzando la transaccion.");
			beginTransaction(em);
			
			//---------------------------ComprobacionesPrevias-------------------------------------//
			// Comprobamos si ambos arrays son del mismo tamaño.
			if (simbolos.length != numeros.length) {
				rollbackTransaction(em);
				throw (new ChemistryException(ChemistryError.TAMAÑOS_INADECUADOS));
			}
			
			MoleculaDAO moleculasDAO = new MoleculaDAO(em);

			//Comprobamos si existe una molecula con ese nombre.
			if(moleculasDAO.findMoleculaByNombre(nombre) != null){
				rollbackTransaction(em);
				throw (new ChemistryException(ChemistryError.NOMBRE_DE_MOLECULA_YA_EXISTENTE));
			}
			
			String form = "";
			int pesoTotal = 0;
			
			for (int i = 0; i < simbolos.length; i++) {
	
				ElementoDAO elementoDao = new ElementoDAO(em);
				Elemento elemento = elementoDao.findById(simbolos[i]);
				if(elemento == null){
					rollbackTransaction(em);
					throw (new ChemistryException(ChemistryError.NO_EXISTE_ATOMO));
				}
				
				//Calculamos el pesoMolecular
				pesoTotal += numeros[i] * elemento.getPesoAtomico();
				
				//Calculamos la formula.
				form = form.concat(simbolos[i]);
				
				if (numeros[i] > 1) {
					form = form.concat("" + numeros[i]);
				}
				
			}
			
			//Comprobamos si existe una molecula con esa formula.
			if(moleculasDAO.findMoleculaByFormula(form) != null){
				rollbackTransaction(em);
				throw (new ChemistryException(ChemistryError.FORMULA_YA_EXISTENTE));
			}
			//---------------------------------------------------------------------------------------//
			
			//Insertamos la molecula.
			Moleculas molFinal = new Moleculas();
//			molFinal.setId(1);
			molFinal.setNombre(nombre);
			molFinal.setPesoMolecular(pesoTotal);
			molFinal.setFormula(form);
			
//			em.persist(molFinal);
	
//			List<Composicion> listaComposiciones = new ArrayList<Composicion>();
			
			for (int i = 0; i < simbolos.length; i++) {
				ComposicionPK composicionPK = new ComposicionPK(simbolos[i], 1);
				Composicion composicion = new Composicion();
				
				ElementoDAO elementoDao = new ElementoDAO(em);
				Elemento elemento = elementoDao.findById(simbolos[i]);
				
				composicion.setId(composicionPK);
				composicion.setElemento(elemento);
				composicion.setMolecula(molFinal);
				composicion.setNroAtomos(numeros[i]);
								
//				listaComposiciones.add(composicion);
				molFinal.addComposicion(composicion);
			}
			
//			molFinal.setComposicions(listaComposiciones);
			
			em.persist(molFinal);
			
			for (Composicion composicion : molFinal.getComposicions()){
				em.persist(composicion);
			}
						
			logger.info("Transaccion correcta. Realizando commit.");
			commitTransaction(em);
			
		} catch (EntityExistsException e) {
			logger.error("La molecula ya existe.");
			rollbackTransaction(em);
			throw (new ChemistryException(ChemistryError.MOLECULA_YA_EXISTENTE));

		} finally {
			// Cerrando recursos
			logger.info("Cerrando recursos.");
			close(em);
		}

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

		EntityManager em = null;

		try {
			em = createSession();
			logger.info("Comenzando la transaccion.");
			beginTransaction(em);

			MoleculaDAO moleculasDAO = new MoleculaDAO(em);
			ComposicionDAO composicionDAO = new ComposicionDAO(em);

			// sacamos la molecula correspondiente al id.
			Moleculas molecula = moleculasDAO.findMoleculaByNombre(nombre);

			// Comprobamos que la molecula correspondiente a ese id es == null
			if (molecula == null) {
				logger.error("La molecula no existe. Realizando rollback...");
				rollbackTransaction(em);
				throw (new ChemistryException(ChemistryError.NO_EXISTE_MOLECULA));
			} else {
				List<Composicion> lista = new ArrayList<Composicion>();

				// Obtenemos las composiciones asociadas a la molecula.
				lista = molecula.getComposicions();

				for (Composicion elem : lista) {
					composicionDAO.remove(elem);
				}

				moleculasDAO.remove(molecula);

				logger.info("Transaccion correcta. Realizando commit.");
				commitTransaction(em);
			}

		} catch (EntityExistsException e) {
			logger.error("La molecula ya existe.");
			rollbackTransaction(em);
			throw (new ChemistryException(ChemistryError.MOLECULA_YA_EXISTENTE));

		} finally {
			// Cerrando recursos
			logger.info("Cerrando recursos.");
			close(em);
		}

	}

	@Override
	public void borrarMolecula(int idMolecula) throws PersistenceException {

		EntityManager em = null;

		try {
			em = createSession();
			logger.info("Comenzando la transaccion.");
			beginTransaction(em);

			MoleculaDAO moleculasDAO = new MoleculaDAO(em);
			ComposicionDAO composicionDAO = new ComposicionDAO(em);

			// sacamos la molecula correspondiente al id.
			Moleculas molecula = moleculasDAO.findById(idMolecula);

			// Comprobamos que la molecula correspondiente a ese id es == null
			if (molecula == null) {
				logger.error("La molecula no existe. Realizando rollback...");
				rollbackTransaction(em);
				throw (new ChemistryException(ChemistryError.NO_EXISTE_MOLECULA));
			} else {
				List<Composicion> lista = new ArrayList<Composicion>();

				// Obtenemos las composiciones asociadas a la molecula.
				lista = molecula.getComposicions();

				for (Composicion elem : lista) {
					composicionDAO.remove(elem);
				}

				moleculasDAO.remove(molecula);

				logger.info("Transaccion correcta. Realizando commit.");
				commitTransaction(em);
			}

		} catch (EntityExistsException e) {
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
