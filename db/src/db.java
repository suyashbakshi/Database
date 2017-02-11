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
		boolean f1,f2 = false,f3=false;
	
		int keynum=0;
		Connectdb c;
		c= new Connectdb();
		c.connecting();
		
		// Reading the input File------------------------
		try{
			BufferedReader in = new BufferedReader(new FileReader("\\F:\\PhD\\DataBase\\Project1\\input8.txt\\"));
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
			 keynum=0;
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
					 				keynum++;
					 }
					 				 }
 
				 
			 }
			
			}
			//1NF Validation------------------------------
			onenfchecker u;
			u= new onenfchecker();
			for (int j=0; j <tables.size(); j++) {
				
				for (int i=0; i <10; i++) {

					try{
					if ((fields[j+2][i].indexOf("K")!=-1)) {
			u.onfvalidation(c.connection,dbname,tables.get(j),fields[j+2][i]);
			
					}
					} catch(Exception e){
						
					}
			
				}
			}
			f1=u.flag;
			f1=false;
			//2NF Validation------------------------------------------
			if (keynum>1){
			if (f1==false){
				
				
			twonfchecker twonf;
			twonf= new twonfchecker();
		
		
			for (int j=0; j <tables.size(); j++) {
				
			for (int i=0; i <10; i++) {
				

				if ((fields[j+2][i].indexOf("K")!=-1) && (fields[j+2][i+1].indexOf("K")==-1)){
				
			twonf.twonfvalidation(c.connection,dbname,tables.get(j),fields[j+2][i],fields[j+2][i+1]);
			f2=twonf.vflag;
			//System.out.println(tables.get(j)+" -- "+fields[j+2][i]+" -- "+fields[j+2][i+1]+" -- "+ tnf.vflag); 
			if (f2==false){
				System.out.println("Table " + tables.get(j) + " is NOT 3NF " + " Cause= " + twonf.main + " => " + twonf.target   );	
				break;
			}
			
				
			
			
				
			}
		}
		
		if (f2==true){
			System.out.println("Table " + tables.get(j) + " is 2NF");	
		}
			
		}	
			}
			}
			else{
				f2=true;
			}
			
			
//3NF Validation -----------------------------------------------------------------
			f2=true;
			
			if (f2==true){	// if the table is 2NF we would validate 3NF
			TnfChecker tnf;
			tnf= new TnfChecker();
		
		
			for (int j=0; j <tables.size(); j++) {
				
			for (int i=0; i <10; i++) {
try{
				if ((fields[j+2][i].indexOf("K")==-1) && (fields[j+2][i+1].indexOf("K")==-1)){
					
			tnf.tnfvalidation(c.connection,dbname,tables.get(j),fields[j+2][i],fields[j+2][i+1]);
			f3=tnf.vflag;
			//System.out.println(tables.get(j)+" -- "+fields[j+2][i]+" -- "+fields[j+2][i+1]+" -- "+ tnf.vflag); 
			if (f3==false){
				System.out.println("Table " + tables.get(j) + " is NOT 3NF " + " Cause= " + tnf.main + " => " + tnf.target   );	
				//break;
			}
			
				
				}
			
} catch (Exception e){
	
}
			
		}
		
		if (f3==true){
			System.out.println("Table " + tables.get(j) + " is 3NF");	
		}
			
		}
			}
		//---------------------------------------------------------------
			
	}

}
