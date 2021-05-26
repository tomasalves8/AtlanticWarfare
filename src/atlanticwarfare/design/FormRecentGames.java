package atlanticwarfare.design;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import atlanticwarfare.database.Player;
import atlanticwarfare.utilities.JCountryComboBox;

public class FormRecentGames extends JFrame implements ActionListener, MouseListener{
	private static final long serialVersionUID = -1999076907143996266L;
	private JTable table;
	private final String[] columnNames = {"Won","Winner",
            "Loser"};
	private String currentOrder = "gamesWon";
	private JCountryComboBox countryBox;

	public FormRecentGames(Game game) {
		super();
		setSize(513,302);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		
		DefaultTableModel model = new DefaultTableModel( game.player.getLastGames(), columnNames);
		table = new JTable(model);
		table.setEnabled(false);
		JScrollPane scroll = new JScrollPane(table);
		table.getTableHeader().addMouseListener(this);
		scroll.setBounds(55,65, 380, 170);
		getContentPane().add(scroll);
		
		countryBox = new JCountryComboBox(true);
		countryBox.setBounds(201, 34, 100, 20);
		countryBox.addActionListener(this);
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
