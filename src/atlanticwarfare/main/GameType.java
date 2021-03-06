package atlanticwarfare.main;

import java.awt.Image;
import java.io.Serializable;

import javax.swing.ImageIcon;

public class GameType implements Serializable{
	private static final long serialVersionUID = 1L;
	protected static final int[] spaces = new int[]{0,2,3,3,4,4};
	
	public static final int EASY = 1;
	public static final int MEDIUM = 2;
	public static final int HARD = 3;
	
	public static final int IDLE = 0;
	public static final int SPLASH = 1;
	public static final int FIRE = 2;
	public static final int DESTROYER = 3; // 2 Spaces
	public static final int SUBMARINE = 4; // 3 Spaces
	public static final int CRUISER = 5; // 3 Spaces
	public static final int BATTLESHIP = 6; // 4 Spaces
	public static final int CARRIER = 7; // 4 Spaces
	
	
	public Image[] ships = new Image[8];
	public Image[] shipVertical = new Image[8];
	
	public GameType() {
		ships[SPLASH] = (new ImageIcon("Images/splash.gif")).getImage();
		ships[FIRE] = (new ImageIcon("Images/fire.gif")).getImage();
	
		ships[CARRIER] = (new ImageIcon("Images/carrier.gif")).getImage();		
		shipVertical[CARRIER] = (new ImageIcon("Images/carrierv.gif")).getImage();
		
		ships[BATTLESHIP] = (new ImageIcon("Images/battleship.gif")).getImage();
		shipVertical[BATTLESHIP] = (new ImageIcon("Images/battleshipv.gif")).getImage();
		
		ships[DESTROYER] = (new ImageIcon("Images/patrol.gif")).getImage();
		shipVertical[DESTROYER] = (new ImageIcon("Images/patrolv.gif")).getImage();
		
		ships[SUBMARINE] = (new ImageIcon("Images/submarine.gif")).getImage();
		shipVertical[SUBMARINE] = (new ImageIcon("Images/submarinev.gif")).getImage();
		
		ships[CRUISER] = (new ImageIcon("Images/seawolf.gif")).getImage();
		shipVertical[CRUISER] = (new ImageIcon("Images/seawolfv.gif")).getImage();
	}
	
	public static int getShipSize(int ship) {
		return spaces[ship-2];	
	}
}
