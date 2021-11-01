package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import javax.swing.*;

public class View extends JFrame {
	private JButton search;
	private String[] lname;
	private JComboBox<String> jComboBox;
	private JLabel jL1, jL2, jL3;
	

	public View() {
		super("各縣市天氣預報");
		
		

		
		ObCenter();
		layoutView();
		setEvent();
		

		setSize(640, 480);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private void ObCenter() {
		//下拉選單
		lname = getLocationName();
		jComboBox = new JComboBox<>(lname);
		jComboBox.setBounds(80, 50, 140, 20);
		
		//Button
		search = new JButton("查詢");
		search.setBounds(100, 100, 90, 20);
		
		//JLable
		jL1 = new JLabel();
	}

	private void layoutView() {
		
		setLayout(new BorderLayout());
		JPanel top = new JPanel(new FlowLayout());
		
		add(top, BorderLayout.NORTH);
		top.add(jComboBox);
		top.add(search);
		
		add(jL1, BorderLayout.CENTER);

	}
	
	private void setEvent() {
		search.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedFruit = jComboBox.getSelectedItem().toString();
				jL1.setText(selectedFruit);
			}});
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
			int i=0;
			System.out.println(lname.getFetchSize());
			while(lname.next()) {
				
				name[i] = lname.getString(1);
				i++;
			}
			
			
			
			
			return name;
		} catch (Exception e) {
			return null;
		}

	}
}
