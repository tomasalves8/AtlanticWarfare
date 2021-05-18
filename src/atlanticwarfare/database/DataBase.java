package atlanticwarfare.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DataBase {
	private static String url = "jdbc:mysql://127.0.0.1:3306/atlanticwarfare?useSSL=false";
	private static String user = "root";
	private static String password = "";
	private static Connection con = null;

	public static void ligar() {
		try {
			con = DriverManager.getConnection(url, user, password);
			System.out.println("Ligar()-sucesso");
		}catch(SQLException ex) {
			System.out.println("Ligar()-ERRO-" + ex.getMessage());
		}
	}
	
	public static void desligar() {
		try {
			con.close();
			System.out.println("Desligar()-sucesso");
		}catch(SQLException ex) {
			System.out.println("Desligar()-ERRO-" + ex.getMessage());
		}
	}
	
	public static Connection getCon() {
		return con;
	}
}
