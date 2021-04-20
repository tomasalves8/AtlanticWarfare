package atlanticwarfare.design;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import atlanticwarfare.database.Player;

public class FormLogin extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	private JTextField tfNomeUtilizador;
	private JPasswordField tfPalavraChave;

	public FormLogin() {
		super();

		setSize(300,200);
		getContentPane().setLayout(null);
		setLocationRelativeTo(null);
		setResizable(false);
		criarTexto();
		criarCaixaTexto();
		criarBotoes();

		setVisible(true);
	}

	public void criarTexto() {
		JLabel lblNomeUtilizador = new JLabel("Nome de Utilizador:");
		lblNomeUtilizador.setBounds(10, 11, 124, 14);
		getContentPane().add(lblNomeUtilizador);

		JLabel lblPalavraChave = new JLabel("Palavra Chave:");
		lblPalavraChave.setBounds(10, 53, 124, 14);
		getContentPane().add(lblPalavraChave);
	}

	public void criarCaixaTexto() {
		tfNomeUtilizador = new JTextField();
		tfNomeUtilizador.setBounds(160, 8, 86, 20);
		getContentPane().add(tfNomeUtilizador);
		tfNomeUtilizador.setColumns(10);

		tfPalavraChave = new JPasswordField();
		tfPalavraChave.setBounds(160, 50, 86, 20);
		getContentPane().add(tfPalavraChave);
		tfPalavraChave.setColumns(10);
	}

	public void criarBotoes() {
		JButton btnLogin = new JButton("Login", new ImageIcon("Images/login.png"));
		btnLogin.setBounds(12,getHeight()-100,125,43);
		btnLogin.addActionListener(this);
		btnLogin.setActionCommand("Login");
		getContentPane().add(btnLogin);

		JButton btnRegister = new JButton("Registrar", new ImageIcon("Images/register.png"));
		btnRegister.setBounds(btnLogin.getBounds().x + 135,btnLogin.getBounds().y,125,43);
		btnRegister.addActionListener(this);
		btnRegister.setActionCommand("Registar");
		getContentPane().add(btnRegister);
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
					JOptionPane.showMessageDialog(new JFrame(), "Utilizador não encontrado!", "ERRO",
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