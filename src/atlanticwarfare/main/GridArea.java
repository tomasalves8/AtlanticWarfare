package atlanticwarfare.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import atlanticwarfare.database.Player;
import atlanticwarfare.design.Game;

public class GridArea extends JPanel
{
	private static final long serialVersionUID = -5546485795145800928L;
	private final int BLOCKSIZE = 30;
	protected int area [][] =
		{{	0,  0,	0,	0,	0,	0,	0,	0,	0,	0	},
		{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0	},
		{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0	},
		{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0	},
		{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0	},
		{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0	},
		{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0	},
		{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0	},
		{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0	},
		{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0	}};
	protected int placed [][] =
		{{	0,  0,	0,	0,	0,	0,	0,	0,	0,	0	},
		{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0	},
		{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0	},
		{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0	},
		{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0	},
		{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0	},
		{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0	},
		{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0	},
		{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0	},
		{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0	}};
	protected int probgrid [][] =
		{{	0,  0,	0,	0,	0,	0,	0,	0,	0,	0	},
		{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0	},
		{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0	},
		{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0	},
		{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0	},
		{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0	},
		{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0	},
		{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0	},
		{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0	},
		{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0	}};
	
	//int[10][10];
	private ArrayList<Point> queuedShots = new ArrayList<Point>();
	private ArrayList<Point> searchPositions = new ArrayList<Point>(4);
	
	protected boolean vertical = false;
	private String title;
	private Point selected;
	private boolean canFire;
	private int targetsHit=0;

	protected Point cursorLocation;
	private Rectangle gridRects[][] = new Rectangle[10][10];
	private Player player;
	private GridArea opponent;
	private Game board;
	protected GameType gametype;
	private Random random;
	private ArrayList<Integer> shipsAlive = new ArrayList<Integer>();
	public void setPlayer(Player player) {
		this.player = player;
	}
	public Player getPlayer() {
		return player;
	}
	public GridArea(String title, Game board)
	{
		for (int i = 3; i <= 7; i++) {
			shipsAlive.add(i);
		}
		this.title = title;
		this.board = board;
		this.gametype = new GameType();
		this.canFire = true;
		try {
			this.random = SecureRandom.getInstanceStrong();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		for (int y=0; y<10; y++)
			for (int x=0; x<10; x++) gridRects[x][y] = new Rectangle(x*25,y*25,25,25);

		addMouseMotionListener(new MouseMovingHandler());
		addMouseListener(new MouseHandler());
		for (int i = 0; i < 4; i++) {
			searchPositions.add(new Point(-1,-1));
		}
		setOpaque(false);
	}
	public String getTitle() {
		return title;
	}
	private GridArea getOpponent() {
		return opponent;
	}
	public void setOpponent(GridArea opponent) {
		this.opponent = opponent;
	}
	public Point getSelected()
	{
		Point temp = selected;
		selected = null;
		//mainHandle.selectedShip = 0;		//be sure to get the ship before getSelected
		return temp;
	}

	@Override
	public Dimension getPreferredSize()		{	return new Dimension(30*10,30*10);	}

	public void setArea(Point point, int contents)
	{
		area[point.y][point.x] = contents;
	}

	
	public int getArea(Point point, int[][] areatocheck)
	{
		if(point.y >= 10 || point.y < 0 || point.x >= 10 || point.x < 0 ) return 0;
		return areatocheck[point.y][point.x];
	}
	
	public int getArea(Point point)
	{
		return getArea(point, area);
	}

	protected boolean validPlacement(int ship, Point location, boolean ver, int[][] areatocheck)
	{
		int shipSize = GameType.getShipSize(ship);
		int newx, newy;
		for (int i = 0; i < shipSize; i++) {
			newx = location.x;
			newy = location.y;
			if(ver) {
				for (int j = -1+location.y; j <= shipSize+location.y; j++) {
					for (int j2 = -1+location.x; j2 <= 1+location.x; j2++) {
						if(getArea(new Point(j2, j), areatocheck) != 0) {
							return false;
						}
					}
				}
				newy = location.y+1;
				if(location.y+shipSize>10) return false;
			}else {
				for (int j = -1+location.y; j <= 1+location.y; j++) {
					for (int j2 = -1+location.x; j2 <= shipSize+location.x; j2++) {
						if(getArea(new Point(j2, j), areatocheck) != 0 ) {
							return false;
						}
					}
				}
				if(location.x+shipSize>10) return false;
				newx = location.x+1;
			}
			if (getArea(new Point(newx, newy), areatocheck) != 0) {
				return false;
			}
		}
		return true;
	}
	protected boolean validPlacement(int ship)
	{
		return validPlacement(ship, cursorLocation, vertical, area);
	}
	protected boolean validPlacement(int ship, boolean ver)
	{
		return validPlacement(ship, cursorLocation, ver, area);
	}
	
	public boolean isInGrid(int x, int y) {
	    return (!(x >= 10 || y >= 10 || x < 0 || y < 0));
	}
	
	public int getGridShip(int x, int y) {
	    return getArea(new Point(x,y))/10;
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;

		g2.fillRect(0, 0, 300, 300);

		g2.setColor(new Color(0,100,90));
		for (int i=1; i<10; i++)
		{
			g2.drawLine(i*BLOCKSIZE,0,i*BLOCKSIZE,300);
			g2.drawLine(0,i*BLOCKSIZE,300,i*BLOCKSIZE);
		}
		g2.setColor(Color.BLACK);
		g2.draw3DRect(0,0,300,300,false);

		g2.setColor(new Color(0,60,60));
		g2.drawString(title, getWidth()/2-(title.length()*3), 325);
		
		int current;

		for (int y=0; y<10; y++) for (int x=0; x<10; x++)
		{
			current = getArea(new Point(x,y));
			if (current !=0 && current % 10 != 0)
			{
				g2.drawImage(gametype.ships[current%10], 30*x, 30*y, this);
			}
		}
	}
	
	public boolean placeShip(Point p, int ship, boolean ver) {
		if(validPlacement(ship, p, ver, area)) {
			if(!ver) {
				for (int i = 0; i < GameType.getShipSize(ship) ; i++) {
					setArea(new Point(p.x+i, p.y), ship*10);
				}
			}else {
				for (int i = 0; i < GameType.getShipSize(ship); i++) {
					setArea(new Point(p.x, p.y+i), ship*10);
				}
			}
			return true;
		}
		return false;
	}

	private class MouseMovingHandler extends MouseMotionAdapter
	{
		private Rectangle lastSelected = new Rectangle();

		@Override
		public void mouseMoved(MouseEvent e)
		{
			int x = (int)(e.getPoint().getX()/BLOCKSIZE);
			int y = (int)(e.getPoint().getY()/BLOCKSIZE);
			
			if(x<10 && y<10 && gridRects[x][y]!=lastSelected)
			{
				lastSelected = gridRects[x][y];
				cursorLocation = new Point(x,y);
				repaint();
			}
		}
	}

	private class MouseHandler extends MouseAdapter
	{
		@Override
		public void mousePressed(MouseEvent e)
		{
			if(e.getButton() == MouseEvent.BUTTON1)
			{
				selected = cursorLocation;
				System.out.println("Selecionou(" + getTitle() + "): " + selected);
				if(getTitle().equals("Home Field") && player.getSelectedShip() != GameType.IDLE) {
					if(validPlacement(getPlayer().getSelectedShip())) {
						placeShip(selected, getPlayer().getSelectedShip(), vertical);
						board.disableShip(getPlayer().getSelectedShip());
						getPlayer().setSelectedShip(GameType.IDLE);
					}
				}else if(getTitle().equals("Opponent's Field") && board.gameStarted
						&& getOpponent().canFire() && firedUpon(selected) != 0){
					fire();
				}

			}
			if(e.getButton() == MouseEvent.BUTTON3)
			{
				vertical = !vertical;
				repaint();
			}
		}
	}
	public void placeShips() {
		for (int i = 3; i <= 7; i++) {
			boolean placedship = false;
			boolean isvertical = random.nextBoolean();
			while(!placedship) {
				Point location = new Point(random.nextInt(10),random.nextInt(10));
				placedship = placeShip(location, i, isvertical);
			}
		}
	}
	public void addSunkShips(){
		ArrayList<Integer> shipsToRemove = new ArrayList<Integer>();
		for (int ship : shipsAlive) {
			boolean found = false;
			for (int j = 0; j < area.length; j++) {
				for (int i = 0; i < area.length; i++) {
					if(getArea(new Point(i,j)) == ship*10) {
						found = true;
					}
				}
			}
			if(!found) {
				shipsToRemove.add(Integer.valueOf(ship));
			}
		}
		for (Integer ship : shipsToRemove) {
			shipsAlive.remove(Integer.valueOf(ship));
		}
	}
	public int firedUpon(Point p) {
		// 0 - Failed
		// 1 - Splash
		// 2 - Hit
		if(p.x >= 10 || p.y >= 10 || p.x < 0 || p.y < 0) return 0;
		int value = area[p.y][p.x];
		int returnvalue;
		if((value % 10) != 2 && value != 1) {
			if(value > 0) {
				this.targetsHit += 1;
				area[p.y][p.x] += 2;
				returnvalue = 2;
				addSunkShips();
			}else {
				area[p.y][p.x] = 1;
				returnvalue = 1;
			}
			repaint();
			if(checkLost()) {
				this.setCanFire(false);
				if(getTitle().equals("Opponent's Field")) {
					board.player.addGame(true, null);
				}else {
					player.addGame(false, null);
				}
				board.gameStarted = false;
			}
			return returnvalue;
		}else {
			return 0;
		}
	}
	private int fireAt(Point p) {
		int result = getOpponent().firedUpon(p);
		if(result != 0) {
			placed[p.y][p.x] = result;
		}
		return result;
	}
	private boolean checkLost() {
		return targetsHit == 17;
	}
	private boolean canFire() {
		return canFire;
	}
	private void setCanFire(boolean canFire) {
		this.canFire = canFire;
	}
	private int alreadyHit(Point shot) {
		if(shot.y >= 10 || shot.x >= 10 || shot.x < 0 || shot.y < 0) return 0;
		return placed[shot.y][shot.x];
	}
	public boolean makesSensetoFire(Point shot) {
		if(alreadyHit(new Point(shot.x+1, shot.y)) != 0 && 
				alreadyHit(new Point(shot.x-1, shot.y)) != 0 && 
				alreadyHit(new Point(shot.x, shot.y+1)) != 0 && 
				alreadyHit(new Point(shot.x, shot.y-1)) != 0) {
				return false;
			}
			for (int i = -1+shot.y; i <= shot.y+1; i++) {
				for (int j = -1+shot.x; j <= shot.x+1; j++) {
					if(alreadyHit(new Point(j, i)) == 2) {
						return false;
					}
				}
			}
		return true;
	}
	public void resetProbTable() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				probgrid[i][j] = 0;
			}
		}
	}
	public int maxOpponentShipSize() {
		int biggestShip = -1;
		for (int ship : getOpponent().shipsAlive) {
			if(ship > biggestShip) {
				biggestShip = ship;
			}
		}
		return biggestShip;
	}
	public ArrayList<Point> placedShipCells(Point initialpoint, int ship, boolean vertical, int[][] areatocheck){
		ArrayList<Point> points = new ArrayList<Point>();
		if(validPlacement(ship, initialpoint, vertical, areatocheck)) {
			if(vertical) {
				for (int i = 0; i < GameType.getShipSize(ship); i++) {
					points.add(new Point(initialpoint.x, initialpoint.y+i));
				}
			}else {
				for (int i = 0; i < GameType.getShipSize(ship) ; i++) {
					points.add(new Point(initialpoint.x+i, initialpoint.y));
				}
			}
		}
		
		return points;	
	}
	public void fire() {
		if(!canFire()) {
			return;
		}
		
		for (Iterator<Point> iterator = queuedShots.iterator(); iterator.hasNext();) {
		    Point shot = iterator.next();
		    int shotstatus = fireAt(shot);
		    iterator.remove();
			if(shotstatus != 0) {
				if(shotstatus == 1) {
					queuedShots.clear();
				}
				return;
			}
		}
		boolean tryvertical = random.nextBoolean();
		for (int i=0, k=3; i < searchPositions.size() && k >= 0; i++, k--) {
			int newi = i;
			if(tryvertical) {
				newi = k;
			}	
			Point shot = searchPositions.get(newi);
			if(shot.x != -1) {
				int shotstatus = getOpponent().firedUpon(shot);
				if(shotstatus != 0) {
					if(shotstatus == 2) {
						if(newi == 0 || newi == 1) {
							//Right or Left
							searchPositions.get(2).x = -1;
							searchPositions.get(3).x = -1;
							if(newi == 0) {
								for (int j = 1; j <= GameType.getShipSize(maxOpponentShipSize())-1; j++) {
									queuedShots.add(new Point(shot.x+j, shot.y));
								}
							}else {
								for (int j = 1; j <= GameType.getShipSize(maxOpponentShipSize())-1; j++) {
									queuedShots.add(new Point(shot.x-j, shot.y));
								}
							}
						}else if(newi == 2 || newi == 3) {
							//Up or Down
							searchPositions.get(0).x = -1;
							searchPositions.get(1).x = -1;
							if(newi == 2) {
								for (int j = 1; j <= 4; j++) {
									queuedShots.add(new Point(shot.x, shot.y+j));
								}
							}else {
								for (int j = 1; j <= 4; j++) {
									queuedShots.add(new Point(shot.x, shot.y-j));
								}
							} 
						}
					}
					shot.x = -1;
					return;
				}
			}
			shot.x = -1;
		}
		while(true) {
			final boolean RANDOM = false;
			Point shot = new Point(random.nextInt(10),random.nextInt(10));
			if(RANDOM) {
				shot = new Point(random.nextInt(10),random.nextInt(10));
			}else {
				int highestprob = -1;
				for (int i = 0; i < 10; i++) {
					for (int j = 0; j < 10; j++) {
						if(probgrid[i][j] > highestprob && makesSensetoFire(new Point(j,i)) && probgrid[i][j] > 0) {
							highestprob = probgrid[i][j];
							shot = new Point(j,i);
						}
					}
				}
			}
			if(!makesSensetoFire(shot)) {
				continue;
			}
			int status = fireAt(shot);
			
			if(status != 0) {
				if(status == 2) {
					searchPositions.set(0, new Point(shot.x+1, shot.y));
					searchPositions.set(1, new Point(shot.x-1, shot.y));
					searchPositions.set(2, new Point(shot.x, shot.y+1));
					searchPositions.set(3, new Point(shot.x, shot.y-1));
				}
				break;
			}
			
		}
		resetProbTable();
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				for (int shipAlive : getOpponent().shipsAlive) {
					for (Point point : placedShipCells(new Point(i, j), shipAlive, true, placed)) {
						probgrid[point.y][point.x] += GameType.getShipSize(shipAlive)*2;
					}
					for (Point point : placedShipCells(new Point(i, j), shipAlive, false, placed)) {
						probgrid[point.y][point.x] += GameType.getShipSize(shipAlive)*2;
					}
				}
			}
		}	
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				System.out.print(probgrid[i][j] + " ");
			}
			System.out.print("\n");
		}
		getOpponent().setCanFire(true);
	}
}