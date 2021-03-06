package atlanticwarfare.utilities;

import java.awt.Component;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class JCountryComboBox extends JComboBox<Object> implements KeyListener{
	private static final long serialVersionUID = 8716447666588329697L;
	private boolean hasGlobal;
	private static String[] allCountries;
	private char lastKey = ',';
	public static String[] getAllCountries() {
	    String[] countries = new String[Locale.getISOCountries().length];
	    String[] countryCodes = Locale.getISOCountries();
	    for (int i = 0; i < countryCodes.length; i++) {
	        Locale obj = new Locale("", countryCodes[i]);
	        countries[i] = obj.getDisplayCountry();
	    }
	    return countries;
	 }
	public static Integer[] initialArray() {
		allCountries = getAllCountries();
		Integer[] intArray = new Integer[allCountries.length];
        for (int i = 0; i < allCountries.length; i++) {
            intArray[i] = Integer.valueOf(i);
        }
        return intArray;
	}
	public JCountryComboBox(boolean hasGlobal) {
		super(initialArray());
        this.hasGlobal = hasGlobal;
		ComboBoxRenderer renderer = new ComboBoxRenderer(hasGlobal);
		setRenderer(renderer);
		addKeyListener(this);
	}
	public JCountryComboBox() {
		this(false);
	}
	public String getSelectedCountryCode() {
		int index = getSelectedIndex();
		if(hasGlobal) {
			if(getSelectedIndex() == 0) {
				return "GLOBAL";
			}else{
				index -= 1;
			}
		}
			
	    return Locale.getISOCountries()[index];
	}
	@Override
	public void keyPressed(KeyEvent arg0) {
		int i = 0;
		if(lastKey == arg0.getKeyChar()) {
			i = getSelectedIndex()+1;
		}
		for (; i < getItemCount(); i++) {
			if(Character.toLowerCase(allCountries[i].charAt(0)) == arg0.getKeyChar()) {
				lastKey = arg0.getKeyChar();
				if(hasGlobal) {
					setSelectedIndex(i+1);
				}else {
					setSelectedIndex(i);
				}
				return;
			}
		}
		
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
		// Not needed
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
		// Not needed
	}
}

class ComboBoxRenderer extends JLabel implements ListCellRenderer<Object> {
	private static final long serialVersionUID = -5900395262959960454L;
	private boolean hasGlobal;
	private String[] countries;
	private String[] countryCodes;
	public ComboBoxRenderer(boolean hasGlobal) {
		setOpaque(true);
		this.hasGlobal = hasGlobal;
		countries = new String[Locale.getISOCountries().length];
	    countryCodes = Locale.getISOCountries();
	    for (int i = 0; i < countryCodes.length; i++) {
	        Locale obj = new Locale("", countryCodes[i]);
	        countries[i] = obj.getDisplayCountry();
	    }
	}

	public Component getListCellRendererComponent(
			JList<?> list,
			Object value,
			int index,
			boolean isSelected,
			boolean cellHasFocus) {
	    
		int selectedIndex = ((Integer)value).intValue();
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		ImageIcon icon;
		try {
			if(hasGlobal) {
				if(selectedIndex == 0) {
					icon = new ImageIcon((ImageIO.read(new File("Images/countries/global.png")).getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
				}else {
					icon = new ImageIcon((ImageIO.read(new File("Images/countries/"+countryCodes[selectedIndex-1]+".png")).getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
				}
			}else {
				icon = new ImageIcon((ImageIO.read(new File("Images/countries/"+countryCodes[selectedIndex]+".png")).getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
			}
			setIcon(icon);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String country = "";
		if(hasGlobal) {
			if(selectedIndex == 0) {
				country = "Global";
			}else {
				country = countries[selectedIndex-1];
			}
		}else {
			country = countries[selectedIndex];
		}
		setText(country);
		setFont(list.getFont());

		return this;
	}

}
