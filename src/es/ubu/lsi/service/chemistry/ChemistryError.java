package es.ubu.lsi.service.chemistry;

/**
 * Errores codificados para la transacci�n de formulas moleculares.
 * 
 * @author <a href="mailto:jmaudes@ubu.es">Jes�s Maudes</a>
 * @author <a href="mailto:rmartico@ubu.es">Ra�l Marticorena</a>
 * @version 1.0
 * @since 1.0
 */
public enum ChemistryError {
	TAMA�OS_INADECUADOS("La lista de s�mbolos y n�meros no coincide en tama�o"),
	NOMBRE_DE_MOLECULA_YA_EXISTENTE("El nombre de la mol�cula ya existe"),
	NO_EXISTE_ATOMO("Alguno de los atomos de la mol�cula no existe"),
	NO_EXISTE_MOLECULA("No existe dicha mol�cula"),
	MOLECULA_NO_CONTIENE_SIMBOLO("La mol�cula especificada no tiene ese s�mbolo"),
	MOLECULA_YA_EXISTENTE("Existe una mol�cula con la misma f�rmula"),	
	SQL_ERROR("Error SQL no determinado "),
	
	//Creada por los alumnos para un mejor funcionamiento.
	FORMULA_YA_EXISTENTE("Ya existe una mol�cula con esa f�rmula");
	
	/** Texto. */
	private String text;
	
	/** 
	 * Constructor.
	 * 
	 * @param texto texto con el mensaje de error generado
	 */
	private ChemistryError(String text) {
		this.text = text;
	}	
	
	/**
	 * Consulta el texto.
	 * 
	 * @return texto
	 */
	public String getText(){
		return text;
	}
}
