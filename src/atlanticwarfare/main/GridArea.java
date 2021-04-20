package atlanticwarfare.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import atlanticwarfare.database.Player;
import atlanticwarfare.design.Game;

public class GridArea extends JPanel
{
	private static final long serialVersionUID = -5546485795145800928L;
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
	
	//int[10][10];

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
	public void setPlayer(Player player) {
		this.player = player;
	}
	public Player getPlayer() {
		return player;
	}
	public GridArea(String title, Game board)
	{
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

	public void setArea(Point where, int contents)
	{
		area[(int)where.getX()][(int)where.getY()] = contents;
	}

	public int getArea(Point check)
	{
		return area[(int)check.getX()][(int)check.getY()];
	}

	protected boolean validPlacement(int ship, Point location)
	{
		int shipSize = Ship.spaces[ship-2];
		if (vertical)
		{
			if ((int)location.getY() + shipSize > 10) return false;
			for (int i = 0; i < shipSize; i++)
				if (area[(int)location.getY()+i][(int)location.getX()] != 0) {
					return false;
				}
		}else{
			if ((int)location.getX() + shipSize > 10) return false;
			for (int i = 0; i < shipSize; i++)
				if (area[(int)location.getY()][(int)location.getX()+i] != 0) {
					return false;
				}
		}
		return true;
	}
	protected boolean validPlacement(int ship)
	{
		return validPlacement(ship, cursorLocation);
	}
	
	public boolean isInGrid(int x, int y) {
	    //Check if a position is valid in the grid
	    if(x < 0 || y < 0) return false;
	    if(x >= 10 || y >= 10) return false;
	    return true;
	}
	
	public int getGridShip(int x, int y) {
	    //Check if a position is valid in the grid
	    return area[y][x]/10;
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;

		//GradientPaint gp = new GradientPaint(0.0f, 0.0f, new Color(40,2020,160),
		// 250.0f, 250.0f, new Color(40,180,230));
		//g2.setPaint(gp);
		g2.fillRect(0, 0, 300, 300);

		g2.setColor(new Color(0,100,90));
		for (int i=1; i<10; i++)
		{
			g2.drawLine(i*30,0,i*30,300);
			g2.drawLine(0,i*30,300,i*30);
		}
		g2.setColor(Color.BLACK);
		g2.draw3DRect(0,0,300,300,false);

		g2.setColor(new Color(0,60,60));
		g2.drawString(title, getWidth()/2-(title.length()*3), 325);
		
		int current;

		for (int y=0; y<10; y++) for (int x=0; x<10; x++)
		{
			current = area[y][x];
			if (area[y][x]!=0)
			{
				if (current % 10 != 0)
				{
					g2.drawImage(gametype.ships[current%10], 30*x, 30*y, this);
				}
			}
		}
	}

	private class MouseMovingHandler extends MouseMotionAdapter
	{
		private Rectangle lastSelected = new Rectangle();

		@Override
		public void mouseMoved(MouseEvent e)
		{
			int x = (int)(e.getPoint().getX()/30);
			int y = (int)(e.getPoint().getY()/30);
			
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
						if(!vertical) {
							for (int i = 0; i < getPlayer().getShipSize(); i++) {
								setArea(new Point(selected.x+1, selected.y), getPlayer().getSelectedShip()*10);
							}
						}else {
							for (int i = 0; i < getPlayer().getShipSize(); i++) {
								setArea(new Point(selected.x, selected.y+1), getPlayer().getSelectedShip()*10);
							}
						}
						board.disableShip(getPlayer().getSelectedShip());
						getPlayer().setSelectedShip(GameType.IDLE);
					}
				}else if(getTitle().equals("Opponent's Field") && board.gameStarted){
					if(getOpponent().canFire() && firedUpon(new Point(selected.x, selected.y))) {
						fire();
					}
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
			boolean placed = false;
			boolean isvertical = random.nextBoolean();
			while(!placed) {
				Point location = new Point(random.nextInt(10),random.nextInt(10));
				if(isvertical) {
					vertical = true;
					if(validPlacement(i, location)) {
						for (int j = 0; j < Ship.spaces[i-2]; j++) {
							area[location.y+j][location.x] = i*10;
						}
						placed = true;
					}
				}else {
					vertical = false;
					if(validPlacement(i, location)) {
						for (int j = 0; j < Ship.spaces[i-2]; j++) {
							area[location.y][location.x+j] = i*10;
						}
						placed = true;
					}
				}
			}
		}
	}
	public boolean firedUpon(Point p) {
		int value = area[p.y][p.x];
		if((value % 10) != 2 && value != 1) {
			if(value > 0 ) {
				this.targetsHit += 1;
				area[p.y][p.x] += 2;
			}else {
				area[p.y][p.x] = 1;
			}
			repaint();
			getOpponent().setCanFire(false);
			if(checkLost()) {
				this.setCanFire(false);
				if(getTitle().equals("Opponent's Field")) {
					board.player.addGame(true, null);
				}else {
					player.addGame(false, null);
				}
				board.gameStarted = false;
			}
			return true;
		}else {
			return false;
		}
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
	
	public void fire() {
		while(!getOpponent().firedUpon(new Point(random.nextInt(10),random.nextInt(10)))) {
			
		};
		getOpponent().setCanFire(true);
	}
}