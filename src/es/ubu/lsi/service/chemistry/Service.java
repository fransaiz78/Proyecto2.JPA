package es.ubu.lsi.service.chemistry;

import es.ubu.lsi.service.PersistenceException;

/**
 * Transaction service.
 * 
 * @author Jes�s Maudes
 * @author Ra�l Marticorena
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
	 *            numero de �tomos de cada s�mbolo
	 * @throws PersistenceException
	 *             si hay cualquier error en la l�gica de persistencia
	 */
	public void insertarMolecula(String nombre, String[] simbolos, int[] numeros) throws PersistenceException;

	/**
	 * Actualizar el n�mero de �tomos de un elemento en una molecula.
	 * 
	 * @param nombreMol
	 *            nombre de la mol�cula
	 * @param simbolo
	 *            simbolo
	 * @param numero
	 *            numero de at�mos a actualizar
	 * @throws PersistenceException
	 *             si hay cualquier error en la l�gica de persistencia
	 */
	public void actualizarMolecula(String nombreMol, String simbolo, int numero) throws PersistenceException;

	/**
	 * Actualizar el n�mero de �tomos de un elemento en una molecula.
	 * 
	 * @param id
	 *            identificador de la molecula
	 * @param simbolo
	 *            simbolo
	 * @param numero
	 *            numero de at�mos a actualizar
	 * @throws PersistenceException
	 *             si hay cualquier error en la l�gica de persistencia
	 */
	public void actualizarMolecula(int id, String simbolo, int numero) throws PersistenceException;

	/**
	 * Borra una mol�cula.
	 * 
	 * @param nombre
	 *            nombre de la mol�cula
	 * @throws PersistenceException
	 *             si hay cualquier error en la l�gica de persistencia
	 */
	public void borrarMolecula(String nombre) throws PersistenceException;

	/**
	 * Borra una mol�cula.
	 * 
	 * @param idMolecula
	 *            identificador de la molecula
	 * @throws PersistenceException
	 *             si hay cualquier error en la l�gica de persistencia
	 */
	public void borrarMolecula(int idMolecula) throws PersistenceException;
}
