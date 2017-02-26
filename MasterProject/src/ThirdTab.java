import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;


public class ThirdTab extends SecondTab {

	Connection thirdConn = null;
	ResultSet thirdResult = null;
	PreparedStatement thirdState = null;
	
	JPanel third = new JPanel();
	JTable thirdTable = new JTable();
	JScrollPane thirdScroll = new JScrollPane(thirdTable);
	
	//Top Panel
	JPanel thirdTopPanel = new JPanel();
	JLabel colorIDLabel = new JLabel("Цвят(съкратено): ");
	JTextField colorF = new JTextField(15);
	JLabel colorNameLabel = new JLabel("Цвят: ");
	JTextField colorField = new JTextField(15);
	
	//Middle Panel
	JPanel thirdMidPanel = new JPanel();
	JButton colorRegisterButton = new JButton("Въведи цвят");
	JButton colorUpdateButton = new JButton("Промени цвят");
	JButton colorDeleteButton = new JButton("Премахни цвят");
	
	//Bottom Panel 
	JPanel thirdBotPanel = new JPanel();
	
	public ThirdTab(){
		super();		
		
		//Top
		thirdTopPanel.setLayout(new GridLayout(2, 2));
		thirdTopPanel.add(colorIDLabel);
		thirdTopPanel.add(colorF);
		thirdTopPanel.add(colorNameLabel);
		thirdTopPanel.add(colorField);
		third.add(thirdTopPanel);
		
		//Mid
		thirdMidPanel.setLayout(new GridLayout(1, 3));
		colorRegisterButton.addActionListener(new ColorRegister());
		thirdMidPanel.add(colorRegisterButton);
		colorUpdateButton.addActionListener(new ColorUpdate());
		thirdMidPanel.add(colorUpdateButton);
		colorDeleteButton.addActionListener(new ColorDelete());
		thirdMidPanel.add(colorDeleteButton);
		third.add(thirdMidPanel);
		
		//Bottom
		thirdScroll.setSize(400, 150);
		thirdBotPanel.add(thirdScroll);		
		third.add(thirdBotPanel);
		refreshThirdTable();
		
		myPane.add("Цветове", third);
	}//end of Constructor
	
	public void thirdClear(){
		colorF.setText("");
		colorField.setText("");
	}//end of clear
	
	public ResultSet getThirdAll(){
		thirdConn = DBConnection.getConnection();
		
		try{
			thirdState = thirdConn.prepareStatement("select * from colors");
			thirdResult = thirdState.executeQuery();
			return thirdResult;
		}catch(SQLException sql){
			sql.printStackTrace();
		}finally{
			return thirdResult;
		}
		
		
	}//end of getThirdAll
	
	public void refreshThirdTable(){
		try {
			thirdTable.setModel(new MyModel(getThirdAll()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}//end of refreshTrirdTable
	
	class ColorRegister implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			thirdConn = DBConnection.getConnection();
			String sql = "insert into colors values(?,?);";
			try{
				thirdState = thirdConn.prepareStatement(sql);
				thirdState.setString(1, colorF.getText());
				thirdState.setString(2, colorField.getText());
				thirdState.execute();
				thirdClear();
				refreshThirdTable();
				fillColors(colorBox);
			}catch(SQLException sq){
				JOptionPane.showMessageDialog(null, "Полето не е въведено или вече съществува.");
			}
			
		}//end of actionPerformed
		
	}//end of ColorRegister
	
	class ColorDelete implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			thirdConn = DBConnection.getConnection();
			String sql = "delete from colors where color_id = ?";
			try{
				thirdState = thirdConn.prepareStatement(sql);
				thirdState.setString(1, colorF.getText());
				thirdState.execute();
				thirdClear();
				refreshThirdTable();
				fillColors(colorBox);
			}catch(SQLException sq){
				JOptionPane.showMessageDialog(null, "Не може да бъде премахнато.");
			}
			
		}//end of actionPerformed
		
	}//end of ColorDelete
	
	class ColorUpdate implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			thirdConn = DBConnection.getConnection();
			String sql = "update colors set color_name = ? where color_id = ?";
			try{
				thirdState = thirdConn.prepareStatement(sql);
				thirdState.setString(1, colorField.getText());
				thirdState.setString(2, colorF.getText());
				thirdState.execute();
				thirdClear();
				refreshThirdTable();
			}catch(SQLException sq){
				JOptionPane.showMessageDialog(null, "Не може да бъде променено.");
			}
			
		}//end of actionPerformed
		
	}//end of ColorUpdate
	
}//end of ThirdTab
