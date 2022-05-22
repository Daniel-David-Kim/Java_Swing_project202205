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
	private LoginInfo agent;
	public UpdateProfile(LoginInfo agent) {
		this.agent = agent;
		this.agent.removePrev();
		setTitle("ȸ������ ����");
		setSize(720,480);
		setResizable(false);
		JPanel base = ref.setBackgroundPanel("./images/quizintro.jpg", 720, 480, null);
		add(base);
		System.out.println(agent.getUser());
		
		String[] names = { "�̸�", "��й�ȣ",  "��й�ȣ ã�� ����", "��й�ȣ ã�� ��", "�޴��� ��ȣ"};
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
		
		JButton apply = new JButton("Ȯ��");
		JButton cancel = new JButton("���");
		
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
		// ����� �ӽ� �α��� ��ü�Դϴ�.
		LoginInfo agent = new LoginInfo(new Members("user3", "3456", "�̰迵", "3.���� �����ϱ�?", "������", 1, "010-3333-4444"));
		new UpdateProfile(agent);
	}
}
