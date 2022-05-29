package sec01;

import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.stream.*;

public class TableDataSheet {
	private QuizDAO dao = new QuizDAO();
	private HashMap<String, String> allTable = dao.getTablesMap().get("allTable");
	private HashMap<String, String> ones = dao.getTablesMap().get("ones");
	private Vector<String> arrangedTableNames = dao.getArrangedTableNames();
	private LoginInfo agent;
	private JFrame curPage;
	
	public TableDataSheet(JFrame curPage, LoginInfo agent) {
		this.agent = agent;
		this.curPage = curPage;
	}
	
	public Vector<String> getHeaders(String listLabel) {
		Vector<String> headers = new Vector<String>();
		String[] headarr = {};
		Collection <String> oneValues = ones.values();
		if(oneValues.contains(listLabel)) {
			headarr = new String[] {"que", "ans", "wr1", "wr2", "wr3"};
			headers = Arrays.asList(headarr).stream().collect(Collectors.toCollection(Vector::new));
		} else if(listLabel.equals("맞춘문제")) { 
			for(String tbl:arrangedTableNames) headers.add(tbl);
		} else {
			switch(listLabel.trim()) {
				case "회원" : headarr = new String[] {"uid", "upw", "uname", "ufindq", "ufinda", "uclass", "utel"}; break;
				case "테이블목록" : headarr = new String[] {"tname", "cname", "isSubject"}; break;
			}
			headers = Arrays.asList(headarr).stream().collect(Collectors.toCollection(Vector::new));
		}
		return headers;
	}
	
	public String getTBLName(String listLabel) {
		String result = "";
		Set<String> onesKey = ones.keySet();
		Collection <String> oneValues = ones.values();
		if(oneValues.contains(listLabel)) {
			for(String sub:onesKey) {
				if(ones.get(sub).equals(listLabel.trim())) { result = sub; break; }
			}
		} else if(listLabel.equals("맞춘문제")) result = "corrects";
		 else if(listLabel.equals("테이블목록")) result = "category";
		 else if(listLabel.equals("회원")) result = "members";
		return result;
	}
	
	public void addRow(Vector<String> header, String tblName) {
		if(tblName.equals("corrects")) { // corrects 테이블에는 추가 작업과 삭제 작업을 할 수 없다. 오로지 숫자 수정작업만 할 수 있다. 아이디 수정도 안된다!
			JOptionPane.showMessageDialog(null, "이 테이블에는 추가 작업을 할 수 없습니다..", "Error!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		new TableDataControl(agent, 1, null, header, tblName, curPage);
	}
	
	public void updateRow(Vector datas, Vector<String> header, String tblName) {
		new TableDataControl(agent, 2, datas, header, tblName, curPage);
	}
	
	public void deleteRow(Vector datas, Vector<String> header, String tblName) {
		if(tblName.equals("corrects")) {
			JOptionPane.showMessageDialog(null, "해당 테이블의 데이터들은 삭제할 수 없습니다.", "Error!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		String sql = String.format("delete from %s where %s='%s'", tblName, header.get(0), datas.get(0));
		HashMap<String, Object> result = null;
		if(tblName.equals("category")) {
			if(datas.get(0).equals("category") || datas.get(0).equals("corrects") || datas.get(0).equals("members")) {
				JOptionPane.showMessageDialog(null, "해당 테이블은 삭제할 수 없습니다.", "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			} else {
				int sel = JOptionPane.showConfirmDialog(null, datas.get(0) + "테이블을 정말 삭제하시겠습니까?\n삭제하면 다시 되돌릴 수 없습니다.", "Warning!", JOptionPane.WARNING_MESSAGE);
				//System.out.println(sel);
				if(sel == 2 || sel == -1) {
					JOptionPane.showMessageDialog(null, "취소되었습니다.", "Cancel", JOptionPane.INFORMATION_MESSAGE);
					return;
				} else { // category 테이블 삭제 진행
					//System.out.println(sql);
					String attrName = datas.get(0).toString().substring(0, datas.get(0).toString().indexOf("TBL"));
					String alterSQL = String.format("alter table corrects drop column %s", attrName);
					String dropSQL = String.format("drop table %s", datas.get(0));
					//System.out.println(alterSQL);
					//System.out.println(dropSQL);
					result = dao.delete(sql, tblName);
					dao.update(alterSQL, tblName);
					dao.delete(dropSQL, tblName);
					JOptionPane.showMessageDialog(null, "삭제가 완료되었습니다.", "Success!", JOptionPane.OK_OPTION);
				}
			}
		} else {
			result = dao.delete(sql, tblName);
			if((int)result.get("numOfRows") == 1) JOptionPane.showMessageDialog(null, "삭제가 완료되었습니다.", "Success!", JOptionPane.OK_OPTION);
			else JOptionPane.showMessageDialog(null, "삭제에 실패했습니다....", "Error!", JOptionPane.ERROR_MESSAGE);
		}
		agent.setPrev(curPage);
		curPage.setVisible(false);
		agent.setCur(new SettingsTemplate(agent));
	}
}
