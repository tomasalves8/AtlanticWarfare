package atlanticwarfare.design;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;

import atlanticwarfare.database.Player;
import atlanticwarfare.main.EnemyArea;
import atlanticwarfare.main.GameType;
import atlanticwarfare.main.HomeArea;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;

public class Game extends JFrame implements ActionListener {
	private static final long serialVersionUID = 5239575538377887828L;
	public Player player;
	private ShipButton btnAircraft, btnBattleShip, btnCruiser, btnSubmarine, btnDestroyer;
	private ArrayList<ShipButton> shipButtons;
	private ArrayList<ShipButton> enemyShipButtons;
	public boolean gameStarted = false;
	private HomeArea homeOcean;
	private EnemyArea enemyOcean;
	private JMenuItem newGame, placeShips;
	private JPanel fields;
	private JMenuItem logOut,leaderBoard,exit,recentGames;
	private long startTime;
	private JLabel temporizador;
	public long elapsedSeconds = 0;
	
	public Game(Player player) {
		super();
		this.player = player;
		
		new Thread() {
			@Override
			 public void run() {
				 while(true) {
					 if(gameStarted) {
						 long elapsedTime = System.currentTimeMillis() - startTime;
						 elapsedSeconds = elapsedTime / 1000;
						 long secondsDisplay = elapsedSeconds % 60;
						 long elapsedMinutes = elapsedSeconds / 60;
						 temporizador.setText(String.format("%02d", elapsedMinutes) + ":" + String.format("%02d", secondsDisplay));
					 }
					 try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
				 }
			 }
		}.start();
		setSize(1044,512);
		setResizable(false);
		setIconImage(new ImageIcon(System.getProperty("user.dir") + "//Images//logo.png").getImage());
		temporizador = new JLabel("00:00");
		temporizador.setFont(new Font("Serif", Font.PLAIN, 25));
		temporizador.setBounds(900, 20, 100, 50);
		
		homeOcean = new HomeArea("Home Field", this);
		homeOcean.setShipsVisible(true);
		homeOcean.setBounds(175, 90, 300, 300);
		homeOcean.setPlayer(player);
		
		
		enemyOcean = new EnemyArea("Opponent's Field", this);
		enemyOcean.setBounds(569, homeOcean.getBounds().y, 300, 300);
		enemyOcean.setOpponent(homeOcean);
		homeOcean.setOpponent(enemyOcean);
		fields = new JPanelBackgroundImage(new ImageIcon(System.getProperty("user.dir") + "//Images//fundoJogo.png"));
		fields.setLayout(null);
		
		fields.add(homeOcean);
		fields.add(enemyOcean);
		fields.add(temporizador);
		fields.setBounds(0,0,800,510);
		fields.setBackground(new Color(47, 103, 176));
		getContentPane().add(fields, BorderLayout.CENTER);
		
		shipButtons = new ArrayList<>();
		enemyShipButtons = new ArrayList<>();
		
		btnAircraft = new ShipButton(GameType.CARRIER);
		btnAircraft.setBounds(20, homeOcean.getBounds().y, 135,30);
		btnAircraft.setFocusable(false);
		btnAircraft.setContentAreaFilled(false);
		btnAircraft.setBorderPainted(false);
		btnAircraft.addActionListener(this);
		fields.add(btnAircraft);
		shipButtons.add(btnAircraft);
		
		btnBattleShip = new ShipButton(GameType.BATTLESHIP);
		btnBattleShip.setBounds(btnAircraft.getBounds().x+8,btnAircraft.getBounds().y+50,135,30);
		btnBattleShip.setFocusable(false);
		btnBattleShip.setContentAreaFilled(false);
		btnBattleShip.setBorderPainted(false);
		btnBattleShip.addActionListener(this);
		fields.add(btnBattleShip);
		shipButtons.add(btnBattleShip);
		
		btnCruiser = new ShipButton(GameType.CRUISER);
		btnCruiser.setBounds(btnBattleShip.getBounds().x+18,btnBattleShip.getBounds().y+50,120,30);
		btnCruiser.setFocusable(false);
		btnCruiser.setContentAreaFilled(false);
		btnCruiser.setBorderPainted(false);
		btnCruiser.addActionListener(this);
		fields.add(btnCruiser);
		shipButtons.add(btnCruiser);
		
		btnSubmarine = new ShipButton(GameType.SUBMARINE);
		btnSubmarine.setBounds(btnCruiser.getBounds().x,btnCruiser.getBounds().y+50,135,30);
		btnSubmarine.setFocusable(false);
		btnSubmarine.setContentAreaFilled(false);
		btnSubmarine.setBorderPainted(false);
		btnSubmarine.addActionListener(this);
		fields.add(btnSubmarine);
		shipButtons.add(btnSubmarine);
		
		btnDestroyer = new ShipButton(GameType.DESTROYER);
		btnDestroyer.setBounds(btnSubmarine.getBounds().x+10,btnSubmarine.getBounds().y+50,135,30);
		btnDestroyer.setFocusable(false);
		btnDestroyer.setContentAreaFilled(false);
		btnDestroyer.setBorderPainted(false);
		btnDestroyer.addActionListener(this);
		fields.add(btnDestroyer);
		shipButtons.add(btnDestroyer);
		
		int[] xValues = {8,15,5,10,10};
		int x = enemyOcean.getX()+320;
		int y = enemyOcean.getY();
		for (int i = 7; i >= 3; i--) {
			ShipButton shipbtn = new ShipButton(i);
			shipbtn.setBounds(x,y,135,30);
			shipbtn.setFocusable(false);
			shipbtn.setContentAreaFilled(false);
			shipbtn.setBorderPainted(false);
			enemyShipButtons.add(shipbtn);
			fields.add(shipbtn);
			y += 50;
			x -= xValues[7-i];
		}
		
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		JMenuBar menuBar = new JMenuBar() {
			private static final long serialVersionUID = 1L;
			@Override
			public void paintComponent(Graphics g){
                g.drawImage(Toolkit.getDefaultToolkit().getImage(System.getProperty("user.dir") + "//Images//corFundo.png"),0,0, getWidth(), getHeight(),this);
            }
		};
		setJMenuBar(menuBar);
		
		JMenu menuGame=new JMenu("Game");
		menuGame.setForeground(Color.white);
		
		newGame = new JMenuItem("New Game");
		newGame.addActionListener(this);
		menuGame.add(newGame);
		
		placeShips = new JMenuItem("Place Ships");
		placeShips.addActionListener(this);
		menuGame.add(placeShips);
		
		JMenu menuGameDifficulty=new JMenu("Difficulty");
		ButtonGroup difficultyGroup = new ButtonGroup();
		difficultyJMenuRadioButton gameDifficulty1 = new difficultyJMenuRadioButton("Easy", GameType.EASY);
		gameDifficulty1.setSelected(true);
		difficultyJMenuRadioButton gameDifficulty2 = new difficultyJMenuRadioButton("Medium", GameType.MEDIUM);
		difficultyJMenuRadioButton gameDifficulty3 = new difficultyJMenuRadioButton("Hard", GameType.HARD);
		gameDifficulty1.addActionListener(this);
		gameDifficulty2.addActionListener(this);
		gameDifficulty3.addActionListener(this);
		menuGameDifficulty.add(gameDifficulty1);
		menuGameDifficulty.add(gameDifficulty2);
		menuGameDifficulty.add(gameDifficulty3);
		difficultyGroup.add(gameDifficulty1);
		difficultyGroup.add(gameDifficulty2);
		difficultyGroup.add(gameDifficulty3);
		menuGame.add(menuGameDifficulty);
		menuBar.add(menuGame);
		
		menuBar.add(Box.createHorizontalGlue());
		
		JMenu menuLogged =new JMenu("Logged in, " + player.getUsername());
		menuLogged.setForeground(Color.white);
		
		leaderBoard = new JMenuItem("Leaderboard");
		leaderBoard.addActionListener(this);
		menuLogged.add(leaderBoard);
		
		recentGames = new JMenuItem("Recent Games");
		recentGames.addActionListener(this);
		menuLogged.add(recentGames);
		
		logOut = new JMenuItem("Log Out");
		logOut.addActionListener(this);
		menuLogged.add(logOut);
		
		exit = new JMenuItem("Exit");
		exit.addActionListener(this);
		menuLogged.add(exit);
		
		menuBar.add(menuLogged);
		setVisible(true);	
	}
	public void restartGame() {
		//if(gameStarted) return;
		gameStarted = false;
		player.setSelectedShip(GameType.IDLE);
		fields.remove(homeOcean);
		fields.remove(enemyOcean);
		homeOcean = new HomeArea("Home Field", this);
		homeOcean.setShipsVisible(true);
		homeOcean.setBounds(175, 90, 300, 300);
		homeOcean.setPlayer(player);
		
		
		enemyOcean = new EnemyArea("Opponent's Field", this);
		enemyOcean.setBounds(569, homeOcean.getBounds().y, 300, 300);
		enemyOcean.setOpponent(homeOcean);
		homeOcean.setOpponent(enemyOcean);
		fields.add(homeOcean);
		fields.add(enemyOcean);
		revalidate();
		repaint();
		for (ShipButton shipButton : enemyShipButtons) {
			shipButton.setEnabled(true);
		}
		for (ShipButton shipButton : shipButtons) {
			shipButton.setEnabled(true);
		}
	}
	public void placeShips() {
		restartGame();
		player.setSelectedShip(GameType.IDLE);
		homeOcean.placeShips();
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource().getClass() == ShipButton.class) {
			player.setSelectedShip(((ShipButton)arg0.getSource()).shipID);
		}else if(arg0.getSource() == logOut) {
			dispose();
			new FormLogin();
		}else if(arg0.getSource() == leaderBoard) {
			new FormLeaderboard();
		}else if(arg0.getSource() == exit) {
			System.exit(0);
		}else if(arg0.getSource() == recentGames) {
			new FormRecentGames(this);
		}else if(arg0.getSource() == newGame) {
			restartGame();
		}else if(arg0.getSource().getClass() == difficultyJMenuRadioButton.class) {
			enemyOcean.setDificulty(((difficultyJMenuRadioButton)arg0.getSource()).difficulty);
		}else if(arg0.getSource() == placeShips) {
			if(gameStarted) return;
			for (ShipButton shipButton : shipButtons) {
				shipButton.setEnabled(false);
			}
			placeShips();
			startGame();
		}
	}
	
	public void disableShip(int i) {
		int disabledships = 0;
		for(ShipButton button : shipButtons) {
			if(i == button.shipID) {
				button.setEnabled(false);
			}
			if(!button.isEnabled()) {
				disabledships += 1;
			}
		}
		if(disabledships == 5) {
			startGame();
		}
	}
	
	public void disableEnemyShip(int i) {
		enemyShipButtons.get(7-i).setEnabled(false);
	}
	public void startGame() {
		System.out.println("Game STARTED");
		gameStarted = true;
		startTime = System.currentTimeMillis();
		temporizador.setText("00:00");
		enemyOcean.placeShips();
		for (ShipButton shipButton : shipButtons) {
			shipButton.setEnabled(false);
		}
	}
}

class ShipButton extends JButton
{
	private static final long serialVersionUID = 1L;
	private GameType gt = new GameType();
	protected int shipID;
	public ShipButton(int shipID) {
		super();
		this.shipID = shipID;
		setIcon(new ImageIcon(gt.ships[shipID]));
	}
}
class JPanelBackgroundImage extends JPanel
{
	private static final long serialVersionUID = 1L;
	ImageIcon image;
	JPanelBackgroundImage(ImageIcon image)
    {
        this.image = image;
    }
    @Override
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        g.drawImage(image.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
    }
}

class difficultyJMenuRadioButton extends JRadioButtonMenuItem
{
	private static final long serialVersionUID = -4321045424752286387L;
	public int difficulty;
	public difficultyJMenuRadioButton(String value, int difficulty) {
		super(value);
		this.difficulty = difficulty;
	}
}