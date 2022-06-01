package sec01;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

public class TableDataControl extends JFrame {
	private QuizDAO dao = new QuizDAO();
	private JPanel background;
	private References ref = new References();
	private Vector<JLabel> lbs = new Vector<JLabel>();
	private Vector<JTextField> ips = new Vector<JTextField>();
	private HashMap<String, String> changed = new HashMap<String, String>();
		// changed : 속성이 변경되는 것을 저장해 둘 필요가 있을 때 사용.
	private LoginInfo agent;
	private JFrame dialog;
	
	private int iu;
	private Vector datas;
	private Vector<String> header;
	private String tblName;
	private JFrame curPage;
	
	// iu : 아이유 아님! 1.insert 2.update
	public TableDataControl(LoginInfo agent, int iu, Vector datas, Vector<String> header, String tblName, JFrame curPage) {
		this.agent = agent; this.iu = iu; this.datas = datas; this.header = header; this.tblName = tblName; this.curPage = curPage; this.dialog = this;
		setTitle("데이터 " + (iu==1?"추가":"수정"));
		setLayout(null);
		background = ref.setBackgroundPanel("./images/pinnedM.png", 500, 220 + 50*(header.size()), null);
		background.setBounds(0, 0, 500, 220 + 50*(header.size()));
		createLists();
		add(getButton());
		add(background);
		setSize(500, 220 + 50*(header.size()));
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void createLists() {
		JLabel title = new JLabel(String.format("%s %s", tblName, (iu==1?"추가":"수정")));
		title.setFont(new Font("함초롬돋움", Font.BOLD, 22));
		title.setBounds(180, 100, 200, 30);
		background.add(title);
		for(int i = 0; i < header.size(); i++) {
			JLabel temp1 = new JLabel(header.get(i));
			temp1.setBounds(100, 190 + 40*(i-1), 200, 25);
			JTextField temp2 = new JTextField(20);
			if(iu == 2) temp2.setText(datas.get(i).toString());
			temp2.setBounds(170, 190 + 40*(i-1), 200, 25);
			background.add(temp1);
			background.add(temp2);
			lbs.add(temp1);
			ips.add(temp2);
			background.revalidate();
		}
	}
	
	private JButton getButton() {
		JButton btn = new JButton(iu==1?"추가":"수정");
		btn.setBounds(220, 190 + 40*(header.size()-1), 70, 30);
		btn.addActionListener(new BtnActionListener());
		return btn;
	}
	
	class BtnActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton target = (JButton)e.getSource();
			String cat = target.getText();
			String sql = makeQuery(iu);
			if(sql == null) return;
			//System.out.println(sql); // 
			if(tblName.equals("category")) { // category 테이블에 변경이 있을 때
				HashMap<String, Object> result = null;
				if(iu == 1) { // category 테이블을 생성할 때
					String newCat = ips.get(0).getText();
					String newAttr = newCat.substring(0, newCat.indexOf("TBL"));
					String createSQL = String.format("create table %s(que varchar(200) not null primary key, ans text not null, wr1 text not null, wr2 text not null, wr3 text not null)", newCat);
					String alterSQL = String.format("alter table corrects add column %s bigint default 0 not null", newAttr);
					//System.out.println(createSQL);
					//System.out.println(alterSQL);
					result = dao.insert(sql);
					dao.insert(createSQL);
					dao.update(alterSQL, tblName);
				} else { // category 테이블을 수정할 때
					String oldTBL = changed.get("oldTName");
					String newTBL = changed.get("newTName");
					result = dao.update(sql, tblName);
					if(((oldTBL != null) && !oldTBL.equals("")) && ((newTBL != null) && !newTBL.equals(""))) {
						String oldAttr = oldTBL.substring(0, oldTBL.indexOf("TBL"));
						String newAttr = newTBL.substring(0, newTBL.indexOf("TBL"));
						String createSQL = String.format("create table %s(que varchar(200) not null primary key, ans text not null, wr1 text not null, wr2 text not null, wr3 text not null)", newTBL);
						String insertSQL = String.format("insert into %s select * from %s", newTBL, oldTBL);
						String alterSQL = String.format("alter table corrects change column %s %s bigint default 0 not null", oldAttr, newAttr);
						String deleteSQL = String.format("drop table %s", oldTBL);
						//System.out.println(createSQL);
						//System.out.println(insertSQL);
						//System.out.println(alterSQL);
						//System.out.println(deleteSQL);
						dao.insert(createSQL);
						dao.insert(insertSQL);
						dao.update(alterSQL, tblName);
						dao.delete(deleteSQL, tblName);
					}	
					changed.clear(); // 다음 변경할 때를 위해 다 쓰면 해시맵을 비운다.
				}
				JOptionPane.showMessageDialog(null, (iu==1?"추가가":"수정이") + " 완료되었습니다.", "Success", JOptionPane.INFORMATION_MESSAGE);
				//System.out.println(""); // 
			} else { // category가 아닌 다른 테이블에 변동이 있을 때
				HashMap<String, Object> result = null;
				if(iu == 1) result = dao.insert(sql);
				else result = dao.update(sql, tblName);
				if(iu == 1 && tblName.equals("members")) { // 회원을 추가했을 때 맞춘 문제 게시판에도 회원을 자동으로 넣어준다.
					String insertSQL = String.format("insert into corrects (uid) values('%s')", ips.get(0).getText());
					dao.insert(insertSQL);
				} else if(iu == 2 && tblName.equals("members")) { 
					if(datas.get(0).equals(agent.getUser().getUid())) { // 관리자 자신의 정보를 수정했을 경우
						String sql2 = String.format("select * from members where uid='%s'",ips.get(0).getText());
						HashMap<String, Object> result2 = dao.select(sql2, "members", false);
						Vector<Members> v = (Vector<Members>)result2.get("resultData");
						agent.setUser(v.get(0));
					}
				}
				if((int)result.get("numOfRows") == 1) JOptionPane.showMessageDialog(null, (iu==1?"추가가":"수정이") + " 완료되었습니다.", "Success", JOptionPane.INFORMATION_MESSAGE);
				else JOptionPane.showMessageDialog(null, (iu==1?"추가":"수정") + "에 실패했습니다....", "Error!", JOptionPane.ERROR_MESSAGE);
			}
			agent.setPrev(curPage);
			curPage.setVisible(false);
			agent.setCur(new SettingsTemplate(agent));
			agent.setDialog(dialog);
			dialog.setVisible(false);
			agent.removeDialog();
		}
		
		/*
			 private int iu;  1.insert 2.update
			 private Vector datas; 이전 데이터들
			 private Vector<String> header; 테이블 칼럼명들
			 private String tblName; 추가나 수정을 할 테이블명
			 
			 private Vector<JLabel> lbs = new Vector<JLabel>();
			private Vector<JTextField> ips = new Vector<JTextField>(); 입력창들
		 */
		private String makeQuery(int iu) {
			String sql = "";
			boolean firstIn = true;
			int change = 0;
			sql += iu == 1? "insert into " + tblName + " values(" : "update " + tblName + " set ";
			HashMap<String, String> ones = dao.getTablesMap().get("ones");
			HashMap<String, Object> validate = null;
			if(tblName.equals("members")) validate = dao.select("select * from members", "members", true);
			for(int i = 0; i < ips.size(); i++) {
				String thisStr = ips.get(i).getText();
				if(thisStr == null || thisStr.equals("")) {
					JOptionPane.showMessageDialog(null, "모든 입력사항은 필수입니다.", "Error!", JOptionPane.ERROR_MESSAGE);
					return null;
				} else if (iu == 2 && (thisStr != null && thisStr.equals(datas.get(i).toString()))) {
					if(i == ips.size()-1 && change == 0) {
						JOptionPane.showMessageDialog(null, "변경할 항목을 입력해주세요.", "Error!", JOptionPane.ERROR_MESSAGE);
						return null;
					}
					continue;
				}
				change++;
				int curAttr = -1;
				if(firstIn == false) sql += ", ";
				try {
					curAttr = Integer.parseInt(thisStr);
					if((tblName.equals("members") && i == 1) || (ones.containsKey(tblName) && i > 0)) throw new NumberFormatException();
					sql += iu == 1? String.valueOf(curAttr): header.get(i) + "=" + String.valueOf(curAttr);
				} catch(NumberFormatException e) {
					sql += iu == 1? "'" + thisStr + "'": header.get(i) + "='" + String.valueOf(thisStr) + "'";
				}
				if(tblName.equals("members")) {
					if(header.get(i).equals("uid")) {
						if(Validation.dataValidate(validate, thisStr) == false) {
							JOptionPane.showMessageDialog(null, "중복되는 아이디입니다. 다시 입력하세요.", "Error!", JOptionPane.ERROR_MESSAGE);
							return null;
						}
					} else if(header.get(i).equals("upw")) {
						if(Validation.pwValidate(thisStr) == false) {
							JOptionPane.showMessageDialog(null, "비밀번호는 8자 이상이어야 합니다. 다시 입력하세요.", "Error!", JOptionPane.ERROR_MESSAGE);
							return null;
						}
					} else if(header.get(i).equals("utel")) {
						if(Validation.telFormValidate(thisStr) == false) {
							JOptionPane.showMessageDialog(null, "번호의 형식에 맞지 않습니다. 다시 입력하세요.", "Error!", JOptionPane.ERROR_MESSAGE);
							return null;
						} else if(Validation.telValidate(validate, thisStr) == false) {
							JOptionPane.showMessageDialog(null, "중복되는 번호입니다. 다시 입력하세요.", "Error!", JOptionPane.ERROR_MESSAGE);
							return null;
						}
					}
				} else if(tblName.equals("category")) { // category(테이블목록) 테이블을 추가하거나 삭제할 때
					if(header.get(i).equals("tname")) { // 테이블 이름이 형식에 맞는지 확인, 안맞으면 킥
						HashMap<String, HashMap<String, String>> res = dao.getTablesMap();
						HashMap<String, String> allTable = res.get("allTable");
						Set<String> tables = allTable.keySet();
						ones = res.get("ones");
						if(iu == 2 && !ones.containsKey(datas.get(i))) { // 과목 테이블이 아닌 테이블은 tname 수정 불가
							JOptionPane.showMessageDialog(null, "과목이 아닌 테이블의 tname은 바꿀 수 없습니다. 다시 입력하세요.", "Error!", JOptionPane.ERROR_MESSAGE);
							return null;
						} else if(Validation.tblNameValidate(thisStr) == false) {
							JOptionPane.showMessageDialog(null, "테이블 이름의 형식에 맞지 않습니다.(영문소문자TBL)\n다시 입력하세요.", "Error!", JOptionPane.ERROR_MESSAGE);
							return null;
						} else if(tables.contains(thisStr)) { // 테이블 이름이 중복되면 킥
							JOptionPane.showMessageDialog(null, "중복되는 테이블 이름입니다. 다시 입력하세요.", "Error!", JOptionPane.ERROR_MESSAGE);
							return null;
						}
						if(iu == 2) { // 테이블 이름을 변경하는 것은 DCL을 동반하므로 변경사항을 changed 해시맵에 저장한다.
							changed.put("oldTName", datas.get(i).toString());
							changed.put("newTName", thisStr);
						}
					} else if (header.get(i).equals("isSubject")) { // 과목 테이블만 추가하거나 변경 할 수 있으며, 과목이 아닌 테이블을 과목 테이블로, 과목 테이블을 과목이 아닌 테이블로 바꿀 수 없다.
						int isSubject = iu == 1? -1 : Integer.parseInt(datas.get(i).toString());
						if(iu == 1 && curAttr == 0) { // 과목 테이블이 아닌 테이블을 추가하려 할 때 킥
							JOptionPane.showMessageDialog(null, "과목 테이블만 추가할 수 있습니다. isSubject 항목을 변경하세요.", "Error!", JOptionPane.ERROR_MESSAGE);
							return null;
						} else if(iu == 2 && isSubject==0 && curAttr == 1) { // 과목 테이블이 아닌 테이블을 과목 테이블로 수정하려 할 때 킥
							JOptionPane.showMessageDialog(null, "해당 테이블을 과목 테이블로 바꿀 수 없습니다. 다시 입력하세요.", "Error!", JOptionPane.ERROR_MESSAGE);
							return null;
						} else if(iu == 2 && isSubject==1 && curAttr == 0) { // 과목 테이블을 과목이 아닌 테이블로 수정하려 할 때 킥
							JOptionPane.showMessageDialog(null, "해당 테이블을 과목이 아닌 테이블로 바꿀 수 없습니다. 다시 입력하세요.", "Error!", JOptionPane.ERROR_MESSAGE);
							return null;
						} 
					}
				} else if(tblName.equals("corrects")) { // corrects 테이블의 수정은 오직 숫자 조작만 가능하다.
					if(header.get(i).equals("uid")) {
						JOptionPane.showMessageDialog(null, "해당 테이블의 id값은 바꿀 수 없습니다. 다시 입력하세요.", "Error!", JOptionPane.ERROR_MESSAGE);
						return null;
					}
				}
				firstIn = false;
			}
			sql += iu == 1? ")" : " where " + header.get(0) + "='" + datas.get(0) + "'";
			return sql;
		}
	}
	
}