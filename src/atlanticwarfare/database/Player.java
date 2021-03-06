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

public class Player extends DataBase{
	private int id;
	private String username;
	private String email;
	private String password;
	private String countryCode;
	private int selectedShip = GameType.IDLE;
	
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
	
	public static Object[][] getAll(){
		Object [][] result = null;
        String query = "SELECT name, gamesPlayed, GamesWon, gamesLost FROM Player INNER JOIN statistics ON Player.id = statistics.player_id;";

        connect();

        try{
            ResultSet rs = getCon().createStatement().executeQuery(query);

            int numColumns = rs.getMetaData().getColumnCount();
            int numRows = 0;

            rs.last();
            numRows = rs.getRow();
            rs.beforeFirst();

            result = new Object[numRows][numColumns];

            for(int l = 0; l < numRows; l++){
                rs.next();

                for(int c = 0; c < numColumns; c++){
                    result[l][c] = rs.getObject(c+1);

                }
            }
        }
        catch(SQLException ex){
            System.out.println("Player - getAll() - " + ex.getMessage());
        }

        disconnect();

        return result;
	}
	public static Object[][] getTop(int num, String orderTable, String countryCode){
		Object [][] result = null;
		
        String query = "SELECT name, gamesPlayed, GamesWon, gamesLost FROM Player INNER JOIN statistics ON Player.id = statistics.player_id ";
        if(!countryCode.equals("GLOBAL")){
        	query += "WHERE countryCode = '" + countryCode + "' ";
        }
        query += "ORDER BY " + orderTable + " DESC LIMIT " + num +";";
        connect();

        try{
            ResultSet rs = getCon().createStatement().executeQuery(query);

            int numColumns = rs.getMetaData().getColumnCount();
            int numRows = 0;

            rs.last();
            numRows = rs.getRow();
            rs.beforeFirst();

            result = new Object[numRows][numColumns];

            for(int l = 0; l < numRows; l++){
                rs.next();

                for(int c = 0; c < numColumns; c++){
                    result[l][c] = rs.getObject(c+1);

                }
            }
        }
        catch(SQLException ex){
            System.out.println("Player - getTop() - " + ex.getMessage());
        }

        disconnect();

        return result;
	}
	
	public Object[][] getLastGames(){
		Object [][] result = null;
		
        String query = "select Winner.name as Winner, Loser.name as Loser, duration from game INNER JOIN Player as Winner on game.Winner = Winner.id INNER JOIN Player as Loser on game.Loser = Loser.id WHERE game.Winner = " + id + " OR game.Loser = " + id + " ORDER BY game.id DESC LIMIT 10;";
        connect();

        try{
            ResultSet rs = getCon().createStatement().executeQuery(query);

            int numColumns = rs.getMetaData().getColumnCount();
            int numRows = 0;

            rs.last();
            numRows = rs.getRow();
            rs.beforeFirst();

            result = new Object[numRows][numColumns+1];

            for(int l = 0; l < numRows; l++){
                rs.next();

                for(int c = 0; c < numColumns; c++){
                    result[l][c+1] = rs.getObject(c+1);

                }
                result[l][0] = result[l][1].equals(getUsername()) ? "Yes" : "No";
                long secondsDisplay = (int)result[l][3] % 60;
				long elapsedMinutes = (int)result[l][3] / 60;
				result[l][3] = String.format("%02d", elapsedMinutes) + ":" + String.format("%02d", secondsDisplay);
				
            }
        }
        catch(SQLException ex){
            System.out.println("Player - getLastGames() - " + ex.getMessage());
        }

        disconnect();

        return result;
	}
	public void addGame(boolean won, String opponentID, long time) {
		String query = "UPDATE Statistics SET gamesPlayed = gamesPlayed + 1 ";
		String queryGame = "";
		try {
			if(won) {
				JOptionPane.showMessageDialog(null, "Game Won.");
				queryGame = "INSERT INTO Game (winner, loser, duration) VALUES(" + id  + ", " + opponentID + ", " + time + ");";
				getCon().createStatement().executeUpdate(query + ", gamesWon = gamesWon + 1 WHERE player_id = " + id + ";");
				getCon().createStatement().executeUpdate(query + ", gamesLost = gamesLost + 1 WHERE player_id = " + opponentID + ";");
			}else {
				queryGame = "INSERT INTO Game (winner, loser, duration) VALUES(" + opponentID  + ", " + id + ", " + time + ");";
				JOptionPane.showMessageDialog(null, "Game Lost.");
				getCon().createStatement().executeUpdate(query + ", gamesLost = gamesLost + 1 WHERE player_id = " + id + ";");
				getCon().createStatement().executeUpdate(query + ", gamesWon = gamesWon + 1 WHERE player_id = " + opponentID + ";");
			}
			getCon().createStatement().executeUpdate(queryGame);
		} catch (SQLException e) {
			System.out.println("Player - addGame()-ERROR-" + e.getMessage());
		}
	}
	public int getSelectedShip() {
		return selectedShip;
	}
	public void setSelectedShip(int selectedShip) {
		this.selectedShip = selectedShip;
	}
	
	public static String sha256(String input) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(hash);
		} catch (NoSuchAlgorithmException e) {
			System.out.println("sha256()-ERROR-" + e.getMessage());
			return "";
		}
	}


	public boolean authenticate(){
		try {
			connect();
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
			System.out.println("Player - authenticate()-ERROR-" + ex.getMessage());
			return false;
		}
	}
	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	private void setEmail(String email) {
		this.email = email;
	}

	private void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public boolean register() {
		String query = "INSERT INTO Player (name, email, password, countryCode)"
				+ "VALUES ('" + this.username + "','"+this.email + "','" +  sha256(this.password) + "', '" + countryCode +"');";

		connect();
		try {
			 PreparedStatement ps = getCon().prepareStatement(query,
		                Statement.RETURN_GENERATED_KEYS);

		    ps.execute();

		    ResultSet rs = ps.getGeneratedKeys();
		    if (rs.next()) {
		        this.id = rs.getInt(1);
		    }
			query = "INSERT INTO Statistics (player_id) VALUES (" + id + ");";
			getCon().createStatement().executeUpdate(query);
			return true;
		}catch(SQLException ex) {
			System.out.println("register()-ERROR-" + ex.getMessage());
			return false;
		}
	}

}
