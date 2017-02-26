import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.h2.table.Table;



public class FirstTab extends JFrame{

	Connection conn = null;
	PreparedStatement state = null;
	ResultSet result = null;
	
	
	JTabbedPane myPane = new JTabbedPane();
	
	JTable firstTable = new JTable();
	JScrollPane firstScroll = new  JScrollPane(firstTable);
		
	JPanel first = new JPanel();
	//first page
		JPanel firstTopPanel = new JPanel();
		JLabel deleteLabel = new JLabel("Номер за манипулация: ");
		JLabel carLabel = new JLabel("Марка");
		JLabel modelLabel = new JLabel("Модел");
		JLabel countryLabel = new JLabel("Държава");
		JLabel yearLabel = new JLabel("Година");
		JLabel colorLabel = new JLabel("Цвят");
		JLabel priceLabel = new JLabel("Цена");
		
		JTextField deleteF = new JTextField(15);
		JTextField carField = new JTextField(15);
		JTextField modelField = new JTextField(15);
		JComboBox<String> countryBox = new JComboBox<String>();
		JTextField yearField = new JTextField(20);
		JComboBox<String> colorBox = new JComboBox<String>();
		JTextField priceField = new JTextField(15);
	//end of TopPanel	
		
	JPanel firstMidPanel = new JPanel();
		JButton registerButton = new JButton("Регистрация");
		JButton updateButton = new JButton("Промяна");
		JButton deleteButton = new JButton("Изтриване");
		
	//end of MidPanel
	JPanel firstBotPanel = new JPanel();
		
	
	
	public FirstTab(){
		this.setSize(500, 500);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		
		
		
		//TOP PANEL
		firstTopPanel.setLayout(new GridLayout(7, 2));
		firstTopPanel.add(carLabel);
		firstTopPanel.add(carField);
		firstTopPanel.add(modelLabel);
		firstTopPanel.add(modelField);
		firstTopPanel.add(yearLabel);
		firstTopPanel.add(yearField);		
		firstTopPanel.add(countryLabel);
		fillCountries(countryBox);
		firstTopPanel.add(countryBox);
		firstTopPanel.add(colorLabel);
		fillColors(colorBox);
		firstTopPanel.add(colorBox);
		firstTopPanel.add(priceLabel);
		firstTopPanel.add(priceField);	
		firstTopPanel.add(deleteLabel);
		firstTopPanel.add(deleteF);
		first.add(firstTopPanel);
		
		//MID PANEL
		firstMidPanel.setLayout(new GridLayout(1, 3));
		registerButton.addActionListener(new RegisterAction());
		firstMidPanel.add(registerButton);
		updateButton.addActionListener(new UpdateAction());
		firstMidPanel.add(updateButton);
		deleteButton.addActionListener(new DeleteAction());
		firstMidPanel.add(deleteButton);
		first.add(firstMidPanel);		
		//BOT PANEL
		firstScroll.setSize(450, 150);
		firstBotPanel.add(firstScroll);

		first.add(firstBotPanel);
		
		
		myPane.add("Коли",first);

		this.add(myPane);
		refreshTable();
	}//end of constructor
	
	
	class UpdateAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			conn = DBConnection.getConnection();
			String sql;
			StringBuilder builder = new StringBuilder();
			int counter = 1;
			try{
			builder.append("update cars set ");
			if(!carField.getText().isEmpty()){
				builder.append("brand = ?,");
			}
			if(!modelField.getText().isEmpty()){
				builder.append("model = ?, ");
			}
			if(!yearField.getText().isEmpty()){
				builder.append("year = ?, ");
			}
			if(!priceField.getText().isEmpty()){
				builder.append("price = ?, ");
			}
			builder.append("country_id = ?, color_id = ? ");
			sql = builder.toString();
			sql += "where car_id = ?";
			state = conn.prepareStatement(sql);
			if(!carField.getText().isEmpty()){
				state.setString(counter, carField.getText());
				counter++;
			}
			if(!modelField.getText().isEmpty()){
				state.setString(counter, modelField.getText());
				counter++;
			}
			if(!yearField.getText().isEmpty()){
				state.setInt(counter, Integer.parseInt(yearField.getText()));
				counter++;
			}
			if(!priceField.getText().isEmpty()){
				state.setDouble(counter, Double.parseDouble(priceField.getText()));
				counter++;
			}
			state.setString(counter, (String)countryBox.getSelectedItem());
			counter++;
			state.setString(counter, (String)colorBox.getSelectedItem());
			counter++;
			state.setInt(counter, Integer.parseInt(deleteF.getText()));
			state.execute();
			
			clear();
			deleteF.setText("");
			refreshTable();
			}catch(SQLException sq){
				sq.printStackTrace();
			}
		}//end of actionPerformed
		
	}//end of UpdateAction
		
	
	class RegisterAction implements ActionListener{

			@Override
		public void actionPerformed(ActionEvent e) {
			conn = DBConnection.getConnection();
			String sql = "insert into cars values(null,?,?,?,?,?,?);";
			try{
				state = conn.prepareStatement(sql);
				state.setString(1, carField.getText());
				state.setString(2, modelField.getText());
				state.setInt(3, Integer.parseInt(yearField.getText()));
				state.setString(4, (String) countryBox.getSelectedItem());
				state.setString(5, (String)colorBox.getSelectedItem());				
				state.setDouble(6, Double.parseDouble(priceField.getText()));
				state.execute();
				clear();
				refreshTable();
			}catch(SQLException sq){
				JOptionPane.showMessageDialog(null, "Данните не са въведени.");
			}			
		}//end of actionPerformed
		

	}//end of ActionListener

	
	class DeleteAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			conn = DBConnection.getConnection();
			String sql;
			try{
				if(deleteF.getText().equals("")){
				sql = "delete from cars where brand = ? and model = ? and year = ? and country = ? and color = ?";
				state = conn.prepareStatement(sql);
				state.setString(1, carField.getText());
				state.setString(2, modelField.getText());
				state.setInt(3, Integer.parseInt(yearField.getText()));
				state.setString(4, (String)countryBox.getSelectedItem());
				state.setString(5, (String)colorBox.getSelectedItem());
				state.execute();
				clear();
				refreshTable();
				}else{
						sql = "delete from cars where car_id = ?";
						state = conn.prepareStatement(sql);
						state.setInt(1, Integer.parseInt(deleteF.getText()));
						state.execute();
						deleteF.setText("");
						refreshTable();					
				}
			}catch(SQLException ex){
				JOptionPane.showMessageDialog(null, "Полето не може да бъде изтрито.");
			}
		}//end of actionPerformed
		
	}//end of DeleteAction



	public ResultSet getAll() {
		conn = DBConnection.getConnection();
		try{
			state = conn.prepareStatement("select * from cars");
			result = state.executeQuery();
			return result;			
		}catch(SQLException sql){
			sql.printStackTrace();
		}finally{
			return result;
		}		
	}//end of getAll


	public void clear() {
		carField.setText("");
		modelField.setText("");
		yearField.setText("");
		priceField.setText("");
		
	}//end of clear

	public void refreshTable() {
		try{
			firstTable.setModel(new MyModel(getAll()));
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}//end of refreshTable
		
	public void fillCountries(JComboBox<String> combo){
		conn = DBConnection.getConnection();
		String sql = "select * from countries";
		combo.removeAllItems();
		try{
			state = conn.prepareStatement(sql);
			result = state.executeQuery();
			
			while(result.next()){
				String name = result.getString("country_id");
				combo.addItem(name);				
			}
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		
	}//end of fillCountries
	
	public void fillColors(JComboBox<String> combo){
		conn = DBConnection.getConnection();
		String sql = "select * from colors";
		combo.removeAllItems();
		try{
			state = conn.prepareStatement(sql);
			result = state.executeQuery();
			while(result.next()){
				String name = result.getString("color_id");
				combo.addItem(name);
			}
		}catch(SQLException sq){
			sq.printStackTrace();
		}
		
	}//end of fillColors
	
}//end of FirstTab


