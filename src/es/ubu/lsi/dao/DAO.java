package es.ubu.lsi.dao;

/**
 * DAO.
 * 
 * @param <E> entity type
 * @param <K> key type
 * @author Jesús Maudes
 * @author Raúl Marticorena
 * @since 1.0
 */
public interface DAO<E,K> {
	/** 
	 * Persist. 
	 *  
	 * @param entity entity
	 */
	void persist(E entity);

	/**
	 * Remove.
	 * 
	 * @param entity entity
	 */
	void remove(E entity);
	
	/**
	 * Find by primary key.
	 * 
	 * @param id value
	 * @return entity
	 */
	E findById(K id);
}