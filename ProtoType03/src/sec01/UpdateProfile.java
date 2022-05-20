package sec01;

import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class UpdateProfile extends JFrame{
	References ref = new References();
	
	public UpdateProfile() {
		
		setTitle("회원정보 수정");
		setSize(720,480);
		JPanel base = ref.setBackgroundPanel("./images/quizintro.jpg", 720, 480, null);
		add(base);
		
		String[] names = { "이름", "비밀번호",  "비밀번호 찾기 질문", "비밀번호 찾기 답", "휴대폰 번호"};
		String[] value = {};
		JLabel[] labels = new JLabel[5];
		JTextField[] ips = new JTextField[5];
		
		for(int i = 0; i < labels.length; i++) {
			labels[i] = new JLabel(names[i]);
			labels[i].setFont(new Font("Dotum", Font.BOLD, 23));
			ips[i] = new JTextField(25);
			ips[i].setFont(new Font("Dotum", Font.PLAIN, 23));
			labels[i].setBounds(50, 75+(i-1)*50, 300, 25);
			ips[i].setBounds(300, 75+(i-1)*50, 200, 35);
			
			base.add(labels[i]);
			base.add(ips[i]);
		}
		
		JButton apply = new JButton("확인");
		JButton cancel = new JButton("취소");
		
		apply.setBounds(100, 350, 100, 50);
		apply.setFont(new Font("Dotum", Font.BOLD, 19));
		cancel.setBounds(250, 350, 100, 50);
		cancel.setFont(new Font("Dotum", Font.BOLD, 19));
		
		base.add(apply);
		base.add(cancel);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	public static void main(String[] args) {
		new UpdateProfile();
	}
}
