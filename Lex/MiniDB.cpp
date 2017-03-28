#include <iostream>
#include <stdlib.h>
#include <string>
#include <fstream>
#include <string.h>
#include <vector>
#include "myvals.h"
using namespace std;

extern int yylex();
extern int yylineno;
extern char* yytext;
extern int yymore;
extern int input;

void split(const string& s, char c,vector<string>& v) {
   string::size_type i = 0;
   string::size_type j = s.find(c);

   while (j != string::npos) {
      v.push_back(s.substr(i, j-i));
      i = ++j;
      j = s.find(c, j);

      if (j == string::npos)
         v.push_back(s.substr(i, s.length()));
   }
}





string getColumnString(string tablename){

 string x;
 ifstream f("catalog.txt",ios::in | ios::binary);
 int i=1,flag=0;
 
 //first find the table in catalog
 while(getline(f,x)){
 	cout<<x<<std::endl;
 	size_t found = x.find(tablename.c_str());
 	if (found!=string::npos){
 		cout << "tablename found in "<<i<<"th iteration at : " << found << '\n';
 		flag=1;
		break;
 	}
 	else{
 		getline(f,x);
 		getline(f,x);
 		getline(f,x);
 		i++;
 	}
 }
 
 //if table is found, retrun the line containing colmn information
 if(flag==1){
 	getline(f,x);
 	return x;
 }

}

bool* getColumnIndexes(string tablename, string columns){

string x;
 ifstream f("catalog.txt",ios::in | ios::binary);
 int i=1,flag=0;
 
 //first find the table in catalog
 while(getline(f,x)){
 	cout<<x<<std::endl;
 	size_t found = x.find(tablename.c_str());
 	if (found!=string::npos){
 		cout << "tablename found in "<<i<<"th iteration at : " << found << '\n';
 		flag=1;
		break;
 	}
 	else{
 		getline(f,x);
 		getline(f,x);
 		getline(f,x);
 		i++;
 	}
 }
 
 //if table is found, find the indexes of columns that need to be accessed
 if(flag==1){
 	getline(f,x);
 	cout<<x<<std::endl;
	
	char* pch = strtok((char*)x.c_str(),"=:, ");
	int j=0, k=0;
	string mcol[50];
	
	string col;
	while (pch != NULL)
  	{
    		//add only the column names to the array mcol
    		if(j%2==1){
    			col=pch;
    			mcol[k]=col;
    			k+=1;
    		}
    		pch = strtok (NULL, "=:, ");
    		j++;
  	}
  	//cout<<"Col array length "<<k<<"\n";
  	
  	bool* idx = new bool[k];
  	//mark columns that need to be selected as true, and rest as false
  	for(int l=0;l<k;l++){
  		//cout<<l<<" "<<mcol[l]<<"\n";
		if(columns.find(mcol[l]) != string::npos){
			cout<<mcol[l]<<" is in Columns\n";
			idx[l]=true;
		}
		else{
			idx[l]=false;
		}
    	}
    	//for(int m=0;m<k;m++)
    	//	cout<<idx[m]<<"\n";
  	return idx;
  	
	
 }
 f.close();

}


void generateOutput(string source, string columns, string destination, int display_flag, int where_flag, string wCol, string wVal){

	string t_source=source+".tbl";
	string t_destination=destination+".tbl";
	
	switch(display_flag){
	
		case 0:{
			//in case=0, we display the output on console
			
			ifstream f(t_source.c_str(),std::ios::in | std::ios::binary);
			
			string temp_cols = getColumnString(source);
			int wIdx=0;
			if(where_flag == 1){
				vector<string> vec1;
				split(temp_cols,'=',vec1);
				
				vector<string> vec2;
				split(vec1[1],',',vec2);
				
				string temp_wCol = wCol+=":";
				
				//cout<<"Displaying tokenized columns\n";
				for(int i=0; i < vec2.size(); i++){
					//cout<<vec2[i];
					if(vec2[i].find(temp_wCol) != string::npos){
						cout<<"Found the where column at index : "<<i<<"\n";
						wIdx=i;
						break;
					}
				}				
			}

						
			
			
			if(columns=="*"){	
				string x;
				while( getline(f,x)){	
					vector<string> v;
						
					//cout<<x<<"\n";
					if(where_flag !=1)
						cout<<x<<"\n";
					else if(where_flag==1){
						
						split(x,' ',v);
						cout<<"flag=1 and v[wIdx]="<<v[wIdx]<<" and wVal="<<wVal<<"\n";
						if(v[wIdx]==wVal){
							
							cout<<x<<"\n";
						}
					
					}	
				}
			}
			else{
				//logic for selecting specific columns here
				//get the indexes of all columns and those to be chosen are marked true
				bool* idx = getColumnIndexes(source,columns);
				
				//now read each line of source file.
				//then for each line, output the word of index that is marked true to the destination file
				//add a \n after each iteration
				string x;
				while(getline(f,x)){
					
					vector<string> v;
					split(x, ' ', v);
					if(where_flag !=1){
						for (int i = 0; i < v.size(); ++i) {
							if(idx[i]==true){
      								cout << v[i] << ' ';
      								
      							}
   						}
   						cout<<"\n";
   					}
   					else if(where_flag==1){
   						
   						cout<<"flag=1 and v[wIdx]="<<v[wIdx]<<"\n";
   						if(v[wIdx] == wVal){
   							for (int i = 0; i < v.size(); ++i) {
								if(idx[i]==true){
      									cout << v[i] << ' ';
      								}
   							}
   							cout<<"\n";
   						}
   						
   					}
   					
				}	
				
				
				
				
				
			}
			f.close();
			break;
		}
		case 1:{
			//in case 1, we save the output in a temporary file
			
			
			//TODO: since we are creating temp tables here, we should check if the table does 
			//not exist already, or we might overwrite an existing table.
			
			cout<<"Generated temporary file : "<<t_destination<<"\n";
			
			//when creating temp tables, add its entry to catalog with tablename_temp 
			//so while retrieving temp table data we can refer to it
			ofstream t_fout;
			t_fout.open("catalog.txt",std::ios_base::app);
			t_fout<<"tablename_temp="<<destination<<"\n";
			
			//get column names for temporary table entry in catalog
			
			//string to diffrentiate * and col1,col2,...
			string temp_cols=getColumnString(source);
			cout<<"Columns got for source :"<<temp_cols<<"\n";			

			//check if where flag is set or not. if yes, determine the index of that column
			//so while reading the data we can compare the value at that index
			int wIdx=0;
			if(where_flag == 1){
				vector<string> vec1;
				split(temp_cols,'=',vec1);
				
				vector<string> vec2;
				split(vec1[1],',',vec2);
				
				string temp_wCol = wCol+=":";
				
				for(int i=0; i < vec2.size(); i++){
					
					if(vec2[i].find(temp_wCol) != string::npos){
						cout<<"Found the where column at index : "<<i<<"\n";
						wIdx=i;
						break;
					}
				}				
			}



			if(columns!="*"){
				temp_cols=columns;
				cout<<"COLOLOL "<<columns<<"\n";
				vector<string> vec;
				split(temp_cols,',',vec);
				cout<<"Vec size "<<vec.size()<<"\n";
				
				t_fout<<"columns=";
				
				//in case where temp table is going to have only one column
				//the split function returns 0 elements and the for loop below does not 
				//insert the entry of that column in catalog since the loop condition does not hold true
				//, in that case we check and manually enter the column value
				if(vec.size()==0){
					t_fout<<temp_cols<<":type ,";
				}
				
				
				//add column names to catalog
				for(int i=0; i<vec.size(); i++){
					cout<<"Vector "<<vec[i]<<"\n";
					t_fout<<vec[i]<<":type ,";
				
				}
			}
			else if(columns=="*"){
				//cout<<"in if";
				//temp_cols=getColumnString(source);
				t_fout<<temp_cols;
			}
			
			
			//vec.erase(vec.begin(),vec.end());
			t_fout<<std::endl;
			t_fout<<"primary key="<<std::endl;
			t_fout<<"recordsize="<<std::endl;
			t_fout.close();
			cout<<"Entry for temp table "<<destination<<" added to catalog"<<std::endl;
			
			
			//file to read data from
			ifstream f(t_source.c_str(),std::ios::in | std::ios::binary);
			
			
			//temp file to write data into
			ofstream fout;
			fout.open(t_destination.c_str(),std::ios_base::app);
			
			if(columns=="*")
				cout<<"Columns to be select are "<<columns<<"\n";	
			
			
			if(columns=="*"){
				cout<<"inside if";	
				string x;
				while( getline(f,x)){
				
					vector<string> v;
						
					if(where_flag !=1)
						fout<<x<<"\n";
					else if(where_flag==1){
						
						split(x,' ',v);
						if(v[wIdx]==wVal){
							fout<<x<<"\n";
						}
					
					}	
				}
			}
			else{
				cout<<"in else";
				//logic for selecting specific columns here
				
				//get the indexes of all columns and those to be chosen are marked true
				bool* idx = getColumnIndexes(source,columns);
				
				//now read each line of source file.
				//then for each line, output the word of index that is marked true to the destination file
				//add a \n after each iteration
				string x;
				while(getline(f,x)){
					vector<string> v;
					split(x, ' ', v);
					
					if(where_flag !=1){
						for (int i = 0; i < v.size(); ++i) {
							if(idx[i]==true){
      								fout<<v[i]<<' ';
      								cout<<v[i]<<' ';
      							}
   						}
   						fout<<"\n";
   						cout<<"\n";
   						
   					}
   					else if(where_flag==1){
   						
   						if(v[wIdx] == wVal){
   							for (int i = 0; i < v.size()-1; ++i) {
								if(idx[i]==true){
									fout<<v[i]<<' ';
      								}
   							}
   							
							fout<<"\n";
   						}

   					}

				}
				cout<<"print after while ends\n";				
			}
			f.close();
			fout.close();
			//cout<<"print before break";
			break;
		}

	}
}



string select(){
	int mtoken = yylex();

	//detect the columns to be selected and store in string "columns"
	string columns;
	columns = yytext;
	while(mtoken=yylex() != FROM){
		yymore;		
		columns+=yytext;
	}
//	cout<<"COLUMNS ARE\t"<<columns<<"\n";
	
	
	//get the tablename now
	string src_tablename;
	mtoken=yylex();

	//after "FROM", check if the query is nested or not
	if( mtoken == OPENBRACE){
		
		mtoken = yylex();
		string alias = select();
		
		
		//call the generate output function here and generate result table for current columns.
		//then proceed with producing alias for next outer query
		
		
		//the next meaningful token can be:
		//1. IDENTIFIER : in case of - select... (select * from T1 ) T2;
		//2. SEMICOLON : in case of - select * from t1;
		//3. WHERE:	in case of - select * from t1 WHERE <some_condition>
		
		int where_flag=0;			
		string where_column, where_value;
		mtoken=yylex();			
		if(mtoken == WHERE){
			cout<<"Where detected when coming outwards\n";
			where_flag=1;
			mtoken=yylex(); //column name
			where_column=yytext;
			mtoken=yylex(); //=
			mtoken=yylex(); //value
			where_value=yytext;
			mtoken=yylex(); // ) or ;
		}		
		
		while((mtoken != IDENTIFIER) && (mtoken!=SEMICOLON)){
			cout<<"I went here\n"<<yytext;
			mtoken=yylex();
		}
		if(mtoken==IDENTIFIER){
			cout<<"Source table : "<<alias<<" Selected Columns : "<<columns<<" Destination table : "<<yytext<<"\n";

			
			
			generateOutput(alias, columns, yytext, 1, where_flag, where_column, where_value);
			
			return yytext;
		}
		else if(mtoken==SEMICOLON){
			cout<<"Source table : "<<alias<<" Selected Columns : "<<columns<<" Destination table : CONSOLE SCREEN\n";
			//actually here we should not return anything since we are on the outermost part of the query and returning 
			//anything doesn't makes sense, but just for the sake of sanity we do it.
			
			generateOutput(alias, columns, "null", 0, where_flag, where_column, where_value);
			return alias;
		}
	}
	else if(mtoken == IDENTIFIER){
		//this will only be called for innermost query
		src_tablename=yytext;
		
		//the next meaningful token can be:
		//1. IDENTIFIER : in case of - select... (select * from T1 ) T2;
		//2. SEMICOLON : in case of - select * from t1;
		//3. WHERE:	in case of - select * from t1 WHERE <some_condition>
		mtoken=yylex();
		int where_flag=0;
		string where_column,where_value;
		if(mtoken==WHERE){
			cout<<"where encountered\n";
			where_flag=1;
			mtoken=yylex();	//column name
			where_column=yytext;
			mtoken=yylex();	//=
			mtoken=yylex();	//value
			where_value=yytext;
			mtoken=yylex();	// ) or ;
		}
		
		while((mtoken != IDENTIFIER) && (mtoken!=SEMICOLON)){
			mtoken=yylex();
		}
		
		if(mtoken==IDENTIFIER){
			cout<<"Source table : "<<src_tablename<<" Selected Columns : "<<columns<<" Destination table : "<<yytext<<"\n";
			
			//call generate output function here for innermost query as
			generateOutput(src_tablename, columns, yytext, 1,where_flag, where_column, where_value);
			
			return yytext;
		}
		else if(mtoken==SEMICOLON){
			//this will be called only when there are no nested queries
			cout<<"Select columns : "<<columns<<" from table : "<<src_tablename<<"\n";
			
			//call the generate output function from here for single select queries as 
			generateOutput(src_tablename, columns, "null", 0, where_flag, where_column, where_value);
			return src_tablename;
		}
		
	}	
}

		

void create(){
	string tablename;
	int token = yylex();
	if(token != TABLE){
		cout<<"Syntax Error "<<yytext<<"\n";
		return; 	
	}
	token = yylex();

	if(token == IDENTIFIER){
		tablename = yytext;
		cout<<"Table "<< tablename <<" will be created.\n";
		yylex();	
	}
	else{
		cout<<"Invalid tablename.\n";	
	}
	

	int nToken, vToken, numOfCol=0;
	
	ofstream fout;
	fout.open("catalog.txt",std::ios_base::app);
	fout<<"tablename="<<tablename<<std::endl;
	fout<<"columns= ";
	
	int recSize=0;
	//detecting column and datatype	
	while(nToken != CLOSEBRACE){
		nToken = yylex();
		cout<<"Column : "<<yytext;
		fout<<yytext<<":";
		vToken = yylex();
		
		cout<<"\tType : "<<yytext<<"\n";
		string datatype = yytext;
		//cout<<datatype<<"\n";
		fout<<datatype;

		if(vToken == CHAR){
			//cout<<"Char detected";
			
			
			vToken=yylex();
			cout<<vToken<<" OPEN\n";
			fout<<yytext;
						
			vToken = yylex();
			cout<<vToken<<" INTNUM\n";
			fout<<yytext;
			recSize+=atoi(yytext);
			
			vToken=yylex();
			cout<<vToken<<" CLOSE\n";		
			fout<<yytext;
		}
		else{
			recSize+=4;
		}
				
		//skip comma		
		nToken=yylex();
		if(nToken == COMMA){
			fout<<", ";		
		}
		numOfCol++;
	}
	cout<<"Record Size "<<recSize<<"\n";
	fout<<std::endl;
	
	nToken = yylex();
	vToken = yylex();
	if(nToken==PRIMARY && vToken==KEY){
		cout<<"PRIMARY KEY DETECTED\nNum of Columns = "<<numOfCol<<"\n";		
		nToken = yylex();	
	}
	
	nToken = yylex();
	if(nToken == IDENTIFIER){
		cout<<"Primary Key is "<<yytext<<"\n";
		fout<<"primary key="<<yytext<<std::endl;	
	}
	fout<<"recordsize="<<recSize<<"\n";
	fout.close();
	
	tablename+=".tbl";
	ofstream mfout (tablename.c_str(), std::ios_base::binary|std::ios_base::app);
	mfout.close();
	
}

void insert(){
	
	int nToken = yylex();
	//cout<<nToken<<"\n";
	string tablename;

	if ((nToken = yylex())== IDENTIFIER){	
		tablename=yytext;
		tablename+=".tbl";	
		cout<<"Tablename is "<<tablename<<"\n";
	}

	//open .tbl file to write the record
	ofstream file(tablename.c_str(), std::ios_base::out | std::ios_base::app | std::ios_base::binary);	

	//skip keyword VALUES
	nToken = yylex();	//ntoken gets value = VALUES
	//cout<<nToken<<"\n";
	//skip open brace
	nToken = yylex();
	//cout<<nToken<<"\n";
	
	string row;
	nToken=yylex();
	while(nToken != CLOSEBRACE){
	
		switch(nToken){
			case QUOTE:
				nToken=yylex();
				if(nToken==IDENTIFIER){
					cout<<"Value : "<<yytext<<"\n";
					row.append(yytext);
					row.append(" ");
				}
				//skip closing '
				if(nToken=yylex() != QUOTE){
					cout<<"Error: Non Terminated String.\n";
					return;
				}
				nToken=yylex();
				if(nToken == COMMA){
					nToken=yylex();
				}
				else{cout<<"Q/C "<<yytext<<"\n";}
				break;
			
			case INTNUM:
				cout<<"Value : "<<yytext<<"\n";
				row.append(yytext);
				row.append(" ");
				nToken=yylex();
				if(nToken == COMMA){
					nToken=yylex();
				}
				break;
			
				
		
		}
	
	}
	
	
	
	
	
	row+="\n";
	cout<<"Row is : "<<row<<" Size: "<<row.size()<<"\n";
	file.write(row.c_str(), row.size());

	file.close();

	ifstream f(tablename.c_str(),std::ios::in);
	string x;
	cout<<"\n";
	while( getline(f,x)){
		cout<<x<<"\n";	
	}
	f.close();
		
}

int main(){
	
	int ntoken = yylex();

	switch(ntoken){
		case CREATE:
			cout<<"Going to create a table.\n";
			create();	
			break;
		case SELECT:{
			cout<<"Going to select from a table.\n";
			string f_table=select();
			cout<<"Final select will be done from table "<<f_table<<"\n";
			break;
			}		
		case INSERT:
			cout<<"Going to insert into a table.\n";
			insert();
			break;
		default:
			cout<<"Unknown query.\n";
			break;
	}
	
}
