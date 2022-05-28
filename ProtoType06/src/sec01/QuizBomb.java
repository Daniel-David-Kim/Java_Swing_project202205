package sec01;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.util.List;
import java.awt.event.*;
import javax.swing.border.*;

public class QuizBomb extends JFrame {
	private QuizBomb thisFrame;
	private JPanel base;
	private JPanel qPanel;
	private References ref = new References();
	private LoginInfo agent;
	private String tblName;
	private HashMap<String, Object> result;
	private QuizDAO dao = new QuizDAO();
	private LinkedList<Integer> extracted = new LinkedList<Integer>();
	private Vector<Question> ques;
	private int second = 0;
	private int count = 0;
	private int queNum;
	private Boolean timeOver = false;
	private Counter counter;
	private ArrayList<Integer> previous = new ArrayList<Integer>();
	private int correct = 0;
	private boolean corOrwr = false;
	private boolean nextQue = false;
	private boolean ended = false;
	private JLabel timer;
	HashMap<String, String> qmaps;
	
	private void plusCorrect() {this.correct += 1;}
	private boolean isCorOrwr() {return corOrwr;}
	private void setCorOrwr(boolean corOrwr) {this.corOrwr = corOrwr;}
	private boolean isNextQue() {return nextQue;}
	private void setNextQue(boolean nextQue) {this.nextQue = nextQue;}
	private boolean isEnded() {return ended;}
	private void setEnded(boolean ended) {this.ended = ended;}
	public QuizBomb(LoginInfo agent, int queNum, String tblName) {
		this.agent = agent;
		this.agent.removePrev();
		result = dao.select(String.format("select * from %s", tblName), tblName, false);
		ques = (Vector<Question>)result.get("resultData");
		qmaps = dao.getTablesMap().get("ones");
		this.queNum = queNum;
		this.tblName = tblName;
		setTitle("Quiz!!");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		base = ref.setBackgroundPanel("./images/desktop.jpg", 720, 480, null);
		processQuestion(queNum);
		Watcher watcher = new Watcher();
		watcher.setDaemon(true);
		watcher.start();
		add(base);
		setSize(720, 480);
		setResizable(false);
		setVisible(true);
		thisFrame = this;
	}
	
	public void processQuestion(int queNum) {
		count++;
		Random rand = new Random();
		JLabel queLabel; JPanel selPanel; JLabel[] sels = new JLabel[4];
		selPanel = new JPanel();
		int randInt = -1;
		while(true) {
			randInt = rand.nextInt(ques.size());
			if(previous.contains(randInt)) continue;
			else break;
		}
		previous.add(randInt);
		Question selected = ques.get(randInt);
		queLabel = new JLabel("<html>[" + count + "]" + selected.getQue() + "</html>");
		queLabel.setFont(new Font("돋움", Font.PLAIN, 13));
		queLabel.setBorder(new LineBorder(Color.BLACK, 1));
		queLabel.setBounds(30, 25, 435, 100);
		String ans = selected.getAns();
		String[] arr = {ans, selected.getWr1(), selected.getWr2(), selected.getWr3()};
		List<String> lis = Arrays.asList(arr);
		for(int j = 0; j < 5; j++) Collections.shuffle(lis);
		int ansNum = lis.indexOf(ans);
		for(int k = 0; k < sels.length; k++) {
			sels[k] = new JLabel("<html>[" + (k+1) + "]" + lis.get(k).trim() + "&nbsp;&nbsp;</html>");
			sels[k].setFont(new Font("돋움", Font.PLAIN, 12));
			sels[k].addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					String text = ((JLabel)e.getSource()).getText();
					int idx = Integer.parseInt(text.substring(7, 8))-1;
					setCorOrwr(true);
					if(idx == ansNum) {
						counter.interrupt();
						if(count <= queNum) JOptionPane.showMessageDialog(null, "맞히셨습니다!", "Correct!", JOptionPane.INFORMATION_MESSAGE);
						plusCorrect();
					} else {
						counter.interrupt();
						if(count <= queNum) JOptionPane.showMessageDialog(null, "틀렸습니다.....\n정답은 " + (ansNum+1) + "번입니다!", "Wrong!", JOptionPane.WARNING_MESSAGE);
					}
					if(count == queNum) count++;
					setNextQue(true);
				}
			});
			selPanel.add(sels[k]);
		}
		selPanel.setBounds(30, 140, 435, 180);
		selPanel.setBorder(new LineBorder(Color.BLACK, 1));
		showQuestion(queLabel, selPanel);
	}
	
	public void showQuestion(JLabel que, JPanel sel) {
		qPanel = new JPanel(null);
		qPanel.setBounds(100, 100, 500, 600);
		timer = new JLabel("타이머 : " + String.valueOf(second));
		timer.setBounds(430, 5, 150, 20);
		timer.setFont(new Font("돋움", Font.BOLD, 12));
		String catstr = qmaps.get(tblName);
		JLabel cat = new JLabel(catstr + " 영역");
		cat.setFont(new Font("돋움", Font.PLAIN, 11));
		cat.setBounds(30, 5, 150, 20);
		qPanel.add(cat);
		qPanel.add(que);
		qPanel.add(sel);
		qPanel.add(timer);
		counter = new Counter();
		counter.start();
		base.add(qPanel);
		base.revalidate();
		base.repaint();
	}
	
	public void removePrevQue() {
		qPanel.removeAll();
		base.removeAll();
	}
	
	class Counter extends Thread {	
		public void run() {
			for(int i = 0; i < 10; i++) {
				try {
					second += 1;
					timer.setText("타이머 : " + String.valueOf(second));
					timer.repaint();
					Thread.sleep(1000);
				} catch(InterruptedException e) { break; }
			}
			if(isCorOrwr() == false && count <= queNum) {
				JOptionPane.showMessageDialog(null, "시간 초과!", "Wrong!", JOptionPane.WARNING_MESSAGE);
				if(count == queNum) count++;
				timeOver = true;
			}
		}
	}

	class Watcher extends Thread {
		public void run() {
			while(true) {
				if(count == queNum + 1) {
					setEnded(true);
					counter.interrupt();
					int idx = tblName.indexOf("TBL");
					String col = tblName.substring(0, idx);
					String sql = String.format("update corrects set %s=%s+%d where uid=\'%s\'", col, col, correct, agent.getUser().getUid());
					HashMap<String, Object> result = dao.update(sql, "corrects");
					if((int)result.get("numOfRows") == 1) System.out.println("count plus success!!");
					else System.out.println("count plus falied.....");
					JOptionPane.showMessageDialog(null, String.format("#####결과#####\n총 문제 갯수 : %d\n맞춘 갯수 : %d\n틀린 갯수 : %d", queNum, correct, queNum-correct), "Result", JOptionPane.INFORMATION_MESSAGE);
					agent.setPrev(thisFrame);
					agent.setCur(new MainScreen(agent));
					thisFrame.setVisible(false);
					return;
				} else if(timeOver == true) {
					timeOver = false;
					second = 0;
					removePrevQue();
					processQuestion(queNum);
				} else if(isNextQue() == true) {
					setNextQue(false);
					setCorOrwr(false);
					second = 0;
					removePrevQue();
					processQuestion(queNum);
				}
				try {Thread.sleep(500);} 
				catch(InterruptedException e) {
					System.out.println("돌아올 수 없는 강을 건넜습니다....");
					return;
				}
			}
		}
	}
	
	public static void main(String[] args) {
		// 실험용 임시 로그인 객체입니다.
		LoginInfo agent = new LoginInfo(new Members("user3", "3456", "이계영", "3.나는 누구일까?", "셋유저", 1, "010-3333-4444"));
		new QuizBomb(agent, 5, "ctTBL");
	}
}