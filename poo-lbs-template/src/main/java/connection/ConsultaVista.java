package connection;

// Fig. 28.29: JdbcRowSetTest.java
// Displaying the contents of the authors table using JdbcRowSet.
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.RowSet;
import javax.sql.rowset.JdbcRowSet;
import com.sun.rowset.JdbcRowSetImpl; // Sun's JdbcRowSet implementation
//import Modelo.*;<---------------------------------------------------------------------------
/**
 * Esta clase permite consultar los datos que ya se encuentrar registrados en la base de datos
 * y que posteriormente se mostraran en las respectivas vistas
 * @author 
 * @version 15/11/2018
 *
 */
public class ConsultaVista {
	
	// JDBC driver name and database URL
	static final String DATABASE_URL = DbPropertiesReader.getString("db.url");
	static final String USERNAME = DbPropertiesReader.getString("db.user");
	static final String PASSWORD = DbPropertiesReader.getString("db.password");
	/**
	 * Metodo que permite inicializar la clase
	 * @param args
	 */
	public static void main(String args[]) {
		lastIdTable("Geocodigo");
		lastIdTable("GeocodigoInverso");
		lastIdTable("CalculoRuta");
	}
	/**
	 * 
	 * Es utilizado para consultar el ultimo Id que se encuentra en cada una de las tablas de la base de datos. 
	 */
	public static int lastIdTable(String tabla) {
		int respuesta = 0;
		try {
			// specify properties of JdbcRowSet
			JdbcRowSet rowSet = new JdbcRowSetImpl();
			JdbcRowSet rowSetaux = new JdbcRowSetImpl();
			rowSet.setUrl(DATABASE_URL); // set database URL
			rowSet.setUsername(USERNAME); // set username
			rowSet.setPassword(PASSWORD); // set password
			rowSet.setCommand("SELECT MAX(id) FROM "+tabla); // set query
			rowSet.execute(); // execute query
			rowSet.last();
			respuesta=rowSet.getInt(1);
			rowSet.close();
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}
		return respuesta;
	}
}
