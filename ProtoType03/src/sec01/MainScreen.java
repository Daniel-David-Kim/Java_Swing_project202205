package sec01;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

public class MainScreen extends JFrame {
	private Container contentPane;
	private JPanel background;
	private References ref = new References();
	private QuizDAO dao = new QuizDAO();
	private LoginInfo agent;
	public MainScreen(LoginInfo agent) {
		this.agent = agent;
		this.agent.removePrev();
		setTitle("Greetings!");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		contentPane = getContentPane();
		background = ref.setBackgroundPanel("./images/quizintro.jpg", 720, 480, null);
		add(background);
		
		JButton UpdateProfileButton = new JButton("프로필 수정");
		UpdateProfileButton.setBounds(100, 350, 100, 50);
		UpdateProfileButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new UpdateProfile(agent);
			}
		});
		background.add(UpdateProfileButton);
		
		setResizable(false);
		setSize(720, 480);
		setVisible(true);
	}
}
