import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class decomposition {
	public void decompose1( Connection connection,String db,String tablem,String tablet,String mainf,String tempf,String field){
		
	
		
		
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
		
		
		String word[]= tempf.split("0");
		String composit1="";
		
		for (int i=0;i<word.length;i++){
			composit1=composit1+word[i] + " Integer ";
			
			if (i<word.length-1){
				composit1=composit1+",";
			}
		}
		String q1p1="CREATE TABLE " + "team04schema."+ tablet + " ( "  + mainf + " Integer, ";
		
		String query1=q1p1+composit1+" )";
		
		
String composit2="";
		
		for (int i=0;i<word.length;i++){
			composit2=composit2+ word[i] ;
			
			if (i<word.length-1){
				composit2=composit2+",";
			}
		}

	// Reporting the decomposition Results
		
	
    	
	
		String composit10="";
		
		for (int m=0;m<word.length;m++){
			composit10=composit10+word[m];
			
			if (m<word.length-1){
				composit10=composit10+",";
			}
		}

		File file =new File("src/DecompositionReport.txt");

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
    		bw.write("#" + tablem + " decomposition:");
    		bw.newLine();
    	bw.write("team04schema."+ tablet + "(" + mainf + "," + composit10 + ")");
    	bw.newLine();
    	
    	bw.write(tablem+"copy"+ "(" + field + ")");
      	bw.newLine();
    	//Closing BufferedWriter Stream
    	bw.close();
    	}catch (Exception e){
    		
    	}
		
		
		//
		String q2p1= "INSERT INTO " + "team04schema."+ tablet +  " ( " +  mainf + ",";
		
		
		String q2p2=") " +  "(SELECT DISTINCT " +  mainf+ " , ";
		
		String composit3="";
		for (int i=0;i<word.length;i++){
			composit3=composit3+ word[i] ;
			
			if (i<word.length-1){
				composit3=composit3+" , ";
			}
		}
		
		
		

		
		String q2p3=	" FROM " +  tablem+ " )";
		
		String query2=q2p1+composit2+q2p2+composit3+q2p3;
		
		
		String q3p1= "ALTER TABLE " +"team04schema."+tablem+"copy"  ;
		
	
	
		
			
		try {
		st3.executeUpdate (query1);
	//	st3.executeUpdate ("CREATE TABLE " + tablet+ " ( " + mainf + " character(80), " + tempf + " character(80) );");

	} catch (SQLException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}


	try {
		st2.executeUpdate(query2);
		//st2.executeUpdate("INSERT INTO " + tablet + " ( " +  mainf + "," + tempf+ ") " +  "(SELECT DISTINCT " +"\""  + mainf+ "\""+ " , " +"\"" + tempf.charAt(0)+ "\""+ " FROM " + "\"" + tablem+ "\"" +" )");

		
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	

	String composit4="";
	String query3 = null;
	for (int i=0;i<word.length;i++){
	query3=	 "ALTER TABLE team04schema." +tablem+"copy" +" drop column " + word[i] ;
	
	try {
		st3.executeUpdate (query3);
	//	st3.executeUpdate ("CREATE TABLE " + tablet+ " ( " + mainf + " character(80), " + tempf + " character(80) );");

	} catch (SQLException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}

				}
	
	
	
	File file2 =new File("src/Queries.txt");

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
	FileWriter	 fw2 = null;
	try {
		fw2 = new FileWriter(file2,true);
	} catch (IOException e2) {
		// TODO Auto-generated catch block
		e2.printStackTrace();
	}
	//BufferedWriter writer give better performance
	BufferedWriter bw2 = new BufferedWriter(fw2);
	
	try{
		bw2.write(query1);
		bw2.newLine();
		
		bw2.write(query2);
		bw2.newLine();
		
		bw2.write(query3);
		bw2.newLine();
	
	//Closing BufferedWriter Stream
	bw2.close();
	}catch (Exception e){
		
	}
	

	}
	

}
