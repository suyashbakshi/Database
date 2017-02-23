import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class tablecopy {
	
	public void copy(Connection connection, String db,String table){
		
		

		
		Statement st = null;
	
	try {
		st = connection.createStatement();
		
	} catch (SQLException f1) {
		// TODO Auto-generated catch block
		f1.printStackTrace();
	}
	ResultSet rs = null;
		
	
	
	try {
		st.executeUpdate("SELECT * INTO " +"team04schema."+ table+"copy" +" FROM " + table);  
		
		
		
	} catch (SQLException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
		
	
	 
}
}
