package atlanticwarfare.design;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

import atlanticwarfare.database.Player;
import atlanticwarfare.utilities.HintPasswordField;
import atlanticwarfare.utilities.HintTextField;

public class FormLogin extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	private HintTextField tfNomeUtilizador;
	private HintPasswordField tfPalavraChave;
	private JLabel iconUser, iconPass;

	public FormLogin() {
		super();

		setSize(400 ,400);
		setResizable(false);
		getContentPane().setLayout(null);
		
		criarFundo();
		criarCaixaTexto();
		criarBotoes();
		criarImagens();
		
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				requestFocus(); 
			}
		});

		setLocationRelativeTo(null);
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
		tfNomeUtilizador = new HintTextField(" Nome de Utilizador");
		tfNomeUtilizador.setBounds(100, 200, 200, 20);
		tfNomeUtilizador.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		getContentPane().add(tfNomeUtilizador);		
		tfNomeUtilizador.setColumns(10);	

		tfPalavraChave = new HintPasswordField(" Palavra Chave");
		tfPalavraChave.setBounds(100, 250, 200, 20);
		tfPalavraChave.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		getContentPane().add(tfPalavraChave);
		tfPalavraChave.setColumns(10);
	}

	public void criarBotoes() {
		JRadioButton btn = new JRadioButton("text");
		getContentPane().add(btn);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setBounds(35,325,150,25);
		btnLogin.addActionListener(this);
		btnLogin.setBorder(null);
		btnLogin.setBackground(Color.WHITE);
		btnLogin.setActionCommand("Login");
		getContentPane().add(btnLogin);

		JButton btnRegister = new JButton("Registar");
		btnRegister.setBounds(215,325,150,25);
		btnRegister.addActionListener(this);
		btnRegister.setFocusable(false);
		btnRegister.setBorder(null);
		btnRegister.setBackground(Color.WHITE);
		btnRegister.setActionCommand("Registar");
		getContentPane().add(btnRegister);
	}
	
	public void criarImagens(){
		iconUser = new JLabel(new ImageIcon(System.getProperty("user.dir") + "//Images//nomeUtilizador.png"));
		iconUser.setBounds(80, 201, 16, 16);
		getContentPane().add(iconUser);
		
		iconPass = new JLabel(new ImageIcon(System.getProperty("user.dir") + "//Images//pass.png"));
		iconPass.setBounds(80, 251, 16, 16);
		getContentPane().add(iconPass);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals("Login")) {
			String nomeUtilizador = tfNomeUtilizador.getText();
			String palavraChave = new String(tfPalavraChave.getPassword());
			if(!nomeUtilizador.equals("") && !palavraChave.equals("")) {
				Player player = new Player(nomeUtilizador, palavraChave, "example@email.com");
				if(player.authenticate()) {
					System.out.println("Utilizador Encontrado");
					new Game(player);
					setVisible(false);
				}else {
					JOptionPane.showMessageDialog(new JFrame(), "Utilizador n�o encontrado!", "ERRO",
							JOptionPane.ERROR_MESSAGE);
				}	
			}else {
				JOptionPane.showMessageDialog(new JFrame(), "Precisa de preencher todos os campos!", "ERRO",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		
		if (ae.getActionCommand().equals("Registar")) {
			new FormRegister();
		}

	}

}