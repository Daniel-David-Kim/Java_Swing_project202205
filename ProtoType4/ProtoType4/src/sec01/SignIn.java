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
		setTitle("회원가입");
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
		String[] names = {"아이디", "비밀번호", "비밀번호 확인", "이름", "비밀번호 찾기 질문", "비밀번호 찾기 답", "휴대폰 번호"};
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
		JButton submit = new JButton("회원가입");
		submit.addActionListener((e) -> {
			String[] values = new String[7];
			for(int i = 0; i < values.length; i++) {
				values[i] = ips[i].getText();
				if(values[i].equals("") && values[i].length() == 0) {
					JOptionPane.showMessageDialog(null, "모든 입력사항은 필수 입력사항입니다.", "Error!", JOptionPane.ERROR_MESSAGE);
					return;
				} 
			}
			if(!values[1].equals(values[2])) JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다.", "Error!", JOptionPane.ERROR_MESSAGE);
			else if(Validation.dataValidate(validate, values[0]) == false)  JOptionPane.showMessageDialog(null, "중복되는 아이디입니다. 다시 입력하세요.", "Error!", JOptionPane.ERROR_MESSAGE);
			else if(Validation.telFormValidate(values[6]) == false) JOptionPane.showMessageDialog(null, "번호의 형식에 맞지 않습니다. 다시 입력하세요.", "Error!", JOptionPane.ERROR_MESSAGE);
			else if(Validation.telValidate(validate, values[6]) == false) JOptionPane.showMessageDialog(null, "중복되는 번호입니다. 다시 입력하세요.", "Error!", JOptionPane.ERROR_MESSAGE);
			else {
				String sql = String.format("insert into members values('%s', '%s', '%s', '%s', '%s', 1, '%s')", values[0], values[2], values[3], values[4], values[5], values[6]);
				HashMap<String, Object> call  = dao.insert(sql);
				int resRow = (int)call.get("numOfRows");
				if(resRow == 1) JOptionPane.showMessageDialog(null, "회원가입이 완료되었습니다!", "Complete", JOptionPane.INFORMATION_MESSAGE);
				setVisible(false);
			}
		});
		submit.setBounds(150, 300, 100, 30);
		background.add(submit);
	}
}