import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;


public class SearchTab extends ThirdTab{

	Connection con = null;
	PreparedStatement stat = null;
	ResultSet rs = null;
	
	//Top
	JPanel searchPanel = new JPanel();
	JPanel topPanel = new JPanel();
	JLabel searchLabel = new JLabel("Търсене по марка: ");
	JLabel inputLabel = new JLabel("Търсене по Цвят: ");
	JTextField brandField = new JTextField(15);
	JTextField colorField = new JTextField(15);
	JButton searchButton = new JButton("Търси");
	//JComboBox<String> searchBox = new JComboBox<String>();

	
	//Bot
	JPanel botPanel = new JPanel();
	JTable searchTable = new JTable();
	JScrollPane searchScroll = new JScrollPane(searchTable);

	
	
	
	public SearchTab() {
		super();
		
		topPanel.setLayout(new GridLayout(2,2));
		topPanel.add(searchLabel);
		topPanel.add(brandField);
		topPanel.add(inputLabel);
		topPanel.add(colorField);
		searchPanel.add(topPanel);
		searchButton.addActionListener(new SearchAction());
		searchPanel.add(searchButton);
		
		
		searchScroll.setSize(400, 350);
		botPanel.add(searchScroll);
		searchPanel.add(botPanel);
		
		myPane.add("Търсене", searchPanel);
	}//end of constructor
	
	class SearchAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			con = DBConnection.getConnection();
			String sql = "select c.brand, c.model from cars c join colors co on c.color_id = co.color_id where c.brand = ? and co.color_name = ?";
			try{
				state = conn.prepareStatement(sql);
				state.setString(1, brandField.getText());
				state.setString(2, colorField.getText());
				rs = state.executeQuery();
				brandField.setText("");
				colorField.setText("");
				searchTable.setModel(new MyModel(rs));
			}				
			catch(Exception sqll){
				sqll.printStackTrace();
			}
			
		}//end of actionPerformed
		
	}//end of SearchAction
	
	private void clearF() {
		brandField.setText("");
		modelField.setText("");		
	}//end of clearF
	
	
	
}//end of SearchTab
