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

//	@Override
//	public void insertarMolecula(String nombre, String[] simbolos, int[] numeros) throws PersistenceException {
//
//		EntityManager em = null;
//
//		try {
//			em = createSession();
//			logger.info("Comenzando la transaccion.");
//			beginTransaction(em);
//
//			// ---------------------------ComprobacionesPrevias-------------------------------------//
//			// Comprobamos si ambos arrays son del mismo tamaño.
//			if (simbolos.length != numeros.length) {
//				rollbackTransaction(em);
//				throw (new ChemistryException(ChemistryError.TAMAÑOS_INADECUADOS));
//			}
//
//			MoleculaDAO moleculasDAO = new MoleculaDAO(em);
//
//			// Comprobamos si existe una molecula con ese nombre.
//			if (moleculasDAO.findMoleculaByNombre(nombre) != null) {
//				rollbackTransaction(em);
//				throw (new ChemistryException(ChemistryError.NOMBRE_DE_MOLECULA_YA_EXISTENTE));
//			}
//
//			String form = "";
//			int pesoTotal = 0;
//
//			for (int i = 0; i < simbolos.length; i++) {
//
//				ElementoDAO elementoDao = new ElementoDAO(em);
//				Elemento elemento = elementoDao.findById(simbolos[i]);
//				if (elemento == null) {
//					rollbackTransaction(em);
//					throw (new ChemistryException(ChemistryError.NO_EXISTE_ATOMO));
//				}
//
//				// Calculamos el pesoMolecular
//				pesoTotal += numeros[i] * elemento.getPesoAtomico();
//
//				// Calculamos la formula.
//				form = form.concat(simbolos[i]);
//
//				if (numeros[i] > 1) {
//					form = form.concat("" + numeros[i]);
//				}
//
//			}
//
//			// Comprobamos si existe una molecula con esa formula.
//			if (moleculasDAO.findMoleculaByFormula(form) != null) {
//				rollbackTransaction(em);
//				throw (new ChemistryException(ChemistryError.FORMULA_YA_EXISTENTE));
//			}
//			// -------------------------------------------------------------------------------------------------------------------//
//
//			// Insertamos la molecula.
//			Moleculas molFinal = new Moleculas();
//			// El id no lo insertamos porque automaticamente, nos coge el de la
//			// secuencia.
//			molFinal.setNombre(nombre);
//			molFinal.setPesoMolecular(pesoTotal);
//			molFinal.setFormula(form);
//
//			for (int i = 0; i < simbolos.length; i++) {
//				
//				ComposicionPK composicionPK = new ComposicionPK(simbolos[i], molFinal.getId());
//				
//				Composicion composicion = new Composicion();
//
//				ElementoDAO elementoDao = new ElementoDAO(em);
//				Elemento elemento = elementoDao.findById(simbolos[i]);
//
//				composicion.setId(composicionPK);
//				composicion.setElemento(elemento);
//				composicion.setMolecula(molFinal);
//				composicion.setNroAtomos(numeros[i]);
//
//				molFinal.addComposicion(composicion);
//			}
//
//			em.persist(molFinal);
//
//			for (Composicion composicion : molFinal.getComposicions()) {
//				em.persist(composicion);
//			}
//
//			logger.info("Transaccion correcta. Realizando commit.");
//			commitTransaction(em);
//
//		} catch (EntityExistsException e) {
//			logger.error("La molecula ya existe.");
//			rollbackTransaction(em);
//			throw (new ChemistryException(ChemistryError.MOLECULA_YA_EXISTENTE));
//
//		} finally {
//			// Cerrando recursos
//			logger.info("Cerrando recursos.");
//			close(em);
//		}
//
//	}
	
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
	public void insertarMolecula(String nombre, String[] simbolos, int[] numeros)
			throws PersistenceException, ChemistryException {
		EntityManager em = null;

		// Se comprueba que el numero de simbolos y de numeros son iguales
		if (simbolos.length != numeros.length) {
			logger.error("El numero de simbolos y numeros no coincide. Realizando rollback.");
			throw (new ChemistryException(ChemistryError.TAMAÑOS_INADECUADOS));
		}

		try {
			em = createSession();
			logger.info("Comenzando la transaccion.");
			beginTransaction(em);

			// Se comprueba si la molecula ya existe en la tabla de moleculas
			MoleculaDAO moleculaDAO = new MoleculaDAO(em);
			if (moleculaDAO.findMoleculaByNombre(nombre) != null) {
				logger.error("El nombre de la molecula ya existe. Realizando rollback.");
				rollbackTransaction(em);
				throw (new ChemistryException(ChemistryError.NOMBRE_DE_MOLECULA_YA_EXISTENTE));
			}

			ElementoDAO elementoDAO = new ElementoDAO(em);

			// Se calcula el peso molecular y la formula
			int pesoMolecular = 0;
			String[] formula = new String[simbolos.length];
			Elemento elem;
			for (int i = 0; i < simbolos.length; i++) {
				elem = elementoDAO.findById(simbolos[i]);
				// Se comprueba que el atomo esta en la tabla de elementos
				if (elem != null) {
					pesoMolecular += elem.getPesoAtomico() * numeros[i];

					// TODO ordenar alfabeticamente los simbolos en la formula
					if (numeros[i] == 1) {
						formula[i] = simbolos[i];
					} else {
						formula[i] = simbolos[i] + numeros[i];
					}
				} else {
					logger.error("El atomo no existe en la tabla de elementos. Realizando rollback.");
					rollbackTransaction(em);
					throw (new ChemistryException(ChemistryError.NO_EXISTE_ATOMO));
				}
			}
			
			Arrays.sort(formula);
			String formulaOrdenada = "";
			for(int i = 0; i < formula.length; i++){
				formulaOrdenada += formula[i];
			}

			// Se comprueba si ya hay alguna molecula con esa formula en la tabla
			// de moleculas
			if (moleculaDAO.findMoleculaByFormula(formulaOrdenada) != null) {
				logger.error("La formula ya existe en la tabla de moleculas. Realizando rollback.");
				rollbackTransaction(em);
				throw (new ChemistryException(ChemistryError.MOLECULA_YA_EXISTENTE));
			}

			// Se crea la nueva molecula
			Moleculas molecula = new Moleculas();
			molecula.setFormula(formulaOrdenada);
			molecula.setNombre(nombre);
			molecula.setPesoMolecular(pesoMolecular);
			
			// Se crean los nuevos elementos de la tabla composicion
			for (int i = 0; i < simbolos.length; i++) {
				Composicion comp = new Composicion();
				elem = elementoDAO.findById(simbolos[i]);
//				comp.setId(new ComposicionPK(simbolos[i], molecula.getId()));
				comp.setId(new ComposicionPK(simbolos[i], 1));
				comp.setNroAtomos(numeros[i]);
				comp.setMolecula(molecula);
				comp.setElemento(elem);
				
				// Se asocia la composicion con la molecula
				molecula.addComposicion(comp);
			}
			
			em.persist(molecula);
			
			for(Composicion composicion : molecula.getComposicions()){
				em.persist(composicion);
			}

			logger.info("Transaccion correcta. Realizando commit.");
			commitTransaction(em);

		} catch (EntityExistsException e) {
			// En el caso de molecula ya existente
			logger.error("La molecula ya existe. Realizando rollback.");
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
