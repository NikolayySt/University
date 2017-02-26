import java.sql.Connection;
import java.sql.DriverManager;


public class DBConnection {

	static Connection conn = null;
	
	static Connection getConnection(){
		try {
			Class.forName("org.h2.Driver");
			conn = DriverManager.getConnection("jdbc:h2:~/MainProjectDB", "sa", "");
			return conn;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			return conn;
		}
		
	}//end of getConnection 
	
}//end of DBConnection
