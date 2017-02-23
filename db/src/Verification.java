import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Verification {

	public void verify(Connection connection, String db,String mtable,String field,String tablem,String table, String keys,String nkeys){
		
		String word1[]=table.split(",");
		String word2[]=keys.split(",");
		String word3[]=nkeys.split(",");
		String composit2="";
		int a=0;
		
		
			String word4[]=nkeys.split(",");
			for (int j=0;j<word4.length;j++){
				composit2=composit2+ "team04schema." +word4[j] + ", ";
			}
		
		
		String q1p1="select "+ composit2+ tablem+".*"+" INTO "+ "team04schema." +"joint"+word1[0]+" from " + tablem ;
		
		String joint="team04schema." +"joint"+word1[0];
		String composit1="";
		
		for (int i=0;i<word1.length;i++){
			
			composit1=composit1+" JOIN " + "team04schema."+word1[i] + " ON " + tablem + "." + word2[i] + " = " +"team04schema."+word1[i] + "." + word2[i];
			
			if (i<word1.length-1){
				composit1=composit1+  " ";
			}
		}
		
		String query1=q1p1+composit1;
		
		
		
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
			
		
		try {
			st.executeUpdate (query1);
		//	st3.executeUpdate ("CREATE TABLE " + tablet+ " ( " + mainf + " character(80), " + tempf + " character(80) );");

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String q2p1="SELECT COUNT (*) FROM (SELECT ";
		
		String composit3="";
		String word5[]=field.split(",");
		for (int i=0;i<word5.length;i++){
			composit3=composit3+joint+ "."+word5[i];
			
			if (i<word5.length-1){
				composit3=composit3+",";
			}
		}
		
		
		String composit4="";
		String word6[]=field.split(",");
		for (int i=0;i<word6.length;i++){
			composit4=composit4+mtable+ "."+word6[i];
			
			if (i<word6.length-1){
				composit4=composit4+",";
			}
		}

		String q2p2=" FROM " +joint + " MINUS SELECT "; 
		String q2p3=" FROM " +  mtable + ") AS val1";
		
		String query2=q2p1+composit3+q2p2+composit4+q2p3;
		
		
		
		
		try {
			rs = st2.executeQuery(query2);
			rs.next();
			a=Integer.valueOf(rs.getString(1));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			a=0;
		}
		
	
		
		File file2 =new File("src/DecompositionReport.txt");

    	/* This logic is to create the file if the
    	 * file is not already present
    	 */
    	if(!file2.exists()){
    	   try {
			file2.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	}

    	//Here true is to append the content to file
    	FileWriter fw2 = null;
		try {
			fw2 = new FileWriter(file2,true);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
    	//BufferedWriter writer give better performance
    	BufferedWriter bw2 = new BufferedWriter(fw2);
    	
    	try{
    		bw2.write("#Verification:");
    		bw2.newLine();
    		
    		String composit30="";
    		for (int i=0;i<word1.length;i++){
    			composit30=composit30+word1[i];
    			
    			if (i<word1.length-1){
    				composit30=composit30+",";
    			}
    		}
    		
    		if (a==0){
    		bw2.write("team04schema." +"joint"+word1[0] + " =Join(" + mtable+"copy"+ "," + composit30 + ") ? YES" );	
    		bw2.newLine();
    		}
    		else{
    			
    			bw2.write("team04schema." +"joint"+word1[0] + " =Join(" + mtable+"copy"+ "," + composit30 + ") ? NO" );	
        		bw2.newLine();
    		}
    	//Closing BufferedWriter Stream
    	bw2.close();
    	}catch (Exception e){
    		
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
			bw.write(query1);
			bw.newLine();
			
			bw.write(query2);
			bw.newLine();
		
		//Closing BufferedWriter Stream
		bw.close();
		}catch (Exception e){
			
		}
		

	System.out.println("All The Reports Generated !");
	}
}
