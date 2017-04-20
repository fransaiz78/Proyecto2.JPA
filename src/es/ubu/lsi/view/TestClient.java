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
		
		

		System.out.println("\n-----------------------------------------------------------------");
		System.out.println("           - Bateria de pruebas para el caso de BORRAR -           ");
		System.out.println("-----------------------------------------------------------------\n");


		try {
			servicio.borrarMolecula(1);
			System.out.println("Borrar molecula mediante id se ha realizado con éxito.");

		} catch (ChemistryException e) {
			System.out.println(e.getError().toString() + ": " + e.getMessage() + ". ");
			System.out.println("\nMAL -> La molecula no existe. \n");
		} catch (PersistenceException e) {
			System.out.println("MAL");
			e.printStackTrace();
		}
		
		
		try {
			servicio.borrarMolecula("AguaOxigenada");
			System.out.println("Borrar molecula mediante nombre se ha realizado con éxito.");

		} catch (ChemistryException e) {
			System.out.println(e.getError().toString() + ": " + e.getMessage() + ". ");
			System.out.println("\nMAL -> La molecula no existe. \n");
		} catch (PersistenceException e) {
			System.out.println("MAL");
			e.printStackTrace();
		}
		
		
		
		System.out.println("\n-----------------------------------------------------------------");
		System.out.println("        - Bateria de pruebas para el caso de ACTUALIZAR -          ");
		System.out.println("-----------------------------------------------------------------\n");
		
		try {
			servicio.actualizarMolecula(1, "H", 4);
			System.out.println("ActualizarMolecula mediante Id se ha realizado con éxito.");
		} catch (ChemistryException e) {
			System.out.println(e.getError().toString() + ": " + e.getMessage() + ". ");
			System.out.println("\nMAL -> La molecula no existe. \n");
		} catch (PersistenceException e) {
			System.out.println("MAL");
			e.printStackTrace();
		}
		
		try {
			servicio.actualizarMolecula("AguaOxigenada", "H", 4);
			System.out.println("ActualizarMolecula mediante Id se ha realizado con éxito.");
		} catch (ChemistryException e) {
			System.out.println(e.getError().toString() + ": " + e.getMessage() + ". ");
			System.out.println("\nMAL -> La molecula no existe. \n");
		} catch (PersistenceException e) {
			System.out.println("MAL");
			e.printStackTrace();
		}
		
		
		
		




	}

	public static void inicializaciones() throws NamingException, SQLException, IOException {
		// Inicializacion de Pool
		pool = PoolDeConexiones.getInstance();
		ExecuteScript.run(sqlScript);
	}
}
