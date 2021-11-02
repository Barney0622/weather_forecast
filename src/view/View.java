package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import javax.swing.*;

public class View extends JFrame {
	private String[][] records1 = new String[5][5];  
	private String[][] records2 = new String[5][5];  
	private String[][] records3 = new String[5][5]; 
	private JButton search;
	private String[] lname;
	private JComboBox<String> jComboBox;
	private JLabel t1, t2, t3;
	private JLabel wx1, wx2, wx3;
	private JLabel pop1, pop2, pop3;
	private JLabel maxT1, maxT2, maxT3;
	private JLabel minT1, minT2, minT3;
	private JLabel ci1, ci2, ci3;
	private JLabel jmid;
	
	
	int w = 640;
	int h = 300;

	public View() {

		super("天氣預報");

		ObCenter();
		layoutView();
		setEvent();
		

		setSize(w, h);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void ObCenter() {
		// 下拉選單
		lname = getLocationName();
		jComboBox = new JComboBox<>(lname);
		//jComboBox.setBounds(80, 50, 140, 20);

		// Button
		search = new JButton("查詢");
		//search.setBounds(100, 100, 90, 20);

		// JLable
		jmid = new JLabel();
		t1 = new JLabel();
		t2 = new JLabel();
		t3 = new JLabel();
		wx1 = new JLabel();
		wx2 = new JLabel();
		wx3 = new JLabel();
		pop1 = new JLabel();
		pop2 = new JLabel();
		pop3 = new JLabel();
		maxT1 = new JLabel();
		maxT2 = new JLabel();
		maxT3 = new JLabel();
		minT1 = new JLabel();
		minT2 = new JLabel();
		minT3 = new JLabel();
		ci1 = new JLabel();
		ci2 = new JLabel();
		ci3 = new JLabel();

	}
	
	

	private void layoutView() {

		setLayout(new BorderLayout());
		JPanel top = new JPanel(new FlowLayout());
		top.setPreferredSize(new Dimension(w, 50));
		JPanel mid = new JPanel(new FlowLayout());
		mid.setPreferredSize(new Dimension(w, 80));
		JPanel bot = new JPanel(new BorderLayout());
		bot.setPreferredSize(new Dimension(w, 150));
		
		JPanel bot1 = new JPanel();
		bot1.setLayout(new BoxLayout(bot1, BoxLayout.PAGE_AXIS));
		bot1.setPreferredSize(new Dimension(200, 150));
		
		JPanel bot2 = new JPanel();
		bot2.setLayout(new BoxLayout(bot2, BoxLayout.PAGE_AXIS));
		bot2.setPreferredSize(new Dimension(200, 150));
		
		JPanel bot3 = new JPanel();
		bot3.setLayout(new BoxLayout(bot3, BoxLayout.PAGE_AXIS));
		bot3.setPreferredSize(new Dimension(200, 150));

		
		

		add(top, BorderLayout.NORTH);
		top.add(jComboBox);
		top.add(search);
		
		add(mid, BorderLayout.CENTER);
		mid.add(jmid);
		
		add(bot, BorderLayout.SOUTH);
		
		
		bot.add(bot1, BorderLayout.WEST);
		bot1.add(t1);
		bot1.add(wx1);
		bot1.add(minT1);
		bot1.add(maxT1);
		bot1.add(ci1);
		bot1.add(pop1);
		
		
		
		bot.add(bot2, BorderLayout.CENTER);
		bot2.add(t2);
		bot2.add(wx2);
		bot2.add(minT2);
		bot2.add(maxT2);
		bot2.add(ci2);
		bot2.add(pop2);
		
		bot.add(bot3, BorderLayout.EAST);
		bot3.add(t3);
		bot3.add(wx3);
		bot3.add(minT3);
		bot3.add(maxT3);
		bot3.add(ci3);
		bot3.add(pop3);
		

	}

	private void setEvent() {
		search.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedFruit = jComboBox.getSelectedItem().toString();
				jmid.setText("查詢縣市: "+selectedFruit);
				
				getT1Record(selectedFruit);
				getT2Record(selectedFruit);
				getT3Record(selectedFruit);
				
				t1.setText("時間: "+records1[0][2]+"\n");
				
				wx1.setText("天氣狀況: "+records1[0][4]+"\n");
				minT1.setText("最低溫度: "+records1[2][4]);
				maxT1.setText("最高溫度: "+records1[4][4]);
				pop1.setText("降雨機率: "+records1[3][4]);
				ci1.setText("舒適程度: "+records1[1][4]);
				
				t2.setText("時間: "+records2[0][2]+"\n");
				wx2.setText("天氣狀況: "+records2[0][4]+"\n");
				minT2.setText("最低溫度: "+records2[2][4]);
				maxT2.setText("最高溫度: "+records2[4][4]);
				pop2.setText("降雨機率: "+records2[3][4]);
				ci2.setText("舒適程度: "+records2[1][4]);
				
				t3.setText("時間: "+records3[0][2]+"\n");
				wx3.setText("天氣狀況: "+records3[0][4]+"\n");
				minT3.setText("最低溫度: "+records3[2][4]);
				maxT3.setText("最高溫度: "+records3[4][4]);
				pop3.setText("降雨機率: "+records3[3][4]);
				ci3.setText("舒適程度: "+records3[1][4]);
			}
		});
	}

	private void getT1Record(String locateionName) {

		
		Properties prop = new Properties();
		prop.put("user", "root");
		prop.put("password", "root");

		String sql = "SELECT location.lname, element.ename, time.st, time.et, record.parameter\r"
				+ "FROM ((record join location \r"
				+ "on record.lid = location.lid)\r"
				+ "join time\r"
				+ "on record.tid = time.tid)\r"
				+ "join element\r"
				+ "on record.eid = element.eid\r"
				+ "WHERE location.lname = ? and time.tid = 1\r"
				+ "ORDER BY location.lid, time.tid, element.eid"
				+ ";";

		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:8889/weather", prop)) {

			PreparedStatement pslname = connection.prepareStatement(sql);
			
			pslname.setString(1, locateionName);
			ResultSet rs = pslname.executeQuery();
			for(int i=0; i<5; i++) {
				if(rs.next()) {
					
					String lname = rs.getString("lname");
					String ename = rs.getString("ename");
					String st = rs.getString("st");
					String et = rs.getString("et");
					String parameter = rs.getString("parameter");
					
					records1[i][0] = lname;
					records1[i][1] = ename;
					records1[i][2] = st;
					records1[i][3] = et;
					records1[i][4] = parameter;
				}
				
			}
			

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
	private void getT2Record(String locateionName) {
		Properties prop = new Properties();
		prop.put("user", "root");
		prop.put("password", "root");

		String sql = "SELECT location.lname, element.ename, time.st, time.et, record.parameter\r"
				+ "FROM ((record join location \r"
				+ "on record.lid = location.lid)\r"
				+ "join time\r"
				+ "on record.tid = time.tid)\r"
				+ "join element\r"
				+ "on record.eid = element.eid\r"
				+ "WHERE location.lname = ? and time.tid = 2\r"
				+ "ORDER BY location.lid, time.tid, element.eid"
				+ ";";

		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:8889/weather", prop)) {

			PreparedStatement pslname = connection.prepareStatement(sql);
			
			pslname.setString(1, locateionName);
			ResultSet rs = pslname.executeQuery();
			for(int i=0; i<5; i++) {
				if(rs.next()) {
					String lname = rs.getString("lname");
					String ename = rs.getString("ename");
					String st = rs.getString("st");
					String et = rs.getString("et");
					String parameter = rs.getString("parameter");
					
					records2[i][0] = lname;
					records2[i][1] = ename;
					records2[i][2] = st;
					records2[i][3] = et;
					records2[i][4] = parameter;
					
				}
			}
			

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
	private void getT3Record(String locateionName) {
		Properties prop = new Properties();
		prop.put("user", "root");
		prop.put("password", "root");

		String sql = "SELECT location.lname, element.ename, time.st, time.et, record.parameter\r"
				+ "FROM ((record join location \r"
				+ "on record.lid = location.lid)\r"
				+ "join time\r"
				+ "on record.tid = time.tid)\r"
				+ "join element\r"
				+ "on record.eid = element.eid\r"
				+ "WHERE location.lname = ? and time.tid = 3\r"
				+ "ORDER BY location.lid, time.tid, element.eid"
				+ ";";

		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:8889/weather", prop)) {

			PreparedStatement pslname = connection.prepareStatement(sql);
			
			pslname.setString(1, locateionName);
			ResultSet rs = pslname.executeQuery();
			for(int i=0; i<5; i++) {
				if(rs.next()) {
					String lname = rs.getString("lname");
					String ename = rs.getString("ename");
					String st = rs.getString("st");
					String et = rs.getString("et");
					String parameter = rs.getString("parameter");
					
					records3[i][0] = lname;
					records3[i][1] = ename;
					records3[i][2] = st;
					records3[i][3] = et;
					records3[i][4] = parameter;
				}
			}
			

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	private String[] getLocationName() {

		Properties prop = new Properties();
		prop.put("user", "root");
		prop.put("password", "root");

		String sql = "SELECT lname FROM location";

		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:8889/weather", prop)) {

			PreparedStatement pslocation = connection.prepareStatement(sql);

			ResultSet lname = pslocation.executeQuery();
			String[] name = new String[22];
			int i = 0;
			
			while (lname.next()) {

				name[i] = lname.getString(1);
				i++;
			}

			return name;
		} catch (Exception e) {
			return null;
		}

	}
}
