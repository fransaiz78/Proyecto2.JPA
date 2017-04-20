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

			// ---------------------------ComprobacionesPrevias-------------------------------------//
			// Comprobamos si ambos arrays son del mismo tamaño.
			if (simbolos.length != numeros.length) {
				rollbackTransaction(em);
				throw (new ChemistryException(ChemistryError.TAMAÑOS_INADECUADOS));
			}

			MoleculaDAO moleculasDAO = new MoleculaDAO(em);

			// Comprobamos si existe una molecula con ese nombre.
			if (moleculasDAO.findMoleculaByNombre(nombre) != null) {
				rollbackTransaction(em);
				throw (new ChemistryException(ChemistryError.NOMBRE_DE_MOLECULA_YA_EXISTENTE));
			}

			String form = "";
			int pesoTotal = 0;

			for (int i = 0; i < simbolos.length; i++) {

				ElementoDAO elementoDao = new ElementoDAO(em);
				Elemento elemento = elementoDao.findById(simbolos[i]);
				if (elemento == null) {
					rollbackTransaction(em);
					throw (new ChemistryException(ChemistryError.NO_EXISTE_ATOMO));
				}

				// Calculamos el pesoMolecular
				pesoTotal += numeros[i] * elemento.getPesoAtomico();

				// Calculamos la formula.
				form = form.concat(simbolos[i]);

				if (numeros[i] > 1) {
					form = form.concat("" + numeros[i]);
				}

			}

			// Comprobamos si existe una molecula con esa formula.
			if (moleculasDAO.findMoleculaByFormula(form) != null) {
				rollbackTransaction(em);
				throw (new ChemistryException(ChemistryError.FORMULA_YA_EXISTENTE));
			}
			// -------------------------------------------------------------------------------------------------------------------//

			// Insertamos la molecula.
			Moleculas molFinal = new Moleculas();
			// El id no lo insertamos porque automaticamente, nos coge el de la
			// secuencia.
			molFinal.setNombre(nombre);
			molFinal.setPesoMolecular(pesoTotal);
			molFinal.setFormula(form);

			for (int i = 0; i < simbolos.length; i++) {
				ComposicionPK composicionPK = new ComposicionPK(simbolos[i], 1);
				// -------------El segundo elemento que hay que pasarle, es el
				// id, o lo hacemos con los sets?
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

	@Override
	public void actualizarMolecula(String nombreMol, String simbolo, int numero) throws PersistenceException {
		EntityManager em = null;

		try {
			em = createSession();
			logger.info("Comenzando la transaccion.");
			beginTransaction(em);

			MoleculaDAO moleculasDAO = new MoleculaDAO(em);

			// sacamos la molecula correspondiente al id.
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

	@Override
	public void actualizarMolecula(int id, String simbolo, int numero) throws PersistenceException {

		EntityManager em = null;

		try {
			em = createSession();
			logger.info("Comenzando la transaccion.");
			beginTransaction(em);

			MoleculaDAO moleculaDAO = new MoleculaDAO(em);

			Boolean existe = false;

			// Comprobar si existe una molecula con los parametros recibidos.

			// Comprobamos si existe una molecula con ese id
			Moleculas molecula = moleculaDAO.findById(id);

			if (molecula != null) { // ------------------------------OPTIMIZAR----------------------------------

				// Comprobamos si la molecula tiene ese simbolo
				for (Composicion composicion : molecula.getComposicions()) {

					if (composicion.getElemento().getSimbolo() == simbolo
							&& composicion.getElemento().getPesoAtomico() == numero) {
						// Ya existe una molecula con esas propiedades
						existe = true;
					}
				}

			}

			// Si existe la molecula con ese simbolo
			if (existe) {

				// Comprobamos que excepcion lanzar.
				if (molecula != null) {
					logger.error("La molecula no contiene ese simbolo.");
					rollbackTransaction(em);
					throw (new ChemistryException(ChemistryError.MOLECULA_NO_CONTIENE_SIMBOLO));
				} else {
					logger.error("No existe molecula con ese id.");
					rollbackTransaction(em);
					throw (new ChemistryException(ChemistryError.NO_EXISTE_MOLECULA));
				}

			} else {
				// Hacemos el cambio.

				// Obtenemos la formula original.
				String formulaOriginal = molecula.getFormula();

				System.out.println("Formula Original: " + formulaOriginal);

				// Actualizamos el numero de atomos en composicion.
				for (Composicion composicion : molecula.getComposicions()) {
					if (composicion.getElemento().getSimbolo().equals(simbolo)) {
						System.out.println("Actualizamos en composicion el numero de atomos");
						composicion.setNroAtomos(numero);
					}
				}

				// Calculamos el nuevo peso y la nueva formula.
				int pesoAtomNuevo = 0;
				String formulaNueva = "";

				for (Composicion composicion : molecula.getComposicions()) {
					pesoAtomNuevo += composicion.getNroAtomos() * composicion.getElemento().getPesoAtomico();
					formulaNueva = formulaNueva.concat(composicion.getElemento().getSimbolo());
					if (composicion.getNroAtomos() > 1) {
						formulaNueva = formulaNueva.concat(String.valueOf(composicion.getNroAtomos()));
					}
				}

				// Comprobar que la formula nueva y la anterior son diferentes:
				if (formulaNueva != formulaOriginal) {
					molecula.setFormula(formulaNueva);
					molecula.setPesoMolecular(pesoAtomNuevo);
				} else {
					logger.error("La molecula no contiene ese simbolo.");
					rollbackTransaction(em);
					throw (new ChemistryException(ChemistryError.FORMULA_YA_EXISTENTE));
				}

				molecula.setFormula(formulaNueva);
				molecula.setPesoMolecular(pesoAtomNuevo);

				em.persist(molecula);

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
	public void borrarMolecula(String nombre) throws PersistenceException {

		EntityManager em = null;

		try {
			em = createSession();
			logger.info("Comenzando la transaccion.");
			beginTransaction(em);

			MoleculaDAO moleculasDAO = new MoleculaDAO(em);

			// sacamos la molecula correspondiente al id.
			Moleculas molecula = moleculasDAO.findMoleculaByNombre(nombre);

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
