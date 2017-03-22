#include <iostream>
#include <string>
#include <fstream>
#include "myvals.h"
using namespace std;

extern int yylex();
extern int yylineno;
extern char* yytext;
extern int yymore;
extern int input;

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

	//check if the query is nested or not
	if( mtoken == OPENBRACE){
		
		mtoken = yylex();
		string alias = select();
		
		
		//call the generate output function here and generate result table for current columns.
		//then proceed with producing alias for next outer query
	
		while((mtoken != IDENTIFIER) && (mtoken!=SEMICOLON)){
			mtoken=yylex();
		}
		if(mtoken==IDENTIFIER){
			cout<<"Source table : "<<alias<<" Selected Columns : "<<columns<<" Destination table : "<<yytext<<"\n";
			
			//generateOutput(alias, columns, yytext, flag=1)
			
			return yytext;
		}
		if(mtoken==SEMICOLON){
			cout<<"Source table : "<<alias<<" Selected Columns : "<<columns<<" Destination table : CONSOLE SCREEN\n";
			//actually here we should not return anything since we are on the outermost part of the query and returning 
			//anything doesn't makes sense, but just for the sake of sanity we do it.
			
			//generateOutput(alias, columns, NULL, flag=0)
			return alias;
		}
	}
	else if(mtoken == IDENTIFIER){
		//this will only be called for innermost query
		src_tablename=yytext;
		mtoken=yylex();
		while((mtoken != IDENTIFIER) && (mtoken!=SEMICOLON)){
			mtoken=yylex();
		}
		
		if(mtoken==IDENTIFIER){
			cout<<"Source table : "<<src_tablename<<" Selected Columns : "<<columns<<" Destination table : "<<yytext<<"\n";
			
			//call generate output function here for innermost query as
			//generateOutput(src_tablename, columns, yytext, flag=1)
			
			return yytext;
		}
		else if(mtoken==SEMICOLON){
			//this will be called only when there are no nested queries
			cout<<"Select columns : "<<columns<<" from table : "<<src_tablename<<"\n";
			
			//call the generate output function from here for single select queries as 
			//generateOutput(src_tablename, columns, NULL, flag=0)
			
			return src_tablename;
		}
		
	}	
}

void generateOutput(string source, string columns, string destination, int flag){

	



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
	//detecting column and datatype	
	while(nToken != CLOSEBRACE){
		nToken = yylex();
		cout<<"Column : "<<yytext;
		fout<<yytext<<":";
		vToken = yylex();
		
		cout<<"\tType : "<<yytext<<"\n";
		string datatype = yytext;
		cout<<datatype<<"\n";

		if(vToken == CHAR){
			//cout<<"Char detected";
			fout<<datatype;
			yylex();
			fout<<yytext;			
			vToken = yylex();
			fout<<yytext;
			cout<<vToken;
			yylex();		
			fout<<yytext;
		}
		else{
			fout<<datatype;		
		}
				
		//skip comma		
		nToken=yylex();
		if(nToken == COMMA){
			fout<<", ";		
		}
		numOfCol++;
	}
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
	fout.close();
	
	tablename+=".tbl";
	ofstream mfout (tablename.c_str());
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
	
	string temp="";
	while( (nToken = yylex()) != SEMICOLON){
		
		switch(nToken){
			
			case INTNUM:
				temp += yytext;
				temp+=" ";
				//file.write(temp.c_str(),sizeof(temp));
				//cout<<temp<<" Inserted\n";
				break;
			case QUOTE:
				nToken = yylex();
				temp += yytext;
				temp+=" ";
				if(nToken == IDENTIFIER){
					//file.write(temp.c_str(),sizeof(temp));				
				}
				//cout<<temp<<"Inserted\n";
				yylex();
				break;
		}	
		yylex();
	}
	temp+="\n";
	file.write(temp.c_str(), sizeof(temp));

	file.close();

	ifstream f(tablename.c_str(),std::ios::in | std::ios::binary);
	string x;
	while( getline(f,x)){
		cout<<x<<"\n";	
	}
		
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
