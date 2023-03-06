%%
%{
  private void echo () { System . out . print (yytext ()); }

  public int position () { return yycolumn; }
%}

%class    MicroPythonLexer
%function nextToken
%type	  Token
%unicode
%line
%column
%eofval{
  { return new Token (Symbol . EOF); }
%eofval}

WhiteSpace = [ \t\n]
Ident = [:letter:] (_?[:letter:] | _?[:digit:])*
Integer = [:digit:] [:digit:]*
Comment = # .* \n

%%
"<"     			{ echo(); return new Token (Symbol . LT, yytext ()); }
">"     			{ echo(); return new Token (Symbol . GT, yytext ()); }
"=="     			{ echo(); return new Token (Symbol . EQ, yytext ()); }
"<="     			{ echo(); return new Token (Symbol . LE, yytext ()); }
">="     			{ echo(); return new Token (Symbol . GE, yytext ()); }
"!="     			{ echo();  return new Token (Symbol . NE, yytext ()); }
"="     			{ echo();  return new Token (Symbol . ASSIGN); }
"+"				{ echo();  return new Token (Symbol . PLUS); }
"-"				{ echo();  return new Token (Symbol . MINUS); }
"*"     			{ echo();  return new Token (Symbol . TIMES); }
"//"  	 			{ echo();  return new Token (Symbol . SLASH); }
"("				{ echo();  return new Token (Symbol . LPAREN); }
")"				{ echo();  return new Token (Symbol . RPAREN); }
"->"  	 			{ echo();  return new Token (Symbol . ARROW); }
";"				{ echo();  return new Token (Symbol . SEMICOLON); }
":"				{ echo();  return new Token (Symbol . COLON); }
","				{ echo();  return new Token (Symbol . COMMA); }
"."				{ echo();  return new Token (Symbol . PERIOD); }
from				{ echo();  return new Token (Symbol . FROM); }
MicroPythonListClass		{ echo();  return new Token (Symbol . CLASS); }
import				{ echo();  return new Token (Symbol . IMPORT); }
MicroPythonList			{ echo();  return new Token (Symbol . LIST); }
def				{ echo();  return new Token (Symbol . DEF); }
return				{ echo();  return new Token (Symbol . RETURN); }
int				{ echo();  return new Token (Symbol . INT); }
if				{ echo();  return new Token (Symbol . IF); }
else				{ echo();  return new Token (Symbol . ELSE); }
while				{ echo();  return new Token (Symbol . WHILE); }
print				{ echo();  return new Token (Symbol . PRINT); }
or				{ echo();  return new Token (Symbol . OR); }
and				{ echo();  return new Token (Symbol . AND); }
not				{  echo(); return new Token (Symbol . NOT); }
input				{ echo();  return new Token (Symbol . INPUT); }
cons				{ echo();  return new Token (Symbol . CONSTANT); }
head				{ echo();  return new Token (Symbol . HEAD); }
tail				{ echo();  return new Token (Symbol . TAIL); }
null				{ echo();  return new Token (Symbol . NULL); }
{Ident}   			{ echo();  return new Token (Symbol . ID, yytext ()); }
{Integer}			{ echo();  return new Token (Symbol . INTEGER, yytext ()); }
{WhiteSpace}			{ echo();  }
{Comment}			{ echo();  }
.				{ echo();  ErrorMessage . print (yycolumn, "Illegal character"); }

















