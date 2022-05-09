package sec01;

import java.sql.*;
import java.util.*;

public class QuizDAO {
	private Statement getStatement() {
		Statement stmt = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Quiz?serverTimezone=Asia/Seoul", "root", "1234");
			stmt = conn.createStatement();
		} catch(ClassNotFoundException e) {
			System.out.println("Class not found..... : QuizDAO/getConnection()");
		} catch(SQLException e) {
			System.out.println("Connetion Error : QuizDAO/getConnection()");
		}
		return stmt;
	}
	
	// int crud : 1.create  2.read  3.update  4.delete   
	private HashMap<String, Object> executeSQL(int crud, String sql, String target, boolean validate) {
		Statement stmt = getStatement();
		HashMap<String, Object> result = new HashMap<String, Object>(); 
		int resRow = 0;
		Vector v = null; Vector v2 = null; Vector v3 = null;
		if(validate == true) { v2 = new Vector<String>(); v3 = new Vector<String>(); }
		if(target != null) {
			switch(target) {
				case "members" : v = new Vector<Members>(); break;
			}
		}
		try {
			if(crud == 2) {
				ResultSet rs = stmt.executeQuery(sql);
				while(rs.next()) {
					if(target != null) {
						switch(target) {
							case "members" : {
								v.add(new Members(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6), rs.getString(7))); 
								if(validate == true) { v2.add(rs.getString(1)); v3.add(rs.getString(7)); }
								break;
							}
						}
					}
					resRow++;
				}
				result.put("resultData", v);
				if(validate == true) {
					result.put("validate1", v2);
					result.put("validate2", v3);
				}
			} else resRow = stmt.executeUpdate(sql);
			
			result.put("numOfRows", resRow);
		} catch(SQLException e) {
			System.out.println("Exception while executing query : QuizDAO/executeSQL()");
		}
		return result;
	}
	
	public HashMap<String, Object> select(String sql, String target, boolean validate) { return executeSQL(2, sql, target, validate); }
	public HashMap<String, Object> insert(String sql) { return executeSQL(1, sql, null, false); }
}
