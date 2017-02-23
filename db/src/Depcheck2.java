import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Depcheck2 {
	boolean flag=true;
	int a;
	public void check(Connection connection, String db,String table,String mainf,String tempf){
	
		

	
		Statement st = null;
	Statement st2 = null;
	Statement st3 = null;
	Statement st4 = null;
	try {
		st = connection.createStatement();
		st2 = connection.createStatement();
		st3 = connection.createStatement();
		st4 = connection.createStatement();
	} catch (SQLException f1) {
		// TODO Auto-generated catch block
		f1.printStackTrace();
	}
	ResultSet rs = null;
		
	
	
	try {
		rs=st.executeQuery("select count(*) from " + table + " o"+ ","+ table + " t" + " where  " + "o." + mainf + "=t." + mainf + " and " + "o." + tempf + "<>t." + tempf + ";");  
		
		rs.next();
		a=Integer.valueOf(rs.getString(1));
		
	} catch (SQLException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
		try {
		rs.close();
		//rs2.close();
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

	if (a!=0){
		flag=false;
	}
	 
	File file =new File("src/Queries.txt");

	/* This logic is to create the file if the
	 * file is not already present
	 */
	if(!file.exists()){
	   try {
		file.createNewFile();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}

	//Here true is to append the content to file
	FileWriter fw = null;
	try {
		fw = new FileWriter(file,true);
	} catch (IOException e2) {
		// TODO Auto-generated catch block
		e2.printStackTrace();
	}
	//BufferedWriter writer give better performance
	BufferedWriter bw = new BufferedWriter(fw);
	
	try{
		bw.write("select count(*) from " + table + " o"+ ","+ table + " t" + " where  " + "o." + mainf + "=t." + mainf + " and " + "o." + tempf + "<>t." + tempf + ";");
		bw.newLine();
	
	//Closing BufferedWriter Stream
	bw.close();
	}catch (Exception e){
		
	}
	
	
	 
}
}
