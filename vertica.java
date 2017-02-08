import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


public class vertica {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 try {
		        Class.forName("com.vertica.jdbc.Driver");
		    } catch (ClassNotFoundException e1) {
		        // Could not find the driver class. Likely an issue
		        // with finding the .jar file.
		        System.err.println("Could not find the JDBC driver class.");
		        e1.printStackTrace();
		        return; // Bail out. We cannot do anything further.
		    }

		    // Create property object to hold username & password
		    Properties myProp = new Properties();
		    
		    
		    Connection conn;
		    try {
		        conn = DriverManager.getConnection(
		                   "jdbc:vertica://129.7.243.243:5433/cosc6340s17", "team04","XCQyntKe");
		    } catch (SQLException e1) {
		        // Could not connect to database.
		        System.err.println("Could not connect to database.");
		        e1.printStackTrace();
		        return;
		    }
		    // Connection is established, do something with it here or
		    // return it to a calling method
		   Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		   ResultSet rs = null;
		   try {
			rs = stmt.executeQuery("SELECT  count(*)  from T");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   try {
			while(rs.next()){
			      try {
					System.out.println( rs.getInt(1) );
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			   }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
