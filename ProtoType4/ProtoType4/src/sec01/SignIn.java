package sec01;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

public class SignIn extends JFrame {
	private Container contentPane;
	private JPanel background;
	private References ref = new References();
	private QuizDAO dao = new QuizDAO();
	private HashMap<String, Object> validate = dao.select("select * from members", "members", true);
	public SignIn() {
		setTitle("ȸ������");
		setLayout(null);
		contentPane = getContentPane();
		background = ref.setBackgroundPanel("./images/quizintro.jpg", 720, 480, null);
		add(background);
		signinSettings();
		setResizable(false);
		setSize(400, 400);
		setVisible(true);
	}
	
	public void signinSettings() {
		String[] names = {"���̵�", "��й�ȣ", "��й�ȣ Ȯ��", "�̸�", "��й�ȣ ã�� ����", "��й�ȣ ã�� ��", "�޴��� ��ȣ"};
		JLabel[] labels = new JLabel[7];
		JTextField[] ips = new JTextField[7];
		for(int i = 0; i < labels.length; i++) {
			labels[i] = new JLabel(names[i]);
			if(i == 1 || i == 2) ips[i] = new JPasswordField(15);
			else ips[i] = new JTextField(15);
			labels[i].setBounds(50, 75+(i-1)*35, 120, 25);
			ips[i].setBounds(200, 75+(i-1)*35, 120, 25);
			background.add(labels[i]);
			background.add(ips[i]);
		}
		JButton submit = new JButton("ȸ������");
		submit.addActionListener((e) -> {
			String[] values = new String[7];
			for(int i = 0; i < values.length; i++) {
				values[i] = ips[i].getText();
				if(values[i].equals("") && values[i].length() == 0) {
					JOptionPane.showMessageDialog(null, "��� �Է»����� �ʼ� �Է»����Դϴ�.", "Error!", JOptionPane.ERROR_MESSAGE);
					return;
				} 
			}
			if(!values[1].equals(values[2])) JOptionPane.showMessageDialog(null, "��й�ȣ�� ��ġ���� �ʽ��ϴ�.", "Error!", JOptionPane.ERROR_MESSAGE);
			else if(Validation.dataValidate(validate, values[0]) == false)  JOptionPane.showMessageDialog(null, "�ߺ��Ǵ� ���̵��Դϴ�. �ٽ� �Է��ϼ���.", "Error!", JOptionPane.ERROR_MESSAGE);
			else if(Validation.telFormValidate(values[6]) == false) JOptionPane.showMessageDialog(null, "��ȣ�� ���Ŀ� ���� �ʽ��ϴ�. �ٽ� �Է��ϼ���.", "Error!", JOptionPane.ERROR_MESSAGE);
			else if(Validation.telValidate(validate, values[6]) == false) JOptionPane.showMessageDialog(null, "�ߺ��Ǵ� ��ȣ�Դϴ�. �ٽ� �Է��ϼ���.", "Error!", JOptionPane.ERROR_MESSAGE);
			else {
				String sql = String.format("insert into members values('%s', '%s', '%s', '%s', '%s', 1, '%s')", values[0], values[2], values[3], values[4], values[5], values[6]);
				HashMap<String, Object> call  = dao.insert(sql);
				int resRow = (int)call.get("numOfRows");
				if(resRow == 1) JOptionPane.showMessageDialog(null, "ȸ�������� �Ϸ�Ǿ����ϴ�!", "Complete", JOptionPane.INFORMATION_MESSAGE);
				setVisible(false);
			}
		});
		submit.setBounds(150, 300, 100, 30);
		background.add(submit);
	}
}