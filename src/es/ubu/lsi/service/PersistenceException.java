package es.ubu.lsi.service;

/**
 * Persistence exception.
 * 
 * @author Jesús Maudes
 * @author Raúl Marticorena
 * @since 1.0
 *
 */
public class PersistenceException extends Exception {

	/** Default. */
	private static final long serialVersionUID = 1L;

	/** 
	 * Constructor. 
	 */
	public PersistenceException() {
		super();
	}
	
	/** 
	 * Constructor. 
	 * 
	 * @param text text 
	 */
	public PersistenceException(String text) {
		super(text);
	}
	
	/** 
	 * Constructor. 
	 * 
	 * @param ex cause
	 */
	public PersistenceException(Throwable ex) {
		super(ex);
	}
	
	/** 
	 * Constructor.
	 * 
	 * @param text text
	 * @param ex cause
	 */
	public PersistenceException(String text, Throwable ex) {
		super(text, ex);
	}
	
}
