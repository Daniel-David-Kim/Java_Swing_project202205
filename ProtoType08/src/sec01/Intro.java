package sec01;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.event.*;

// 초기 화면. 로그인과 회원가입, 아이디/비번찾기입니다. 
public class Intro extends JFrame {
	private Container contentPane;
	private JPanel background;
	private References ref = new References();
	private QuizDAO dao = new QuizDAO();
	public Intro(LoginInfo agent) {
		if(agent != null) {
			agent.removePrev();
			agent = null;
			System.out.println("로그인 객체를 삭제했습니다.");
		} else {
			System.out.println("로그인 객체가 비었습니다.");
		}
		setTitle("Greetings!");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		contentPane = getContentPane();
		background = ref.setBackgroundPanel("./images/main.png", 720, 480, null);
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
			labels[i].setForeground(new Color(253, 217, 39));
			labels[i].setBounds(350, 200 + (i-1)*50, 70, 30);
			ips[i].setBounds(450, 200+(i-1)*50, 150, 30);
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
		JButton login = new JButton("로그인") {
			public void paintComponent(Graphics g) {
				g.drawImage((new ImageIcon("./images/loginmain.png")).getImage(), 0, 0, 110, 40, null);
			}
		};
		login.addActionListener(btnLis);
		login.setBounds(420, 250, 110, 40);
		login.setBorder(new LineBorder(new Color(13, 23, 60)));
		background.add(login);
		JButton signin = new JButton("회원가입") {
			public void paintComponent(Graphics g) {
				g.drawImage((new ImageIcon("./images/signinmain.png")).getImage(), 0, 0, 120, 40, null);
			}
		};
		signin.addActionListener(btnLis);
		signin.setBounds(350, 370, 120, 40);
		signin.setBorder(new LineBorder(new Color(13, 23, 60)));
		background.add(signin);
		JButton find = new JButton("ID/PW 찾기") {
			public void paintComponent(Graphics g) {
				g.drawImage((new ImageIcon("./images/findmain.png")).getImage(), 0, 0, 120, 40, null);
			}
		};
		find.addActionListener(btnLis);
		find.setBounds(480, 370, 120, 40);
		find.setBorder(new LineBorder(new Color(13, 23, 60)));
		background.add(find);
	}
	
	public static void main(String[] args) { new Intro(null); }
}
