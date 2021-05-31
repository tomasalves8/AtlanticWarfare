package atlanticwarfare.design;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class FormRecentGames extends JFrame{
	private static final long serialVersionUID = -1999076907143996266L;
	private JTable table;
	private final String[] columnNames = {"Won","Winner",
            "Loser", "Duration"};

	public FormRecentGames(Game game) {
		super();
		setSize(513,302);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		
		DefaultTableModel model = new DefaultTableModel( game.player.getLastGames(), columnNames);
		table = new JTable(model);
		table.setEnabled(false);
		JScrollPane scroll = new JScrollPane(table);
		scroll.setBounds(55,65, 380, 170);
		getContentPane().add(scroll);
		
		setResizable(false);
		setVisible(true);
	}
	public static void main(String[] args) {
		new FormLeaderboard();
	}
}
