package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertTable {
	private static final String URL = DbPropertiesReader.getString("db.url");
	static final String USERNAME = DbPropertiesReader.getString("db.user");
	static final String PASSWORD = DbPropertiesReader.getString("db.password");

	private Connection connection = null; // manages connection
	private PreparedStatement insertNewCalculoRuta = null;
	private PreparedStatement insertNewGeocodigo = null;
	private PreparedStatement insertNewGeocodigoInverso = null;

	public InsertTable() {
		try {
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			insertNewCalculoRuta = connection
					.prepareStatement("INSERT INTO CalculoRuta " + "(id, origen, destino) " + "VALUES ( ?, ?, ?)");
			// ******************************************
			insertNewGeocodigo = connection
					.prepareStatement("INSERT INTO Geocodigo " + "(id, destino) " + "VALUES ( ?, ?)");
			// ******************************************
			insertNewGeocodigoInverso = connection.prepareStatement(
					"INSERT INTO GeocodigoInverso " + "( id, longitud, latitud) " + "VALUES ( ?, ?, ?)");
			// ******************************************
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
			System.exit(1);
		} // end catch
	} // end PersonQueries constructor

	public int agregarCalculoRuta(int id, String origen, String destino) {
		int result = 0;
		try {
			insertNewCalculoRuta.setInt(1, id);
			insertNewCalculoRuta.setString(2, origen);
			insertNewCalculoRuta.setString(3, destino);
			result = insertNewCalculoRuta.executeUpdate();
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
			close();
		}
		return result;
	}

	public int agregarGeocodigo(int id, String destino) {
		int result = 0;
		try {
			insertNewGeocodigo.setInt(1, id);
			insertNewGeocodigo.setString(2, destino);
			result = insertNewGeocodigo.executeUpdate();
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
			close();
		}
		return result;
	}

	public int agregarGeocodigoInverso(int id, float longitud, float latitud) {
		int result = 0;
		try {
			insertNewGeocodigoInverso.setInt(1, id);
			insertNewGeocodigoInverso.setFloat(2, longitud);
			insertNewGeocodigoInverso.setFloat(3, latitud);
			result = insertNewGeocodigoInverso.executeUpdate();
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
			close();
		}
		return result;
	}

	public void close() {
		try {
			connection.close();
		} // end try
		catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}
	}
}