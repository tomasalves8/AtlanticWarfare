package atlanticwarfare.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import atlanticwarfare.design.Game;

public class HomeArea extends GridArea
{
	private static final long serialVersionUID = 1L;
	public HomeArea(String title, Game window)
	{
		super(title, window);
		addMouseListener(new MouseHandler());
	}
	
	private class MouseHandler extends MouseAdapter
	{
		@Override
		public void mousePressed(MouseEvent e)
		{
			if(e.getButton() == MouseEvent.BUTTON1)
			{
				selected = cursorLocation;
				if(player.getSelectedShip() != GameType.IDLE && validPlacement(getPlayer().getSelectedShip())) {
					placeShip(selected, getPlayer().getSelectedShip(), vertical);
					board.disableShip(getPlayer().getSelectedShip());
					getPlayer().setSelectedShip(GameType.IDLE);
					repaint();
				}

			}
			if(e.getButton() == MouseEvent.BUTTON3 && player.getSelectedShip() != GameType.IDLE )
			{
				vertical = !vertical;
				repaint();
			}
		}
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;

		if(getPlayer().getSelectedShip()!=GameType.IDLE) {
			if (!validPlacement(getPlayer().getSelectedShip())){
				g2.setColor(new Color(150,0,0));
			}
			int posX = ((int) cursorLocation.getX())*30;
			int posY = ((int) cursorLocation.getY())*30;
			
			int size = 30*GameType.getShipSize(getPlayer().getSelectedShip());
			if (vertical) {
				while(size+posY > getPreferredSize().height) {
					size -= 30;
				}
				g2.fill3DRect(posX, posY, 30, size, false);
			}else {
				while(size+posX > getPreferredSize().width) {
					size -= 30;
				}
				g2.fill3DRect(posX, posY, size, 30, false);
			}
		}
	}
}

