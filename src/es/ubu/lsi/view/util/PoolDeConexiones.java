package es.ubu.lsi.view.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oracle.jdbc.pool.OracleDataSource;

/**
 * Connection pool.
 * 
 * @author Jesús Maudes
 * @author Raúl Marticorena
 * @since 1.1
 */
public class PoolDeConexiones {

	/** Logger. */
	private static final Logger logger = LoggerFactory.getLogger(PoolDeConexiones.class);

	// WARNING: review with your current DB

	// private static final boolean DEBUG_MODE = true;
	private static final String DRIVER_TYPE = "thin";
	private static final int PORT = 1521;
	private static final String SID = "XE";
	private static final String HOST = "localhost";
	// WARNING: review with your current DB
	private static final String USER = "HR";
	private static final String PASSWORD = "hr";

	/** Connection pool. */
	private static PoolDeConexiones poolDeConexiones = null;

	/** Data source. */
	private DataSource ds = null;

	/**
	 * Constructor.
	 * 
	 * @throws NamingException
	 *             if exists a problem with the resource
	 */
	private PoolDeConexiones() throws NamingException {
		Context context = null;
		try {
			Properties properties = new Properties();
			properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.fscontext.RefFSContextFactory");
			properties.setProperty(Context.PROVIDER_URL, "file:./res");

			context = new InitialContext(properties);

			ds = (DataSource) context.lookup("jdbc/testdb_ds");

			// Settings propios de Oracle
			OracleDataSource ods = (OracleDataSource) ds;

			logger.info(traceOracleSettings());

			ods.setConnectionCachingEnabled(true); // Activacion de la cache de
													// conexiones
			ods.setImplicitCachingEnabled(true); // Activacion de la cache de
													// sentencias
			// Fin Settings propios de oracle
		} catch (Exception ex) {
			logger.error("Error resolviendo recurso JNDI. Compruebe que se ha creado previamente\n"
					+ " el fichero .bindings correspondiente en el subdirectorio res.");
			logger.error(ex.getMessage());
		} finally {
			if (context != null)
				context.close();
		}
		return;
	}

	/**
	 * Gets instance.
	 * 
	 * @return pool
	 * @throws NamingException
	 *             naming exception
	 */
	public static PoolDeConexiones getInstance() throws NamingException {
		if (poolDeConexiones == null) {
			poolDeConexiones = new PoolDeConexiones();
		}
		return poolDeConexiones;
	}

	/**
	 * Undo transaction.
	 * 
	 * @param conn
	 *            connection
	 */
	public void undo(Connection conn) {
		if (conn != null) {
			try {
				conn.rollback();
				logger.info("Rollback  OK");
			} catch (SQLException e) {
				logger.error("No permite el ROLLBACK");
			}
		}
	}

	/**
	 * Close any resource.
	 * 
	 * @param resource
	 *            resource to close
	 * @since 1.1
	 */
	// New solution using interface (avoid overload methods) !!
	public void close(AutoCloseable resource) {
		if (resource != null) {
			try {
				resource.close();
				logger.info("Cierre del recurso {} OK.", resource.toString());
			} catch (Exception ex) {
				logger.error("No permite el cierre del recurso {}", resource.toString());
			}
		}
	}

	/**
	 * Gets connection from pool.
	 * 
	 * @return new logical conneciton
	 * @throws SQLException
	 *             sql exeption
	 */
	public Connection getConnection() throws SQLException {

		Connection conn = null;
		// Comentar que no hace falta el sync, porque ya lo hace el driver
		// synchronized(this) {
		conn = ds.getConnection();
		// }
		logger.info("Activacion de Autocommit = {}", conn.getAutoCommit());

		conn.setAutoCommit(false);
		// conn.commit(); //Para evitar ORA-00054: resource busy and acquire
		// with NOWAIT specified or timeout expired
		/*
		 * Esto es debido a que si en el pool una transaccion que se ha quedado
		 * abierta en una conexion y esa conexion la hacemos close(), el close()
		 * solo es de la conexion logica, con lo que la conexion fisica sigue
		 * con la transaccion abierta, y a la espera de que se cierre podria
		 * mantener bloquedas fials o tablas enteras dando el error: ORA-00054:
		 * resource busy and acquire with NOWAIT specified or timeout expired
		 */

		// conn.setTransactionIsolation(Connection.TRANSACTION_NONE); //No
		// válido en Oracle
		conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		// conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
		// //No válido en Oracle
		// conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
		// //No valido en Oracle
		// conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

		logger.info(traceConnectionSettings(conn));

		return conn;
	}

	/**
	 * Traces connection settings.
	 * 
	 * @param conn
	 *            connections
	 * @return string representation
	 * @throws SQLException
	 *             sql exeption
	 */
	public String traceConnectionSettings(Connection conn) throws SQLException {

		String retorno = "Activacion de Autocommit=" + conn.getAutoCommit() + "\n";

		retorno += "Nivel de Aislamiento=";
		switch (conn.getTransactionIsolation()) {
		case Connection.TRANSACTION_NONE:
			retorno += "TRANSACTION_NONE";
			break;
		case Connection.TRANSACTION_READ_COMMITTED:
			retorno += "TRANSACTION_READ_COMMITTED";
			break;
		case Connection.TRANSACTION_READ_UNCOMMITTED:
			retorno += "TRANSACTION_READ_UNCOMMITTED";
			break;
		case Connection.TRANSACTION_REPEATABLE_READ:
			retorno += "TRANSACTION_REPEATABLE_READ";
			break;
		case Connection.TRANSACTION_SERIALIZABLE:
			retorno += "TRANSACTION_REPEATABLE_READ";
			break;
		default:
			throw new RuntimeException("NOT Transaction Isolation Level");
		}

		return retorno;
	}

	/**
	 * Traces oracle settings.
	 * 
	 * @param string
	 *            representation
	 * @trhows SQLException sql exception
	 */
	public String traceOracleSettings() throws SQLException {
		OracleDataSource ods = (OracleDataSource) ds;
		String retorno = "trabajando con OracleDataSource";
		retorno += "Activacion de Cache de Conexiones=" + ods.getConnectionCachingEnabled() + "\n";

		Properties props = ods.getConnectionCacheProperties();
		retorno += "Tamaño Inicial Cache de Conexiones=" + props.getProperty("InitialLimit") + "\n";
		retorno += "Tamaño Minimo Cache de Conexiones=" + props.getProperty("MinLimit") + "\n";
		retorno += "Tamaño Maximo Cache de Conexiones=" + props.getProperty("MaxLimit") + "\n";

		return retorno;
	}

	/**
	 * Reconfingura el pool.
	 * 
	 * @throws NamingException
	 *             naming exception
	 */
	static void reconfigurarPool() throws NamingException {
		Context context = null;
		try {
			Properties properties = new Properties();
			properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.fscontext.RefFSContextFactory");
			properties.setProperty(Context.PROVIDER_URL, "file:./res");

			context = new InitialContext(properties);

			// System.out.println("Conectando...");

			OracleDataSource ods = null;
			// Creación de un objeto DataSource
			ods = new OracleDataSource();

			String url = "jdbc:oracle:" + DRIVER_TYPE + ":" + USER + "/" + PASSWORD + "@" + HOST + ":" + PORT + ":"
					+ SID;
			ods.setURL(url);
			ods.setUser(USER);
			ods.setPassword(PASSWORD);

			ods.setConnectionCachingEnabled(true);
			Properties cacheProperties = new Properties();

			cacheProperties.setProperty("InitialLimit", "5");
			cacheProperties.setProperty("MinLimit", "3");
			cacheProperties.setProperty("MaxLimit", "10");

			ods.setConnectionCacheProperties(cacheProperties);

			ods.setImplicitCachingEnabled(true);

			context.rebind("jdbc/testdb_ds", ods);

		} catch (Exception ex) {
			logger.error("Error generando recurso JNDI. Compruebe parámetros y que se ha creado previamente\n"
					+ " el directorio ./res.");
			logger.error(ex.getMessage());
		} finally {
			if (context != null) {
				context.close();
			}
		}

	}

	/**
	 * Resizes cache.
	 * 
	 * @param InitialLimit
	 *            initial limit
	 * @param MinLimit
	 *            min limit
	 * @param MaxLimit
	 *            max limit
	 * @throws SQLException
	 *             sql exception
	 */
	public void resizeCache(int InitialLimit, int MinLimit, int MaxLimit) throws SQLException {
		Properties cacheProperties = new Properties();

		cacheProperties.setProperty("InitialLimit", "" + InitialLimit);
		cacheProperties.setProperty("MinLimit", "" + MinLimit);
		cacheProperties.setProperty("MaxLimit", "" + MaxLimit);

		((OracleDataSource) ds).setConnectionCacheProperties(cacheProperties);
		return;
	}

	/**
	 * Main. Regenerates the binding.
	 * 
	 * @param args
	 *            arguments
	 * @throws NamingException
	 *             naming exception
	 * @throws SQLException
	 *             sql exception
	 */
	public static void main(String[] args) throws NamingException, SQLException {
		System.out.println("Regenerando el fichero bindings para JNDI...(ver carpeta ./res)");
		reconfigurarPool();
		System.out.println("¡Completado con ÉXITO!");
	}

}
