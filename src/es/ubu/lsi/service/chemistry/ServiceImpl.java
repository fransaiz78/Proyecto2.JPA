/**
 * 
 */
package es.ubu.lsi.service.chemistry;

import es.ubu.lsi.service.PersistenceException;
import es.ubu.lsi.service.PersistenceService;

/**
 * @author Mario Santamaria
 * @author Francisco Saiz
 *
 */
public class ServiceImpl extends PersistenceService implements Service{

	/**
	 * 
	 */
	public ServiceImpl() {
	}

	@Override
	public void insertarMolecula(String nombre, String[] simbolos, int[] numeros) throws PersistenceException {
		
	}

	@Override
	public void actualizarMolecula(String nombreMol, String simbolo, int numero) throws PersistenceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actualizarMolecula(int id, String simbolo, int numero) throws PersistenceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void borrarMolecula(String nombre) throws PersistenceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void borrarMolecula(int idMolecula) throws PersistenceException {
		// TODO Auto-generated method stub
		
	}

}
