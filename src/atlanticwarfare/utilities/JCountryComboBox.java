package atlanticwarfare.utilities;

import java.awt.Component;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class JCountryComboBox extends JComboBox<Object>{
	private static final long serialVersionUID = 8716447666588329697L;
	private boolean hasGlobal;
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
		Integer[] intArray = new Integer[getAllCountries().length];
        for (int i = 0; i < getAllCountries().length; i++) {
            intArray[i] = Integer.valueOf(i);
        }
        return intArray;
	}
	public JCountryComboBox(boolean hasGlobal) {
		super(initialArray());
        this.hasGlobal = hasGlobal;
		ComboBoxRenderer renderer = new ComboBoxRenderer(hasGlobal);
		setRenderer(renderer);
	}
	public JCountryComboBox() {
		this(false);
	}
	public String getSelectedCountryCode() {
		if(hasGlobal && getSelectedIndex() == 0)
			return "GLOBAL";
		Map<String, String> countries = new HashMap<>();
	    for (String iso : Locale.getISOCountries()) {
	        Locale l = new Locale("", iso);
	        countries.put(l.getDisplayCountry(), iso);
	    }
	    return countries.get(getSelectedItem().toString());
	}
}

class ComboBoxRenderer extends JLabel implements ListCellRenderer<Object> {
	private static final long serialVersionUID = -5900395262959960454L;
	private boolean hasGlobal;
	public ComboBoxRenderer(boolean hasGlobal) {
		setOpaque(true);
		this.hasGlobal = hasGlobal;
	}

	public Component getListCellRendererComponent(
			JList<?> list,
			Object value,
			int index,
			boolean isSelected,
			boolean cellHasFocus) {
		
		String[] countries = new String[Locale.getISOCountries().length];
	    String[] countryCodes = Locale.getISOCountries();
	    for (int i = 0; i < countryCodes.length; i++) {
	        Locale obj = new Locale("", countryCodes[i]);
	        countries[i] = obj.getDisplayCountry();
	    }
	    
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
