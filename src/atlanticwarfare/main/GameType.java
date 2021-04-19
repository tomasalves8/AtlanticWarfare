package atlanticwarfare.main;

import java.awt.Image;
import javax.swing.ImageIcon;

public class GameType {
	public static final int PLAYERTURN = 0;
	public static final int ENEMYTURN = 1;
	
	public static final int IDLE = 0;
	public static final int SPLASH = 1;
	public static final int FIRE = 2;
	public static final int DESTROYER = 3; // 2 Spaces
	public static final int SUBMARINE = 4; // 3 Spaces
	public static final int CRUISER = 5; // 3 Spaces
	public static final int BATTLESHIP = 6; // 4 Spaces
	public static final int CARRIER = 7; // 5 Spaces
	
	
	public Image[] ships = new Image[8];
	public Image[] shipVertical = new Image[8];
	
	public GameType() {
		ships[SPLASH] = (new ImageIcon("Images/splash.gif")).getImage();
		
		ships[FIRE] = (new ImageIcon("Images/fire.gif")).getImage();
		ships[CARRIER] = (new ImageIcon("Images/carrier.gif")).getImage();
		
		shipVertical[CARRIER] = (new ImageIcon("Images/carrierv.gif")).getImage();
	}
}
