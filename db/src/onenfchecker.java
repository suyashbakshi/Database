import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class onenfchecker {
	int a=0;
	int b=0;
	boolean flag=false;
	public void onfvalidation( Connection connection,String db,String table,String mainf){
		Statement st = null;
		Statement st2 = null;
		Statement st3 = null;
		int c = 0;
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
			rs = st.executeQuery("SELECT  COUNT( " + "\"" + mainf+ "\"" + ") FROM \"" + table+ "\"");
			rs2=st2.executeQuery("SELECT COUNT(DISTINCT " + "\"" + mainf+ "\"" + ") FROM \"" + table+ "\"");
			rs3=st3.executeQuery("SELECT COUNT ( *) FROM" + "\"" + table + "\"" + "WHERE" + "\"" + mainf + "\"" + "IS NULL");
			
			rs2.next();
			rs.next();
			rs3.next();
			 a=Integer.valueOf(rs2.getString(1));
			 b=Integer.valueOf(rs.getString(1));
			c=Integer.valueOf(rs3.getString(1));
			
		} catch (SQLException g1) {
			// TODO Auto-generated catch block
			g1.printStackTrace();
		}
		try {
			rs.close();
			rs2.close();
			rs3.close();
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


if ((a!=b)|| (c!=0)){
	flag=true;
	System.out.println(c);
	System.out.println("No 1nf");
}
	}
}
