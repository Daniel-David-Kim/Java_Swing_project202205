package sec01;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

public class FindAccount extends JFrame {
	private Container contentPane;
	private JPanel background;
	private References ref = new References();
	private QuizDAO dao = new QuizDAO();
	FindAccount(){
		setTitle("ID / Password ã��");
		contentPane = getContentPane();
		background = ref.setBackgroundPanel("./images/security.jpg", 300, 300, new BorderLayout());
		background.add(new FindID(), BorderLayout.CENTER);
		background.add(createRadios(), BorderLayout.NORTH);
		add(background);
		setSize(300, 300);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	/*public static void main(String[] args) {
		new FindAccount();
	}*/
	private JPanel createRadios() {
		JPanel pan = new JPanel();
		pan.setOpaque(false);
		ButtonGroup rg = new ButtonGroup();
		JRadioButton[] radios = new JRadioButton[2];
		String[] menus = {"ID ã��", "PW ã��"};
		ItemListener MyItem = (e) -> {
			String label = ((JRadioButton)e.getSource()).getText();
			switch(label) {
				case "ID ã��" : showPanel(new FindID()); break;
				case "PW ã��" : showPanel(new FindPW()); break;
			}
		};
		for(int i = 0; i < radios.length; i++) {
			radios[i] = new JRadioButton(menus[i]);
			radios[i].addItemListener(MyItem);
			rg.add(radios[i]);
			pan.add(radios[i]);
		}
		 radios[0].setSelected(true);
		return pan;
	}
	private void showPanel(JPanel panel) {
		background.remove(0);
		background.add(panel, 0);
		background.revalidate();
	}
	class FindID extends JPanel {
		FindID() {
			setSize(200, 150);
			setLayout(null);
			setOpaque(false);
			JComponent[][] comps = new JComponent[2][2];
			String[] names = {"�̸�", "�޴��� ��ȣ"};
			for(int i = 0; i < comps.length; i++) {
				for(int j = 0; j < comps[i].length; j++) {
					comps[i][j] = j%2==0?new JLabel(names[i]):new JTextField(15);
					comps[i][j].setBounds(125+(j-1)*95, 85+(i-1)*40, j%2==0?100:120, 20);
					add(comps[i][j]);
				}
			}
			JButton btn = new JButton("���̵� ã��");
			btn.addActionListener((e)->{
				String name = ((JTextField)comps[0][1]).getText();
				String tel = ((JTextField)comps[1][1]).getText();
				if(!name.equals("") && !tel.equals("")) {
					String sql = String.format("select * from members where uname='%s' and utel='%s'", name, tel);
					HashMap<String, Object> call = dao.select(sql, "members", false);
					int resRow = (int)call.get("numOfRows");
					if(resRow == 0) JOptionPane.showMessageDialog(null, "�ش� ȸ�� ������ ã�� �� �����ϴ�.....", "Error!!", JOptionPane.ERROR_MESSAGE);
					else if(resRow == 1) {
						String id = ((Vector<Members>)call.get("resultData")).get(0).getUid();
						JOptionPane.showMessageDialog(null, String.format("���̵�� %s �Դϴ�", id), "Result", JOptionPane.PLAIN_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(null, "�̸��� �޴��� ��ȣ�� �ʼ� �Է»����Դϴ�.", "Error!", JOptionPane.ERROR_MESSAGE);
				}
			});
			btn.setBounds(90, 125, 100, 25);
			add(btn);
		}
	}
	class FindPW extends JPanel {
		FindPW() {
			setSize(200, 150);
			setLayout(null);
			setOpaque(false);
			JLabel label = new JLabel("���̵�");
			label.setBounds(50, 75, 100, 20);
			JTextField ip = new JTextField(15);
			ip.setBounds(110, 75, 120, 20);
			JButton btn = new JButton("��й�ȣ ã��");
			btn.addActionListener((e)->{
				String id = ip.getText();
				if(!id.equals("")) {
					HashMap<String, Object> call = dao.select(String.format("select * from members where uid='%s'", id), "members", false);
					int resRow = (int)call.get("numOfRows");
					if(resRow == 0) JOptionPane.showMessageDialog(null, "�ش� ȸ�� ������ ã�� �� �����ϴ�.....", "Error!", JOptionPane.ERROR_MESSAGE);
					else if(resRow == 1) {
						Members target = ((Vector<Members>)call.get("resultData")).get(0);
						String ans = JOptionPane.showInputDialog(null, target.getUfindq(), "��й�ȣ ã�� ����", JOptionPane.QUESTION_MESSAGE);
						if(ans != null && !ans.equals(""))
							if(ans.equals(target.getUfinda())) JOptionPane.showMessageDialog(null, String.format("��й�ȣ�� %s �Դϴ�", target.getUpw()), "Result", JOptionPane.PLAIN_MESSAGE);
							else if(!ans.equals(target.getUfinda())) JOptionPane.showMessageDialog(null, "�Էµ� ������ �߸��Ǿ����ϴ�.", "Error!", JOptionPane.ERROR_MESSAGE);
					}
				} else JOptionPane.showMessageDialog(null, "���̵�� �ʼ� �Է»����Դϴ�.", "Error!", JOptionPane.ERROR_MESSAGE);
			});
			btn.setBounds(80, 125, 120, 25);
			add(label);
			add(ip);
			add(btn);
		}
	}
}
