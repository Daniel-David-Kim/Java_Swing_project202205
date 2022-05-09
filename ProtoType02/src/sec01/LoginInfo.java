package sec01;

import javax.swing.*;

// ·Î±×ÀÎ °´Ã¼.
public class LoginInfo {
	private Members user;
	private JFrame prev;
	private JFrame cur;
	public LoginInfo(Members user) { this.user = user; }
	public void removePrev() { prev = null; }
	
	public Members getUser() { return user; }
	public void setUser(Members user) { this.user = user; }
	public JFrame getPrev() { return prev; }
	public void setPrev(JFrame prev) { this.prev = prev; }
	public JFrame getCur() { return cur; }
	public void setCur(JFrame cur) { this.cur = cur; }
}
