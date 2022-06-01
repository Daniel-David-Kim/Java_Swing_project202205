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
		// changed : �Ӽ��� ����Ǵ� ���� ������ �� �ʿ䰡 ���� �� ���.
	private LoginInfo agent;
	private JFrame dialog;
	
	private int iu;
	private Vector datas;
	private Vector<String> header;
	private String tblName;
	private JFrame curPage;
	
	// iu : ������ �ƴ�! 1.insert 2.update
	public TableDataControl(LoginInfo agent, int iu, Vector datas, Vector<String> header, String tblName, JFrame curPage) {
		this.agent = agent; this.iu = iu; this.datas = datas; this.header = header; this.tblName = tblName; this.curPage = curPage; this.dialog = this;
		setTitle("������ " + (iu==1?"�߰�":"����"));
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
		JLabel title = new JLabel(String.format("%s %s", tblName, (iu==1?"�߰�":"����")));
		title.setFont(new Font("���ʷҵ���", Font.BOLD, 22));
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
		JButton btn = new JButton(iu==1?"�߰�":"����");
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
			if(tblName.equals("category")) { // category ���̺� ������ ���� ��
				HashMap<String, Object> result = null;
				if(iu == 1) { // category ���̺��� ������ ��
					String newCat = ips.get(0).getText();
					String newAttr = newCat.substring(0, newCat.indexOf("TBL"));
					String createSQL = String.format("create table %s(que varchar(200) not null primary key, ans text not null, wr1 text not null, wr2 text not null, wr3 text not null)", newCat);
					String alterSQL = String.format("alter table corrects add column %s bigint default 0 not null", newAttr);
					//System.out.println(createSQL);
					//System.out.println(alterSQL);
					result = dao.insert(sql);
					dao.insert(createSQL);
					dao.update(alterSQL, tblName);
				} else { // category ���̺��� ������ ��
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
					changed.clear(); // ���� ������ ���� ���� �� ���� �ؽø��� ����.
				}
				JOptionPane.showMessageDialog(null, (iu==1?"�߰���":"������") + " �Ϸ�Ǿ����ϴ�.", "Success", JOptionPane.INFORMATION_MESSAGE);
				//System.out.println(""); // 
			} else { // category�� �ƴ� �ٸ� ���̺� ������ ���� ��
				HashMap<String, Object> result = null;
				if(iu == 1) result = dao.insert(sql);
				else result = dao.update(sql, tblName);
				if(iu == 1 && tblName.equals("members")) { // ȸ���� �߰����� �� ���� ���� �Խ��ǿ��� ȸ���� �ڵ����� �־��ش�.
					String insertSQL = String.format("insert into corrects (uid) values('%s')", ips.get(0).getText());
					dao.insert(insertSQL);
				} else if(iu == 2 && tblName.equals("members")) { 
					if(datas.get(0).equals(agent.getUser().getUid())) { // ������ �ڽ��� ������ �������� ���
						String sql2 = String.format("select * from members where uid='%s'",ips.get(0).getText());
						HashMap<String, Object> result2 = dao.select(sql2, "members", false);
						Vector<Members> v = (Vector<Members>)result2.get("resultData");
						agent.setUser(v.get(0));
					}
				}
				if((int)result.get("numOfRows") == 1) JOptionPane.showMessageDialog(null, (iu==1?"�߰���":"������") + " �Ϸ�Ǿ����ϴ�.", "Success", JOptionPane.INFORMATION_MESSAGE);
				else JOptionPane.showMessageDialog(null, (iu==1?"�߰�":"����") + "�� �����߽��ϴ�....", "Error!", JOptionPane.ERROR_MESSAGE);
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
			 private Vector datas; ���� �����͵�
			 private Vector<String> header; ���̺� Į�����
			 private String tblName; �߰��� ������ �� ���̺��
			 
			 private Vector<JLabel> lbs = new Vector<JLabel>();
			private Vector<JTextField> ips = new Vector<JTextField>(); �Է�â��
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
					JOptionPane.showMessageDialog(null, "��� �Է»����� �ʼ��Դϴ�.", "Error!", JOptionPane.ERROR_MESSAGE);
					return null;
				} else if (iu == 2 && (thisStr != null && thisStr.equals(datas.get(i).toString()))) {
					if(i == ips.size()-1 && change == 0) {
						JOptionPane.showMessageDialog(null, "������ �׸��� �Է����ּ���.", "Error!", JOptionPane.ERROR_MESSAGE);
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
							JOptionPane.showMessageDialog(null, "�ߺ��Ǵ� ���̵��Դϴ�. �ٽ� �Է��ϼ���.", "Error!", JOptionPane.ERROR_MESSAGE);
							return null;
						}
					} else if(header.get(i).equals("upw")) {
						if(Validation.pwValidate(thisStr) == false) {
							JOptionPane.showMessageDialog(null, "��й�ȣ�� 8�� �̻��̾�� �մϴ�. �ٽ� �Է��ϼ���.", "Error!", JOptionPane.ERROR_MESSAGE);
							return null;
						}
					} else if(header.get(i).equals("utel")) {
						if(Validation.telFormValidate(thisStr) == false) {
							JOptionPane.showMessageDialog(null, "��ȣ�� ���Ŀ� ���� �ʽ��ϴ�. �ٽ� �Է��ϼ���.", "Error!", JOptionPane.ERROR_MESSAGE);
							return null;
						} else if(Validation.telValidate(validate, thisStr) == false) {
							JOptionPane.showMessageDialog(null, "�ߺ��Ǵ� ��ȣ�Դϴ�. �ٽ� �Է��ϼ���.", "Error!", JOptionPane.ERROR_MESSAGE);
							return null;
						}
					}
				} else if(tblName.equals("category")) { // category(���̺���) ���̺��� �߰��ϰų� ������ ��
					if(header.get(i).equals("tname")) { // ���̺� �̸��� ���Ŀ� �´��� Ȯ��, �ȸ����� ű
						HashMap<String, HashMap<String, String>> res = dao.getTablesMap();
						HashMap<String, String> allTable = res.get("allTable");
						Set<String> tables = allTable.keySet();
						ones = res.get("ones");
						if(iu == 2 && !ones.containsKey(datas.get(i))) { // ���� ���̺��� �ƴ� ���̺��� tname ���� �Ұ�
							JOptionPane.showMessageDialog(null, "������ �ƴ� ���̺��� tname�� �ٲ� �� �����ϴ�. �ٽ� �Է��ϼ���.", "Error!", JOptionPane.ERROR_MESSAGE);
							return null;
						} else if(Validation.tblNameValidate(thisStr) == false) {
							JOptionPane.showMessageDialog(null, "���̺� �̸��� ���Ŀ� ���� �ʽ��ϴ�.(�����ҹ���TBL)\n�ٽ� �Է��ϼ���.", "Error!", JOptionPane.ERROR_MESSAGE);
							return null;
						} else if(tables.contains(thisStr)) { // ���̺� �̸��� �ߺ��Ǹ� ű
							JOptionPane.showMessageDialog(null, "�ߺ��Ǵ� ���̺� �̸��Դϴ�. �ٽ� �Է��ϼ���.", "Error!", JOptionPane.ERROR_MESSAGE);
							return null;
						}
						if(iu == 2) { // ���̺� �̸��� �����ϴ� ���� DCL�� �����ϹǷ� ��������� changed �ؽøʿ� �����Ѵ�.
							changed.put("oldTName", datas.get(i).toString());
							changed.put("newTName", thisStr);
						}
					} else if (header.get(i).equals("isSubject")) { // ���� ���̺� �߰��ϰų� ���� �� �� ������, ������ �ƴ� ���̺��� ���� ���̺��, ���� ���̺��� ������ �ƴ� ���̺�� �ٲ� �� ����.
						int isSubject = iu == 1? -1 : Integer.parseInt(datas.get(i).toString());
						if(iu == 1 && curAttr == 0) { // ���� ���̺��� �ƴ� ���̺��� �߰��Ϸ� �� �� ű
							JOptionPane.showMessageDialog(null, "���� ���̺� �߰��� �� �ֽ��ϴ�. isSubject �׸��� �����ϼ���.", "Error!", JOptionPane.ERROR_MESSAGE);
							return null;
						} else if(iu == 2 && isSubject==0 && curAttr == 1) { // ���� ���̺��� �ƴ� ���̺��� ���� ���̺�� �����Ϸ� �� �� ű
							JOptionPane.showMessageDialog(null, "�ش� ���̺��� ���� ���̺�� �ٲ� �� �����ϴ�. �ٽ� �Է��ϼ���.", "Error!", JOptionPane.ERROR_MESSAGE);
							return null;
						} else if(iu == 2 && isSubject==1 && curAttr == 0) { // ���� ���̺��� ������ �ƴ� ���̺�� �����Ϸ� �� �� ű
							JOptionPane.showMessageDialog(null, "�ش� ���̺��� ������ �ƴ� ���̺�� �ٲ� �� �����ϴ�. �ٽ� �Է��ϼ���.", "Error!", JOptionPane.ERROR_MESSAGE);
							return null;
						} 
					}
				} else if(tblName.equals("corrects")) { // corrects ���̺��� ������ ���� ���� ���۸� �����ϴ�.
					if(header.get(i).equals("uid")) {
						JOptionPane.showMessageDialog(null, "�ش� ���̺��� id���� �ٲ� �� �����ϴ�. �ٽ� �Է��ϼ���.", "Error!", JOptionPane.ERROR_MESSAGE);
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