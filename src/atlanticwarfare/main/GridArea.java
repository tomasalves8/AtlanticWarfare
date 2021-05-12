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
	private static final int BLOCKSIZE = 30;
	protected int area [][] = new int[10][10];
	protected int placed [][] = new int[10][10];
	
	protected boolean vertical = false;
	private String title;
	private Point selected;
	private boolean canFire;
	private int targetsHit=0;

	protected Point cursorLocation;
	private boolean shipsVisible = false;
	private Rectangle gridRects[][] = new Rectangle[10][10];
	private Player player;
	private GridArea opponent;
	private Game board;
	protected GameType gametype;
	protected Random random;
	protected ArrayList<Integer> shipsAlive = new ArrayList<Integer>();
	private int dificulty;
	
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
		setOpaque(false);
	}
	public String getTitle() {
		return title;
	}
	protected GridArea getOpponent() {
		return opponent;
	}
	public void setOpponent(GridArea opponent) {
		this.opponent = opponent;
	}
	public Point getSelected()
	{
		Point temp = selected;
		selected = null;
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
						if(j >= location.y && j < shipSize+location.y && j2 == location.x) {
							if(getArea(new Point(j2, j), areatocheck) != 0) {
								return false;
							}
						}else {
							if(getArea(new Point(j2, j), areatocheck) != 0 && getArea(new Point(j2, j), areatocheck) != 1 ) {
								return false;
							}
						}
					}
				}
				newy = location.y+1;
				if(location.y+shipSize>10) return false;
			}else {
				for (int j = -1+location.y; j <= 1+location.y; j++) {
					for (int j2 = -1+location.x; j2 <= shipSize+location.x; j2++) {
						if(j2 >= location.x && j2 < shipSize+location.x && j == location.y) {
							if(getArea(new Point(j2, j), areatocheck) != 0) {
								return false;
							}
						}else {
							if(getArea(new Point(j2, j), areatocheck) != 0 && getArea(new Point(j2, j), areatocheck) != 1 ) {
								return false;
							}
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
	public void setShipsVisible(boolean bool) {
		this.shipsVisible = bool;
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;

		g2.setColor(new Color(255,255,255));
		for (int i=1; i<10; i++)
		{
			g2.drawLine(i*BLOCKSIZE,0,i*BLOCKSIZE,300);
			g2.drawLine(0,i*BLOCKSIZE,300,i*BLOCKSIZE);
		}
		g2.setColor(Color.WHITE);
		g2.draw3DRect(0,0,299,299,false);

		g2.setColor(Color.WHITE);
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
		
		if(shipsVisible) {
			for (int y=0; y<10; y++) for (int x=0; x<10; x++)
			{
				current = area[y][x];
				if (area[y][x]!=0)
				{
					int ship = current/10;
					if (ship != 0) {
						if((!isInGrid(y-1, x) || getGridShip(x, y-1) != ship) && (!isInGrid(y, x+1) || getGridShip(x+1, y) != ship) && (!isInGrid(y, x-1) || getGridShip(x-1, y)  != ship)) {
							g2.drawImage(gametype.shipVertical[ship], 30*x, 30*y, this);
						}else {
							if((!isInGrid(y, x-1) || getGridShip(x-1, y) != ship) && (!isInGrid(y-1, x) || getGridShip(x, y-1) != ship)) {
								g2.drawImage(gametype.ships[ship], 30*x, 30*y, this);
							}
						}
						
					}
				
					if (current % 10 != 0)
					{
						g2.drawImage(gametype.ships[current%10], 30*x, 30*y, this);
					}
				}
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
						repaint();
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
		ArrayList<Integer> shipsToRemove = new ArrayList<>();
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
				if(getTitle().equals("Opponent's Field")) {
					board.disableEnemyShip(ship);
				}
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
				setShipsVisible(true);
				getOpponent().setShipsVisible(true);
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
	protected int fireAt(Point p) {
		int result = getOpponent().firedUpon(p);
		if(result != 0) {
			placed[p.y][p.x] = result;
		}
		return result;
	}
	private boolean checkLost() {
		return targetsHit == 17;
	}
	protected boolean canFire() {
		return canFire;
	}
	protected void setCanFire(boolean canFire) {
		this.canFire = canFire;
	}
	protected int alreadyHit(Point shot) {
		if(shot.y >= 10 || shot.x >= 10 || shot.x < 0 || shot.y < 0) return 0;
		return placed[shot.y][shot.x];
	}
	public int maxOpponentShipSize() {
		int biggestShip = 3;
		for (int ship : getOpponent().shipsAlive) {
			if(ship > biggestShip) {
				biggestShip = ship;
			}
		}
		return biggestShip;
	}
	public int getDifficulty() {
		return dificulty;
	}
	public void setDificulty(int dificulty) {
		this.dificulty = dificulty;
	}
}