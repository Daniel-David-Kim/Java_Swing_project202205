package sec01;

import java.awt.*;
import java.util.*;
import java.util.stream.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

public class QuizSelection extends JFrame {
	private Container contentPane;
	private JPanel base;
	private References ref = new References();
	private QuizDAO dao = new QuizDAO();
	private LoginInfo agent;
	private JList<String> subjects;
	private HashMap<String, String> result;
	private JFrame curPage;
	public QuizSelection(LoginInfo agent) {
		this.agent = agent;
		this.agent.removePrev();
		setTitle("Select Category!!");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		contentPane = getContentPane();
		base = ref.setBackgroundPanel("./images/desktop.jpg", 720, 480, null);
		base.add(mainPanel());
		add(base);	
		setResizable(false);
		setSize(720, 480);
		setLocationRelativeTo(null);
		setVisible(true);
		this.curPage = this;
	}
	
	private JPanel mainPanel() {
		JPanel res = new JPanel(null) {
			public void paintComponent(Graphics g) {
				g.drawImage((new ImageIcon("./images/exam.png")).getImage(), 0, 0, 500, 590, null);
			}
		};
		res.add(cancel());
		res.add(selectMenu());
		res.add(submitBtn());
		res.add(nameLabel());
		res.add(titleLabel());
		res.setBounds(100, 50, 500, 390);
		return res;
	}
	
	public JButton cancel() {
		JButton cancel = new JButton("����") {
			public void paintComponent(Graphics g) {
				g.drawImage((new ImageIcon("./images/giveup.png")).getImage(), 0, 0, 65, 30, null);
			}
		};
		cancel.setBorder(null);
		cancel.addActionListener((e) -> {
			agent.setPrev(curPage);
			curPage.setVisible(false);
			agent.setCur(new MainScreen(agent));
		});
		cancel.setBounds(40, 115, 65, 30);
		return cancel;
	}
	
	private JScrollPane selectMenu() {
		result = dao.getTablesMap().get("ones");
		Collection<String> temp = result.values();
		Vector<String> values = temp.stream().collect(Collectors.toCollection(Vector::new));
		subjects = new JList<String>(values);
		subjects.setFont(new Font("sans", Font.BOLD, 17));
		JScrollPane sp = new JScrollPane(subjects);
		sp.setBounds(57, 218, 387, 172);
		return sp;
	}
	
	private JButton submitBtn() {
		JButton btn = new JButton("����") {
			public void paintComponent(Graphics g) {
				g.drawImage((new ImageIcon("./images/start.jpg")).getImage(), 0, 0, 65, 30, null);
			}
		};
		btn.addActionListener((e) -> {
			String target = "";
			String select = subjects.getSelectedValue();
			if(select==null||select.equals("")) {
				JOptionPane.showMessageDialog(null, "������ �������ּ���.", "Error", JOptionPane.WARNING_MESSAGE);
				return;
			}
			Set<String> keys = result.keySet();
			for(String key:keys) {
				if(result.get(key).equals(select)) {
					target = key;
					break;
				}
			}
			int numQue = -1;
			//System.out.println("selected -> " + target);
			// ���� ���� 20�� ���ϸ� �غ��� �߰� ���Ѿ�� ��. 
			String sql = String.format("select * from %s", target);
			//System.out.println(sql);
			HashMap<String, Object> result = dao.select(sql, target, false);
			int numOfRows = (int)result.get("numOfRows");
			//System.out.println(numOfRows);
			if(numOfRows < 20) {
				JOptionPane.showMessageDialog(null, "�ش� ������ �غ� ���Դϴ�.", "Ready....", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			while(true) {
				String ip = JOptionPane.showInputDialog(null, "Ǯ ������ ������ �Է��ϼ���.(5/10/20 �� ����)", "input numbers", JOptionPane.INFORMATION_MESSAGE);
				if(ip==null||ip.equals("")) return;
				try {
					numQue = Integer.parseInt(ip);
					if(numQue != 5 && numQue != 10 && numQue != 20) throw new NotRangeException();
					break;
				} catch(NumberFormatException except) {
					JOptionPane.showMessageDialog(null, "�������� �Է����ּ���.", "Error", JOptionPane.WARNING_MESSAGE);
				} catch(NotRangeException except) {
					JOptionPane.showMessageDialog(null, "5/10/20 �߿����� �Է����ּ���.", "Error", JOptionPane.WARNING_MESSAGE);
				}
			}
			agent.setPrev(this);
			agent.setCur(new QuizBomb(agent, numQue, target));
			this.setVisible(false);
		});
		btn.setBounds(399, 115, 65, 30);
		return btn;
	}
	
	private JLabel nameLabel() {
		JLabel lb = new JLabel(agent.getUser().getUname());
		lb.setFont(new Font("sans", Font.BOLD, 12));
		lb.setBounds(123, 164, 75, 40);
		return lb;
	}
	
	private JLabel titleLabel() {
		JLabel lb = new JLabel("������ �������ּ���.") {
			public void paintComponent(Graphics g) {
				g.drawImage((new ImageIcon("./images/select.jpg")).getImage(), 0, 0, 175, 30, null);
			}
		};
		lb.setBounds(160, 79, 175, 50);
		return lb;
	}
	
	/*public static void main(String[] args) {
		// ����� �ӽ� �α��� ��ü�Դϴ�.
		LoginInfo agent = new LoginInfo(new Members("user3", "3456", "�̰迵", "3.���� �����ϱ�?", "������", 1, "010-3333-4444"));
		new QuizSelection(agent);
	}*/
}

class NotRangeException extends RuntimeException {
	public NotRangeException() {}
	public NotRangeException(String message) {super(message);}
}