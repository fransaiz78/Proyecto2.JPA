package es.ubu.lsi.view;

import java.io.IOException;
import java.sql.SQLException;

import javax.naming.NamingException;

import es.ubu.lsi.service.PersistenceException;
import es.ubu.lsi.service.PersistenceFactorySingleton;
import es.ubu.lsi.service.chemistry.ChemistryException;
import es.ubu.lsi.service.chemistry.Service;
import es.ubu.lsi.service.chemistry.ServiceImpl;
import es.ubu.lsi.view.util.ExecuteScript;
import es.ubu.lsi.view.util.PoolDeConexiones;
import es.ubu.lsi.service.chemistry.*;

/**
 * Clase TestClient donde se realizan las pruebas correspondientes.
 * 
 * @author Mario Santamaria
 * @author Francisco Saiz
 *
 */
public class TestClientBateriaPruebas {

	static final String scriptSQL = "./sql/formulas.sql";
	private static PoolDeConexiones pool;

	/**
	 * Main donde se realizaran las inicializaciones y la bateria de pruebas.
	 * 
	 * @param args
	 *            Argumentos
	 * @throws NamingException
	 *             Excepcion
	 * @throws SQLException
	 *             Excepcion
	 */
	public static void main(String[] args) throws NamingException, SQLException {

		try {
			inicializaciones();

			Service servicio = new ServiceImpl();

			System.out.println("->  Script cargado con la molecula: H2O.\n\n");

			System.out.println("\n-----------------------------------------------------------------");
			System.out.println("          - Bateria de pruebas para el caso de INSERTAR -          ");
			System.out.println("-----------------------------------------------------------------\n");

			try {
				String[] simbolos = { "H", "O" };
				int[] nros = { 2, 2 };
				servicio.insertarMolecula("AguaOxigenada", simbolos, nros);
				System.out.println("Insertar molecula AguaOxigenada con formula H2O2 se ha realizado con éxito.");

			} catch (ChemistryException e) {
				if (e.getError() == ChemistryError.FORMULA_YA_EXISTENTE) {
					System.out.println("Insertar molecula con formula existente. OK. ");
				} else {
					System.out.println("Insertar molecula con formula existente. MAL. ");
				}
			} catch (PersistenceException e) {
				System.out.println("MAL");
				e.printStackTrace();
			}

			try {
				String[] simbolos = { "H", "O" };
				int[] nros = { 2, 1 };
				servicio.insertarMolecula("AguaNueva", simbolos, nros);
				System.out.println("Insertar molecula Agua se ha realizado con éxito.");

			} catch (ChemistryException e) {
				if (e.getError() == ChemistryError.FORMULA_YA_EXISTENTE) {
					System.out.println("Insertar molecula con formula existente. OK. ");

				} else {
					System.out.println("Insertar molecula con formula existente. MAL. ");
				}
			} catch (PersistenceException e) {
				System.out.println("MAL");
				e.printStackTrace();
			}

			try {
				String[] simbolos = { "C", "H", "V" };
				int[] nros = { 1, 4 };
				servicio.insertarMolecula("Agua", simbolos, nros);
				System.out.println("Borrar molecula por un id se ha realizado con éxito.");

			} catch (ChemistryException e) {
				if (e.getError() == ChemistryError.TAMAÑOS_INADECUADOS) {
					System.out.println("Insertar molecula con arrays de diferente tam. OK. ");
				} else {
					System.out.println("Insertar molecula con arrays de diferente tam. MAL. ");
				}
			} catch (PersistenceException e) {
				System.out.println("MAL");
				e.printStackTrace();
			}

			try {
				String[] simbolos = { "C", "H" };
				int[] nros = { 1, 4 };
				servicio.insertarMolecula("Metano", simbolos, nros);
				System.out.println("Insertar molecula se ha realizado con éxito.");

			} catch (ChemistryException e) {
				if (e.getError() == ChemistryError.NO_EXISTE_ATOMO) {
					System.out.println("Insertar molecula con atomo inexistente. OK. ");
				} else {
					System.out.println("Insertar molecula con atomo inexistente. MAL. ");
				}
			} catch (PersistenceException e) {
				System.out.println("MAL");
				e.printStackTrace();
			}

			try {
				String[] simbolos = { "H", "O" };
				int[] nros = { 2, 1 };
				servicio.insertarMolecula("AguaO", simbolos, nros);
				System.out.println("Insertar molecula se ha realizado con éxito.");

			} catch (ChemistryException e) {
				if (e.getError() == ChemistryError.FORMULA_YA_EXISTENTE) {
					System.out.println("Insertar molecula con formula existente. OK. ");
				} else {
					System.out.println("Insertar molecula con formula existente. MAL. ");
				}
			} catch (PersistenceException e) {
				System.out.println("MAL");
				e.printStackTrace();
			}

			try {
				String[] simbolos = { "H", "O" };
				int[] nros = { 4, 5 };
				servicio.insertarMolecula("Agua", simbolos, nros);
				System.out.println("Insertar molecula se ha realizado con éxito.");

			} catch (ChemistryException e) {
				if (e.getError() == ChemistryError.NOMBRE_DE_MOLECULA_YA_EXISTENTE) {
					System.out.println("Insertar molecula con nombre existente. OK. ");
				} else {
					System.out.println("Insertar molecula con nombre existente. MAL. ");
				}
			} catch (PersistenceException e) {
				System.out.println("MAL");
				e.printStackTrace();
			}

			System.out.println("\n->  Moleculas existentes: H2O - H2O2 \n\n");

			System.out.println("\n-----------------------------------------------------------------");
			System.out.println("        - Bateria de pruebas para el caso de ACTUALIZAR -          ");
			System.out.println("-----------------------------------------------------------------\n");

			try {
				servicio.actualizarMolecula(1, "V", 4);
				System.out.println("ActualizarMolecula mediante Id con simbolo inexistente se ha realizado con éxito.");

			} catch (ChemistryException e) {
				if (e.getError() == ChemistryError.MOLECULA_NO_CONTIENE_SIMBOLO) {
					System.out.println("ActualizarMolecula mediante Id con simbolo inexistente. OK. ");
				} else {
					System.out.println("ActualizarMolecula mediante Id con simbolo inexistente. MAL. ");
				}
			} catch (PersistenceException e) {
				System.out.println("MAL");
				e.printStackTrace();
			}

			try {
				servicio.actualizarMolecula(1, "H", 2);
				System.out.println("ActualizarMolecula mediante Id con molecula existente se ha realizado con éxito.");

			} catch (ChemistryException e) {
				if (e.getError() == ChemistryError.MOLECULA_YA_EXISTENTE) {
					System.out.println("ActualizarMolecula mediante Id con formula existente. OK. ");
				} else {
					System.out.println("ActualizarMolecula mediante Id con formula existente. MAL. ");
				}
			} catch (PersistenceException e) {
				System.out.println("MAL");
				e.printStackTrace();
			}

			try {
				servicio.actualizarMolecula(1, "H", 4);
				System.out.println("ActualizarMolecula mediante Id se ha realizado con éxito.");
			} catch (ChemistryException e) {
				if (e.getError() == ChemistryError.NO_EXISTE_MOLECULA) {
					System.out.println("ActualizarMolecula mediante Id. OK. ");
				} else {
					System.out.println("ActualizarMolecula mediante Id. MAL. ");
				}
			} catch (PersistenceException e) {
				System.out.println("MAL");
				e.printStackTrace();
			}

			try {
				servicio.actualizarMolecula("Agua", "V", 4);
				System.out.println(
						"ActualizarMolecula mediante Nombre con simbolo inexistente se ha realizado con éxito.");

			} catch (ChemistryException e) {
				if (e.getError() == ChemistryError.MOLECULA_NO_CONTIENE_SIMBOLO) {
					System.out.println("ActualizarMolecula mediante Nombre con simbolo inexistente. OK. ");
				} else {
					System.out.println("ActualizarMolecula mediante Nombre con simbolo inexistente. MAL. ");
				}
			} catch (PersistenceException e) {
				System.out.println("MAL");
				e.printStackTrace();
			}

			// En la ultima actualizacion, el H se ha quedado con 4
			try {
				servicio.actualizarMolecula("Agua", "H", 4);
				System.out
						.println("ActualizarMolecula mediante Nombre con formula existente se ha realizado con éxito.");

			} catch (ChemistryException e) {
				if (e.getError() == ChemistryError.MOLECULA_YA_EXISTENTE) {
					System.out.println("ActualizarMolecula mediante Nombre con formula existente. OK. ");
				} else {
					System.out.println("ActualizarMolecula mediante Nombre con formula existente. MAL. ");
				}
			} catch (PersistenceException e) {
				System.out.println("MAL");
				e.printStackTrace();
			}

			try {
				servicio.actualizarMolecula("Agua", "H", 2);
				System.out.println("ActualizarMolecula mediante Nombre se ha realizado con éxito.");

			} catch (ChemistryException e) {
				if (e.getError() == ChemistryError.NO_EXISTE_MOLECULA) {
					System.out.println("ActualizarMolecula mediante Nombre. OK. ");
				} else {
					System.out.println("ActualizarMolecula mediante Nombre. MAL. ");
				}
			} catch (PersistenceException e) {
				System.out.println("MAL");
				e.printStackTrace();
			}

			// Acabamos la bateria de pruebas para el caso Actualizar con la
			// molecula: H2O.

			System.out.println("\n->  Moleculas existentes: H2O - H2O2 \n\n");

			System.out.println("\n-----------------------------------------------------------------");
			System.out.println("           - Bateria de pruebas para el caso de BORRAR -           ");
			System.out.println("-----------------------------------------------------------------\n");

			try {
				servicio.borrarMolecula(1);
				System.out.println("Borrar molecula mediante Id se ha realizado con éxito.");

			} catch (ChemistryException e) {
				if (e.getError() == ChemistryError.NO_EXISTE_MOLECULA) {
					System.out.println("Borrar molecula mediante Id si no existe. OK. ");
				} else {
					System.out.println("Borrar molecula mediante Id si no existe. MAL.		 ");
				}
			} catch (PersistenceException e) {
				System.out.println("MAL");
				e.printStackTrace();
			}

			try {
				servicio.borrarMolecula(1);
				System.out.println("Borrar molecula mediante Id se ha realizado con éxito.");

			} catch (ChemistryException e) {
				if (e.getError() == ChemistryError.NO_EXISTE_MOLECULA) {
					System.out.println("Borrar molecula mediante Id si no existe. OK. ");
				} else {
					System.out.println("Borrar molecula mediante Id si no existe. MAL. ");
				}
			} catch (PersistenceException e) {
				System.out.println("MAL");
				e.printStackTrace();
			}

			System.out.println("\n->  Insertamos una nueva molecula para poder probar el borrado por nombre:");
			try {
				String[] simbolos = { "H", "O" };
				int[] nros = { 2, 1 };
				servicio.insertarMolecula("Agua", simbolos, nros);
				System.out.println("Insertar molecula se ha realizado con éxito.\n");

			} catch (ChemistryException e) {
				if (e.getError() == ChemistryError.FORMULA_YA_EXISTENTE) {
					System.out.println("Insertar molecula con formula existente. OK. ");
				} else {
					System.out.println("Insertar molecula con formula existente. MAL. ");
				}
			} catch (PersistenceException e) {
				System.out.println("MAL");
				e.printStackTrace();
			}

			try {
				servicio.borrarMolecula("Agua");
				System.out.println("Borrar molecula mediante Nombre se ha realizado con éxito.");

			} catch (ChemistryException e) {
				if (e.getError() == ChemistryError.NO_EXISTE_MOLECULA) {
					System.out.println("Borrar molecula mediante Nombre si no existe. OK. ");
				} else {
					System.out.println("Borrar molecula mediante Nombre si no existe. MAL. ");
				}
			} catch (PersistenceException e) {
				System.out.println("MAL");
				e.printStackTrace();
			}

			try {
				servicio.borrarMolecula("Agua");
				System.out.println("Borrar molecula mediante Nombre se ha realizado con éxito.");

			} catch (ChemistryException e) {
				if (e.getError() == ChemistryError.NO_EXISTE_MOLECULA) {
					System.out.println("Borrar molecula mediante Nombre si no existe. OK. ");
				} else {
					System.out.println("Borrar molecula mediante Nombre si no existe. MAL. ");
				}
			} catch (PersistenceException e) {
				System.out.println("MAL");
				e.printStackTrace();
			}

		} catch (IOException e1) {
			e1.printStackTrace();

		} finally {

			// Cerramos recursos
			PersistenceFactorySingleton.close();
		}

	}

	/**
	 * Metodo que realiza las inicializaciones correspondientes.
	 * 
	 * @throws NamingException
	 *             Excepcion
	 * @throws SQLException
	 *             Excepcion
	 * @throws IOException
	 *             Excepcion
	 */
	public static void inicializaciones() throws NamingException, SQLException, IOException {
		// Inicializacion de Pool
		pool = PoolDeConexiones.getInstance();
		// Cargamos el script.
		System.out.println("Cargando de nuevo el Script...");
		ExecuteScript.run(scriptSQL);
	}
}
