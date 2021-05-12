package atlanticwarfare.design;

import atlanticwarfare.database.Player;
import atlanticwarfare.utilities.HintPasswordField;
import atlanticwarfare.utilities.HintTextField;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
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
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class FormRegister extends JFrame implements ActionListener {

  private static final long serialVersionUID = 1L;
  private HintTextField tfNomeUtilizador, tfEmail;
  private HintPasswordField tfPalavraChave;
  private JLabel iconUser, iconPass, iconEmail;

  public FormRegister() {
    super();
    //setResizable(false);
    getContentPane().setLayout(null);

    criarFundo();
    criarImagens();
    criarCaixaTexto();
    criarBotoes();

    addMouseListener(
      new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
          requestFocus();
        }
      }
    );

    setLocationRelativeTo(null);
    setVisible(true);
  }

  public void criarFundo() {
    try {
      setContentPane(
        new JLabel(new ImageIcon(ImageIO.read(new File("Images/Fundo2.png"))))
      );
      setBounds(0, 0, getWidth(), getHeight());
    } catch (IOException e) {
      e.printStackTrace();
    }
    pack();
  }

  public void criarImagens() {
    iconUser =
      new JLabel(
        new ImageIcon(
          System.getProperty("user.dir") + "//Images//nomeUtilizador.png"
        )
      );
    iconUser.setBounds(80, 201, 16, 16);
    getContentPane().add(iconUser);

    iconEmail =
      new JLabel(
        new ImageIcon(
          System.getProperty("user.dir") + "//Images//iconEmail.png"
        )
      );
    iconEmail.setBounds(80, 241, 16, 16);
    getContentPane().add(iconEmail);

    iconPass =
      new JLabel(
        new ImageIcon(System.getProperty("user.dir") + "//Images//pass.png")
      );
    iconPass.setBounds(80, 281, 16, 16);
    getContentPane().add(iconPass);
  }

  public void criarCaixaTexto() {
    tfNomeUtilizador = new HintTextField(" Nome de Utilizador");
    tfNomeUtilizador.setBounds(100, 200, 200, 20);
    tfNomeUtilizador.setBorder(null);
    tfNomeUtilizador.setColumns(10);
    getContentPane().add(tfNomeUtilizador);

    tfEmail = new HintTextField(" Email");
    tfEmail.setBounds(100, 240, 200, 20);
    tfEmail.setBorder(null);
    tfEmail.setColumns(10);
    getContentPane().add(tfEmail);

    tfPalavraChave = new HintPasswordField(" Palavra Chave");
    tfPalavraChave.setBounds(100, 280, 200, 20);
    tfPalavraChave.setBorder(null);
    tfPalavraChave.setColumns(10);
    getContentPane().add(tfPalavraChave);
  }

  public void criarBotoes() {
    JRadioButton btn = new JRadioButton("text");
    getContentPane().add(btn);

    JButton btnRegister = new JButton("Registar");
    btnRegister.setBounds(35, 325, 150, 25);
    btnRegister.addActionListener(this);
    btnRegister.setFont(new Font("Verdana", Font.PLAIN, 12));
    btnRegister.setBorder(null);
    btnRegister.setBackground(Color.WHITE);
    btnRegister.setActionCommand("Register");
    getContentPane().add(btnRegister);
    getRootPane().setDefaultButton(btnRegister);

    JButton btnLeave = new JButton("Sair");
    btnLeave.setBounds(215, 325, 150, 25);
    btnLeave.addActionListener(this);
    btnLeave.setFont(new Font("Verdana", Font.PLAIN, 12));
    btnLeave.setBorder(null);
    btnLeave.setBackground(Color.WHITE);
    btnLeave.setActionCommand("Leave");
    getContentPane().add(btnLeave);
  }

  public boolean isValidEmailAddress(String email) {
    String ePattern =
      "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
    java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
    java.util.regex.Matcher m = p.matcher(email);
    return m.matches();
  }

  @Override
  public void actionPerformed(ActionEvent ae) {
    if (ae.getActionCommand().equals("Leave")) System.exit(0);

    if (ae.getActionCommand().equals("Register")) {
      String nomeUtilizador = tfNomeUtilizador.getText();
      String palavraChave = new String(tfPalavraChave.getPassword());
      String email = tfEmail.getText();
      Player player = new Player(nomeUtilizador, palavraChave, email);
      if (
        (
          !nomeUtilizador.equals("") &&
          !nomeUtilizador.equals(" Nome de Utilizador")
        ) &&
        (!palavraChave.equals("") && !palavraChave.equals(" Palavra Chave"))
      ) {
        if (isValidEmailAddress(email)) {
          if (player.register()) {
            new FormLogin();
            JOptionPane.showMessageDialog(
              new JFrame(),
              "Utilizador registrado com sucesso!",
              "Sucesso",
              JOptionPane.INFORMATION_MESSAGE
            );
            setVisible(false);
          } else {
            JOptionPane.showMessageDialog(
              new JFrame(),
              "Utilizador não foi registrado com sucesso!",
              "ERRO",
              JOptionPane.ERROR_MESSAGE
            );
          }
        } else {
          JOptionPane.showMessageDialog(
            new JFrame(),
            "O Email inserido não é valido!",
            "ERRO",
            JOptionPane.ERROR_MESSAGE
          );
        }
      } else {
        JOptionPane.showMessageDialog(
          new JFrame(),
          "Precisa de preencher todos os campos!",
          "ERRO",
          JOptionPane.ERROR_MESSAGE
        );
      }
    }
  }
}
