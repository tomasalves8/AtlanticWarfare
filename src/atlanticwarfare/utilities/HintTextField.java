package atlanticwarfare.utilities;

import java.awt.Color;  
import java.awt.Font;  
import java.awt.event.FocusAdapter;  
import java.awt.event.FocusEvent;  
import javax.swing.JTextField;  

public class HintTextField extends JTextField { 

	private static final long serialVersionUID = 1L;
	private String hint;
	Font gainFont = new Font("Verdana", Font.PLAIN, 11);  
	Font lostFont = new Font("Verdana", Font.PLAIN, 11);  

	
	public boolean isEmpty() {
		return getText().equals(hint);
	}
	
	public HintTextField(final String hint) {  
		this.hint = hint;
		setText(hint);  
		setFont(lostFont);  
		setForeground(Color.BLACK);  

		this.addFocusListener(new FocusAdapter() {  

			@Override  
			public void focusGained(FocusEvent e) {  
				if (getText().equals(hint)) {  
					setText("");  
					setFont(gainFont);
					setForeground(Color.BLACK);
				} else {  
					setText(getText());  
					setFont(gainFont);  
				}  
			}  

			@Override  
			public void focusLost(FocusEvent e) {  
				if (getText().equals(hint)|| getText().length()==0) {  
					setText(hint);  
					setFont(lostFont);  
					setForeground(Color.BLACK);  
				} else {  
					setText(getText());
					setFont(gainFont);  
					setForeground(Color.BLACK);  
				}  
			}  
		});  

	}  
}  