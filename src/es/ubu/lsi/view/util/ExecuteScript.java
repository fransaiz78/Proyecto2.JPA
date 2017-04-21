package es.ubu.lsi.view.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Execute script.
 * 
 * @author Jesús Maudes
 * @author Raúl Marticorena
 * @since 1.1
 */
public class ExecuteScript {
	
	/**
	 * Obtenemos el logger
	 */
	private static Logger l = LoggerFactory.getLogger(ExecuteScript.class);;
	
	/**
	 * Database user (schema).
	 */
	private static final String USER = "HR";
	// WARNING: review with your current DB 
	
	/**
	 * Database password user.
	 */
	private static final String PASSWORD = "hr";
	// WARNING: review with your current DB

	/**
	 * Main.
	 * 
	 * @param args arguments
	 */
	public static void main(String[] args) {
		run(args[0]);
	}

	/**
	 * Run script.
	 * 
	 * @param file_name script path
	 */
	public static void run(String file_name) {
		try {
			String line;
			//No te olvides de hacer que el script acabe con "exit;"
			Process p = Runtime.getRuntime().exec("sqlplus " + USER + "/" + PASSWORD + " @" + file_name);
			
			BufferedReader input = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			while ((line = input.readLine()) != null) {
//				System.out.println(line);
				l.debug(line);
			}
			input.close();
			p.destroy();
		} catch (Exception err) {
			err.printStackTrace();
		}
	}

}
