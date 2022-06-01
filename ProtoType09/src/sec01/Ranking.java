package sec01;

import java.awt.*;
import java.sql.*;
import java.util.*;
import java.util.stream.*;
import javax.security.auth.Subject;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;


public class Ranking extends JFrame {
	References ref = new References();
	private LoginInfo agent;
	private Connection con = makeConnection();
	private UpdateProfile thisPage;
	private QuizDAO dao = new QuizDAO();
	private HashMap<String, String> ones = dao.getTablesMap().get("ones"); // ���� ���̺���� ���ִ� �ؽø�
	public Collection<String> strtemp = ones.values();  
	public Vector<String> str = strtemp.stream().collect(Collectors.toCollection(Vector::new)); // ����� ����Ʈ �ؿ� 1�����ٰ� ��
	private String firstKey = "";
	{
		for(String key : ones.keySet()) {
			if(ones.get(key).equals(str.get(0))) {
				firstKey = key;
				break;
			}
		}
		firstKey = firstKey.substring(0, firstKey.indexOf("TBL"));
		//System.out.println("ù Ű : " + firstKey);
	}
	
	Container c = getContentPane();
	Rank rank;
	private JPanel background;
	
	JLabel rankLabels[] = new JLabel[5];
	JLabel nameLabels[] = new JLabel[5];
	JLabel scoreLabels[] = new JLabel[5];
	
	public Ranking(LoginInfo agent) {
		this.agent = agent;
		this.agent.removePrev();
		setTitle("ȸ������ ����");
		//System.out.println("user : " + agent.getUser());	
		setSize(720,480);
		//setLayout(null);
		background = ref.setBackgroundPanel("./images/RankBoard.png", 720, 450, null);
		JPanel subject = new Subject();
		
		JButton cancel = new JButton("���ư���") {
			public void paintComponent(Graphics g) {
				g.drawImage((new ImageIcon("./images/returnArr.png")).getImage(), 0, 0, 50, 40, null);
			}
		};
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				agent.setPrev(thisPage);
				setVisible(false);
				agent.setCur(new MainScreen(agent));
			}
		});
		cancel.setBounds(650, 390, 50, 40);
		cancel.setBorder(null);
		
		subject.setOpaque(false);
		rank =  new Rank(firstKey);
		rank.setOpaque(false);
		
		background.add(cancel);
		background.add(rank);
		background.add(subject);
		
		add(background);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	class Subject extends JPanel{ // ����� �ø��� �г�
		private Vector vec;
		private JList list;
		public Subject() {
			setLayout(null);
			JTextArea textArea = new JTextArea("������");
			setLayout(null);
			setBounds(45, 20, 200, 360);
			setBackground(Color.YELLOW);
			
			vec = new Vector<String>();
			list = new JList();
			list.setFont(new Font("���ʷҵ���", Font.BOLD, 12));
			for(int i=0; i<str.size(); i++)  // 1  Vector<String> str -> ���̺� �ѱ۸���� ��Ƴ��� ����
				vec.addElement(str.get(i)); // ���Ϳ��ٰ� �غ��Ͻð�
			list.setListData(vec);  // ����Ʈ���ٰ� ���
			list.setSelectedIndex(0); // ó�� �� ���� �� ���õǾ� �ִ� ����Ʈ
			
			list.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) { // ����Ʈ ������ �� ��ȭ�Ǵ� ������
						JList list = (JList)e.getSource();
						String subject = "os";
						String val = list.getSelectedValue().toString();
						for(String key : ones.keySet()) {
							if(ones.get(key).equals(val)) {
								subject = key;
								break;
							}
						}
						subject = subject.substring(0, subject.indexOf("TBL"));
						background.remove(rank);
						background.repaint();
						rank = new Rank(subject);
						background.add(rank);
						background.repaint();
				}
			});
			
			JScrollPane scroll = new JScrollPane(list);
			scroll.setBounds(0, 120, 160, 230);
			
			add(scroll);
		}
	}
	
	class Rank extends JPanel{ // ��ŷ �ø��� �г�
		public Rank(String sub) {
			setBounds(270, 20, 400, 400);
			setLayout(null);
			setBackground(Color.yellow);
			
			ShowRank(sub);
			setOpaque(false);
	}
		
	public void ShowRank(String sub) {
		//System.out.println(sub);
		ArrayList<Ranker> ranker = new ArrayList<Ranker>(); // corrects�� �ִ� ��� ȸ���� ��� ��
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
		
		Arrays.sort(rankers);
		ranker.sort(Comparator.comparing(Ranker::getIntScore, Comparator.reverseOrder())); // �÷����� ����ȭ�� ������ �������� ����
		LinkedList<String> ids = ranker.stream().map((r) -> r.getName()).collect(Collectors.toCollection(LinkedList::new)); // �̸��� �̾Ƽ� �÷������� ����
		//for(Ranker r:ranker) System.out.println(r);
		//System.out.println();
		//for(String s:ids) System.out.println(s);
		int myRank = ids.indexOf(agent.getUser().getUid()) + 1; // �� ��� ���� 
		//System.out.println();
		//System.out.println(myRank);
		
		String name[] = new String[5]; // ���� 5��
		try {
			String sql = "select uname from members where uid = ?";
			PreparedStatement pstmt = con.prepareStatement(sql);
			for(int i=0; i<name.length; i++) { // ���� 5������� 
				pstmt.setString(1, rankers[i].getName());
				ResultSet rs = pstmt.executeQuery();
				rs.next();
				name[i] = rs.getString(1); // �̸� �̾ƿ���
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		rankLabels = new JLabel[5];
		nameLabels = new JLabel[5];
		scoreLabels = new JLabel[5];
		
		for(int i = 0; i < 5; i++) {
			rankLabels[i] = new JLabel(String.valueOf(i+1));
			rankLabels[i].setFont(new Font("Dotum", Font.BOLD, 23));
			nameLabels[i] = new JLabel(name[i]);
			//System.out.print(name[i] + " : ");
			nameLabels[i].setFont(new Font("HY������", Font.BOLD, 23));
			scoreLabels[i] = new JLabel(rankers[i].getScore()); // �� ��Ŀ�� ���� ����
			//System.out.println(rankers[i].getScore());
			scoreLabels[i].setFont(new Font("Dotum", Font.BOLD, 23));
			rankLabels[i].setBounds(20, 120+(i-1)*50, 100, 35);
			rankLabels[i].setForeground(Color.BLUE);
			nameLabels[i].setBounds(100, 120+(i-1)*50, 190, 35);
			nameLabels[i].setForeground(Color.WHITE);
			scoreLabels[i].setBounds(300, 120+(i-1)*50, 200, 35);
			scoreLabels[i].setForeground(Color.YELLOW);
			
			this.add(rankLabels[i]);
			this.add(nameLabels[i]); // �гο��ٰ� �߰�
			this.add(scoreLabels[i]);
		}
		JLabel myRankLb = new JLabel(String.valueOf(myRank));
		myRankLb.setFont(new Font("Dotum", Font.BOLD, 23));
		myRankLb.setForeground(Color.BLUE);
		myRankLb.setBounds(20, 365, 100, 35);
		JLabel myNameLb = new JLabel(agent.getUser().getUname());
		myNameLb.setFont(new Font("HY������", Font.BOLD, 23));
		myNameLb.setForeground(Color.WHITE);
		myNameLb.setBounds(100, 365, 190, 35);
		JLabel myScoreLb = new JLabel(ranker.get(myRank-1).getScore());
		myScoreLb.setFont(new Font("Dotum", Font.BOLD, 23));
		myScoreLb.setForeground(Color.YELLOW);
		myScoreLb.setBounds(300, 365, 200, 35);
		add(myRankLb);
		add(myNameLb);
		add(myScoreLb);
		
		JLabel dotted = new JLabel("...");
		dotted.setFont(new Font("Dotum", Font.BOLD, 23));
		dotted.setForeground(Color.YELLOW);
		dotted.setBounds(170, 310, 200, 35);
		add(dotted);
		//System.out.println();
		repaint();
		revalidate();
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
	
	/*public static void main(String[] args) { // ����� �ӽ� �α��� ��ü�Դϴ�.
		LoginInfo agent = new LoginInfo(new Members("admin1", "01234", "������", "���� �����ϱ�?", "������", 0, "010-1000-1111"));
		new Ranking(agent);
	}*/
		
}