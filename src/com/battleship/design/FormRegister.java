package com.battleship.design;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.battleship.database.Player;

public class FormRegister extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	private JTextField tfNomeUtilizador;
	private JPasswordField tfPalavraChave;
	private JTextField tfEmail;

	public FormRegister() {
		super();

		setSize(300,200);
		getContentPane().setLayout(null);
		setLocationRelativeTo(null);
		setResizable(false);
		criarTexto();
		criarCaixaTexto();
		createButtons();

		setVisible(true);
	}

	public void criarTexto() {
		JLabel lblNomeUtilizador = new JLabel("Nome de Utilizador:");
		lblNomeUtilizador.setBounds(10, 15, 124, 14);
		getContentPane().add(lblNomeUtilizador);
		
		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setBounds(10, 45, 86, 14);
		getContentPane().add(lblEmail);

		JLabel lblPalavraChave = new JLabel("Palavra Chave:");
		lblPalavraChave.setBounds(10, 76, 86, 17);
		getContentPane().add(lblPalavraChave);
	}

	public void criarCaixaTexto() {
		tfNomeUtilizador = new JTextField();
		tfNomeUtilizador.setBounds(125, 12, 86, 20);
		getContentPane().add(tfNomeUtilizador);
		tfNomeUtilizador.setColumns(10);
		
		tfEmail = new JTextField();
		tfEmail.setColumns(10);
		tfEmail.setBounds(125, 43, 86, 20);
		getContentPane().add(tfEmail);

		tfPalavraChave = new JPasswordField();
		tfPalavraChave.setBounds(125, 74, 86, 20);
		getContentPane().add(tfPalavraChave);
		tfPalavraChave.setColumns(10);
		
	}

	public void createButtons() {
		JButton btnRegister = new JButton("Registar");
		btnRegister.setBounds(getWidth()/8,getHeight()-75,100,20);
		btnRegister.addActionListener(this);
		btnRegister.setActionCommand("Register");
		getContentPane().add(btnRegister);

		JButton btnLeave = new JButton("Sair");
		btnLeave.setBounds(btnRegister.getBounds().x + 110,btnRegister.getBounds().y,100,20);
		btnLeave.addActionListener(this);
		btnLeave.setActionCommand("Leave");
		getContentPane().add(btnLeave);
		
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals("Leave"))
			System.exit(0);

		if (ae.getActionCommand().equals("Register")) {
			String nomeUtilizador = tfNomeUtilizador.getText();
			String palavraChave = new String(tfPalavraChave.getPassword());
			String email = tfEmail.getText();
			Player player = new Player(nomeUtilizador, palavraChave, email);
			if(!nomeUtilizador.equals("") && !palavraChave.equals("")) {
				if(player.register()) {
					JOptionPane.showMessageDialog(new JFrame(), "Utilizador registrado com sucesso!", "Sucesso",
					        JOptionPane.INFORMATION_MESSAGE);
					setVisible(false);
				}else {
					JOptionPane.showMessageDialog(new JFrame(), "Utilizador não registrado com sucesso!", "ERRO",
					        JOptionPane.ERROR_MESSAGE);
				}	
			}else {
				JOptionPane.showMessageDialog(new JFrame(), "Precisa de preencher todos os campos!", "ERRO",
				        JOptionPane.ERROR_MESSAGE);
			}
			
		}

	}
}