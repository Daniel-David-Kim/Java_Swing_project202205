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
	private HashMap<String, String> ones = dao.getTablesMap().get("ones"); // 과목 테이블들이 모여있는 해시맵
	public Collection<String> strtemp = ones.values();  
	public Vector<String> str = strtemp.stream().collect(Collectors.toCollection(Vector::new)); // 과목들 리스트 밑에 1번에다가 쏨
	private String firstKey = "";
	{
		for(String key : ones.keySet()) {
			if(ones.get(key).equals(str.get(0))) {
				firstKey = key;
				break;
			}
		}
		firstKey = firstKey.substring(0, firstKey.indexOf("TBL"));
		//System.out.println("첫 키 : " + firstKey);
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
		setTitle("회원정보 수정");
		//System.out.println("user : " + agent.getUser());	
		setSize(720,480);
		//setLayout(null);
		background = ref.setBackgroundPanel("./images/RankBoard.png", 720, 450, null);
		JPanel subject = new Subject();
		
		JButton cancel = new JButton("돌아가기") {
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
	
	class Subject extends JPanel{ // 과목들 올리는 패널
		private Vector vec;
		private JList list;
		public Subject() {
			setLayout(null);
			JTextArea textArea = new JTextArea("연습용");
			setLayout(null);
			setBounds(45, 20, 200, 360);
			setBackground(Color.YELLOW);
			
			vec = new Vector<String>();
			list = new JList();
			list.setFont(new Font("함초롬돋움", Font.BOLD, 12));
			for(int i=0; i<str.size(); i++)  // 1  Vector<String> str -> 테이블 한글명들을 모아놓은 벡터
				vec.addElement(str.get(i)); // 벡터에다가 준비하시고
			list.setListData(vec);  // 리스트에다가 쏘세요
			list.setSelectedIndex(0); // 처음 들어가 있을 때 선택되어 있는 리스트
			
			list.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) { // 리스트 눌렀을 때 변화되는 리스너
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
	
	class Rank extends JPanel{ // 랭킹 올리는 패널
		public Rank(String sub) {
			setBounds(270, 20, 400, 400);
			setLayout(null);
			setBackground(Color.yellow);
			
			ShowRank(sub);
			setOpaque(false);
	}
		
	public void ShowRank(String sub) {
		//System.out.println(sub);
		ArrayList<Ranker> ranker = new ArrayList<Ranker>(); // corrects에 있는 모든 회원들 담는 곳
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
		ranker.sort(Comparator.comparing(Ranker::getIntScore, Comparator.reverseOrder())); // 컬렉션을 정수화한 점수를 기준으로 정렬
		LinkedList<String> ids = ranker.stream().map((r) -> r.getName()).collect(Collectors.toCollection(LinkedList::new)); // 이름만 뽑아서 컬렉션으로 추출
		//for(Ranker r:ranker) System.out.println(r);
		//System.out.println();
		//for(String s:ids) System.out.println(s);
		int myRank = ids.indexOf(agent.getUser().getUid()) + 1; // 내 등수 추출 
		//System.out.println();
		//System.out.println(myRank);
		
		String name[] = new String[5]; // 상위 5명
		try {
			String sql = "select uname from members where uid = ?";
			PreparedStatement pstmt = con.prepareStatement(sql);
			for(int i=0; i<name.length; i++) { // 상위 5명까지만 
				pstmt.setString(1, rankers[i].getName());
				ResultSet rs = pstmt.executeQuery();
				rs.next();
				name[i] = rs.getString(1); // 이름 뽑아오기
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
			nameLabels[i].setFont(new Font("HY헤드라인", Font.BOLD, 23));
			scoreLabels[i] = new JLabel(rankers[i].getScore()); // 그 랭커의 점수 추출
			//System.out.println(rankers[i].getScore());
			scoreLabels[i].setFont(new Font("Dotum", Font.BOLD, 23));
			rankLabels[i].setBounds(20, 120+(i-1)*50, 100, 35);
			rankLabels[i].setForeground(Color.BLUE);
			nameLabels[i].setBounds(100, 120+(i-1)*50, 190, 35);
			nameLabels[i].setForeground(Color.WHITE);
			scoreLabels[i].setBounds(300, 120+(i-1)*50, 200, 35);
			scoreLabels[i].setForeground(Color.YELLOW);
			
			this.add(rankLabels[i]);
			this.add(nameLabels[i]); // 패널에다가 추가
			this.add(scoreLabels[i]);
		}
		JLabel myRankLb = new JLabel(String.valueOf(myRank));
		myRankLb.setFont(new Font("Dotum", Font.BOLD, 23));
		myRankLb.setForeground(Color.BLUE);
		myRankLb.setBounds(20, 365, 100, 35);
		JLabel myNameLb = new JLabel(agent.getUser().getUname());
		myNameLb.setFont(new Font("HY헤드라인", Font.BOLD, 23));
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
	
	/*public static void main(String[] args) { // 실험용 임시 로그인 객체입니다.
		LoginInfo agent = new LoginInfo(new Members("admin1", "01234", "관리쟝", "나는 누구일까?", "관리자", 0, "010-1000-1111"));
		new Ranking(agent);
	}*/
		
}