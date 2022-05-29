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
		String[] labels = agent.getUser().getUclass() == 1?  new String[] {"문제 풀기", "랭킹", "회원 정보 수정"} :  new String[] {"문제 풀기", "랭킹", "관리자 화면"};
		for(int i = 0; i < menus.length; i++) {
			menus[i] = new JButton(labels[i]) {
				public void paintComponent(Graphics g) {
					String label = this.getText();
					String url = "";
					switch(label) {
						case "문제 풀기" : url = "./images/que.jpg"; break;
						case "랭킹" : url = "./images/ranking.jpg"; break;
						case "회원 정보 수정" : url = "./images/revise.jpg"; break;
						case "관리자 화면" : url = "./images/admin.jpg"; break;
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
		// 실험용 임시 로그인 객체입니다.
		LoginInfo agent = new LoginInfo(new Members("user3", "3456", "이계영", "3.나는 누구일까?", "셋유저", 1, "010-3333-4444"));
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
		case "문제 풀기" : 
			agent.setPrev(thisPage);
			agent.setCur(new QuizSelection(agent));
			thisPage.setVisible(false);
			break;
		case "랭킹" : 
			agent.setPrev(thisPage);
			agent.setCur(new Ranking(agent));
			thisPage.setVisible(false);
			break;
		case "회원 정보 수정" : 
			agent.setPrev(thisPage);
			agent.setCur(new UpdateProfile(agent));
			thisPage.setVisible(false);
			break;
		case "관리자 화면" : 
			agent.setPrev(thisPage);
			agent.setCur(new SettingsTemplate(agent));
			thisPage.setVisible(false);
			break;
		}
	}
}
