%union {
	int mint;
	double mdouble;
	char *mstr;
}

%token NAME
%token STRING
%token INTNUM

%left OR
%left AND
%left NOT
%left COMPARISON

%token AND AS BY CHAR CREATE DISTINCT DELETE EXISTS FROM		
%token GROUP HAVING INSERT INT INTO KEY NOT NULLX OR ORDER
%token PRIMARY SELECT SHOW TABLE TABLES UNIQUE UPDATE VALUES WHERE

%%

createTableDef:
		CREATE TABLE table '(' table_column_list ')'
		;
table:
		NAME
		;
table_column_list:
		table_column
	|	table_column_list ',' table_column
	;
table_column:
		column_def
	|	constraint_def
	;
column_def:
		column data_type column_def_opt_list
	;
data_type:
		CHAR
	|	CHAR '(' INTNUM ')'
	;
column_def_opt_list:
		/*empty*/
	|	column_def_opt_list column_def_opt
	;
column_def_opt:
		NOT NULLX
	|	NOT NULLX UNIQUE
	|	NOT NULLX PRIMARY KEY
	;
constraint_def:
		UNIQUE '(' table_column_list ')'
	|	PRIMARY KEY '(' table_column_list ')'
	;
column:
		NAME
	;

%%
