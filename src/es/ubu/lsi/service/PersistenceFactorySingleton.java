package es.ubu.lsi.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Singleton to obtain entity managers.
 * 
 * @author Jesús Maudes
 * @author Raúl Marticorena
 * @since 1.0
 */
public class PersistenceFactorySingleton {

	/** 
	 * Name of the persistence-unit.
	 */
	// WARNING: review with your current persistence.xml
	private static final String PERSISTENCE_CONTEXT_NAME = "Chemistry";

	/** Singleton variable. */
	private EntityManagerFactory emf;
	
	/** Singleton instance. */
	private static PersistenceFactorySingleton singleton = new PersistenceFactorySingleton();

	/** Constructor. */
	private PersistenceFactorySingleton() {
		emf = Persistence.createEntityManagerFactory(PERSISTENCE_CONTEXT_NAME);
	}

	/**
	 * Gets a new entity manager.
	 * 
	 * @return entity manager
	 */
	public static EntityManager getEntityManager() {
		EntityManager em = null;
		synchronized (singleton) {
			em = singleton.emf.createEntityManager();
		}
		return em;
	}

	/**
	 * Closes the entity manager factory if it is open.
	 */
	public static void close() {
		if (singleton.emf.isOpen()) {
			singleton.emf.close();
		}
	}
}
