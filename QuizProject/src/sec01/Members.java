package sec01;


// Members 테이블 Bean 클래스 
public class Members {
	private String uid;
	private String upw;
	private String uname;
	private String ufindq;
	private String ufinda;
	private int uclass;
	private String utel;
	public String getUid() {return uid;}
	public void setUid(String uid) {this.uid = uid;}
	public String getUpw() {return upw;}
	public void setUpw(String upw) {this.upw = upw;}
	public String getUname() {return uname;}
	public void setUname(String uname) {this.uname = uname;}
	public String getUfindq() {return ufindq;}
	public void setUfindq(String ufindq) {this.ufindq = ufindq;}
	public String getUfinda() {return ufinda;}
	public void setUfinda(String ufinda) {this.ufinda = ufinda;}
	public int getUclass() {return uclass;}
	public void setUclass(int uclass) {this.uclass = uclass;}
	public String getUtel() {return utel;}
	public void setUtel(String utel) {this.utel = utel;}
	public Members(String uid, String upw, String uname, String ufindq, String ufinda, int uclass, String utel) {
		this.uid = uid; this.upw = upw; this.uname = uname; this.ufindq = ufindq;
		this.ufinda = ufinda; this.uclass = uclass; this.utel = utel;
	}
	public String toString() {
		return String.format("Member %s / %s / %s / %s / %s / %d / %s", uid, upw, uname, ufindq, ufinda, uclass, utel);
	}
}
