package es.ubu.lsi.view;

import java.io.IOException;
import java.sql.SQLException;

import javax.naming.NamingException;

import es.ubu.lsi.service.PersistenceException;
import es.ubu.lsi.service.chemistry.ChemistryException;
import es.ubu.lsi.service.chemistry.Service;
import es.ubu.lsi.service.chemistry.ServiceImp;
import es.ubu.lsi.view.util.ExecuteScript;
import es.ubu.lsi.view.util.PoolDeConexiones;

public class TestClient {

	static final String sqlScript = "./sql/formulas.sql";
	private static PoolDeConexiones pool;

	public static void main(String[] args) throws NamingException, SQLException {
		
		System.out.println("\n-----------------------------------------------------------------");
		System.out.println("           - Bateria de pruebas para el caso de BORRAR -           ");
		System.out.println("-----------------------------------------------------------------\n");
		
		try {
			inicializaciones();
			Service servicio = new ServiceImp();
			/*----------------------------insertarMolecula(nombre, simbolos, nros)----------------------------*/

			/* Prueba insertaMolécula correctamente */
			try {
				System.out.println("Borrar molecula por ID: ");
				ExecuteScript.run(sqlScript);
				servicio.borrarMolecula(555);
				System.out.println("Todo OK.");

			} catch (ChemistryException e) {
				System.out.println(e.getError().toString() + ": " + e.getMessage());
				System.out.println("MAL");
			} catch (PersistenceException e) {
				System.out.println("MAL");
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void inicializaciones() throws NamingException, SQLException, IOException {
		// Inicializacion de Pool
		pool = PoolDeConexiones.getInstance();
//		ExecuteScript.run(sqlScript);
	}
}
