package sec01;

import java.sql.*;
import java.util.*;

public class QuizDAO {
	private boolean returnVector = false;
	public boolean isReturnVector() { return returnVector; }
	public void setReturnVector(boolean returnVector) { this.returnVector = returnVector;}

	private Statement getStatement() {
		Statement stmt = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Quiz?serverTimezone=Asia/Seoul", "root", "1234");
			stmt = conn.createStatement();
		} catch(ClassNotFoundException e) {
			System.out.println("Class not found..... : QuizDAO/getConnection()");
		} catch(SQLException e) {
			System.out.println("Connetion Error : QuizDAO/getConnection()"); // �������� ��ġ�� �ľ��� �� �ֵ��� (���� ���� : ������ Ŭ����/������ �޼���) ������ �ۼ�.
		}
		return stmt;
	}
	
	// int crud : 1.create  2.read  3.update  4.delete   / boolean validate : ������ ������ ���� ������ ����
	private HashMap<String, Object> executeSQL(int crud, String sql, String target, boolean validate) { // int crud: crud �� ���� , String sql : sql������ , String target : ������ ��ǥ�� �Ǵ� ���̺� �̸� 
		Statement stmt = getStatement();
		HashMap<String, Object> result = new HashMap<String, Object>(); // ���̽��� ��ųʸ�ó�� ������ �ϱ� ���� �ؽø��� ��. ����, ���� Object�ϱ� ���ε��� ���� �°� ���� Ÿ�Ժ�ȯ�ؾ� �Ѵ�.
		int resRow = 0; // ������ ����� ������ ���̳� ������ ���� ���� ����
		// v�� �б� ������ ����� ��� ����, v2�� v3�� ������ ��ȿ�� ������ ���� ���� ���� ����� ���� ���͵�.
		Vector v = null; Vector v2 = null; Vector v3 = null; Vector v4 = null;
		if(validate == true) { v2 = new Vector<String>(); v3 = new Vector<String>(); }
		if(crud == 2) {
			if(this.returnVector == true || ((target != null) && target.equals("corrects")) ||  ((target != null) && target.equals("category"))) v4 = new Vector();
			if(target != null) { // target�� ���� Ÿ�� ���̺� ���� ���Ϳ� �� ���������� �ٲ�.
				if(target.equals("members")) v = new Vector<Members>();
				else if(getTablesMap().get("ones").containsKey(target)) v = new Vector<Question>(); // Ÿ�� ���̺��� ���� ���̺��� ���
			}
		}
		try {
			if(crud == 2) { // ** read�� ��
				ResultSet rs = stmt.executeQuery(sql);
				while(rs.next()) {
					if(target != null) {
						if(target.equals("members")) { 
							if(this.returnVector == true) {
								Vector temp = new Vector();
								temp.add(rs.getString(1)); temp.add(rs.getString(2)); temp.add(rs.getString(3)); temp.add(rs.getString(4)); temp.add(rs.getString(5)); temp.add(rs.getInt(6)); temp.add(rs.getString(7));
								v4.add(temp);
							} else {
								v.add(new Members(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6), rs.getString(7))); 
								if(validate == true) { v2.add(rs.getString(1)); v3.add(rs.getString(7)); }
							}
						} else if(getTablesMap().get("ones").containsKey(target)) { // Ÿ�� ���̺��� ���� ���̺��� ���
							if(this.returnVector == true) {
								Vector temp = new Vector();
								for(int i = 1; i <= 5; i++) temp.add(rs.getString(i));
								v4.add(temp);
							} else v.add(new Question(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));	
						} else if(target.equals("corrects")) {
							Vector temp = new Vector();
							HashMap<String, String> ones = getTablesMap().get("ones");
							Collection onesValue = ones.values();
							for(int i = 1; i <= onesValue.size()+1; i++) {
								if(i == 1) temp.add(rs.getString(i));
								else temp.add(rs.getInt(i));
							}
							v4.add(temp);
						} else if(target.equals("category")) {
							Vector temp = new Vector(Arrays.asList(rs.getString(1), rs.getString(2), rs.getInt(3)));
							v4.add(temp);
						}
					}
					resRow++;
				}
				if(this.returnVector == true || ((target != null) && target.equals("corrects"))) { 
					result.put("resultVector", v4);
					setReturnVector(false);
				} else result.put("resultData", v); // read ����� ���� ����� ��� ���� ���͸� �ؽøʿ� resultData ��� Ű�� ���ε���. 
				if(validate == true) { // ���� validate�� true�� ���, v2�� v3�� ��ȿ�� �˻縦 ���� ��鸸 ���� �����͸� ��� ���� validate2, validate3��� Ű�� ���ε���. 
					result.put("validate1", v2); 
					result.put("validate2", v3);
				}
			} else resRow = stmt.executeUpdate(sql); // ** read�� �ƴ� �ٸ� ������ ��
			
			result.put("numOfRows", resRow); // ������ ����� ������ ���̳� ������ ���� ���� ������ numOfRows��� Ű�� ���ε�. �׳� ��� �� ���� ���� ��쿡�� �̰͸� ���� ���� ��.
		} catch(SQLException e) {
			e.printStackTrace();
			System.out.println("Exception while executing query : QuizDAO/executeSQL()");
		}
		return result; // �� �޼��带 �Ἥ ��� �ؽø��� �޴´ٸ�, ��� ��="numOfRows" / read����� ���� �����͵�="resultData" / ��ȿ���� �����ϱ� ���� �÷��ǵ�="validate1", "validate2" ��� Ű������ ���ϴ� ���� �������� �ȴ�. 
	}
	
	public HashMap<String, Object> select(String sql, String target, boolean validate) { return executeSQL(2, sql, target, validate); }
	public HashMap<String, Object> insert(String sql) { return executeSQL(1, sql, null, false); }
	public HashMap<String, Object> update(String sql, String target) { return executeSQL(3, sql, target, false); }
	public HashMap<String, Object> delete(String sql, String target) { return executeSQL(4, sql, target, false); }
	public HashMap<String, HashMap<String, String>> getTablesMap() { // �������� ���� ���̺� �̸���� �� ���̺� �̸��� ��Ī�Ǵ� �ѱ��̸����� �����´�. 
		HashMap<String, HashMap<String, String>> qmaps = new HashMap<String, HashMap<String, String>>(); // ���� 1�� 2�� �� ��� �ؽø�. �׷��� Ű�� ���ڿ�, ���� ���� �ؽøʵ��� ������Ÿ���� HashMap<String, String>�Դϴ�.
			// �Ѹ����, ���̽����� ġ�� ��ųʸ� �ȿ� �� ��ųʸ��� ���� ���̶�� ���� �˴ϴ�.
		HashMap<String, String> allTable = new HashMap<String, String>(); // 1. �̰� ��� ���̺� ��ϵ��� �� ��� �ؽø�(���̽��� ��ųʸ�)
		HashMap<String, String> ones = new HashMap<String, String>(); // 2. �̰� category ���̺��� isSubject�� ���� 1�� ���̺��, ��, ���� ���̺�鸸 ��� �ؽø�!
		Statement stmt = getStatement();
		try {
			ResultSet rs = stmt.executeQuery("select * from category");
			while(rs.next()) {
				allTable.put(rs.getString(1), rs.getString(2)); // ��� ���̺� ��ϵ��� �� ����ϴ�.
				if(rs.getInt(3) == 1) ones.put(rs.getString(1), rs.getString(2)); // ���� ���̺�鸸 ��� ����ϴ�.
			}
			rs.close();
		} catch(SQLException e) {
			e.printStackTrace();
			System.out.println("Exception while executing query : QuizDAO/getTablesMap()");
		}
		qmaps.put("allTable", allTable); // ��� ���̺� ����� allTable�̶�� Ű������ �����մϴ�. ����, �� �޼��带 �����Ͽ� ���ϰ��� qmaps�� ������, qmaps.get("allTable")�� ������ �� �ֽ��ϴ�.
		qmaps.put("ones", ones);  // ���� ���̺� ����� ones�̶�� Ű������ �����մϴ�. ����, �� �޼��带 �����Ͽ� ���ϰ��� qmaps�� ������, qmaps.get("ones")�� ������ �� �ֽ��ϴ�.
		return qmaps;
	}
	
	public Vector<String> getArrangedTableNames() {
		Vector<String> result = new Vector<String>();
		Statement stmt = getStatement();
		try {
			ResultSet rs = stmt.executeQuery("desc corrects");
			while(rs.next()) result.add(rs.getString(1));
			rs.close();
		} catch(SQLException e) {
			e.printStackTrace();
			System.out.println("Exception while executing query : QuizDAO/getArrangedTableNames()");
		}
		//System.out.println(result);
		return result;
	}
}
