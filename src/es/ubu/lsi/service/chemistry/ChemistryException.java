package es.ubu.lsi.service.chemistry;

import es.ubu.lsi.service.PersistenceException;

/**
 * Tipo error en transacci�n de formulas moleculares.
 * 
 * @author <a href="mailto:jmaudes@ubu.es">Jes�s Maudes</a>
 * @author <a href="mailto:rmartico@ubu.es">Ra�l Marticorena</a>
 * @version 1.1 201602280938
 * @since 1.0
 */
@SuppressWarnings("serial")
public class ChemistryException extends PersistenceException {

	/** Error. */
	private ChemistryError error;
	
	/**
	 * Constructor.
	 * 
	 * @param texto texto descriptivo
	 */
	public ChemistryException(String texto) {
		super(texto);
	}

	/**
	 * Constructor.
	 * 
	 * @param error error
	 */
	public ChemistryException(ChemistryError error) {
		this.error = error;
	}

	/**
	 * Constructor.
	 * 
	 * @param error
	 *            error
	 * @param ex
	 *            si la excepci�n ha sido originada por otra excepci�n
	 */
	public ChemistryException(ChemistryError error, Exception ex) {
		super(ex);
		this.error = error;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param error
	 *            error
	 * @param texto
	 *            texto descriptivo
	 * @param ex
	 * 			  si la excepci�n ha sido originada por otra excepci�n
	 */
	public ChemistryException(ChemistryError error, String texto, Exception ex) {
		super(texto, ex);
		this.error = error;
	}

	/**
	 * Constructor.
	 * 
	 * @param error
	 *            error
	 * @param texto
	 *            texto descriptivo
	 */
	public ChemistryException(ChemistryError error, String texto) {
		super(texto);
		this.error = error;
	}

	/**
	 * Obtiene el tipo de error que genera la excepci�n.
	 * 
	 * @return error
	 */
	public ChemistryError getError() {
		return error;
	}

	/**
	 * Obtiene el texto del error generado.
	 * 
	 * @return texto texto descriptivo del error
	 */
	@Override
	public String getMessage() {
		if (error != null)
			return error.getText() + super.getMessage();
		return getMessage();
	}
}
