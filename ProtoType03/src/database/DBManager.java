package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import sec01.Members;

public class DBManager {
	public static Connection con = makeConnection();

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
	
	public void UpdateProfile(Members member) {
		PreparedStatement pstmt;
		String sql = "update members set upw = ?, uname = ?, ufindq =?, ufinda = ?,"
				+ " utel = ? where uid = ?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, member.getUpw());
			pstmt.setString(2, member.getUname());
			pstmt.setString(3, member.getUfindq());
			pstmt.setString(4, member.getUfinda());
			pstmt.setString(5, member.getUtel());
			pstmt.setString(6, member.getUid());
			pstmt.executeUpdate();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
