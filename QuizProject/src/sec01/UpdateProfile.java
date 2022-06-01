package sec01;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class UpdateProfile extends JFrame{
	References ref = new References();
	private LoginInfo agent;
	private Connection con = makeConnection();
	private UpdateProfile thisPage;
	private QuizDAO dao = new QuizDAO();
	private HashMap<String, Object> validate = dao.select("select * from members", "members", true);
	public UpdateProfile(LoginInfo agent) {
		this.agent = agent;
		this.agent.removePrev();
		setTitle("회원정보 수정");
		setSize(720,480);
		JPanel base = ref.setBackgroundPanel("./images/clipboard.jpg", 720, 480, null);
		add(base);
		
		String[] names = { "이름", "비밀번호",  "비밀번호 찾기 질문", "비밀번호 찾기 답", "휴대폰 번호"};
		String[] value = {agent.getUser().getUname(),agent.getUser().getUpw(),agent.getUser().getUfindq(),agent.getUser().getUfinda(),agent.getUser().getUtel()};
		JLabel[] labels = new JLabel[5];
		JTextField[] ips = new JTextField[5];
		
		for(int i = 0; i < labels.length; i++) {
			labels[i] = new JLabel(names[i]);
			labels[i].setFont(new Font("함초롬돋움", Font.BOLD, 12));
			ips[i] = new JTextField(25);
			ips[i].setText(value[i]);
			ips[i].setFont(new Font("함초롬돋움", Font.PLAIN, 12));
			labels[i].setBounds(140, 118+(i)*56, 300, 23);
			ips[i].setBounds(390, 118+(i)*56, 200, 23);
			
			base.add(labels[i]);
			base.add(ips[i]);
		}
		
		JButton apply = new JButton("확인");
		apply.setBorder(null);
		apply.setBackground(Color.WHITE);
		apply.setFont(new Font("함초롬돋움", Font.BOLD, 12));
		JButton cancel = new JButton("취소");
		cancel.setBorder(null);
		cancel.setBackground(Color.WHITE);
		cancel.setFont(new Font("함초롬돋움", Font.BOLD, 12));
		
		apply.setBounds(250, 398, 80, 23);
		cancel.setBounds(400, 398, 80, 23);
		
		apply.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 여기에서 validation 체크
				Members user = agent.getUser();
				if(ips[0].getText().equals(user.getUname()) && ips[1].getText().equals(user.getUpw()) && ips[2].getText().equals(user.getUfindq()) && ips[3].getText().equals(user.getUfinda()) && ips[4].getText().equals(user.getUtel())) {
					JOptionPane.showMessageDialog(null, "변경할 항목을 입력해주세요.", "Error!", JOptionPane.ERROR_MESSAGE);
					return;
				} else if(!ips[1].getText().equals(user.getUpw()) && (Validation.pwValidate(ips[1].getText()) == false)) {
					JOptionPane.showMessageDialog(null, "비밀번호는 8자 이상이어야 합니다. 다시 입력하세요.", "Error!", JOptionPane.ERROR_MESSAGE);
					return;
				} else if(Validation.telFormValidate(ips[4].getText()) == false) {
					JOptionPane.showMessageDialog(null, "번호의 형식에 맞지 않습니다. 다시 입력하세요.", "Error!", JOptionPane.ERROR_MESSAGE);
					return;
				} else if(Validation.telValidate(validate, ips[4].getText()) == false) {
					JOptionPane.showMessageDialog(null, "중복되는 번호입니다. 다시 입력하세요.", "Error!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				agent.getUser().setUname(ips[0].getText()); // 이름
				agent.getUser().setUpw(ips[1].getText()); // 비밀번호
				agent.getUser().setUfindq(ips[2].getText()); // 비번 찾기 질문
				agent.getUser().setUfinda(ips[3].getText()); // 비번 찾기 답
				agent.getUser().setUtel(ips[4].getText()); // 휴대폰 번호
				UpdateProfile(agent.getUser());
				JOptionPane.showMessageDialog(null, "수정되었습니다", "Complete", JOptionPane.INFORMATION_MESSAGE);
				dispose();
				agent.setPrev(thisPage);
				setVisible(false);
				agent.setCur(new MainScreen(agent));
			}
		});
		cancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				agent.setPrev(thisPage);
				setVisible(false);
				agent.setCur(new MainScreen(agent));
			}
		});
		JLabel title = new JLabel("회원 정보 수정");
		title.setFont(new Font("함초롬돋움", Font.BOLD, 17));
		title.setBounds(310, 88, 120, 23);
		
		base.add(title);
		base.add(apply);
		base.add(cancel);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public void UpdateProfile(Members member) {
		PreparedStatement pstmt;
		String sql = "update members set upw = ?, uname = ?, ufindq =?, ufinda = ?,"
				+ " utel = ? where uid = ?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, member.getUpw());
			pstmt.setString(2, member.getUname());
			pstmt.setString(3, member.getUfindq());
			pstmt.setString(4, member.getUfinda());
			pstmt.setString(5, member.getUtel());
			pstmt.setString(6, member.getUid());
			pstmt.executeUpdate();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static Connection makeConnection() {
		Connection con = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			try {
				String url = "jdbc:mysql://localhost:3306/Quiz?serverTimezone=Asia/Seoul";
				con = DriverManager.getConnection(url,"root","1234");
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}
	
	public static void main(String[] args) {
		// 실험용 임시 로그인 객체입니다.
		LoginInfo agent = new LoginInfo(new Members("user3", "3456", "이계영", "3.나는 누구일까?", "셋유저", 1, "010-3333-4444"));
		new UpdateProfile(agent);
	}
	
}
