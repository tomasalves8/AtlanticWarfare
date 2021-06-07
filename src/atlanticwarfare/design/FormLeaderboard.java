package atlanticwarfare.design;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import atlanticwarfare.database.Player;
import atlanticwarfare.utilities.JCountryComboBox;


public class FormLeaderboard extends JFrame implements MouseListener, ActionListener {
	private static final long serialVersionUID = -1999076907143996266L;
	private JTable table;
	private final String[] columnNames = {"Name",
			"Games Played",
			"Games Won",
	"Games Lost"};
	private String currentOrder = "gamesWon";
	private JCountryComboBox countryBox;

	public FormLeaderboard() {
		super();
		setSize(513,302);
		criarFundo();
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);


		setIconImage(new ImageIcon(System.getProperty("user.dir") + "//Images//logo.png").getImage());
		DefaultTableModel model = new DefaultTableModel( Player.getTop(10, "gamesWon", "GLOBAL"), columnNames);
		table = new JTable(model);
		table.setEnabled(false);
		JScrollPane scroll = new JScrollPane(table);
		table.getTableHeader().addMouseListener(this);
		table.getTableHeader().setBackground(new Color(58, 104, 171));
		table.getTableHeader().setForeground(Color.WHITE);
		scroll.setBounds(60,100, 380, 100);
		scroll.setBackground(new Color(58, 104, 171));
		getContentPane().add(scroll);

		countryBox = new JCountryComboBox(true);
		countryBox.setBounds(60, 225, 100, 20);
		countryBox.addActionListener(this);
		countryBox.setFocusable(false);
		getContentPane().add(countryBox);
		setResizable(false);
		setVisible(true);
	}
	public static void main(String[] args) {
		new FormLeaderboard();
	}
	public void tableRefresh() {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setNumRows(0);
		Object[][] players = Player.getTop(10, currentOrder.replace(" ", ""), countryBox.getSelectedCountryCode());
		for (int i = 0; i < players.length; i++) {
			model.addRow(players[i]);
		}
	}
	public void criarFundo(){
		try {
			setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("Images/bg.png")))));
			setBounds(0, 0, getWidth(), getHeight());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		pack();
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		int col = table.columnAtPoint(e.getPoint());
		currentOrder = table.getColumnName(col);
		tableRefresh();
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// Not needed
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// Not needed
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// Not needed
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// Not needed
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == countryBox) {
			tableRefresh();
		}
	}
}
