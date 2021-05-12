package atlanticwarfare.main;

public class Ship {
	public static final int[] spaces = new int[]{0,2,3,3,4,5};
	
	public static int getShipSize(int ship) {
		return spaces[ship-2];	
	}
}
