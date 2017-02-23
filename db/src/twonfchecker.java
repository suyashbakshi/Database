import java.sql.Connection;


public class twonfchecker {
	boolean vflag=true;
	String main,target;
		
		public void twonfvalidation( Connection connection,String db,String table,String mainf,String tempf){
		Depcheck2 a;
		a= new Depcheck2();
		
		
		try{
			if ((mainf.length()!=0) && (tempf.length()!=0)){
				a.check(connection,db,table,mainf,tempf);
			if (a.flag==true){
				
			vflag=false;
			main=mainf;
			target=tempf;
				}
			else{
				vflag=true;
			}
			
							}
			
	} catch (Exception e){
			
		}	

	}
}
