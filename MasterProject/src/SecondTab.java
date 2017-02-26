import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;


public class SecondTab extends FirstTab{
	
	Connection secondConn = null;
	ResultSet secondResult = null;
	PreparedStatement secondState = null;

	JPanel second = new JPanel();
	JTable secondTable = new JTable();
	JScrollPane secondScroll = new JScrollPane(secondTable);
	
	//Top Panel
	JPanel secondTopPanel = new JPanel();
	JLabel countryLabel = new JLabel("Държава: ");
	JTextField countryField = new JTextField(20);
	JLabel country = new JLabel("Държава (съкратено): ");
	JTextField countryF = new JTextField(3);
	
	//Mid Panel
	JPanel secondMidPanel = new JPanel();
	JButton countryRegister = new JButton("Въвеждане");
	JButton countryUpdate = new JButton("Промяна");
	JButton countryDelete = new JButton("Изтриване");
	
	//Bot Panel	
	JPanel secondBottomPanel = new JPanel();
	
	public SecondTab(){
		super();
		
		//Top Panel
		secondTopPanel.setLayout(new GridLayout(2, 2));
		secondTopPanel.add(country);
		secondTopPanel.add(countryF);
		secondTopPanel.add(countryLabel);
		secondTopPanel.add(countryField);
		second.add(secondTopPanel);
		
		// Mid Panel
		secondMidPanel.setLayout(new GridLayout(1,3));
		countryRegister.addActionListener(new CountryRegister());
		countryDelete.addActionListener(new CountryDelete());
		countryUpdate.addActionListener(new CountryUpdate());
		secondMidPanel.add(countryRegister);
		secondMidPanel.add(countryUpdate);
		secondMidPanel.add(countryDelete);
		
		second.add(secondMidPanel);
		
		//Bottom Panel
		secondScroll.setSize(400, 150);
		secondBottomPanel.add(secondScroll);
		
		second.add(secondBottomPanel);
				
		myPane.add("Държава", second);
		
		secondRefreshTable();
	}//end of constructor

	class CountryRegister implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			secondConn = DBConnection.getConnection();
			String sql = "insert into countries values(?,?);";
			try{
			secondState = secondConn.prepareStatement(sql);
			secondState.setString(1, countryF.getText());
			secondState.setString(2, countryField.getText());
			secondState.execute();
			secondClear();
			secondRefreshTable();
			fillCountries(countryBox);
			}catch(SQLException sqlE){
				JOptionPane.showMessageDialog(null, "Полето не е въведено или вече съществува.");
			}
		}
		
	}//end of CountryRegister
	
	class CountryUpdate implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			secondConn = DBConnection.getConnection();
			String sql = "update countries set country_name = ? where country_id = ?";
			try{
				secondState = secondConn.prepareStatement(sql);
				secondState.setString(1, countryField.getText());
				secondState.setString(2, countryF.getText());
				secondState.execute();
				secondClear();
				secondRefreshTable();
			}catch(SQLException sq){
				JOptionPane.showMessageDialog(null, "Не може да бъде променено.");
			}
			
		}//end of actionPerformed
	
		
	}//end of CountryUpdate
	
	class CountryDelete implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			secondConn = DBConnection.getConnection();
			String sql = "delete from countries where country_id = ?";
			try{
				secondState = secondConn.prepareStatement(sql);
				secondState.setString(1, countryF.getText());
				secondState.execute();
				secondClear();
				secondRefreshTable();
				fillCountries(countryBox);
			}catch(SQLException sq){
				JOptionPane.showMessageDialog(null, "Не може да бъде изтрито.");
			}
		}//end of actionPerformed
		
	}//end of CountryDelete
	
	
	public void secondClear(){
		countryF.setText("");
		countryField.setText("");
	}//end of clear
	
	
	public ResultSet getSecondAll(){
		secondConn = DBConnection.getConnection();
		
		try{
			secondState = secondConn.prepareStatement("select * from countries");
			secondResult = secondState.executeQuery();
			return secondResult;
		}catch(SQLException sq){
			sq.printStackTrace();
		}finally{
			return secondResult;
		}
		
	}//end of getAll
	
	
	public void secondRefreshTable(){
		try{
			secondTable.setModel(new MyModel(getSecondAll()));
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}//end of refreshTable

}//end of SecondPanel
