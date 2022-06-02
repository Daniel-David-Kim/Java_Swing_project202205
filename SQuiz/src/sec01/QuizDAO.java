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
			System.out.println("Connetion Error : QuizDAO/getConnection()"); // 에러문은 위치를 파악할 수 있도록 (에러 내용 : 에러난 클래스/에러난 메서드) 순으로 작성.
		}
		return stmt;
	}
	
	// int crud : 1.create  2.read  3.update  4.delete   / boolean validate : 데이터 검증을 위한 건지의 여부
	private HashMap<String, Object> executeSQL(int crud, String sql, String target, boolean validate) { // int crud: crud 중 선택 , String sql : sql쿼리문 , String target : 쿼리의 목표가 되는 테이블 이름 
		Statement stmt = getStatement();
		HashMap<String, Object> result = new HashMap<String, Object>(); // 파이썬의 딕셔너리처럼 쓰고자 하기 위해 해시맵을 씀. 물론, 값은 Object니까 바인딩된 값에 맞게 강제 타입변환해야 한다.
		int resRow = 0; // 쿼리의 결과로 나오는 행이나 영향을 받은 행의 갯수
		// v는 읽기 쿼리의 결과를 담는 벡터, v2나 v3는 데이터 유효성 검증을 위해 쓰일 때를 대비한 예비 벡터들.
		Vector v = null; Vector v2 = null; Vector v3 = null; Vector v4 = null;
		if(validate == true) { v2 = new Vector<String>(); v3 = new Vector<String>(); }
		if(crud == 2) {
			if(this.returnVector == true || ((target != null) && target.equals("corrects")) ||  ((target != null) && target.equals("category"))) v4 = new Vector();
			if(target != null) { // target에 들어온 타겟 테이블에 따라 벡터에 들어갈 데이터형이 바뀜.
				if(target.equals("members")) v = new Vector<Members>();
				else if(getTablesMap().get("ones").containsKey(target)) v = new Vector<Question>(); // 타겟 테이블이 문제 테이블일 경우
			}
		}
		try {
			if(crud == 2) { // ** read일 때
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
						} else if(getTablesMap().get("ones").containsKey(target)) { // 타겟 테이블이 문제 테이블일 경우
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
				} else result.put("resultData", v); // read 결과로 나온 행들을 모두 담은 벡터를 해시맵에 resultData 라는 키로 바인딩함. 
				if(validate == true) { // 위의 validate가 true인 경우, v2와 v3에 유효성 검사를 위한 행들만 따로 데이터를 담아 각각 validate2, validate3라는 키로 바인딩함. 
					result.put("validate1", v2); 
					result.put("validate2", v3);
				}
			} else resRow = stmt.executeUpdate(sql); // ** read가 아닌 다른 목적일 때
			
			result.put("numOfRows", resRow); // 쿼리의 결과로 나오는 행이나 영향을 받은 행의 갯수를 numOfRows라는 키로 바인딩. 그냥 결과 행 수만 원할 경우에는 이것만 꺼내 쓰면 됨.
		} catch(SQLException e) {
			e.printStackTrace();
			System.out.println("Exception while executing query : QuizDAO/executeSQL()");
		}
		return result; // 이 메서드를 써서 결과 해시맵을 받는다면, 결과 행="numOfRows" / read결과로 나온 데이터들="resultData" / 유효성을 검증하기 위한 컬렉션들="validate1", "validate2" 라는 키값으로 원하는 값을 가져오면 된다. 
	}
	
	public HashMap<String, Object> select(String sql, String target, boolean validate) { return executeSQL(2, sql, target, validate); }
	public HashMap<String, Object> insert(String sql) { return executeSQL(1, sql, null, false); }
	public HashMap<String, Object> update(String sql, String target) { return executeSQL(3, sql, target, false); }
	public HashMap<String, Object> delete(String sql, String target) { return executeSQL(4, sql, target, false); }
	public HashMap<String, HashMap<String, String>> getTablesMap() { // 문제들을 담은 테이블 이름들과 그 테이블 이름에 매칭되는 한글이름들을 가져온다. 
		HashMap<String, HashMap<String, String>> qmaps = new HashMap<String, HashMap<String, String>>(); // 밑의 1과 2를 다 담는 해시맵. 그래서 키는 문자열, 값은 밑의 해시맵들의 데이터타입인 HashMap<String, String>입니다.
			// 한마디로, 파이썬으로 치면 딕셔너리 안에 또 딕셔너리를 넣은 것이라고 보면 됩니다.
		HashMap<String, String> allTable = new HashMap<String, String>(); // 1. 이건 모든 테이블 목록들을 다 담는 해시맵(파이썬의 딕셔너리)
		HashMap<String, String> ones = new HashMap<String, String>(); // 2. 이건 category 테이블의 isSubject의 값이 1인 테이블들, 즉, 과목 테이블들만 담는 해시맵!
		Statement stmt = getStatement();
		try {
			ResultSet rs = stmt.executeQuery("select * from category");
			while(rs.next()) {
				allTable.put(rs.getString(1), rs.getString(2)); // 모든 테이블 목록들을 다 담습니다.
				if(rs.getInt(3) == 1) ones.put(rs.getString(1), rs.getString(2)); // 과목 테이블들만 골라서 담습니다.
			}
			rs.close();
		} catch(SQLException e) {
			e.printStackTrace();
			System.out.println("Exception while executing query : QuizDAO/getTablesMap()");
		}
		qmaps.put("allTable", allTable); // 모든 테이블 목록을 allTable이라는 키값으로 저장합니다. 이후, 이 메서드를 실행하여 리턴값인 qmaps를 받으면, qmaps.get("allTable")로 가져올 수 있습니다.
		qmaps.put("ones", ones);  // 과목 테이블 목록을 ones이라는 키값으로 저장합니다. 이후, 이 메서드를 실행하여 리턴값인 qmaps를 받으면, qmaps.get("ones")로 가져올 수 있습니다.
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
