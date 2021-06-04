package atlanticwarfare.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DataBase {
	private static String url = "jdbc:mysql://127.0.0.1:3306/atlanticwarfare?useSSL=false";
	private static String user = "root";
	private static String password = "";
	private static Connection con = null;

	public static Connection getCon() {
		return con;
	}
	
	public static void connect() {
		try {
			con = DriverManager.getConnection(url, user, password);
		}catch(SQLException ex) {
			System.out.println("connect()-ERRO-" + ex.getMessage());
		}
	}
	
	public static void disconnect() {
		try {
			getCon().close();
		}catch(SQLException ex) {
			System.out.println("disconnect()-ERRO-" + ex.getMessage());
		}
	}
}
