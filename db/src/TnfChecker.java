import java.sql.Connection;


public class TnfChecker {
	
boolean vflag=true;
String main,target;
	
	public void tnfvalidation( Connection connection,String db,String table,String mainf,String tempf){
	Depcheck a;
	a= new Depcheck();
	
	//a.check("first","info","city","state");

	//System.out.println(fields[2][i]);
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
		
		 if (vflag==true){
		a.check(connection,db,table,tempf,mainf);
		if (a.flag==true){
			
		vflag=false;
		main=tempf;
		target=mainf;
			}
		else{
			vflag=true;
		}
		}
			}
		
} catch (Exception e){
		
	}	

}
}
