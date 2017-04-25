/**
 * 
 */
package es.ubu.lsi.service.chemistry;

import java.util.ArrayList;
import java.util.Arrays;
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
 * Clase ServiceImpl.
 * 
 * @author Mario Santamaria
 * @author Francisco Saiz
 *
 */
public class ServiceImpl extends PersistenceService implements Service {

	// Inicializacion del Logger
	private static Logger logger = LoggerFactory.getLogger(ServiceImpl.class);

	/**
	 * Método que inserta una molecula conociendo su nombre y su composición.
	 * 
	 * @param nombre
	 *            String que representa el nombre
	 * @param simbolos
	 *            Array de String que contiene los simbolos
	 * @param numeros
	 *            Array de int que contiene los nros
	 * @throws ChemistryException
	 *             Excepcion
	 * @throws PersistenceException
	 *             Excepcion
	 */
	
	@Override
	public void insertarMolecula(String nombre, String[] simbolos, int[] numeros) throws PersistenceException {

		EntityManager em = null;

		try {
			em = createSession();
			logger.info("Comenzando la transaccion.");
			beginTransaction(em);

			// ---------------------------ComprobacionesPrevias-------------------------------------//
			// Se comprueba si ambos arrays son del mismo tamaño.
			if (simbolos.length != numeros.length) {
				rollbackTransaction(em);
				throw (new ChemistryException(ChemistryError.TAMAÑOS_INADECUADOS));
			}

			MoleculaDAO moleculasDAO = new MoleculaDAO(em);

			// Se comprueba si existe una molecula con ese nombre.
			if (moleculasDAO.findMoleculaByNombre(nombre) != null) {
				rollbackTransaction(em);
				throw (new ChemistryException(ChemistryError.NOMBRE_DE_MOLECULA_YA_EXISTENTE));
			}

			String[] formula = new String[simbolos.length];
			int pesoTotal = 0;

			for (int i = 0; i < simbolos.length; i++) {

				ElementoDAO elementoDao = new ElementoDAO(em);
				Elemento elemento = elementoDao.findById(simbolos[i]);
				if (elemento == null) {
					rollbackTransaction(em);
					throw (new ChemistryException(ChemistryError.NO_EXISTE_ATOMO));
				}

				// Se calcula el pesoMolecular
				pesoTotal += numeros[i] * elemento.getPesoAtomico();
				
				// Se ordenan alfabeticamente los simbolos en la formula
				if (numeros[i] != 1) {
					formula[i] = simbolos[i] + numeros[i];
				} else {
					formula[i] = simbolos[i];
				}

			}
			
			Arrays.sort(formula);
			String formOrdenada = "";
			for(int i = 0; i < formula.length; i++){
				formOrdenada += formula[i];
			}

			// Se comprueba si existe una molecula con esa formula.
			if (moleculasDAO.findMoleculaByFormula(formOrdenada) != null) {
				rollbackTransaction(em);
				throw (new ChemistryException(ChemistryError.FORMULA_YA_EXISTENTE));
			}
			// -------------------------------------------------------------------------------------------------------------------//

			// Se inserta la molecula.
			Moleculas molFinal = new Moleculas();

			molFinal.setNombre(nombre);
			molFinal.setPesoMolecular(pesoTotal);
			molFinal.setFormula(formOrdenada);

			for (int i = 0; i < simbolos.length; i++) {
				
				ComposicionPK composicionPK = new ComposicionPK(simbolos[i], molFinal.getId());
				
				Composicion composicion = new Composicion();

				ElementoDAO elementoDao = new ElementoDAO(em);
				Elemento elemento = elementoDao.findById(simbolos[i]);

				composicion.setId(composicionPK);
				composicion.setElemento(elemento);
				composicion.setMolecula(molFinal);
				composicion.setNroAtomos(numeros[i]);

				molFinal.addComposicion(composicion);
			}

			em.persist(molFinal);

			for (Composicion composicion : molFinal.getComposicions()) {
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
	

	/**
	 * Actualiza la información de una molecula mediante el nombre.
	 * 
	 * @param nombreMol
	 *            String que representa el nombre de la molecula
	 * @param simbolo
	 *            String que representa el simbolo
	 * @param numero
	 *            Entero que representa el nro
	 * @throws PersistenceException
	 *             Excepcion
	 */
	@Override
	public void actualizarMolecula(String nombreMol, String simbolo, int numero) throws PersistenceException {
		EntityManager em = null;

		try {
			em = createSession();
			logger.info("Comenzando la transaccion.");
			beginTransaction(em);

			MoleculaDAO moleculasDAO = new MoleculaDAO(em);

			// Se identifica la molecula correspondiente al id.
			Moleculas molecula = moleculasDAO.findMoleculaByNombre(nombreMol);

			int id = molecula.getId();

			actualizarMolecula(id, simbolo, numero);

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

	/**
	 * Actualiza la información de una molecula mediante el ID.
	 * 
	 * @param id
	 *            Entero que representa el id
	 * @param simbolo
	 *            String que representa el simbolo
	 * @param numero
	 *            Entero que representa el nro
	 * 
	 * @throws PersistenceException
	 *             Excepcion
	 */
	@Override
	public void actualizarMolecula(int id, String simbolo, int numero) throws PersistenceException {

		EntityManager em = null;

		try {
			em = createSession();
			logger.info("Comenzando la transaccion.");
			beginTransaction(em);

			MoleculaDAO moleculaDAO = new MoleculaDAO(em);

			Boolean existe = false;
			Boolean contiene = false;

			// Se comprueba si existe una molecula con los parametros recibidos.

			// Se comprueba si existe una molecula con ese id
			Moleculas molecula = moleculaDAO.findById(id);

			if (molecula != null) { // ------------------------------OPTIMIZAR----------------------------------

				// Se comprueba si la molecula tiene ese simbolo
				for (Composicion composicion : molecula.getComposicions()) {

					if (composicion.getElemento().getSimbolo().equals(simbolo)) {
						contiene = true;
						if (composicion.getNroAtomos() == numero) {
							existe = true;
							logger.error("La formula actual es la misma que la inicial.");
							rollbackTransaction(em);
							throw (new ChemistryException(ChemistryError.MOLECULA_YA_EXISTENTE));
						}
					}

				}

			} else {
				logger.error("No existe molecula con ese id.");
				rollbackTransaction(em);
				throw (new ChemistryException(ChemistryError.NO_EXISTE_MOLECULA));
			}

			// Si existe la molecula con ese simbolo
			if (contiene == false) {

				logger.error("La molecula no contiene ese simbolo.");
				rollbackTransaction(em);
				throw (new ChemistryException(ChemistryError.MOLECULA_NO_CONTIENE_SIMBOLO));

			} else {
				// Se realiza el cambio.

				// Se obtiene la formula original.
				String formulaOriginal = molecula.getFormula();

				// Se actualiza el numero de atomos en composicion.
				for (Composicion composicion : molecula.getComposicions()) {
					if (composicion.getElemento().getSimbolo().equals(simbolo)) {
						composicion.setNroAtomos(numero);
					}
				}

				// Se calcula el nuevo peso y la nueva formula.
				int pesoAtomNuevo = 0;
				String formulaNueva = "";

				for (Composicion composicion : molecula.getComposicions()) {
					pesoAtomNuevo += composicion.getNroAtomos() * composicion.getElemento().getPesoAtomico();
					formulaNueva = formulaNueva.concat(composicion.getElemento().getSimbolo());
					if (composicion.getNroAtomos() > 1) {
						formulaNueva = formulaNueva.concat(String.valueOf(composicion.getNroAtomos()));
					}
				}

				// Se comprueba que la formula nueva y la anterior son diferentes
				if (formulaNueva != formulaOriginal) {
					molecula.setFormula(formulaNueva);
					molecula.setPesoMolecular(pesoAtomNuevo);
				} else {
					logger.error("La formula actual es la misma que la inicial.");
					rollbackTransaction(em);
					throw (new ChemistryException(ChemistryError.FORMULA_YA_EXISTENTE));
				}

				molecula.setFormula(formulaNueva);
				molecula.setPesoMolecular(pesoAtomNuevo);

				em.persist(molecula);

				logger.info("Transaccion correcta. Realizando commit.");
				commitTransaction(em);
			}

		} catch (

		EntityExistsException e) {
			logger.error("La molecula ya existe.");
			rollbackTransaction(em);
			throw (new ChemistryException(ChemistryError.MOLECULA_YA_EXISTENTE));

		} finally {
			// Cerrando recursos
			logger.info("Cerrando recursos.");
			close(em);
		}
	}

	/**
	 * Borra una molecula mediante el nombre.
	 * 
	 * @param nombre
	 *            String que representa el nombre
	 * @throws PersistenceException
	 *             Excepcion
	 */
	@Override
	public void borrarMolecula(String nombre) throws PersistenceException {

		EntityManager em = null;

		try {
			em = createSession();
			logger.info("Comenzando la transaccion.");
			beginTransaction(em);

			MoleculaDAO moleculasDAO = new MoleculaDAO(em);

			// Se obtiene la molecula correspondiente al id.
			Moleculas molecula = moleculasDAO.findMoleculaByNombre(nombre);

			if (molecula == null) {
				logger.error("No existe la molecula.");
				rollbackTransaction(em);
				throw (new ChemistryException(ChemistryError.NO_EXISTE_MOLECULA));
			}

			int id = molecula.getId();

			borrarMolecula(id);

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

	/**
	 * Borra una molecula mediante el id.
	 * 
	 * @param idMolecula
	 *            Entero que representa el id
	 * @throws PersistenceException
	 *             Excepcion
	 */
	@Override
	public void borrarMolecula(int idMolecula) throws PersistenceException {

		EntityManager em = null;

		try {
			em = createSession();
			logger.info("Comenzando la transaccion.");
			beginTransaction(em);

			MoleculaDAO moleculasDAO = new MoleculaDAO(em);
			ComposicionDAO composicionDAO = new ComposicionDAO(em);

			// Se obtiene la molecula correspondiente al id.
			Moleculas molecula = moleculasDAO.findById(idMolecula);

			// Se comprueba que la molecula correspondiente a ese id es == null
			if (molecula == null) {
				logger.error("La molecula no existe. Realizando rollback...");
				rollbackTransaction(em);
				throw (new ChemistryException(ChemistryError.NO_EXISTE_MOLECULA));
			} else {
				List<Composicion> lista = new ArrayList<Composicion>();

				// Se obtiene las composiciones asociadas a la molecula.
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
