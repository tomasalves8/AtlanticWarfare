package com.battleship.database;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import com.battleship.main.GameType;
import com.battleship.main.Ship;

public class Player extends DataBase{
	private String nomeUtilizador;
	private String email;
	private String palavraPasse;
	private int selectedShip = GameType.IDLE;

	public int getSelectedShip() {
		return selectedShip;
	}
	public void setSelectedShip(int selectedShip) {
		this.selectedShip = selectedShip;
	}
	public int getShipSize() {
		return Ship.getShipSize(getSelectedShip());
	}
	public Player(String nomeUtilizador, String palavraPasse, String email) {
		super();
		this.nomeUtilizador = nomeUtilizador;
		this.palavraPasse = palavraPasse;
		this.email = email;
	}
	public static String sha256(String input) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(hash);
		} catch (NoSuchAlgorithmException e) {
			System.out.println("sha256()-ERRO-" + e.getMessage());
			return "";
		}

	}


	public boolean autenticar(){
		try {
			ligar();
			String query = "SELECT * FROM Jogador WHERE nome = '" + getNomeUtilizador() + "' AND password = '" + sha256(getPalavraPasse()) + "';";
			ResultSet rs = getCon().createStatement().executeQuery(query);
			if(rs.isBeforeFirst()) {
				rs.next();
				setEmail(rs.getString(3));
				setPalavraPasse("Authenticated");
				return true;
			}
			return false;
		}catch(Exception ex) {
			System.out.println("autenticar()-ERRO-" + ex.getMessage());
			return false;
		}
	}
	private String getNomeUtilizador() {
		return nomeUtilizador;
	}

	@SuppressWarnings("unused")
	private void setNomeUtilizador(String nomeUtilizador) {
		this.nomeUtilizador = nomeUtilizador;
	}

	@SuppressWarnings("unused")
	private String getEmail() {
		return email;
	}

	private void setEmail(String email) {
		this.email = email;
	}

	private void setPalavraPasse(String palavraPasse) {
		this.palavraPasse = palavraPasse;
	}

	private String getPalavraPasse() {
		return palavraPasse;
	}

	public boolean register() {
		String query = "INSERT INTO Jogador (nome, email, password)"
				+ "VALUES ('" + this.nomeUtilizador + "','"+this.email + "','" +  sha256(this.palavraPasse) + "');";

		ligar();
		try {
			getCon().createStatement().executeUpdate(query);
			return true;
		}catch(SQLException ex) {
			System.out.println("register()-ERRO-" + ex.getMessage());
			return false;
		}
	}

}
