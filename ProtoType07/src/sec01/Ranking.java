package sec01;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import javax.security.auth.Subject;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Ranking extends JFrame {
	References ref = new References();
	private LoginInfo agent;
	private Connection con = makeConnection();
	private UpdateProfile thisPage;
	public String[] str = {"운영체제","정보통신"};
	Container c = getContentPane();
	Rank rank = new Rank("os");
	
	JLabel nameLabels[] = new JLabel[5];
	JLabel scoreLabels[] = new JLabel[5];
	
	public Ranking(LoginInfo agent) {
		this.agent = agent;
		this.agent.removePrev();
		setTitle("회원정보 수정");
		setSize(720,480);
		setLayout(null);
		JPanel subject = new Subject();
		
		JButton cancel = new JButton("돌아가기");
		
		cancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				agent.setPrev(thisPage);
				setVisible(false);
				agent.setCur(new MainScreen(agent));
			}
		});
		
		cancel.setBounds(600, 390, 100, 40);
		
		add(cancel);
		add(rank);
		add(subject);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
	}
	
	class Subject extends JPanel{
		private Vector vec;
		private JList list;
		public Subject() {
			setLayout(null);
			JTextArea textArea = new JTextArea("연습용");
			setLayout(null);
			setBounds(50, 20, 200, 360);
			setBackground(Color.YELLOW);
			
			vec = new Vector<String>();
			list = new JList();
			for(int i=0; i<str.length; i++) 
				vec.addElement(str[i]);
			list.setListData(vec);
			list.setSelectedIndex(0);
			
			list.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
						JList list = (JList)e.getSource();
						String subject = "os";
						int index = list.getSelectedIndex();
						switch (index) {
						case 0:	subject = "os"; break;
						case 1:	subject = "ct";break;
						}
						
						c.remove(rank);
						c.repaint();
						rank = new Rank(subject);
						c.add(rank);
						c.repaint();
				}
			});
			
			JLabel title = new JLabel("명예의 전당");
			title.setFont(new Font("Dotum", Font.BOLD, 20));
			title.setBounds(50,10,150,20);
			
			add(title);
			
			JScrollPane scroll = new JScrollPane(list);
			scroll.setBounds(20, 60, 160, 280);
			
			add(scroll);
		}
	}
	
	class Rank extends JPanel{
		
		public Rank(String sub) {
			setBounds(270, 20, 400, 360);
			setLayout(null);
			setBackground(Color.yellow);
			
			JLabel title = new JLabel("Ranking");
			title.setFont(new Font("Dotum", Font.BOLD, 24));
			title.setBounds(150,10,150,40);
			
			ShowRank(sub);
			
			add(title);
			
	}
	public void ShowRank(String sub) {
		ArrayList<Ranker> ranker = new ArrayList<Ranker>();
		try {
			Statement stmt = con.createStatement();
			String sql = "select uid, "+ sub+" from corrects";	
		
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				Ranker r = new Ranker(rs.getString("uid"),rs.getString(sub));
				ranker.add(r);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		Ranker[] rankers = new Ranker[ranker.size()];
		
		for(int i = 0; i < ranker.size(); i++) 
			rankers[i] = ranker.get(i);
		
		for(int i = 0; i < rankers.length; i++) {
			for(int j = 0; j < rankers.length - i-1; j++) {
				if(Integer.parseInt(rankers[j].getScore()) > Integer.parseInt(rankers[j+1].getScore())) {
					Object temp = rankers[i];
					rankers[i] = rankers[j];
					rankers[j] = (Ranker)temp;
		}
			}
		}
		
		Arrays.sort(rankers);
		String name[] = new String[5];
		try {
			String sql = "select uname from members where uid = ?";
			PreparedStatement pstmt = con.prepareStatement(sql);
			for(int i=0; i<name.length; i++) {
				pstmt.setString(1, rankers[i].getName());
				ResultSet rs = pstmt.executeQuery();
				rs.next();
				name[i] = rs.getString(1);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		nameLabels = new JLabel[5];
		scoreLabels = new JLabel[5];
		
		for(int i = 0; i < 5; i++) {
			nameLabels[i] = new JLabel((i+1)+"등 "+name[i]+":");
			nameLabels[i].setFont(new Font("Dotum", Font.BOLD, 23));
			scoreLabels[i] = new JLabel(rankers[i].getScore());
			scoreLabels[i].setFont(new Font("Dotum", Font.BOLD, 23));
			nameLabels[i].setBounds(50, 100+(i-1)*50, 300, 35);
			scoreLabels[i].setBounds(250, 100+(i-1)*50, 200, 35);
			
			this.add(nameLabels[i]);
			this.add(scoreLabels[i]);
		}
		repaint();
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
	
	
}

	

