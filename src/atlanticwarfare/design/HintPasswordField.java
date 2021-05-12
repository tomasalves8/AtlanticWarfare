package atlanticwarfare.design;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JPasswordField;

public class HintPasswordField extends JPasswordField {

  private static final long serialVersionUID = 1L;

  private char echoChar;

  Font gainFont = new Font("Verdana", Font.PLAIN, 11);
  Font lostFont = new Font("Verdana", Font.PLAIN, 11);

  public HintPasswordField(final String hint) {
    echoChar = getEchoChar();

    setText(hint);
    setEchoChar((char) 0);
    setFont(lostFont);
    setForeground(Color.BLACK);

    this.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusGained(FocusEvent e) {
            if (new String(getPassword()).equals(hint)) {
              setText("");
              setFont(gainFont);
              setForeground(Color.BLACK);
              setEchoChar(echoChar);
            } else {
              setText(new String(getPassword()));
              setFont(gainFont);
            }
          }

          @Override
          public void focusLost(FocusEvent e) {
            if (
              new String(getPassword()).equals(hint) ||
              new String(getPassword()).length() == 0
            ) {
              setText(hint);
              setFont(lostFont);
              setForeground(Color.BLACK);
              setEchoChar((char) 0);
            } else {
              setText(new String(getPassword()));
              setFont(gainFont);
              setForeground(Color.BLACK);
            }
          }
        }
      );
  }
}
