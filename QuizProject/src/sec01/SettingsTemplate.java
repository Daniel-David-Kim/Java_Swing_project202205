package sec01;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

public class SettingsTemplate extends JFrame {
	private JPanel background;
	private References ref = new References();
	SettingsTemplate() {
		setTitle("SettingsTemplate");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		background = ref.setBackgroundPanel("./images/quizintro.jpg", 720, 480, null);
		add(background);
		
		setResizable(false);
		setSize(720, 480);
		setVisible(true);
	}
	
	
	
	public static void main(String[] args) { new SettingsTemplate(); }
}
