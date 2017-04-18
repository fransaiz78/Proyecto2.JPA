package es.ubu.lsi.service.chemistry;

import es.ubu.lsi.service.PersistenceException;

/**
 * Transaction service.
 * 
 * @author Jesús Maudes
 * @author Raúl Marticorena
 * @since 1.0
 */
public interface Service {

	/**
	 * Inserta una molecula.
	 * 
	 * @param nombre
	 *            nombre
	 * @param simbolos
	 *            simbolos
	 * @param numeros
	 *            numero de átomos de cada símbolo
	 * @throws PersistenceException
	 *             si hay cualquier error en la lógica de persistencia
	 */
	public void insertarMolecula(String nombre, String[] simbolos, int[] numeros) throws PersistenceException;

	/**
	 * Actualizar el número de átomos de un elemento en una molecula.
	 * 
	 * @param nombreMol
	 *            nombre de la molécula
	 * @param simbolo
	 *            simbolo
	 * @param numero
	 *            numero de atómos a actualizar
	 * @throws PersistenceException
	 *             si hay cualquier error en la lógica de persistencia
	 */
	public void actualizarMolecula(String nombreMol, String simbolo, int numero) throws PersistenceException;

	/**
	 * Actualizar el número de átomos de un elemento en una molecula.
	 * 
	 * @param id
	 *            identificador de la molecula
	 * @param simbolo
	 *            simbolo
	 * @param numero
	 *            numero de atómos a actualizar
	 * @throws PersistenceException
	 *             si hay cualquier error en la lógica de persistencia
	 */
	public void actualizarMolecula(int id, String simbolo, int numero) throws PersistenceException;

	/**
	 * Borra una molécula.
	 * 
	 * @param nombre
	 *            nombre de la molécula
	 * @throws PersistenceException
	 *             si hay cualquier error en la lógica de persistencia
	 */
	public void borrarMolecula(String nombre) throws PersistenceException;

	/**
	 * Borra una molécula.
	 * 
	 * @param idMolecula
	 *            identificador de la molecula
	 * @throws PersistenceException
	 *             si hay cualquier error en la lógica de persistencia
	 */
	public void borrarMolecula(int idMolecula) throws PersistenceException;
}
