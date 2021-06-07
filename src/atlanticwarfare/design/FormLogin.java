package atlanticwarfare.design;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import atlanticwarfare.database.Player;
import atlanticwarfare.utilities.HintPasswordField;
import atlanticwarfare.utilities.HintTextField;

public class FormLogin extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	private HintTextField tfUsername;
	private HintPasswordField tfPassword;

	public FormLogin() {
		super();

		setResizable(false);
		getContentPane().setLayout(null);
		
		criarFundo();
		criarCaixaTexto();
		criarBotoes();
		criarImagens();
		setIconImage(new ImageIcon(System.getProperty("user.dir") + "//Images//logo.png").getImage());
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				requestFocus(); 
			}
		});

		setLocationRelativeTo(null);
		getContentPane().requestFocusInWindow();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	public void criarFundo(){
		try {
			setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("Images/Fundo.png")))));
			setBounds(0, 0, getWidth(), getHeight());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		pack();
	}

	public void criarCaixaTexto() {
		tfUsername = new HintTextField(" Username");
		tfUsername.setBounds(100, 200, 200, 20);
		tfUsername.setBorder(null);
		getContentPane().add(tfUsername);		
		tfUsername.setColumns(10);	

		tfPassword = new HintPasswordField(" Password");
		tfPassword.setBounds(100, 250, 200, 20);
		tfPassword.setBorder(null);
		getContentPane().add(tfPassword);
		tfPassword.setColumns(10);
	}

	public void criarBotoes() {
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setBounds(35,340,150,25);
		btnLogin.addActionListener(this);
		btnLogin.setFont(new Font("Verdana", Font.PLAIN, 12));
		btnLogin.setBorder(null);
		btnLogin.setBackground(Color.WHITE);
		btnLogin.setActionCommand("Login");
		getRootPane().setDefaultButton(btnLogin);
		getContentPane().add(btnLogin);

		JButton btnRegister = new JButton("Register");
		btnRegister.setBounds(215,340,150,25);
		btnRegister.addActionListener(this);
		btnRegister.setFont(new Font("Verdana", Font.PLAIN, 12));
		btnRegister.setFocusable(false);
		btnRegister.setBorder(null);
		btnRegister.setBackground(Color.WHITE);
		btnRegister.setActionCommand("Register");
		getContentPane().add(btnRegister);
	}
	
	public void criarImagens(){
		JLabel iconUser = new JLabel(new ImageIcon(System.getProperty("user.dir") + "//Images//nomeUtilizador.png"));
		iconUser.setBounds(80, 201, 16, 16);
		getContentPane().add(iconUser);
		
		JLabel iconPass = new JLabel(new ImageIcon(System.getProperty("user.dir") + "//Images//pass.png"));
		iconPass.setBounds(80, 251, 16, 16);
		getContentPane().add(iconPass);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals("Login")) {
			String nomeUtilizador = tfUsername.getText();
			String palavraChave = new String(tfPassword.getPassword());
			if(!tfUsername.isEmpty() && !palavraChave.isEmpty()) {
				Player player = new Player(nomeUtilizador, palavraChave, "example@email.com");
				if(player.authenticate()) {
					System.out.println("Player Found");
					new Game(player);
					dispose();
				}else {
					JOptionPane.showMessageDialog(new JFrame(), "Player not found!", "ERROR",
							JOptionPane.ERROR_MESSAGE);
				}	
			}else {
				JOptionPane.showMessageDialog(new JFrame(), "You need to fill all of the fields!", "ERROR",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		
		if (ae.getActionCommand().equals("Register")) {
			dispose();
			new FormRegister();
		}

	}
	public static void main(String[] args) {
		new FormLogin();
	}
}