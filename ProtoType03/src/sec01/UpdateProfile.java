package sec01;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import database.DBManager;

public class UpdateProfile extends JFrame{
	References ref = new References();
	private LoginInfo agent;
	String id = "user1";
	public UpdateProfile(LoginInfo agent) {
		this.agent = agent;
		this.agent.removePrev();
		setTitle("ȸ������ ����");
		setSize(720,480);
		JPanel base = ref.setBackgroundPanel("./images/quizintro.jpg", 720, 480, null);
		add(base);
		DBManager dbManager = new DBManager();
		
		
		String[] names = { "�̸�", "��й�ȣ",  "��й�ȣ ã�� ����", "��й�ȣ ã�� ��", "�޴��� ��ȣ"};
		String[] value = {agent.getUser().getUname(),agent.getUser().getUpw(),agent.getUser().getUfindq(),agent.getUser().getUfinda(),agent.getUser().getUtel()};
		JLabel[] labels = new JLabel[5];
		JTextField[] ips = new JTextField[5];
		
		for(int i = 0; i < labels.length; i++) {
			labels[i] = new JLabel(names[i]);
			labels[i].setFont(new Font("Dotum", Font.BOLD, 23));
			ips[i] = new JTextField(25);
			ips[i].setText(value[i]);
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
		
		apply.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				agent.getUser().setUname(ips[0].getText());
				agent.getUser().setUpw(ips[1].getText());
				agent.getUser().setUfindq(ips[2].getText());
				agent.getUser().setUfinda(ips[3].getText());
				agent.getUser().setUtel(ips[4].getText());
				dbManager.UpdateProfile(agent.getUser());
				JOptionPane.showMessageDialog(null, "�����Ǿ����ϴ�", "Complete", JOptionPane.INFORMATION_MESSAGE);
				dispose();
			}
		});
		cancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		base.add(apply);
		base.add(cancel);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
}
