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
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		
		DefaultTableModel model = new DefaultTableModel( Player.getTop(10, "gamesWon", "GLOBAL"), columnNames);
		table = new JTable(model);
		table.setEnabled(false);
		JScrollPane scroll = new JScrollPane(table);
		table.getTableHeader().addMouseListener(this);
		scroll.setBounds(55,41, 312, 150);
		getContentPane().add(scroll);
		
		countryBox = new JCountryComboBox(true);
		countryBox.setBounds(391, 60, 100, 20);
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
		// TODO Auto-generated method stub
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == countryBox) {
			tableRefresh();
		}
	}
}
