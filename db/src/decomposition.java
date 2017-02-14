import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class decomposition {
	public void decompose1( Connection connection,String db,String tablem,String tablet,String mainf,String tempf){
		
	
		
		
		Statement st = null;
		Statement st2 = null;
		Statement st3 = null;
		try {
			st = connection.createStatement();
			st2 = connection.createStatement();
			st3 = connection.createStatement();
		} catch (SQLException f1) {
			// TODO Auto-generated catch block
			f1.printStackTrace();
		}
		ResultSet rs = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		
			try {
				//st3.executeUpdate("CREATE TABLE "  + table  + "( "+ mainf+ "character( 80 ) )");
				st3.executeUpdate ("CREATE TABLE " + tablet+ " ( " + mainf + " character(80), " + tempf + " character(80) );");
		
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		

			try {
				st2.executeUpdate("INSERT INTO " + tablet + " ( " +  mainf + "," + tempf+ ") " +  "(SELECT DISTINCT " +"\""  + mainf+ "\""+ " , " +"\"" + tempf.charAt(0)+ "\""+ " FROM " + "\"" + tablem+ "\"" +" )");
	
				//st2.executeQuery( "(SELECT " +"\""  + "D"+ "\""+ " , " +"\"" + tempf.charAt(0)+ "\""+ " FROM " + "\"" + tablem+ "\"" +" )" ) ;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				st2.executeUpdate("ALTER TABLE " + "\""  + tablem+ "\"" + " DROP " + "\""  +tempf.charAt(0)+ "\"") ;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


	}
	
public void decompose2( Connection connection,String db,String tablem,String tablet,String mainf,String tempf1,String tempf2){
		
	
		
		
		Statement st = null;
		Statement st2 = null;
		Statement st3 = null;
		try {
			st = connection.createStatement();
			st2 = connection.createStatement();
			st3 = connection.createStatement();
		} catch (SQLException f1) {
			// TODO Auto-generated catch block
			f1.printStackTrace();
		}
		ResultSet rs = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		
			try {
				//st3.executeUpdate("CREATE TABLE "  + table  + "( "+ mainf+ "character( 80 ) )");
				st3.executeUpdate ("CREATE TABLE " + tablet+ " ( " + mainf + " character(80), " + tempf1 + " character(80), " + tempf2 + " character(80) );");
		
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		

			try {
				st2.executeUpdate("INSERT INTO " + tablet + " ( " +  mainf + "," + tempf1+ "," + tempf2+ ") " +  "(SELECT DISTINCT " +"\""  + mainf+ "\""+ " , " +"\"" + tempf1.charAt(0)+ "\""+ " , " +"\"" + tempf2.charAt(0)+ "\""+ " FROM " + "\"" + tablem+ "\"" +" )");
	
				//st2.executeQuery( "(SELECT " +"\""  + "D"+ "\""+ " , " +"\"" + tempf.charAt(0)+ "\""+ " FROM " + "\"" + tablem+ "\"" +" )" ) ;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				st2.executeUpdate("ALTER TABLE " + "\""  + tablem+ "\"" + " DROP " + "\""  +tempf1.charAt(0)+ "\"") ;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

try {
	st.close();
	st2.close();
} catch (SQLException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}


	}

}
