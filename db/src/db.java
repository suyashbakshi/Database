import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
		String tablej="";
		String fkeys="";
		String nkeys="";
		String fd="";
		boolean f1,f2 = false,f3=false,f11=false,f22=true,f33=true;
		String [][][] decomp2nf= new String[10][20][3] ;
		String [][][] decomp3nf= new String[10][20][3] ;
		String [][] report= new String[20][5] ;
		int decount=0;
		int keynum=0;
		int dropcoun=0;
		int u=0;
		int co=0;
		int fdco=0;
		String field="";
		Connectdbv c;
		c= new Connectdbv();
		c.connecting();
		
		//Deleting the previous Report--------------
		
		File file =new File("src/DecompositionReport.txt");

    	/* This logic is to create the file if the
    	 * file is not already present
    	 */
    	if(file.exists()){
    	   try {
			file.delete();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	}
    	
    	
    	
    	 file =new File("src/Queries.txt");

    	/* This logic is to create the file if the
    	 * file is not already present
    	 */
    	if(file.exists()){
    	   try {
			file.delete();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	}
		//-----------------------------------
		
		
			
			
			 String schemaFile = "src/input2.txt";
		        BufferedReader reader = null;
				try {
					reader = new BufferedReader(new FileReader(schemaFile));
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

		        String schema;
int z=0;
String temp;
		        try {
					while ((schema = reader.readLine()) != null) {

					   
					    
					   

					    //Parse each line to retrieve tablename and columns
					    int idx = schema.indexOf('(');
					    String tablename = schema.substring(0, idx);
					   
					    tables.add(tablename);

					    String sub = schema.substring(++idx, schema.length() - 1);
					    String[] columns = sub.split(",");
					    
					  int  g=0;
					  
					    for (String s : columns) {
					    
					    	temp="";
					    	int w=0;
					    	if (s.indexOf("k")!=-1){
					    	while (s.charAt(w)!='('){
					    	temp=temp+	s.charAt(w);
					    	w++;
					    	}
					    	s=temp;
					    	}
					    	
					    	temp="";
					    	if (s.indexOf(")")!=-1){
						    	while (s.charAt(w)!=')'){
						    	temp=temp+	s.charAt(w);
						    	w++;
						    	}
						    	s=temp;
						    	}
					        fields[z][g]=s;
					        g++;
					        
					    }
					    z++;

					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
			
							
			
			// Creating object for starting decompsition process----
			decomposition p;
			p= new decomposition();
			
			//creating a copy of original table in our schema----------------
			tablecopy cop;
			cop= new tablecopy();
			
			for (int j=0; j <tables.size(); j++) {
		cop.copy(c.connection,dbname,tables.get(j));
			}
			//
			
			//1NF Validation------------------------------
			onenfchecker uv;
			uv= new onenfchecker();
			for (int j=0; j <tables.size(); j++) {
				String keys="";
				for (int i=0; i <40; i++) {

					
				
					try{
					if ((fields[j][i].indexOf("k")!=-1)) {
						keys=keys+fields[j][i]+",";
			
			
					}
					
					else	if ((fields[j][i].indexOf("K")!=-1)) {
						keys=keys+fields[j][i]+",";
			
			
					}
					
					} catch(Exception e){
						
					}
			
				}
				uv.onfvalidation(c.connection,dbname,tables.get(j),keys);
				f1=uv.flag;
				report[j][0]=String.valueOf(f1);
				if (f1==true){
					f11=true;
				}
			}
			
		
			if (f11==false){
				
			//2NF Validation------------------------------------------
			
			if (f11==false){
				
				
			twonfchecker twonf;
			twonf= new twonfchecker();
		
		
			for (int j=0; j <tables.size(); j++) {
				
				report[j][3]="";
			for (int i=0; i <40; i++) {
				co=0;
				
				decomp2nf[j][i][2]="";
				
				for (int k=i+1; k <10; k++) {
				
				try{

				if (((fields[j][i].indexOf("k")!=-1) && (fields[j][k].indexOf("k")==-1)) ||((fields[j][i].indexOf("K")!=-1) && (fields[j][k].indexOf("K")==-1))){
				
			twonf.twonfvalidation(c.connection,dbname,tables.get(j),fields[j][i],fields[j][k]);
			f2=twonf.vflag;
			
		 
			if (f2==false){
							
				report[j][3]=report[j][3]+twonf.main + " => " + twonf.target +", ";
				f22=false;
				decomp2nf[j][i][1]=twonf.main;
				decomp2nf[j][i][2]=decomp2nf[j][i][2]+twonf.target+"0";
				decomp2nf[j][i][0]=tables.get(j);
				fd=fd+twonf.target;
				
				//break;
			}
			
							
			
				
			}
} catch (Exception e){
					
				}

		}
				
				
					
			}
			
			report[j][1]=String.valueOf(f22);
		if (f22==true){
				
			
		}
			
		}	
			}
			}
			else{
				f2=true;
			}
		
			
			//---- Decomposing  (2NF)------------------
			
			
			
			
			if (f22==false){
				
				for (int i=0; i <tables.size(); i++) {
					tablej="";
					fkeys="";
					nkeys="";
					 field="";
						for (int k=0; k <40; k++) {
							
							if (fields[i][k]!=null){
								field=field+fields[i][k]+",";
							}
						}
				for (int j=0; j <40; j++) {
					if ((decomp2nf[i][j][1]!=null)){ 
				
					
						

						String word10[]=field.split(",");
						String yt="";
						for (int v=0;v<word10.length;v++){
							if (decomp2nf[i][j][2].indexOf(word10[v])==-1){
								yt=yt+word10[v];
								if (v<word10.length-1){
									yt=yt+",";
								}
							}
						}
						
						
						
						p.decompose1(c.connection, dbname,decomp2nf[i][j][0], "decom2nf"+decomp2nf[i][j][0]+decomp2nf[i][j][1]+decomp2nf[i][j][2]   ,decomp2nf[i][j][1],decomp2nf[i][j][2],yt);
						tablej=tablej+"decom2nf"+decomp2nf[i][j][0]+decomp2nf[i][j][1]+decomp2nf[i][j][2]+",";
						fkeys=fkeys+decomp2nf[i][j][1]+",";
						String word2[]= decomp2nf[i][j][2].split("0");
				
						for (int k=0; k <word2.length; k++) {
						nkeys=nkeys+"decom2nf"+decomp2nf[i][j][0]+decomp2nf[i][j][1]+decomp2nf[i][j][2]+"." + word2[k] + "," ;
						
						}
					}
					
				}
				
				//creating verification object
				
				Verification v;
				v= new Verification();
				
				
				
				try{
				v.verify(c.connection, dbname,tables.get(i),field,"team04schema."+tables.get(i)+"copy", tablej, fkeys,nkeys);
				} catch (Exception e){
					
				}
				}
			
//3NF Validation -----------------------------------------------------------------
			
		
			
	if		(f22==true){
			
			if (f2==true){	// if the table is 2NF we would validate 3NF
			TnfChecker tnf;
			tnf= new TnfChecker();
		
		
			for (int j=0; j <tables.size(); j++) {
				
			for (int i=0; i <40; i++) {
				co=0;
				decomp3nf[j][i][2]="";
				fd="";
				
				for (int k=i+1; k <10; k++) {
try{
				if (((fields[j][i].indexOf("k")==-1) && (fields[j][k].indexOf("k")==-1))|| ((fields[j][i].indexOf("K")==-1) && (fields[j][k].indexOf("K")==-1))){
					
			tnf.tnfvalidation(c.connection,dbname,tables.get(j),fields[j][i],fields[j][k]);
			f3=tnf.vflag;
			
			report[j][2]=String.valueOf(f3);
			 
			if (f3==false){
				f33=false;
				
				report[j][4]=report[j][4]+tnf.main + " => " + tnf.target +", ";
					
				
				
			
				decomp3nf[j][i][1]=tnf.main;
				
				decomp3nf[j][i][2]=decomp3nf[j][i][2]+tnf.target+"0";
				
				
				decomp3nf[j][i][0]=tables.get(j);  //Storing the number of Table for name of new decomposed tables
			
				
			}
			
				
				}
			
} catch (Exception e){
	
}
			
		}
				co++;
			
			}
			
		if (f33==true){
				
		}
			
		}
			}
	
		//---------------------------------------------------------------
		
			//---- Decomposing (3NF)
			//creating verification object
			
			
			
			if (f33==false){
				
				for (int i=0; i <tables.size(); i++) {
					tablej="";
					fkeys="";
					nkeys="";
					
					 field="";
					for (int k=0; k <40; k++) {
						
						if (fields[i][k]!=null){
							field=field+fields[i][k]+",";
						}
					}
				for (int j=0; j <40; j++) {
					if ((decomp3nf[i][j][1]!=null)){ 
				
					


						String word10[]=field.split(",");
						String yt="";
						for (int v=0;v<word10.length;v++){
							if (decomp3nf[i][j][2].indexOf(word10[v])==-1){
								yt=yt+word10[v];
								if (v<word10.length-1){
									yt=yt+",";
								}
							}
						}
						
						
						
						
					p.decompose1(c.connection, dbname,decomp3nf[i][j][0], "decom3nf"+decomp3nf[i][j][0]+decomp3nf[i][j][1]+decomp3nf[i][j][2]   ,decomp3nf[i][j][1],decomp3nf[i][j][2],yt);
						tablej=tablej+"decom3nf"+decomp3nf[i][j][0]+decomp3nf[i][j][1]+decomp3nf[i][j][2]+",";
						fkeys=fkeys+decomp3nf[i][j][1]+",";
						String word2[]= decomp3nf[i][j][2].split("0");
				
						for (int k=0; k <word2.length; k++) {
						nkeys=nkeys+"decom3nf"+decomp3nf[i][j][0]+decomp3nf[i][j][1]+decomp3nf[i][j][2]+"." + word2[k] + "," ;
						}
					}
					
				}
				
				Verification v;
				v= new Verification();
				
			
				try{
				v.verify(c.connection, dbname,tables.get(i),field,"team04schema."+tables.get(i)+"copy", tablej, fkeys,nkeys);
				} catch (Exception e){
					
				}
				}
			}
	}
			}
				//-------------------------
				
			
			// Generating the report--------------------
			
			File fout1 = new File("src/Report.txt");
			FileOutputStream fos1=null;
			try {
				fos1 = new FileOutputStream(fout1);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos1));
			
			try {
				bw.write("#Table    3NF    Failed    Reason");
				bw.newLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String line=null;
			String item2;
			String item3 = null;
			String item4 = null;
			for (int j=0; j <tables.size(); j++) {
				
		String item1=tables.get(j);
		
		if (report[j][0].compareTo("true")==0){
			 item2="NO";
			 item3="1NF";
		}
		
		else if (report[j][3]!=null){
			item2="NO";
			item3="2NF";
			item4=report[j][3];
			
		}
		else	if (report[j][4]!=null){
			item2="NO";
			item3="3NF";
			item4=report[j][4];
		}
		else{
			item2="YES";	
			
		}
		
		line= " "+item1 +"       "+item2+"      "+item3+"      "+ item4;
		try {
			bw.write(line);
			bw.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			}
			try {
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		System.out.println("Summary Reports Generated");
			//---------------------------
			
	}

}
