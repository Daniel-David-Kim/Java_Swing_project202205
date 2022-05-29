package sec01;

import java.util.*;
import javax.swing.*;

// ·Î±×ÀÎ °´Ã¼.
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
}
