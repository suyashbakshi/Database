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
		String [] dropped= new String[20] ;
		String [][] fields= new String[20][40] ;
		String tt[];
		boolean f1,f2 = false,f3=false;
		String [][] decomp2nf= new String[20][3] ;
		String [][] decomp3nf= new String[20][3] ;
		int decount=0;
		int keynum=0;
		int dropcoun=0;
		Connectdb c;
		c= new Connectdb();
		c.connecting();
		
		// Reading the input File------------------------
		try{
			BufferedReader in = new BufferedReader(new FileReader("\\F:\\PhD\\DataBase\\Project1\\input9.txt\\"));
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
			
			// Creating object for starting decompsition process----
			decomposition p;
			p= new decomposition();
			
			//----------------
			
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
				
				decomp2nf[i][2]="";
				for (int k=i+1; k <10; k++) {
				
				try{

				if ((fields[j+2][i].indexOf("K")!=-1) && (fields[j+2][k].indexOf("K")==-1)){
				
			twonf.twonfvalidation(c.connection,dbname,tables.get(j),fields[j+2][i],fields[j+2][k]);
			f2=twonf.vflag;
			//System.out.println(tables.get(j)+" -- "+fields[j+2][i]+" -- "+fields[j+2][i+1]+" -- "+ tnf.vflag); 
			if (f2==false){
				System.out.println("Table " + tables.get(j) + " is NOT 2NF " + " Cause= " + twonf.main + " => " + twonf.target   );	
			//	p.decompose(c.connection, dbname, "decomposed2nf"+ j  ,twonf.main,twonf.target);
				decomp2nf[i][1]=twonf.main;
				decomp2nf[i][2]=decomp2nf[i][2]+twonf.target+" ";
				//break;
			}
			
							
			
				
			}
} catch (Exception e){
					
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
				decomp3nf[i][2]="";
				for (int k=i+1; k <10; k++) {
try{
				if ((fields[j+2][i].indexOf("K")==-1) && (fields[j+2][k].indexOf("K")==-1)){
					
			tnf.tnfvalidation(c.connection,dbname,tables.get(j),fields[j+2][i],fields[j+2][k]);
			f3=tnf.vflag;
			//System.out.println(tables.get(j)+" -- "+fields[j+2][i]+" -- "+fields[j+2][i+1]+" -- "+ tnf.vflag); 
			if (f3==false){
				System.out.println("Table " + tables.get(j) + " is NOT 3NF " + " Cause= " + tnf.main + " => " + tnf.target   );	
				
				
			
				decomp3nf[i][1]=tnf.main;
				decomp3nf[i][2]=decomp3nf[i][2]+tnf.target+" ";
				decomp3nf[i][0]=tables.get(j);  //Storing the number of Table for name of new decomposed tables
			
				//break;
			}
			
				
				}
			
} catch (Exception e){
	
}
			
		}
			}
		
		if (f3==true){
			System.out.println("Table " + tables.get(j) + " is 3NF");	
		}
			
		}
			}
		//---------------------------------------------------------------
		
			//---- Decomposing 1 to 1 Functional Dependency (3NF)
			for (int j=0; j <20; j++) {
				if ((decomp3nf[j][1]!=null) && (decomp3nf[j][2].length()==2)){ 
					System.out.println(decomp2nf[j][0]);
					p.decompose1(c.connection, dbname,decomp3nf[j][0], "decomposed3nf"+decomp3nf[j][1]+decomp3nf[j][2]   ,decomp3nf[j][1],decomp3nf[j][2]);
					dropped[dropcoun]=decomp3nf[j][2];
					dropcoun++;
				}
				
			}
				//-------------------------
				
			
			
			
			//---- Decomposing 1 to 2 Functional Dependency (3NF)
			boolean[] flag= new boolean[3]; 
			flag[0]=false;
			flag[1]=false;
			for (int j=0; j <20; j++) {
				
				if ((decomp3nf[j][1]!=null) && (decomp3nf[j][2].length()==4)){ 
				
					String[] tword=decomp3nf[j][2].split(" ");
					int wcoun=0;
					for (int m=0; m <tword.length; m++) {
						for (int v=0; v <dropcoun; v++) {
							String temp;
							temp=dropped[v];
							char trt= temp.charAt(0);
							String temp2=String.valueOf(trt);
						if (tword[m].compareTo(temp2)!=0){
							flag[m]=true;
							
							wcoun++;
						}
						}
					}
					
					if (wcoun==2){
						p.decompose2(c.connection, dbname,decomp3nf[j][0], "decomposed3nf2"+decomp3nf[j][1]+decomp3nf[j][2]   ,decomp3nf[j][1],tword[0],tword[1]);
						System.out.println("both");
					}
					
				else if ((wcoun==1)&& (flag[1]==true)){
						//p.decompose1(c.connection, dbname,decomp3nf[j][0], "decomposed3nf"+decomp3nf[j][1]+decomp3nf[j][2]   ,decomp3nf[j][1],decomp3nf[j][2]);
					System.out.println("number1");
					}
					
				else if ((wcoun==1)&& (flag[0]=true)){
					p.decompose1(c.connection, dbname,decomp3nf[j][0], "decom3nf" + wcoun  ,decomp3nf[j][1],tword[0]);

				
				}

					//
				}
			}
			
			
			//---------------------------------------
			for (int j=0; j <20; j++) {
				if (decomp3nf[j][1]!=null)
				System.out.println(decomp3nf[j][1]+ "--" + decomp3nf[j][2] );
			}
			
			
	}

}
