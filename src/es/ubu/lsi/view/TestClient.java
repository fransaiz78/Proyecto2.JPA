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

		
		try {
			inicializaciones();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Service servicio = new ServiceImp();
		

		System.out.println("\n-----------------------------------------------------------------");
		System.out.println("           - Bateria de pruebas para el caso de INSERTAR -           ");
		System.out.println("-----------------------------------------------------------------\n");


		try {
			String[] simbolos = { "H", "O" };
			int[] nros = { 2, 1 };
			servicio.insertarMolecula("Agua", simbolos, nros);
			System.out.println("Insertar molecula se ha realizado con éxito.");

		} catch (ChemistryException e) {
			System.out.println(e.getError().toString() + ": " + e.getMessage() + ". ");
			System.out.println("\nMAL -> La molecula no existe. \n");
		} catch (PersistenceException e) {
			System.out.println("MAL");
			e.printStackTrace();
		}
		
		

//		System.out.println("\n-----------------------------------------------------------------");
//		System.out.println("           - Bateria de pruebas para el caso de BORRAR -           ");
//		System.out.println("-----------------------------------------------------------------\n");
//
//
//		try {
//			System.out.println("Borrar molecula con un ID existente: ");
//			servicio.borrarMolecula(1);
//			System.out.println("Existe molecula con ese id y se ha eliminado con exito.\n");
//
//		} catch (ChemistryException e) {
//			System.out.println(e.getError().toString() + ": " + e.getMessage() + ". ");
//			System.out.println("\nMAL -> La molecula no existe. \n");
//		} catch (PersistenceException e) {
//			System.out.println("MAL");
//			e.printStackTrace();
//		}
//		
//		
//		
//		try {
//			System.out.println("Borrar molecula con un nombre existente: ");
//			servicio.borrarMolecula("AguaOxigenada");
//			System.out.println("Existe molecula con ese nombre y se ha eliminado con exito.\n");
//
//		} catch (ChemistryException e) {
//			System.out.println(e.getError().toString() + ": " + e.getMessage() + ". ");
//			System.out.println("\nMAL -> La molecula no existe. \n");
//		} catch (PersistenceException e) {
//			System.out.println("MAL");
//			e.printStackTrace();
//		}




	}

	public static void inicializaciones() throws NamingException, SQLException, IOException {
		// Inicializacion de Pool
		pool = PoolDeConexiones.getInstance();
		ExecuteScript.run(sqlScript);
	}
}
