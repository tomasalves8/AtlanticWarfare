package atlanticwarfare.main;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import atlanticwarfare.design.Game;

public class EnemyArea extends GridArea {
	private static final long serialVersionUID = -3611455892756696072L;
	protected int probgrid [][] = new int[10][10];
	private int dificulty;
	
	private ArrayList<Point> queuedShots = new ArrayList<>();
	private ArrayList<Point> searchPositions = new ArrayList<>(4);

	public int getDifficulty() {
		return dificulty;
	}
	public void setDificulty(int dificulty) {
		this.dificulty = dificulty;
	}
	
	private class MouseHandler extends MouseAdapter
	{
		@Override
		public void mousePressed(MouseEvent e)
		{
			if(e.getButton() == MouseEvent.BUTTON1)
			{
				selected = cursorLocation;
				if(board.gameStarted && getOpponent().canFire() && firedUpon(selected) != 0){
					fire();
				}

			}
		}
	}
	
	
	public EnemyArea(String title, Game board) {
		super(title, board);
		addMouseListener(new MouseHandler());
		for (int i = 0; i < 4; i++) {
			searchPositions.add(new Point(-1,-1));
		}
	}
	
	private boolean makesSensetoFire(Point shot) {
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
	
	public void calculateProbability() {
		resetProbTable();
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				for (int shipAlive : getOpponent().shipsAlive) {
					List<Point> shipCells = placedShipCells(new Point(i, j), shipAlive, true, placed);
					shipCells.addAll(placedShipCells(new Point(i, j), shipAlive, false, placed));
					for (Point point : shipCells) {
						probgrid[point.y][point.x] += GameType.getShipSize(shipAlive);
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
		System.out.print("\n");
	}
	
	public List<Point> placedShipCells(Point initialpoint, int ship, boolean vertical, int[][] areatocheck){
		ArrayList<Point> points = new ArrayList<>();
		int shipSize = GameType.getShipSize(ship);
		if(validPlacement(ship, initialpoint, vertical, areatocheck)) {
			for (int i = 0; i < shipSize; i++) {
				Point shot;
				if(vertical) {
					shot = new Point(initialpoint.x, initialpoint.y+i);
				}else {
					shot = new Point(initialpoint.x+i, initialpoint.y);
				}
				if(makesSensetoFire(shot))
					points.add(shot);
			}
		}
		
		return points;	
	}
	public Point getBestShot() {
		int highestprob = 0;
		Point shot = new Point(random.nextInt(10),random.nextInt(10));
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if(probgrid[i][j] > highestprob && makesSensetoFire(new Point(j,i)) && probgrid[i][j] > 0) {
					highestprob = probgrid[i][j];
					shot = new Point(j,i);
				}
			}
		}
		return shot;
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
			Point shot;
			if(getDifficulty() == GameType.EASY || getDifficulty() == GameType.MEDIUM ) {
				shot = new Point(random.nextInt(10),random.nextInt(10));
			}else {
				shot = getBestShot();
			}
			if((getDifficulty() == GameType.MEDIUM || getDifficulty() == GameType.HARD) && !makesSensetoFire(shot)) {
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
		if(getDifficulty() == GameType.HARD)
			calculateProbability();
		
		getOpponent().setCanFire(true);
	}

}
