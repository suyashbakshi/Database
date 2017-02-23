import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
		
		String word[]= mainf.split(",");
		String composit="";
		for (int i=0;i<word.length;i++){
			composit=composit+word[i];
			if (i<word.length-1){
				composit=composit+",";
			}
		}
		String q1p1="SELECT COUNT (DISTINCT concat(";
		
			String q1p2	=  composit +")) FROM " + table  ;
		
			String query1=q1p1+q1p2;
			
			
			String q2p1="SELECT COUNT ( concat(";
			
			String q2p2	=  composit +")) FROM " + table  ;
		
			String query2=q2p1+q2p2;
			
			
		try {
			
			rs=st.executeQuery(query1);
			rs2=st2.executeQuery(query2);
		//	rs3=st3.executeQuery("SELECT COUNT ( *) FROM" + "\"" + table + "\"" + "WHERE" + "\"" + mainf + "\"" + "IS NULL");
			
			rs2.next();
			rs.next();
			//rs3.next();
			 a=Integer.valueOf(rs2.getString(1));
			 b=Integer.valueOf(rs.getString(1));
			//c=Integer.valueOf(rs3.getString(1));
			
		} catch (SQLException g1) {
			// TODO Auto-generated catch block
			g1.printStackTrace();
		}
		//try {
		//	rs.close();
			//rs2.close();
		//	rs3.close();
	//	} catch (SQLException h1) {
			// TODO Auto-generated catch block
			//h1.printStackTrace();
		//}
		try {
			st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


if ((a!=b)){
	flag=true;
	
}
else{
	
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
	
bw.write(query1);
bw.newLine();

bw.write(query2);
bw.newLine();


//Closing BufferedWriter Stream
bw.close();
}catch (Exception e){
	
}
	}
}
