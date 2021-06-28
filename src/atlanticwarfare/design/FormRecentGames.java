package atlanticwarfare.design;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
        createBackground();
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);
        setIconImage(new ImageIcon(System.getProperty("user.dir") + "//Images//logo.png").getImage());
        Object[][] recentGames = game.player.getLastGames();
        DefaultTableModel model = new DefaultTableModel( recentGames, columnNames);
        table = new JTable(model);
        table.setEnabled(false);


        JScrollPane scroll = new JScrollPane(table);
        table.getTableHeader().setBackground(new Color(58, 104, 171));
        table.getTableHeader().setForeground(Color.WHITE);
        scroll.setBounds(60,100, 380, 100);
        scroll.setBackground(new Color(58, 104, 171));
        getContentPane().add(scroll);

        setResizable(false);
        setVisible(true);
    }

    public void createBackground(){
        try {
            setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("Images/bg.png")))));
            setBounds(0, 0, getWidth(), getHeight());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        pack();
    }
}