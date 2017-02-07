import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class db {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
		List<String> list = new ArrayList<String>();
		List<String> al = new ArrayList<String>();
		List<String> tables = new ArrayList<String>();
		String dbname=null;
		String[] word;
		String [][] fields= new String[20][40] ;
		String tt[];
		boolean f=false;
	//	Depcheck a;
	//	a= new Depcheck();
		//a.check("first","info","city","state");
		
		Connectdb c;
		c= new Connectdb();
		c.connecting();
		
		// Reading the input File------------------------
		try{
			BufferedReader in = new BufferedReader(new FileReader("\\F:\\PhD\\DataBase\\Project1\\input5.txt\\"));
			String str;

			
			while((str = in.readLine()) != null){
			    list.add(str);
			    
			}
			}catch(IOException ex) {
				
			}
			//-----------------------------------------
			
			// Detecting the name of the Data base--------
			dbname=list.get(0);
			
			tt=dbname.split("\\.");
			dbname=tt[1];
			
			//---------------
			
			
			// Detecting the name of the tables -----------
			
			for (int i=2; i <list.size(); i++) {
				char a=list.get(i).charAt(0);
				tables.add(String.valueOf(a));
			
			}
			
			//-----------------------------------
			
			// Detecting the name of columns ------------------------
			
			
			
			for (int i=2; i <list.size(); i++) {
			StringBuilder tempstr = new StringBuilder(list.get(i));
			tempstr.deleteCharAt(0);
			list.set(i,tempstr.toString());
			}
			
			word=(list.get(2)).split(",");
			 
			for (int i=2; i <list.size(); i++) {
			 word=(list.get(i)).split(",");
			
			 char a=list.get(i).charAt(0);
				//System.out.println(a);
			 int coun=1;
			 for (int j=0; j <word.length; j++) {
				 for(char alphabet = 'A'; alphabet <= 'J';alphabet++) {
					   
					
if (word[j].indexOf(alphabet)!=-1){
				 fields[i][j]=String.valueOf(alphabet);
}
				 }

				 for(char alphabet = 'K'; alphabet <= 'L';alphabet++) {
					   
						
					 if (word[j].indexOf(alphabet)!=-1){
					 				 fields[i][j]=String.valueOf(alphabet);
					 				fields[i][j]=fields[i][j]+coun;
					 				coun++;
					 }
					 				 }
 
				 
			 }
			
			}
			
			//2NF Validation------------------------------------------
			
			twonfchecker twonf;
			twonf= new twonfchecker();
		
		
			for (int j=0; j <tables.size(); j++) {
				
			for (int i=0; i <4; i++) {

				if ((fields[j+2][i].indexOf("K")!=-1) && (fields[j+2][i+1].indexOf("K")==-1)){

			twonf.twonfvalidation(c.connection,dbname,tables.get(j),fields[j+2][i],fields[j+2][i+1]);
			f=twonf.vflag;
			//System.out.println(tables.get(j)+" -- "+fields[j+2][i]+" -- "+fields[j+2][i+1]+" -- "+ tnf.vflag); 
			if (f==false){
				System.out.println("Table " + tables.get(j) + " is NOT 3NF " + " Cause= " + twonf.main + " => " + twonf.target   );	
				break;
			}
			
				
			
			
				
			}	
		}
		
		if (f==true){
			System.out.println("Table " + tables.get(j) + " is 2NF");	
		}
			
		}	
		
//3NF Validation -----------------------------------------------------------------
			
			if (f==true){	// if the table is 2NF we would validate 3NF
			TnfChecker tnf;
			tnf= new TnfChecker();
		
		
			for (int j=0; j <tables.size(); j++) {
				
			for (int i=0; i <4; i++) {


			tnf.tnfvalidation(c.connection,dbname,tables.get(j),fields[j+2][i],fields[j+2][i+1]);
			f=tnf.vflag;
			//System.out.println(tables.get(j)+" -- "+fields[j+2][i]+" -- "+fields[j+2][i+1]+" -- "+ tnf.vflag); 
			if (f==false){
				System.out.println("Table " + tables.get(j) + " is NOT 3NF " + " Cause= " + tnf.main + " => " + tnf.target   );	
				break;
			}
			
				
			
			
				
			
		}
		
		if (f==true){
			System.out.println("Table " + tables.get(j) + " is 3NF");	
		}
			
		}
			}
		//---------------------------------------------------------------
			
	}

}
