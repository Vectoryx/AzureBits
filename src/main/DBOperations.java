package main;

/* 
TODO: funzioni diverse per: query, update, insert, remove
TODO: storia di tutte le operazioni fatte sotto forma di testo in un LOG
TODO: aggiungere la possibilità di eseguire procedure
TODO: esegue una query e ne stampa automaticamente il risultato
*/

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DBOperations {

	private Connection conn;

	public DBOperations() {
		try {
			Properties props = new Properties();
			FileInputStream in = new FileInputStream("src\\res\\database.properties");
			props.load(in);
			in.close();

			String drivers = props.getProperty("jdbc.drivers");
			if (drivers != null) {
				System.setProperty("jdbc.drivers", drivers);
			}
			String url = props.getProperty("jdbc.url");
			String server = props.getProperty("jdbc.server");
			String db = props.getProperty("jdbc.db");
			String username = props.getProperty("jdbc.username");
			String password = props.getProperty("jdbc.password");
			String s = url + "//" + server + "/" + db + "?user=" + username + "&password=" + password;

			conn = DriverManager.getConnection(s, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ResultSet Query(String query) {

		try {
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(query);

			return resultSet;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public boolean Update(String update) {

		try {
			Statement stat = conn.createStatement();
			return stat.execute(update);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
