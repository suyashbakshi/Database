%{
#include "myvals.h"
#include <string.h>
#include <stdio.h>
int line_num = 1;
void yyerror(char *s);
%}

%%
"AND"	|
"and"		{return AND;}
"AS"	|
"as"		{return AS;}
"BY"	|
"by"		{return BY;}
"CHAR"	|
"char"		{return CHAR;}
"CREATE"	|
"create"	{return CREATE;}
"DISTINCT"	|	
"distinct"	{return DISTINCT;}
"DELETE"	|
"delete"	{return DELETE;}
"EXISTS"	|
"exists"	{return EXISTS;}
"FROM"		|
"from"		{return FROM;}
"GROUP"		|
"group"		{return GROUP;}
"HAVING"	|
"having"	{return HAVING;}
"INSERT"	|
"insert"	{return INSERT;}
"INT"		|
"int"		{return INT;}
"INTO"		|
"into"		{return INTO;}
"JOIN"		|
"join"		{return JOIN;}
"KEY"		|
"key"		{return KEY;}
"NOT"		|
"not"		{return NOT;}
"NULLX"		|	
"nullx"		{return NULLX;}
"ON"		|
"on"		{return ON;}
"OR"		|
"or"		{return OR;}
"ORDER"		|	
"order"		{return ORDER;}
"PRIMARY"	|
"primary"	{return PRIMARY;}
"SELECT"	|
"select"	{return SELECT;}
"SHOW"		|
"show"		{return SHOW;}
"TABLE"		|
"table"		{return TABLE;}
"TABLES"	|
"tables"	{return TABLES;}
"UNIQUE"	|
"unique"	{return UNIQUE;}
"UPDATE"	|
"update"	{return UPDATE;}
"VALUES"	|
"values"	{return VALUES;}
"WHERE"		|
"where"		{return WHERE;}

"QUIT"		|
"quit"		{exit(0);}

"="	|
"<>"	|
">"	|
"<"	|
">="	|
"<="		{return COMPARISON;}

","		{return COMMA;}
"."		{return PERIOD;}
":"		{return COLON;}
";"		{return SEMICOLON;}
"("		{return OPENBRACE;}
")"		{return CLOSEBRACE;}
"*"		{return STAR;}

"'"		{return QUOTE;}

[A-Za-z][A-Za-z0-9_]*	{return IDENTIFIER;}

[0-9]+		{return INTNUM;}

[ \n\t]+		line_num++;


.		{printf("UNEXPECTED CHARACTER\n");}

%%


int yywrap(void){
	return 1;
}
