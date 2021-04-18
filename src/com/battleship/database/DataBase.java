package com.battleship.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DataBase {
	private String url;
	private String user;
	private String password;
	private Connection con;

	public DataBase() {
		this.url = "jdbc:mysql://127.0.0.1:3306/atlanticwarfare?useSSL=false";
		user="root";
		password="";
		con=null;
	}

	public void ligar() {
		try {
			con = DriverManager.getConnection(this.url, this.user, this.password);
			System.out.println("Ligar()-sucesso");
		}catch(SQLException ex) {
			System.out.println("Ligar()-ERRO-" + ex.getMessage());
		}
	}
	
	public void desligar() {
		try {
			con.close();
			System.out.println("Desligar()-sucesso");
		}catch(SQLException ex) {
			System.out.println("Desligar()-ERRO-" + ex.getMessage());
		}
	}
	
	public Connection getCon() {
		return con;
	}
}
