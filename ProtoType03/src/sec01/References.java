package sec01;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

public class References {
	public JPanel setBackgroundPanel(String path, int width, int height, LayoutManager layout) {
		ImageIcon img = new ImageIcon(path);
		JPanel background = new JPanel(layout) {
			public void paintComponent(Graphics g) {
				g.drawImage(img.getImage(), 0, 0, width, height, null);
				setOpaque(false);
			}
		};
		background.setBounds(0, 0, 720, 480);
		return background;
	}
}
