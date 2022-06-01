package sec01;

// osTBL, ctTBL 등 문제 테이블을 위한 Besn 클래스
public class Question {
	private String que;
	private String ans;
	private String wr1;
	private String wr2;
	private String wr3;
	public String getQue() {return que;}
	public void setQue(String que) {this.que = que;}
	public String getAns() {return ans;}
	public void setAns(String ans) {this.ans = ans;}
	public String getWr1() {return wr1;}
	public void setWr1(String wr1) {this.wr1 = wr1;}
	public String getWr2() {return wr2;}
	public void setWr2(String wr2) {this.wr2 = wr2;}
	public String getWr3() {return wr3;}
	public void setWr3(String wr3) {this.wr3 = wr3;}
	public Question(String que, String ans, String wr1, String wr2, String wr3) {this.que = que; this.ans = ans; this.wr1 = wr1; this.wr2 = wr2; this.wr3 = wr3;}
}
