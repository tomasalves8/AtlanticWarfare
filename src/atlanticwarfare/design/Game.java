package atlanticwarfare.design;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import atlanticwarfare.database.Player;
import atlanticwarfare.main.*;

import atlanticwarfare.main.GameType;
import atlanticwarfare.main.GridArea;

import javax.swing.JButton;

public class Game extends JFrame implements ActionListener {
	private static final long serialVersionUID = 5239575538377887828L;
	public Player player;
	private shipButton btnAircraft, btnBattleShip, btnCruiser, btnSubmarine, btnDestroyer;
	private ArrayList<shipButton> shipButtons;
	public boolean gameStarted = false;
	private GridArea enemyOcean;
	public Game(Player player) {
		super();
		this.player = player;
		setSize(800,510);
		setResizable(false);
		HomeField homeOcean = new HomeField("Home Field", this);
		homeOcean.setPlayer(player);
		enemyOcean = new GridArea("Opponent's Field", this);
		enemyOcean.setOpponent(homeOcean);
		homeOcean.setOpponent(enemyOcean);
		JPanel fields = new JPanel();
		fields.setLayout(new FlowLayout());
		
		fields.add(homeOcean);
		fields.add(enemyOcean);
		fields.setBounds(0,10,800,360);
		//fields.setBackground(new Color(0,0,255));
		getContentPane().add(fields, BorderLayout.CENTER);
		
		JPanel ships = new JPanel();
		ships.setLayout(null);
		ships.setBounds(0, 380, 605, 300);
		//ships.setBackground(new Color(0,255,0));
		
		shipButtons = new ArrayList<shipButton>();
		
		btnAircraft = new shipButton("Aircraft Carrier", GameType.CARRIER);
		btnAircraft.setBounds(getWidth()/7+5,ships.getBounds().y,135,20);
		btnAircraft.addActionListener(this);
		ships.add(btnAircraft);
		shipButtons.add(btnAircraft);
		
		btnBattleShip = new shipButton("Battleship", GameType.BATTLESHIP);
		btnBattleShip.setBounds(btnAircraft.getBounds().x,btnAircraft.getBounds().y+30,135,20);
		btnBattleShip.addActionListener(this);
		ships.add(btnBattleShip);
		shipButtons.add(btnBattleShip);
		
		btnCruiser = new shipButton("Cruiser", GameType.CRUISER);
		btnCruiser.setBounds(btnAircraft.getBounds().x + btnAircraft.getWidth() + 5,btnAircraft.getBounds().y,135,20);
		btnCruiser.addActionListener(this);
		ships.add(btnCruiser);
		shipButtons.add(btnCruiser);
		
		btnSubmarine = new shipButton("Submarine", GameType.SUBMARINE);
		btnSubmarine.setBounds(btnBattleShip.getBounds().x + btnBattleShip.getWidth() + 5,btnBattleShip.getBounds().y,135,20);
		btnSubmarine.addActionListener(this);
		ships.add(btnSubmarine);
		shipButtons.add(btnSubmarine);
		
		btnDestroyer = new shipButton("Destroyer", GameType.DESTROYER);
		btnDestroyer.setBounds(btnCruiser.getBounds().x + btnCruiser.getWidth() + 5,btnCruiser.getBounds().y,135,20);
		btnDestroyer.addActionListener(this);
		ships.add(btnDestroyer);
		shipButtons.add(btnDestroyer);
		
		getContentPane().add(ships);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource().getClass() == shipButton.class) {
			player.setSelectedShip(((shipButton)arg0.getSource()).shipID);
		}
	}
	
	public void disableShip(int i) {
		int disabledships = 0;
		for(shipButton button : shipButtons) {
			if(i == button.shipID) {
				button.setEnabled(false);
			}
			if(!button.isEnabled()) {
				disabledships += 1;
			}
		}
		if(disabledships == 5) {
			System.out.println("Game STARTED");
			gameStarted = true;
			enemyOcean.placeShips();
		}
	}
}

class shipButton extends JButton
{
	private static final long serialVersionUID = 1L;
	public int shipID;
	public shipButton(String title, int shipID) {
		super(title);
		this.shipID = shipID;
	}
}
class HomeField extends GridArea
{
	private static final long serialVersionUID = 1L;
	public HomeField(String title, Game Window)
	{
		super(title, Window);
	}
	

	@Override
	public void paintComponent(Graphics g)
	{
		
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;

		int current;
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
		if(getPlayer().getSelectedShip()!=GameType.IDLE) {
			if (!validPlacement(getPlayer().getSelectedShip())){
				g2.setColor(new Color(150,0,0));
			}
			int posX = ((int) cursorLocation.getX())*30;
			int posY = ((int) cursorLocation.getY())*30;
			
			int size = 30*getPlayer().getShipSize();
			if (vertical) {
				while(size+posY > getPreferredSize().height) {
					size -= 30;
				}
				g2.fill3DRect(posX, posY, 30, size, false);
			}else {
				while(size+posX > getPreferredSize().width) {
					size -= 30;
				}
				g2.fill3DRect(posX, posY, size, 30, false);
			}
		}
	}
}