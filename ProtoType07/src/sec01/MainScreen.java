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
		setTitle("Main Menu");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		contentPane = getContentPane();
		background = ref.setBackgroundPanel("./images/board.jpg", 705, 450, null);
		add(background);
		System.out.println(agent.getUser());	
		
		add(setMenuButtons());

		setResizable(false);
		setSize(720, 480);
		setVisible(true);
	}
	
	private JPanel setMenuButtons() {
		JPanel btnPan = new JPanel();
		JButton[] menus = new JButton[3];
		String[] labels = agent.getUser().getUclass() == 1?  new String[] {"���� Ǯ��", "��ŷ", "ȸ�� ���� ����"} :  new String[] {"���� Ǯ��", "��ŷ", "������ ȭ��"};
		for(int i = 0; i < menus.length; i++) {
			menus[i] = new JButton(labels[i]) {
				public void paintComponent(Graphics g) {
					String label = this.getText();
					String url = "";
					switch(label) {
						case "���� Ǯ��" : url = "./images/que.jpg"; break;
						case "��ŷ" : url = "./images/ranking.jpg"; break;
						case "ȸ�� ���� ����" : url = "./images/revise.jpg"; break;
						case "������ ȭ��" : url = "./images/admin.jpg"; break;
					}
					g.drawImage((new ImageIcon(url)).getImage(), 0, 0, 200, 70, null);
				}
			};
			menus[i].setBounds(50, 50+i*90, 200, 70);
			menus[i].addActionListener(new AllActionListener(agent, this));
			background.add(menus[i]);
		}
		return btnPan;
	}
	
	public static void main(String[] args) {
		// ����� �ӽ� �α��� ��ü�Դϴ�.
		LoginInfo agent = new LoginInfo(new Members("user3", "3456", "�̰迵", "3.���� �����ϱ�?", "������", 1, "010-3333-4444"));
		new MainScreen(agent);
	}
}

class AllActionListener implements ActionListener {
	private MainScreen thisPage;
	private LoginInfo agent;
	public AllActionListener(LoginInfo agent, MainScreen thisPage) {
		this.agent = agent;
		this.thisPage = thisPage;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton target = (JButton)e.getSource();
		String label = target.getText();
		switch(label) {
		case "���� Ǯ��" : 
			agent.setPrev(thisPage);
			agent.setCur(new QuizSelection(agent));
			thisPage.setVisible(false);
			break;
		case "��ŷ" : 
			agent.setPrev(thisPage);
			agent.setCur(new Ranking(agent));
			thisPage.setVisible(false);
			break;
		case "ȸ�� ���� ����" : 
			agent.setPrev(thisPage);
			agent.setCur(new UpdateProfile(agent));
			thisPage.setVisible(false);
			break;
		case "������ ȭ��" : 
			agent.setPrev(thisPage);
			agent.setCur(new SettingsTemplate(agent));
			thisPage.setVisible(false);
			break;
		}
	}
}
