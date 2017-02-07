import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Depcheck {
	boolean flag=true;
	
	public void check(Connection connection, String db,String table,String mainf,String tempf){
	
		

	
		Statement st = null;
	Statement st2 = null;
	try {
		st = connection.createStatement();
		st2 = connection.createStatement();
	} catch (SQLException f1) {
		// TODO Auto-generated catch block
		f1.printStackTrace();
	}
	ResultSet rs = null;
	ResultSet rs2 = null;
	try {
		rs = st.executeQuery("SELECT DISTINCT " + "\"" + mainf+ "\"" + " FROM \"" + table+ "\"");
		rs2=st2.executeQuery("SELECT COUNT(DISTINCT " + "\"" + mainf+ "\"" + ") FROM \"" + table+ "\"");
		rs2.next();
		
		int a=Integer.valueOf(rs2.getString(1));
		for (int i=1;i<=a; i++){
			rs.next();
			rs2=st2.executeQuery("SELECT COUNT(DISTINCT " +"\"" + tempf+ "\""+ ") FROM \"" + table+ "\""+" WHERE " +"\"" + mainf+ "\""+ "='" +rs.getString(1)+"'" );
			rs2.next();
			if (rs2.getString(1).compareTo("1")!=0){
				flag=false;
			}
			//System.out.println(rs2.getString(1));
		
		}
		
		
		
	} catch (SQLException g1) {
		// TODO Auto-generated catch block
		g1.printStackTrace();
	}
	try {
		rs.close();
		rs2.close();
	} catch (SQLException h1) {
		// TODO Auto-generated catch block
		h1.printStackTrace();
	}
	try {
		st.close();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}


}
}
