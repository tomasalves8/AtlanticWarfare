package atlanticwarfare.database;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;

import javax.swing.JOptionPane;

import atlanticwarfare.main.GameType;
import atlanticwarfare.main.Ship;

public class Player extends DataBase{
	private int id;
	private String username;
	private String email;
	private String password;
	private String countryCode;
	private int selectedShip = GameType.IDLE;
	public static Object[][] getAll(){
		Object [][] resultado = null;
        String query = "SELECT name, gamesPlayed, GamesWon, gamesLost FROM Player INNER JOIN statistics ON Player.id = statistics.player_id;";

        ligar();

        try{
            ResultSet rs = getCon().createStatement().executeQuery(query);

            int numColumns = rs.getMetaData().getColumnCount();
            int numRows = 0;

            rs.last();
            numRows = rs.getRow();
            rs.beforeFirst();

            resultado = new Object[numRows][numColumns];

            for(int l = 0; l < numRows; l++){
                rs.next();

                for(int c = 0; c < numColumns; c++){
                    resultado[l][c] = rs.getObject(c+1);

                }
            }
        }
        catch(SQLException ex){
            System.out.println("Player - getAll() - " + ex.getMessage());
        }

        desligar();

        return resultado;
	}
	public static Object[][] getTop(int num, String orderTable, String countryCode){
		Object [][] resultado = null;
		
        String query = "SELECT name, gamesPlayed, GamesWon, gamesLost FROM Player INNER JOIN statistics ON Player.id = statistics.player_id ";
        if(!countryCode.equals("GLOBAL")){
        	query += "WHERE countryCode = '" + countryCode + "' ";
        }
        query += "ORDER BY " + orderTable + " DESC LIMIT " + num +";";
        ligar();

        try{
            ResultSet rs = getCon().createStatement().executeQuery(query);

            int numColumns = rs.getMetaData().getColumnCount();
            int numRows = 0;

            rs.last();
            numRows = rs.getRow();
            rs.beforeFirst();

            resultado = new Object[numRows][numColumns];

            for(int l = 0; l < numRows; l++){
                rs.next();

                for(int c = 0; c < numColumns; c++){
                    resultado[l][c] = rs.getObject(c+1);

                }
            }
        }
        catch(SQLException ex){
            System.out.println("Player - getAll() - " + ex.getMessage());
        }

        desligar();

        return resultado;
	}
	public void addGame(boolean won, String opponentID) {
		String query = "UPDATE Statistics SET gamesPlayed = gamesPlayed + 1 ";
		String queryGame = "";
		try {
			if(won) {
				JOptionPane.showMessageDialog(null, "Game Won.");
				queryGame = "INSERT INTO Game (winner, loser) VALUES(" + id  + ", " + opponentID + ");";
				query += ", gamesWon = gamesWon + 1";
			}else {
				queryGame = "INSERT INTO Game (winner, loser) VALUES(" + opponentID  + ", " + id + ");";
				JOptionPane.showMessageDialog(null, "Game Lost.");
				query += ", gamesLost = gamesLost + 1";
			}
			query += " WHERE player_id = " + id + ";";
			getCon().createStatement().executeUpdate(query);
			getCon().createStatement().executeUpdate(queryGame);
		} catch (SQLException e) {
			System.out.println("Player - addGame()-ERRO-" + e.getMessage());
		}
	}
	public int getSelectedShip() {
		return selectedShip;
	}
	public void setSelectedShip(int selectedShip) {
		this.selectedShip = selectedShip;
	}
	public int getShipSize() {
		return Ship.getShipSize(getSelectedShip());
	}
	public Player(String username, String password, String email, String countryCode) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
		this.countryCode = countryCode;
	}
	public Player(String username, String password, String email) {
		super();
		this.username = username;
		this.password = password;
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


	public boolean authenticate(){
		try {
			ligar();
			String query = "SELECT * FROM Player WHERE name = '" + getUsername() + "' AND password = '" + sha256(getPassword()) + "';";
			ResultSet rs = getCon().createStatement().executeQuery(query);
			if(rs.isBeforeFirst()) {
				rs.next();
				this.id = rs.getInt(1);
				setEmail(rs.getString(3));
				setPassword("Authenticated");
				return true;
			}
			return false;
		}catch(Exception ex) {
			System.out.println("Player - authenticate()-ERRO-" + ex.getMessage());
			return false;
		}
	}
	private String getUsername() {
		return username;
	}

	@SuppressWarnings("unused")
	private void setUsername(String username) {
		this.username = username;
	}

	@SuppressWarnings("unused")
	private String getEmail() {
		return email;
	}

	private void setEmail(String email) {
		this.email = email;
	}

	private void setPassword(String password) {
		this.password = password;
	}

	private String getPassword() {
		return password;
	}

	public boolean register() {
		String query = "INSERT INTO Player (name, email, password, countryCode)"
				+ "VALUES ('" + this.username + "','"+this.email + "','" +  sha256(this.password) + "', '" + countryCode +"');";

		ligar();
		try {
			 PreparedStatement ps = getCon().prepareStatement(query,
		                Statement.RETURN_GENERATED_KEYS);

		    ps.execute();

		    ResultSet rs = ps.getGeneratedKeys();
		    if (rs.next()) {
		        this.id = rs.getInt(1);
		    }
			query = "INSERT INTO Statistics (player_id)"
					+ " VALUES (" + id + ");";
			getCon().createStatement().executeUpdate(query);
			return true;
		}catch(SQLException ex) {
			System.out.println("register()-ERRO-" + ex.getMessage());
			return false;
		}
	}

}
