#include <iostream>
#include <stdlib.h>
#include <string>
#include <stdio.h>
#include <fstream>
#include <string.h>
#include <vector>
#include "myvals.h"
using namespace std;

//TODO:
//1. Checking datatype and primary key while insertion.	->	done
//2. Checking if table already exists while creating.	->	done
//3. datatype checking in insert with selects		->	done
//4. Where clause in insertWithSelect			-> 	done
//5. column name issue for joins
//6. nested selects in insert.				->	done
//7. drop table.					->	done

extern int yylex();
extern int yylineno;
extern char* yytext;
extern int yymore;
extern int input;
extern FILE *yyin;

string matched;
string select();
void split(const string&, char c,vector<string>&);



/*this area for catalog_list functions*/
/*-----------------------------------------------------------------------------------------*/

struct list
{
    string tablename;
    string columns;
    string pk;
    int recordSize;
    int totalsize;
    int records;
    int is_temp;
    struct list *next;
};

struct list *newList(string tablename,string columns,string pk,int recSize,int totalsize,int records,int is_temp)
{
    struct list *temp =  new list;
    temp->tablename = tablename;
    temp->columns = columns;
    temp->pk = pk;
    temp->recordSize= recSize;
    temp->totalsize= totalsize;
    temp->records=records;
    temp->is_temp=is_temp;
    temp->next=NULL;
    return temp;
}

struct list *start=NULL;

void append(string tablename,string columns,string pk,int recSize,int totalsize,int records,int is_temp)
{
    	struct list *p=start , *n;
    	n = newList(tablename,columns,pk,recSize,totalsize,records,is_temp);
	n->next = NULL;
	if ( start == NULL )
	{
		start = n;
		return ;
	}
	while( p -> next != NULL)
	{
	     p=p->next ;
	}
	p->next = n ;
}

void update(string tablename)
{
	  struct list *p = start ;
	  if ( p == NULL)
	  {
	  	cout<<"\nEmpty link node ";
		return;
	  }
	  while ( p != NULL )
	  {
	  	if ( tablename == p->tablename )
	 	{
	 		p->records++;
	 		p->totalsize= p->records*p->recordSize;
			return;
		}
		p = p->next ;
	  }
	  cout<<"\nElement not found ";
}

bool tableExists( string table)
{
	struct list *p = start;
	 if ( p == NULL)
	 {
	 	cout<<"Empty list.\n";
		return NULL;
	 }
	 while ( p != NULL )
	 {	
	 	if(p->tablename==table){
	 		return true;
	 	}
	        p=p->next ;
	 }
	 return false;
}

void dropTable(string tablename){

	struct list *p = start;
	
	if(p==NULL){
		cout<<"No tables in catalog.\n";
		return;
	}
	if(p->tablename == tablename){
		start = p->next;
		return;
	}
	while(p->next->tablename !=tablename){
		p=p->next;
	}
	p->next = p->next->next;


}

void showTable( struct list *p, string table)
{
	 if ( p == NULL)
	 {
	 	cout<<"Empty list.\n";
		return;
	 }
	 while ( p != NULL )
	 {	
	 	if(p->tablename==table){
	 		cout<<"Tablename: "<<p->tablename<<"\n";
	 		cout<<"Columns: "  <<p->columns<<"\n";
	 		cout<<"Primary Key: "<<p->pk<<"\n";
	 		return;
	 	}
	        p=p->next ;
	 }
}

void showTables( struct list *p)
{
	 if ( p == NULL)
	 {
	 	cout<<"Empty list.\n";
		return;
	 }
	 while ( p != NULL )
	 {	
	 	cout<<"Tablename: "<<p->tablename<<"\n";
	 	cout<<"Columns: "  <<p->columns<<"\n";
	 	cout<<"Primary Key: "<<p->pk<<"\n";
	 	p=p->next ;
	 }
}

string getColumnString(string tablename)
{
	 struct list *p = start ;
	 if ( p == NULL)
	 {
		cout<<"\nEmpty link list ";
		return NULL;
	 }
	 while ( p != NULL )
	 {
		if (tablename == p->tablename )
		{
			 return p->columns;
		}
		p = p->next ;
	 }
	 cout<<"\nTable "<<tablename<<" not found\n";
}

string getPrimaryKey(string tablename)
{
	 struct list *p = start ;
	 if ( p == NULL)
	 {
		cout<<"\nEmpty link list ";
		return NULL;
	 }
	 while ( p != NULL )
	 {
		if (tablename == p->tablename )
		{
			 return p->pk;
		}
		p = p->next ;
	 }
	 cout<<"\nTable "<<tablename<<" not found\n";
}

void dump()
{
	ofstream fout;
	fout.open("catalog.txt",ios::out);

	struct list *p=start;
	if ( p == NULL)
	{
		cout<<"Empty list.\n";
		return;
	}
	while ( p != NULL )
	{	
		if(p->is_temp ==0){
			fout<<"tablename="<<p->tablename<<"\n";
			fout<<"columns="  <<p->columns<<"\n";
			fout<<"primary key="<<p->pk<<"\n";
			fout<<"recordsize="<<p->recordSize<<"\n";
			fout<<"totalsize="<<p->totalsize<<"\n";
			fout<<"records="<<p->records<<"\n";
			p=p->next ;
		}
		else{
			p=p->next;
		}
	}
	fout.close();
}

void load(){

	string x;
	ifstream f("catalog.txt",ios::in);
	
	while(getline(f,x)){
	
		vector<string> tablename;
		split(x,'=',tablename);
		
		getline(f,x);
		
		vector<string> columns;
		split(x,'=',columns);
	
		getline(f,x);
		
		vector<string> pk;
		split(x,'=',pk);
		
		getline(f,x);
		
		vector<string> recordsize;
		split(x,'=',recordsize);
		
		getline(f,x);
		
		vector<string> totalsize;
		split(x,'=',totalsize);
		
		getline(f,x);
		
		vector<string> records;
		split(x,'=',records);
		
		append(tablename[1],columns[1],pk[1],atoi(recordsize[1].c_str()),atoi(totalsize[1].c_str()),atoi(records[1].c_str()),0);
	}

}



/*catalog function area ends*/
/*-----------------------------------------------------------------------------------------*/

/*tree function area starts*/
/*-----------------------------------------------------------------------------------------*/

struct node
{
    string key;
    string val;
    struct node *left, *right;
};

struct node *newNode(string key,string val)
{
    struct node *temp =  new node;
    temp->key = key;
    temp->val = val;
    temp->left = temp->right = NULL;
    return temp;
}

void inorder_full(struct node *root)
{
    if (root != NULL)
    {
        inorder_full(root->left);
        cout<<root->key<<"\t"<<root->val<<std::endl;
        matched.append(root->val);
        matched.append("/");
        inorder_full(root->right);
    }
}

void inorder(struct node *root, string mKey)
{
    if (root != NULL)
    {
        inorder(root->left, mKey);
        if(mKey == root->key){
        	cout<<root->key<<"\t"<<root->val<<std::endl;
        	matched.append(root->val);
        	matched.append("/");
        }
        else if(root->key > mKey){
        	return;
        }
        inorder(root->right, mKey);
    }
}
  
struct node* insert(struct node* node, string key, string val)
{
    if (node == NULL) return newNode(key,val);
 
 
    if (key <= node->key)
        node->left  = insert(node->left, key, val);
    else if (key > node->key)
        node->right = insert(node->right, key, val);   
 
 return node;
}


/*tree function area ends*/
/*-----------------------------------------------------------------------------------------*/


/*function to split a string and return it into a vector*/
/*-----------------------------------------------------------------------------------------*/

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
   if(v.size()==0)
   	v.push_back(s);
   	
}

/*-----------------------------------------------------------------------------------------*/


/*string getPrimaryKey(string tablename){

 string x;
 ifstream f("catalog.txt",ios::in | ios::binary);
 int i=1,flag=0;
 
 //first find the table in catalog
 while(getline(f,x)){
 	//cout<<x<<std::endl;
 	size_t found = x.find(tablename.c_str());
 	if (found!=string::npos){
 		//cout << "tablename found in "<<i<<"th iteration at : " << found << '\n';
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
 	getline(f,x);
 	vector<string> v1;
 	split(x,'=',v1);
 	return v1[1];
 }
	

}*/


/*function to check if the new data violates primary key constraint*/
/*-----------------------------------------------------------------------------------------*/

bool checkPKViolation(string tablename,string row, int pkIdx){

	vector<string> pk;
	split(row,' ',pk);

	string x;
	ifstream f((tablename+".tbl").c_str(),ios::in | ios::binary);
	while(getline(f,x)){
	
		vector<string> mrow;
		split(x,' ',mrow);
		
		if(mrow[pkIdx]==pk[pkIdx]){
			return false;
		}
		
	}
	return true; 


}

/*-----------------------------------------------------------------------------------------*/

/*string getColumnString(string tablename){

 string x;
 ifstream f("catalog.txt",ios::in | ios::binary);
 int i=1,flag=0;
 
 //first find the table in catalog
 while(getline(f,x)){
 	//cout<<x<<std::endl;
 	size_t found = x.find(tablename.c_str());
 	if (found!=string::npos){
 		//cout << "tablename found in "<<i<<"th iteration at : " << found << '\n';
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

}*/


/*Helper function that returns indexes of columns that need to be accessed from the specified table*/
/*-----------------------------------------------------------------------------------------*/

int* getColumnIntIndexes(string tablename, string columns){

	//cout<<"Table "<<tablename<<" Columns "<<columns;
 	
 	vector<string> vec;
 	split(columns,',',vec); //A C D G
 	
 	string x = getColumnString(tablename);
 	//split the columns: A:INT <> B:CHAR <> C:INT
 	vector<string> vec2;
 	split(x,',',vec2); 
 	
 	int* idx = new int[vec.size()];	
 	
 	
 	int flag;
 	for(int i=0; i<vec.size(); i++){
 		flag=0;
 		//cout<<"vec[0] is "<<vec[0]<<"\n";
 		for(int j=0; j<vec2.size(); j++){
 		 			
 			vector<string> vec3;
 			split(vec2[j],':',vec3);
 			//cout<<"vec3[0] is "<<vec3[0]<<"\n";
 			if(vec3[0] == vec[i]){
 				idx[i]=j;
 				flag=1;
 				//cout<<"\n"<<j<<" is in columns\n";
 				break;
 			}
 		}
 		if(flag==0){
 			//cout<<"Marking false for "<<vec3[0]<<" i="<<i<<"\n";
 			idx[i]=-1;
 		}
 	} 	
  	return idx;
  	
	
}

/*-----------------------------------------------------------------------------------------*/


/*bool* getColumnIndexes(string tablename, string columns){

cout<<"Table "<<tablename<<" Columns "<<columns;
string x;
 ifstream f("catalog.txt",ios::in | ios::binary);
 int i=1,flag=0;
 
 //first find the table in catalog
 while(getline(f,x)){
 	//cout<<x<<std::endl;
 	size_t found = x.find(tablename.c_str());
 	if (found!=string::npos){
 		//cout << "tablename found in "<<i<<"th iteration at : " << found << '\n';
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
 	
 	vector<string> vec;
 	split(columns,',',vec); //A C D G
 	
 	//split columns "=" A:INT...
 	vector<string> vec1;
 	split(x,'=',vec1);
 	//split the columns: A:INT <> B:CHAR <> C:INT
 	vector<string> vec2;
 	split(vec1[1],',',vec2); 
 	
 	bool* idx = new bool[vec2.size()];	
 	
 	
 	int flag;
 	for(int i=0; i<vec2.size(); i++){
 		flag=0;
 		vector<string> vec3;
 		split(vec2[i],':',vec3); //A
 		for(int j=0; j<vec.size(); j++){
 			if(vec3[0] == vec[j]){
 				idx[i]=true;
 				flag=1;
 				cout<<"\n"<<vec3[0]<<" is in columns\n";
 				break;
 			}
 		}
 		if(flag==0){
 			cout<<"Marking false for "<<vec3[0]<<" i="<<i<<"\n";
 			idx[i]=false;
 		}
 	}
 	
 	//cout<<x<<std::endl;
	
	/*char* pch = strtok((char*)x.c_str(),"=:, ");
	int j=0, k=0;
	string mcol[50];
	
	string col;
	while (pch != NULL)
  	{
    		//add only the column names to the array mcol
    		if(j%2==1){
    			cout<<col<<"\n";
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
			//cout<<mcol[l]<<" is in Columns\n";
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

}*/


/*the join() function only creates the join table using "NODE" tree and terminates. Selection of relevant columns is done later by parseJoin()*/
/*-----------------------------------------------------------------------------------------*/

void join(string table1, string table2, int cc_idx1, int cc_idx2){

   struct node *root = NULL;
   table1+=".tbl";
   table2+=".tbl";
   
   //read file 1 and generate tree
   ifstream f(table2.c_str(),std::ios::in | std::ios::binary);
   string x;
   getline(f,x);
   vector<string> vec;
   split(x,' ',vec);
   root = insert(root, vec[cc_idx2],x);
   while(getline(f,x)){
   	vector<string> vec1;
   	split(x,' ',vec1);
   	insert(root, vec1[cc_idx2], x);
   
   }
    
   
   //tree has been generated now

   //now open file2 for comparing and creating joins
   ifstream f1(table1.c_str(),std::ios::in | std::ios::binary);  
   string x1;
   
   ofstream fout((table1+"_join_"+table2+".tbl").c_str(),std::ios::out | std::ios::binary);
     
   //do inorder traversal for each row in file 2.
   while(getline(f1,x1)){
   	matched="";
  
   	vector<string> vec2;
   	split(x1,' ',vec2);
   	cout<<"Will now check common columns for : "<<x1<<std::endl;
   	
   	//inorder traversal gives all the rows with common key in file f and f1 
   	//here vec2[0] is the common column with file f.
   	inorder(root, vec2[cc_idx1]);
  
   	vector<string> vec3;
   	split(matched,'/',vec3);
   	
   	if(vec3.size()>0){
   		cout<<"Matched rows are: "<<std::endl;
   		for(int i=0;i<vec3.size()-1;i++){
   			fout<<x1<<" "<<vec3[i]<<std::endl;
   			cout<<vec3[i]<<std::endl;
   		}
   	}
   	cout<<matched<<"\n";
   }

}

/*-----------------------------------------------------------------------------------------*/


/*parseJoin() extracts compare column information for generating the join and calls join() function to create the join table.*/
/*it then uses this joint table to select relevant columns(passed as 3rd param)*/
/*-----------------------------------------------------------------------------------------*/

void parseJoin(string table1, string table2, string columns){
	
	
	/*if token after join is a select statement, we call select function which generates output of select query in the alias table given.*/
	/*-------------------------------------------------------*/
	int mtoken = yylex();		
	if(mtoken==SELECT){
		cout<<"Select detected\n";
		
		string alias=select();
		
		table2=alias;
		
		mtoken=yylex();
	}
	/*-------------------------------------------------------*/
	
	
	/*first of all we make a catalog entry for the join table*/
	/*-------------------------------------------------------*/
	string columnT1 = getColumnString(table1);
	string columnT2 = getColumnString(table2);
	
	vector<string> v1;
	split(columnT1,',',v1);
	
	vector<string> v2;
	split(columnT2,',',v2);
	
	string join_tb_name = table1+"_join_"+table2;
	string append_columns="";
	
	for(int i=0;i<v1.size(); i++){
		append_columns.append(table1+"."+v1[i]+",");
	}
	
	for(int i=0;i<v2.size(); i++){
		if(i==v2.size()-1)
			append_columns.append(table2+"."+v2[i]+",");
		else
			append_columns.append(table2+"."+v2[i]+",");
	}
	
	append(join_tb_name,append_columns,"",0,0,0,1);	
	
	/*-------------------------------------------------------*/

	
	
	if(mtoken==ON){
	
		
		
		mtoken=yylex();
		mtoken=yylex();
		mtoken=yylex();
		
		/*first we get indexes of common columns for comparison*/
		/*-------------------------------------------------------*/
			
		string t1_col = yytext;
		int *col_idx1 = getColumnIntIndexes(table1,t1_col);
		int cc_idx1 = col_idx1[0];
	
		mtoken=yylex();
		mtoken=yylex();
		mtoken=yylex();
		mtoken=yylex();
		
		
		string t2_col = yytext;
		int *col_idx2 = getColumnIntIndexes(table2,t2_col);
		int cc_idx2 = col_idx2[0];
				
		/*-------------------------------------------------------*/
		
		//TODO: get the next token here, and check if it is a where clause.
		//if yes, get the value and column, and modify loops below.
		
		join(table1,table2,cc_idx1,cc_idx2);
		
		cout<<"The columns to be selectd from join are "<<columns<<"\n";
		
		vector<string> size_vec;
		split(columns,',',size_vec);
		
		int size=size_vec.size();
		
		int* idx;
		
		string joinTable = table1+".tbl_join_"+table2+".tbl.tbl";
		ifstream f(joinTable.c_str(), ios::in | ios::binary);
		cout<<"Size is"<<size<<"\n";
		if(columns != "*"){
			idx=getColumnIntIndexes(table1+"_join_"+table2, columns);
			
			
			string x;
			while(getline(f,x)){
				vector<string> vec5;
				split(x,' ',vec5);
				cout<<"X is "<<x<<"\n";
				for(int i=0; i<size; i++){
						cout<<vec5[idx[i]]<<" ";
				}
				cout<<"\n";
			}
		}	
		else if(columns=="*"){
			cout<<"Select all\n";
			string x;
			while(getline(f,x)){
				cout<<x<<"\n";
			}
		}
	}
	
	/*this else if was meant for cartesian product in case the query does not have an "ON" clause*/
	/*else if(mtoken==SEMICOLON){
		cout<<"Semicolon detected\n";
		
		ifstream f1((table1+".tbl").c_str(), ios::in);
		ifstream f2((table2+".tbl").c_str(), ios::in);
		ofstream fout((table1+".tbl_join_"+table2+".tbl.tbl").c_str(),ios::out | ios::binary);
		
		string x1;
		while(getline(f1,x1)){
			string x2;
			while(getline(f2,x2)){
				cout<<x1<<" "<<x2<<"\n";
			}			
		}
		f1.close();
		f2.close();
		fout.close();		
	}*/	
}

/*-----------------------------------------------------------------------------------------*/


/*generateOutput processes the parsed select statements,and provides output at console or in a temporary file
depending on the query*/		
/*-----------------------------------------------------------------------------------------*/

void generateOutput(string source, string columns, string destination, int display_flag, int where_flag, string wCol, string wVal){

	string t_source=source+".tbl";
	string t_destination=destination+".tbl";
	
	switch(display_flag){
	
		case 0:{
			//in case=0, we display the output on console
			
			ifstream f(t_source.c_str(),std::ios::in | std::ios::binary);
			int wIdx;
			if(where_flag == 1){
			
				/*find the index of where_column*/
				int *where_idx = getColumnIntIndexes(source,wCol);
				wIdx= where_idx[0];
							
			}

						
			
			
			if(columns=="*"){
			
				//logic to display column names on top:
				string t_cols = getColumnString(source);
				vector<string> v_col1;
				split(t_cols,',',v_col1);
	
				for(int i=0;i<v_col1.size();i++){
						vector<string> v_col2;
						split(v_col1[i],':',v_col2);					
						cout<<v_col2[0]<<" | ";
				}
				cout<<"\n-------\n";
				
				
				string x;
				while( getline(f,x)){
					
					vector<string> v;
					
					if(where_flag !=1)
						cout<<x<<"\n";
					
					else if(where_flag==1){
						
						split(x,' ',v);
						if(v[wIdx]==wVal){
							
							cout<<x<<"\n";
						}
					
					}	
				}
			}
			else{
				//logic for selecting specific columns here
				//get the indexes of all columns and those to be chosen are marked true
	
				vector<string> temp_vector;
				split(columns,',',temp_vector);
				int size=temp_vector.size();
				
				int *idx = getColumnIntIndexes(source,columns);			
	
				
				//logic to display column names on top:
				string t_cols = getColumnString(source);
				vector<string> v_col1;
				split(t_cols,',',v_col1);
				
				for(int i=0;i<size;i++){
						vector<string> v_col2;
						split(v_col1[idx[i]],':',v_col2);					
						cout<<v_col2[0]<<" | ";
				}
				cout<<"\n-------\n";
				
				
				//now read each line of source file.
				//then for each line, output the word of index that is marked true to the destination file
				//add a \n after each iteration
				string x;
				while(getline(f,x)){
					
					vector<string> v;
					split(x, ' ', v);
					if(where_flag !=1){
						for (int i = 0; i < size; ++i) {
      								cout << v[idx[i]] << ' ';
   						}
   						cout<<"\n";
   					}
   					else if(where_flag==1){
   						
   						//cout<<"flag=1 and v[wIdx]="<<v[wIdx]<<"\n";
   						if(v[wIdx] == wVal){
   							for (int i = 0; i < size; ++i) {
      									cout << v[idx[i]] << ' ';
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
			if(tableExists(destination)){
				cout<<"Error: Alias name "<<destination<<" already exists. Please use a different alias in the query.\n";
				return;
			}
			
			
			cout<<"Generated temporary file : "<<t_destination<<"\n";
					
			/*adding an entry to list for new table*/
			string temp_tb = destination;
			string append_columns="";
			string temp_cols = getColumnString(source);
			
			
			int *catInsertIdx = getColumnIntIndexes(source,columns);

				
			vector<string> size_vec;
			split(columns,',',size_vec);
			int size = size_vec.size();
			
			//check if where flag is set or not. if yes, determine the index of that column
			//so while reading the data we can compare the value at that index
			int wIdx;
			if(where_flag == 1){
			
				/*find the index of where_column*/
				int *where_idx = getColumnIntIndexes(source,wCol);
				wIdx= where_idx[0];				
			}

			

			if(columns!="*"){
				
				vector<string> col_vec;
				split(temp_cols,',',col_vec);
				
				for(int i=0;i<size;i++){
					append_columns.append(col_vec[catInsertIdx[i]]);
					
					if(i!=size-1)
						append_columns.append(",");
				
				}
				
				
				
			}
			else if(columns=="*"){
				append_columns = temp_cols;
			}
			
			
			
			append(temp_tb,append_columns,"",0,0,0,1);			
			cout<<"Entry for temp table "<<destination<<" added to catalog"<<std::endl;
			
			
			//file to read data from
			ifstream f(t_source.c_str(),std::ios::in | std::ios::binary);
			
			
			//temp file to write data into
			ofstream fout;
			fout.open(t_destination.c_str(),ios::out);

			
			if(columns=="*"){
				//cout<<"inside if";	
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
				//cout<<"in else";
				//logic for selecting specific columns here
				
				int *idx = getColumnIntIndexes(source,columns);
				
				//now read each line of source file.
				//then for each line, output the word of index that is marked true to the destination file
				//add a \n after each iteration
				string x;
				while(getline(f,x)){
					vector<string> v;
					split(x, ' ', v);
					
					if(where_flag !=1){
						for (int i = 0; i < size; ++i) {
      								if(i==size-1)
      									fout<<v[idx[i]];
      								else
      									fout<<v[idx[i]]<<' ';
   						}
   						fout<<"\n";   						
   					}
   					else if(where_flag==1){
   						
   						if(v[wIdx] == wVal){
   							for (int i = 0; i < size; ++i) {
								if(i==size-1)
      									fout<<v[idx[i]];
      								else
      									fout<<v[idx[i]]<<' ';
   						
   							}
   							
							fout<<"\n";
   						}

   					}

				}
				//cout<<"print after while ends\n";				
			}
			f.close();
			fout.close();
			//cout<<"print before break";
			break;
		}

	}
}

/*-----------------------------------------------------------------------------------------*/



//destination is the table into which the result of select has to be inserted
//it should not be confused with alias for select query.
//the alias for select query is a temporary table = "_IS.tbl"
/*-----------------------------------------------------------------------------------------*/
void insertWithSelect(string destination){

	
	int mtoken=yylex();
	//now to parse the select inside an insert, we do not call select function,
	//since there is not alias for with this query, the output will be shown on console.
	//but we want it in the destination table. So we call function generateOutput with display flag set to 1.
	
	string columns;
	columns = yytext;
	while(mtoken=yylex() != FROM){
		yymore;		
		columns+=yytext;
	}
	cout<<"Columns "<<columns<<"\n";
	
	//TODO: here add logic to detect if next token is a IDENTIFIER(tablename) or an OPENBRACE to detect nested select.
	
	mtoken=yylex();	
	string source_tb = yytext;
		
	if(mtoken==OPENBRACE){
		mtoken=yylex();
		if(mtoken==SELECT){
			source_tb=select();
		}
	}

	string temp_table = source_tb+"_IS_"+columns;
	
	
	//we first check for column compatibility of two tables.
	//First check: COLUMN COUNT COMPATIBILITY
	//Second check: COLUMN TYPE COMPATIBILITY
	
	//if columns = *, then get the columns for both tables and check the count.
	//if count equals, then check for datatypes of all columns.
	if(columns=="*"){
		
		string col_d = getColumnString(destination);
		string col_s = getColumnString(source_tb);
		
		vector<string> vec2;
		split(col_d,',',vec2);
		int count_d = vec2.size();
		
		vector<string> vec4;
		split(col_s,',',vec4);
		int count_s = vec4.size();
		
		if(count_d != count_s){
		
			cout<<"Error column count mismatch\n";
			return;
		}
		{
			cout<<"Size of vec2="<<vec2.size()<<"\n";
			cout<<"Size of vec4="<<vec4.size()<<"\n";
			
		}
		//if column count check is passed, check the datatype compatibility.
		int *check_d = new int[vec2.size()];
		int *check_s = new int[vec4.size()];
		
		for(int i=0;i<vec2.size();i++){
			
			vector<string> vec5;
			split(vec2[i],':',vec5);
			
			if((vec5[1]=="INT") || (vec5[1]=="int")){
				
				check_d[i]=1;
				cout<<vec5[0]<<" marking as 1\n";
			}
			else{
				check_d[i]=0;
				cout<<vec5[0]<<" marking as 0\n";
			}
			
		}
		
		for(int i=0;i<vec4.size();i++){
			
			vector<string> vec6;
			split(vec4[i],':',vec6);
			
			if((vec6[1]=="INT") || (vec6[1]=="int")){
				
				check_s[i]=1;
				cout<<vec6[0]<<" marking as 1\n";
			}
			else{
				check_s[i]=0;
				cout<<vec6[0]<<" marking as 0\n";
			}
			
		}
		
		
		
		for(int i=0;i<vec2.size();i++){
			cout<<check_d[i]<<" "<<check_s[i]<<"\n";
			if(check_d[i] != check_s[i]){
				
				cout<<"Type compatibility error at column index : "<<i+1;
				return;
			
			}
		
		}
	}
	else{
	
		//if the columns are not *, but of the form A,B,C..., split the string
		//get columns of destination table, and check column count.
		//then check datatype compatibility
		
		vector<string> vec1;
		split(columns,',',vec1);
		
		string col_s = getColumnString(source_tb);
		string col_d = getColumnString(destination);
		
		vector<string> vec3;
		split(col_d,',',vec3);
		
		if(vec1.size() != vec3.size()){
			cout<<"Error: column count mismatch for tables provided\nRequired "<<vec3.size()<<", found "<<vec1.size()<<".\n";
			return;
		}
		
		//if column count matches, check for column type
		
		int *index = getColumnIntIndexes(source_tb,columns);
		int size = vec1.size();
		
		vector<string> vec5;
		split(col_s,',',vec5);
		
		int *check_s = new int[vec1.size()];
		int *check_d = new int[vec3.size()];
		
		int j=0;
		
		for(int i=0; i<size; i++){
			
				vector<string> vec6;
				split(vec5[index[i]],':',vec6);
				
				if((vec6[1]=="INT")||(vec6[1]=="int")){
					cout<<"Marking "<<vec6[0]<<" as 1\n";
					check_s[j]=1;
					j++;
				}
				else{
					cout<<"Marking "<<vec6[0]<<" as 0\n";
					check_s[j]=0;
					j++;
				}
		
		}
		for(int i=0;i<vec3.size();i++){
			
			vector<string> vec7;
			split(vec3[i],':',vec7);
			
			if((vec7[1]=="INT") || (vec7[1]=="int")){
				
				check_d[i]=1;
				cout<<vec7[0]<<" marking as 1\n";
			}
			else{
				check_d[i]=0;
				cout<<vec7[0]<<" marking as 0\n";
			}
			
		}
		
		for(int i=0;i<vec1.size();i++){
			if(check_s[i]!=check_d[i]){
				cout<<"Type mismatch on columns "<<vec5[i]<<" and "<<vec3[i]<<".\n";
				return;
			}
		}
	
	}
	
	//TODO: modify for query with where clause
	mtoken=yylex();
	if(mtoken==WHERE){
		mtoken = yylex();
		string where_col=yytext;
		cout<<"Where columns="<<where_col<<"\n";
		mtoken=yylex();
		mtoken=yylex();
		if(mtoken==QUOTE){
			mtoken=yylex();
		}
		string where_val = yytext;
		cout<<"Where val="<<where_val<<"\n";
		mtoken=yylex();
		generateOutput(source_tb,columns,temp_table,1,1,where_col,where_val);
	}else{
		generateOutput(source_tb,columns,temp_table,1,0,"","");
	}
	
	//open file containing output of select.
	ifstream f((temp_table+".tbl").c_str(),ios::in|ios::binary);
	
	//open file in which insert is to be done
	ofstream file((destination+".tbl").c_str(), std::ios_base::out | std::ios_base::app | std::ios_base::binary);	
	
	//get primary key of destination table to check if the insertion data does not violate the PK condition in destination table
	string primaryKey = getPrimaryKey(destination);
	cout<<"Primary key is "<<primaryKey<<"\n";
	int *mIndex = getColumnIntIndexes(destination,primaryKey);
	cout<<"Primary key index is "<<mIndex[0]<<"\n";
	
	
	string x;
	while(getline(f,x)){
		
		if(checkPKViolation(destination,x,mIndex[0])){
			cout<<"Inserting "<<x<<"\n";
			file<<x<<"\n";
		}
	}
	f.close();
	file.close();
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
	
	
	//get the tablename now
	string src_tablename;
	mtoken=yylex();

	//after "FROM", check if the query is nested or not
	if( mtoken == OPENBRACE){
		
		mtoken = yylex();
		string alias = select();		
		
		//the next meaningful token can be:
		//1. IDENTIFIER : in case of - select... (select * from T1 ) T2;
		//2. SEMICOLON : in case of - select * from t1;
		//3. WHERE:	in case of - select * from t1 WHERE <some_condition>
		//4. JOIN: in case of - select * from (select * from t1) t4 JOIN T2;
		
		int where_flag=0;			
		string where_column, where_value;
		
		mtoken=yylex();			
		
		if(mtoken == WHERE){
			//cout<<"Where detected when coming outwards\n";
			where_flag=1;
			mtoken=yylex(); //column name
			where_column=yytext;
			mtoken=yylex(); //=
			mtoken=yylex(); //value
			if(mtoken==QUOTE){
				mtoken=yylex();
			}
			where_value=yytext;
			mtoken=yylex();
			if(mtoken==QUOTE){
				mtoken=yylex();
			}
		}
		
		//if join is detected, we call function parseJoin, which parses the query
		//i.e. extracts column names to be compared, their indexes in corresponding tables
		//and then calls the join() function with those parameters.
		if(mtoken== JOIN){
		
			
			mtoken=yylex();
			cout<<"Source table : "<<alias+"_JOIN_"<<yytext<<" Selected Columns : "<<columns<<"\n";
			//parseJoin(columns,table1,table2)
			string table2 = yytext;
			parseJoin(alias, table2, columns);
			return alias+"_JOIN_"+table2;
		}		
		
		while((mtoken != IDENTIFIER) && (mtoken!=SEMICOLON)){
			//cout<<"I went here\n"<<yytext;
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
		

		if(!tableExists(src_tablename)){
			cout<<"Table "<<src_tablename<<" does not exist\n";
			return "error";
		}
		
		//the next meaningful token can be:
		//1. IDENTIFIER : in case of - select... (select * from T1 ) T2;
		//2. SEMICOLON : in case of - select * from t1;
		//3. WHERE:	in case of - select * from t1 WHERE <some_condition>
		//4. JOIN:	in case of - select * from t1 JOIN t2...
		mtoken=yylex();
		int where_flag=0;
		string where_column,where_value;
		if(mtoken==WHERE){
			//cout<<"where encountered\n";
			where_flag=1;
			mtoken=yylex();	//column name
			where_column=yytext;
			mtoken=yylex();	//=
			mtoken=yylex();	//value or '
			if(mtoken==QUOTE){
				mtoken=yylex();
			}
			where_value=yytext;
			mtoken=yylex();	
			if(mtoken==QUOTE){
				mtoken=yylex();
			}
		}
		
		
		//if join is detected, we call function parseJoin, which parses the query
		//i.e. extracts column names to be compared, their indexes in corresponding tables
		//and then calls the join() function with those parameters.
		if(mtoken== JOIN){
		
			
			mtoken=yylex();
			cout<<"Source table : "<<src_tablename+"_JOIN_"<<yytext<<" Selected Columns : "<<columns<<"\n";
			//parseJoin(columns,table1,table2)
			string table2 = yytext;
			parseJoin(src_tablename, table2, columns);
			return src_tablename+"_JOIN_"+table2;
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
	string append_columns="";
	string pk;
	int recSize=0;
	
	int token = yylex();
	//cout<<token;
	if(token != TABLE){
		cout<<"Syntax Error "<<yytext<<"\n";
		return; 	
	}
	token = yylex();
	//cout<<token;
	if(token == IDENTIFIER){
		tablename = yytext;
		
		
		if(tableExists(tablename)){
			cout<<"Error: Table "<<tablename<<" already exists.\n";
			return;
		}
		//else{return;}

		cout<<"Table "<< tablename <<" will be created.\n";
		yylex();	
	}
	else{
		cout<<"Invalid tablename.\n";
		return;
	}
	

	int nToken, vToken, numOfCol=0;
	
	
	nToken = yylex();
	//detecting column and datatype	
	while(nToken != CLOSEBRACE){
		cout<<"Column : "<<yytext;
		append_columns.append(yytext);
		append_columns.append(":");
		vToken = yylex();
		
		cout<<"\tType : "<<yytext<<"\n";
		string datatype = yytext;
		append_columns.append(yytext);

		if(vToken == CHAR){
			//cout<<"Char detected";
			
			
			numOfCol++;
			vToken=yylex();
			cout<<vToken<<" OPEN\n";
			append_columns.append(yytext);
						
			vToken = yylex();
			cout<<vToken<<" INTNUM\n";
			append_columns.append(yytext);
			recSize+=atoi(yytext);
			
			vToken=yylex();
			cout<<vToken<<" CLOSE\n";
			append_columns.append(yytext);
		}
		else{
			
			numOfCol++;
			recSize+=4;
		}
				
		//skip comma		
		nToken=yylex();
		vToken=yylex();
		if(vToken == PRIMARY){
			break;
		}
		if(nToken == COMMA ){
			append_columns.append(",");		
		}
	}
	cout<<"Record Size "<<recSize<<"\n";
	
	nToken = yylex();
	if(vToken==PRIMARY && nToken==KEY){
		cout<<"PRIMARY KEY DETECTED\nNum of Columns = "<<numOfCol<<"\n";		
		nToken = yylex();	
	}
	nToken = yylex();
	if(nToken == IDENTIFIER){
		cout<<"Primary Key is "<<yytext<<"\n";
		pk=yytext;	
	}
	
	append(tablename,append_columns,pk,recSize,0,0,0);
	
	tablename+=".tbl";
	ofstream mfout (tablename.c_str(), std::ios_base::binary|std::ios_base::app);
	mfout.close();
	
}

void insert(){
	
	int nToken = yylex();
	//cout<<nToken<<"\n";
	string tablename;
	string temp_tb;
	if ((nToken = yylex())== IDENTIFIER){	
		tablename=yytext;
		temp_tb=tablename;
		tablename+=".tbl";	
		cout<<"Tablename is "<<tablename<<"\n";
	}

	
	if(!tableExists(temp_tb)){
		cout<<"Error:"<<temp_tb<<" does not exist\n";
		return;
	}

	//open .tbl file to write the record
	ofstream file(tablename.c_str(), std::ios_base::out | std::ios_base::app | std::ios_base::binary);	
	
	
	
	
	
	//skip keyword VALUES
	nToken = yylex();	//ntoken gets value = VALUES
	
	
	//insert with a select detection
	if(nToken == SELECT){
		cout<<"Select detected";
		insertWithSelect(temp_tb);
		return;
	}
	
	
	
	//cout<<nToken<<"\n";
	//skip open brace
	nToken = yylex();
	//cout<<nToken<<"\n";
	
	string columns = getColumnString(temp_tb);
	string primaryKey = getPrimaryKey(temp_tb);
	
	vector<string> vec2;
	split(columns,',',vec2);
	
	int check[vec2.size()];
	
	for(int i=0;i<vec2.size();i++){
		
		vector<string> vec3;
		split(vec2[i],':',vec3);
		
		if(vec3[1]=="int" || vec3[1]=="INT")
			check[i]=1;
		else
			check[i]=0;
		
	}	
	
	
	string row;
	nToken=yylex();
	int i=0;
	while(nToken != CLOSEBRACE){
		//cout<<i<<"\n";
		switch(nToken){
			case QUOTE:
				
				if(check[i]!=0){
					nToken=yylex();
					cout<<"Error: Column type mismatch at "<<yytext<<"\nExpected an INT but found CHAR.\n";
					return;
				}
					
				nToken=yylex();
				if(nToken==IDENTIFIER){
					//cout<<"Value : "<<yytext<<"\n";
					row.append(yytext);
				}
				//skip closing '
				if(nToken=yylex() != QUOTE){
					cout<<"Error: Non Terminated String.\n";
					return;
				}
				nToken=yylex();
				if(nToken == COMMA){
					nToken=yylex();
					row.append(" ");
				}
				else{//cout<<"Q/C "<<yytext<<"\n";
				}
				i++;
				break;
			
			case INTNUM:
				
				if(check[i]!=1){
					cout<<"Error: Column type mismatch at "<<yytext<<"\nExpected a CHAR but found INT.\n";
				}
				
				//cout<<"Value : "<<yytext<<"\n";
				row.append(yytext);
				
				nToken=yylex();
				if(nToken == COMMA){
					nToken=yylex();
					row.append(" ");
				}
				i++;
				break;
			
				
		
		}
	
	}
	
	
	
	
	
	row+="\n";
	cout<<"Row is : "<<row<<"\n";//<<"Size: "<<row.size()<<"\n";
	
	int *mIndex = getColumnIntIndexes(temp_tb,primaryKey);
	
	//cout<<"Primary key index is "<<mIndex[0]<<"\n";

	
	bool checkPk = checkPKViolation(temp_tb,row,mIndex[0]);
	
	if(checkPk==false){
		cout<<"Error: Primary key violation while inserting.\n";
		file.close();
		return;
	}
	
	
	file.write(row.c_str(), row.size());

	file.close();
	
	update(temp_tb);

	ifstream f(tablename.c_str(),std::ios::in);
	string x;
	cout<<"\n";
	while( getline(f,x)){
		cout<<x<<"\n";	
	}
	f.close();
		
}

/*
void tables(){
	
	ifstream f("catalog.txt",ios::in);
	
	string x;
	while(getline(f,x)){
		vector<string> vec1;
		split(x,'=',vec1);
		cout<<"Tablename : "<<vec1[1]<<"\n";
		getline(f,x);
		cout<<x<<"\n\n";
		
		getline(f,x);
		getline(f,x);
		
	}
	f.close();
	
}*/

int main(int argc, char **argv){

	load();
	
	//set the lex input to the file specified in arguments
	vector<string> vec1;
	split(argv[1],'=',vec1);
	
	yyin = fopen(vec1[1].c_str(),"r");
	
	int ntoken = yylex();
	
//	while(ntoken){

		switch(ntoken){
			case CREATE:{
				cout<<"\n\nGoing to create a table.\n";
				create();	
				break;
			}
			case SELECT:{
				cout<<"\n\nGoing to select from a table.\n";
				string f_table=select();
				cout<<"Final select will be done from table "<<f_table<<"\nhere";
				//ntoken=yylex();
				break;
			}		
			case INSERT:{
				cout<<"\n\nGoing to insert into a table.\n";
				insert();
				
				break;
			}
			case SHOW:{
				
				ntoken=yylex();
				if(ntoken==TABLES){
					cout<<"\n\nShowing all tables.\n";
					showTables(start);
				}
				else if(ntoken==TABLE){
					ntoken=yylex();
					string tablename = yytext;
					cout<<"\n\nShowing table "<<tablename<<".\n";
					showTable(start,tablename);
					
					
				}
				break;
			}
			case DROP:{
				ntoken=yylex();
				if(ntoken==TABLE){
					ntoken=yylex();
					dropTable(yytext);
				}
				break;
			}
			default:{
				cout<<"Unknown query.\n";
				break;
			}
		}
		cout<<"\nToken "<<ntoken<<"\n";
		ntoken=yylex();
		cout<<"\nToken "<<ntoken<<"\n";
		
		//TODO: Do a while loop until next create|select|insert|show token is encountered.
		//then continue again.
//	}
	dump();	
}
