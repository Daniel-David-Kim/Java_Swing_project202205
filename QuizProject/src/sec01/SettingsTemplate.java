package sec01;

import java.awt.*;
import java.util.*;
import java.util.stream.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class SettingsTemplate extends JFrame {
	private JPanel background;
	private References ref = new References();
	private QuizDAO dao = new QuizDAO();
	private LoginInfo agent;
	private TableDataSheet tbdata;
	private JList<String> tables;
	private HashMap<String, String> allTable = dao.getTablesMap().get("allTable");
	private JScrollPane tableScroll;
	private Vector<String> header;
	private String tblName;
	SettingsTemplate(LoginInfo agent) {
		this.agent = agent;
		this.agent.removePrev();
		tbdata = new TableDataSheet(this, agent);
		setTitle("SettingsTemplate");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		background = ref.setBackgroundPanel("./images/clipboard7.jpg", 720, 480, null);
		add(background);
		System.out.println(agent.getUser());
		
		background.add(getList());
		tables.setSelectedIndex(0);
		
		tableScroll = getTable(getLabel(tables.getSelectedValue()));
		background.add(tableScroll);
		background.add(getButton(0));
		background.add(getButton(1));
		background.add(getButton(2));
		setResizable(false);
		setSize(720, 480);
		setVisible(true);
	}
	
	private JScrollPane getList() {
		Collection <String> values = allTable.values();
		Vector<String> valuesVec = values.stream().map((s) -> s += " ����").collect(Collectors.toCollection(Vector::new));
		tables = new JList<String>(valuesVec);
		tables.setFont(new Font("���ʷҵ���", Font.BOLD, 12));
		tables.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				String selected = tables.getSelectedValue();
				background.remove(tableScroll);
				tableScroll = getTable(getLabel(selected));
				background.add(tableScroll);
				background.revalidate();
				background.repaint();
			}
		});
		JScrollPane span = new JScrollPane(tables);
		span.setBounds(90, 115, 150, 311);
		return span;
	}
	
	private Vector datas = new Vector();
	private boolean rowSelected = false;
	public boolean isRowSelected() {return rowSelected;}
	public void setRowSelected(boolean rowSelected) {this.rowSelected = rowSelected;}
	private JScrollPane getTable(String label) {
		header = tbdata.getHeaders(label);
		tblName = tbdata.getTBLName(label);
		
		dao.setReturnVector(true);
		HashMap<String, Object> result = dao.select(String.format("select * from %s", tblName), tblName, false);
		Vector contain = (Vector)result.get("resultVector");
		DefaultTableModel dtm = new DefaultTableModel(contain, header);
		JTable tb = new JTable(dtm);
		tb.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				int selRow = tb.getSelectedRow();
				datas.clear();
				for(int i = 0; i < header.size(); i++) {
					datas.add(dtm.getValueAt(selRow, i));
					//System.out.print(datas.get(i) + " | ");
				}
				//System.out.println();
				setRowSelected(true);
			}
		});
		
		JScrollPane tbscroll = new JScrollPane(tb);
		tbscroll.setBounds(260, 115, 380, 283);
		return tbscroll;
	}
	
	private JButton getButton(int cud) {
		String[] labels = {"�߰�", "����", "����"};
		String[] paths = {"./images/add.png", "./images/update.png", "./images/delet.png"};
		JButton btn = new JButton(labels[cud]) {
			public void paintComponent(Graphics g) { g.drawImage((new ImageIcon(paths[cud])).getImage(), 0, 0, 36, 23, null); }
		};
		btn.setBorder(null);
		btn.setFont(new Font("���ʷҵ���", Font.BOLD, 12));
		btn.setBounds(510 + (cud*47), 400, 36, 23);
		btn.addActionListener((e) -> {
			JButton target = (JButton)e.getSource();
			if(rowSelected == true) {
				switch(target.getText()) {
					case "�߰�" : tbdata.addRow(header, tblName); break;
					case "����" : tbdata.updateRow(datas, header, tblName); break;
					case "����" : tbdata.deleteRow(datas, header, tblName); break;
				}
			} else if(target.getText().equals("�߰�")) tbdata.addRow(header, tblName);
			 else JOptionPane.showMessageDialog(null, "���� �������ּ���.", "Error!", JOptionPane.ERROR_MESSAGE);	
		});
		return btn;
	}
	
	private String getLabel(String label) {
		String[] divs = label.split(" ");
		return divs[0];
	}
	
	public static void main(String[] args) { // ����� �ӽ� �α��� ��ü�Դϴ�.
		LoginInfo agent = new LoginInfo(new Members("admin1", "01234", "������", "���� �����ϱ�?", "������", 0, "010-1000-1111"));
		new SettingsTemplate(agent);
	}
}
