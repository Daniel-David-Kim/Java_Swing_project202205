package sec01;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

// 초기 화면. 로그인과 회원가입, 아이디/비번찾기입니다. 
public class Intro extends JFrame {
	private Container contentPane;
	private JPanel background;
	private References ref = new References();
	private QuizDAO dao = new QuizDAO();
	public Intro() {
		setTitle("Greetings!");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		contentPane = getContentPane();
		background = ref.setBackgroundPanel("./images/quizintro.jpg", 720, 480, null);
		add(background);
		loginSettings();
		setResizable(false);
		setSize(720, 480);
		setVisible(true);
	}
	
	public void loginSettings() {
		JLabel[] labels = new JLabel[2];
		JTextField[] ips = new JTextField[2];
		String[] labelsstr = {"ID", "PW"};
		for(int i = 0;i < labels.length; i++) {
			labels[i] = new JLabel(labelsstr[i]);
			if(i == 0) ips[i] = new JTextField(10);
			else if(i == 1) {ips[i] = new JPasswordField(10);}
			labels[i].setFont(new Font("Dotum", Font.BOLD, 24));
			labels[i].setBounds(200, 200 + (i-1)*50, 70, 30);
			ips[i].setBounds(300, 200+(i-1)*50, 150, 30);
			background.add(labels[i]);
			background.add(ips[i]);
		}
		ActionListener btnLis = (e) -> {
			JButton target = (JButton)e.getSource();
			if(target.getText().equals("로그인")) {
				String id = ips[0].getText();
				String pass = ips[1].getText();
				HashMap<String, Object> call = dao.select(String.format("select * from members where uid='%s' and upw='%s'", id, pass), "members", false);
				int resRow = (int)call.get("numOfRows");
				if(resRow == 0) JOptionPane.showMessageDialog(null, "회원정보가 일치하지 않습니다.", "Error!", JOptionPane.ERROR_MESSAGE);
				else {
					Members member = ((Vector<Members>)call.get("resultData")).get(0);
					LoginInfo agent = new LoginInfo(member);
					if(member.getUclass() == 1) JOptionPane.showMessageDialog(null, String.format("%s님 환영합니다!", member.getUname()), "Complete", JOptionPane.INFORMATION_MESSAGE);
					else if(member.getUclass() == 0) JOptionPane.showMessageDialog(null, String.format("[관리자] %s님 환영합니다!", member.getUid()), "Complete", JOptionPane.INFORMATION_MESSAGE);
					agent.setPrev(this);
					setVisible(false);
					agent.setCur(new MainScreen(agent));
				}
			} else if(target.getText().equals("회원가입")) { 
				JFrame signin = new SignIn();
				if(signin.isVisible() == false) signin = null;
			} else {
				new FindAccount();
			}
		};
		JButton login = new JButton("로그인");
		login.addActionListener(btnLis);
		login.setBounds(320, 250, 110, 40);
		background.add(login);
		JButton signin = new JButton("회원가입");
		signin.addActionListener(btnLis);
		signin.setBounds(380, 370, 110, 40);
		background.add(signin);
		JButton find = new JButton("ID/PW 찾기");
		find.addActionListener(btnLis);
		find.setBounds(500, 370, 120, 40);
		background.add(find);
	}
	
	public static void main(String[] args) { new Intro(); }
}
