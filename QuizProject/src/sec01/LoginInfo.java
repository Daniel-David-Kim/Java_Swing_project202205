package sec01;

import java.awt.Font;
import java.util.*;
import javax.swing.*;

// 로그인 객체.
public class LoginInfo {
	private Members user;
	private JFrame prev;
	private JFrame cur;
	private JFrame dialog;
	private int rankclass;
	private QuizDAO dao;
	private HashMap<String, Object> correctData;
	public LoginInfo(Members user) { 
		this.user = user; 
		dao = new QuizDAO();
		correctData = dao.select("select * from corrects where uid='" + user.getUid() + "'", "corrects", false);
		
	}
	public void removePrev() { prev = null; }
	public void removeDialog() { dialog = null; }
	
	public Members getUser() { return user; }
	public void setUser(Members user) { this.user = user; }
	public JFrame getPrev() { return prev; }
	public void setPrev(JFrame prev) { this.prev = prev; }
	public JFrame getCur() { return cur; }
	public void setCur(JFrame cur) { this.cur = cur; }
	public JFrame getDialog() {return dialog;}
	public void setDialog(JFrame dialog) {this.dialog = dialog;}
	public JButton logout(JFrame curPage, int x, int y, int width, int height) {
		JButton logout = new JButton("로그아웃");
		logout.setBounds(x, y, width, height);
		logout.addActionListener((e) -> {
			JOptionPane.showMessageDialog(null, "로그아웃 되었습니다.", "Log out", JOptionPane.INFORMATION_MESSAGE);
			setPrev(curPage);
			curPage.setVisible(false);
			setCur(new Intro(this));
		});
		return logout;
	}
	public JLabel loginLabel(int x, int y, int width, int height, Font font) {
		JLabel lb = new JLabel("[" + (getUser().getUclass() == 0? "관리자":"회원") + "] " + getUser().getUname());
		lb.setBounds(x, y, width, height);
		lb.setFont(font);
		return lb;
	}
}
