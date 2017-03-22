%{
#include "myvals.h"
#include <string.h>
#include <stdio.h>
int line_num = 1;
void yyerror(char *s);
%}

%%
"AND"		{return AND;}
"AS"		{return AS;}
"BY"		{return BY;}
"CHAR"		{return CHAR;}
"CREATE"	{return CREATE;}
"DISTINCT"	{return DISTINCT;}
"DELETE"	{return DELETE;}
"EXISTS"	{return EXISTS;}
"FROM"		{return FROM;}
"GROUP"		{return GROUP;}
"HAVING"	{return HAVING;}
"INSERT"	{return INSERT;}
"INT"		{return INT;}
"INTO"		{return INTO;}
"KEY"		{return KEY;}
"NOT"		{return NOT;}
"NULLX"		{return NULLX;}
"OR"		{return OR;}
"ORDER"		{return ORDER;}
"PRIMARY"	{return PRIMARY;}
"SELECT"	{return SELECT;}
"SHOW"		{return SHOW;}
"TABLE"		{return TABLE;}
"TABLES"	{return TABLES;}
"UNIQUE"	{return UNIQUE;}
"UPDATE"	{return UPDATE;}
"VALUES"	{return VALUES;}
"WHERE"		{return WHERE;}

"QUIT"		{exit(0);}

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
