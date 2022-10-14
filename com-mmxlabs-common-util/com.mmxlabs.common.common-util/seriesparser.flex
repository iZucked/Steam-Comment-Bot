// Originally based on example from http://www2.cs.tum.edu/projekte/cup/examples.php

package com.mmxlabs.common.parser.impl;

import com.mmxlabs.common.parser.astnodes.*;

import java.time.Month;
import java_cup.runtime.Symbol;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.ComplexSymbolFactory.Location;


%%
%public
%class Lexer
%cup
%implements ParserSymbols
%char
%line
%column

%{
    StringBuffer string = new StringBuffer();
    public Lexer(java.io.Reader in, ComplexSymbolFactory sf){
	this(in);
	symbolFactory = sf;
    }
    ComplexSymbolFactory symbolFactory;

  private Symbol symbol(String name, int sym) {
      return symbolFactory.newSymbol(name, sym, new Location(yyline+1,yycolumn+1,yychar), new Location(yyline+1,yycolumn+yylength(),yychar+yylength()));
  }

  private Symbol symbol(String name, int sym, Object val) {
      Location left = new Location(yyline+1,yycolumn+1,yychar);
      Location right= new Location(yyline+1,yycolumn+yylength(), yychar+yylength());
      return symbolFactory.newSymbol(name, sym, left, right,val);
  }
  private Symbol symbol(String name, int sym, Object val,int buflength) {
      Location left = new Location(yyline+1,yycolumn+yylength()-buflength,yychar+yylength()-buflength);
      Location right= new Location(yyline+1,yycolumn+yylength(), yychar+yylength());
      return symbolFactory.newSymbol(name, sym, left, right,val);
  }
  private void error(String message) {
    throw new com.mmxlabs.common.parser.series.GenericSeriesParsesException(message + " at column "+(yycolumn+1)+".");
  }
%}

%eofval{
     return symbolFactory.newSymbol("EOF", EOF, new Location(yyline+1,yycolumn+1,yychar), new Location(yyline+1,yycolumn+1,yychar+1));
%eofval}


FLit1    = [0-9]+ \. [0-9]* 
FLit2    = \. [0-9]+ 
FLit3    = [0-9]+ 
Exponent = [eE] [+-]? [0-9]+

Integer = 0 | [1-9][0-9]* 

PARAM = @[a-zA-Z_0-9]+[a-zA-Z_][a-zA-Z_0-9]*
VAR = \#[a-zA-Z]+[a-zA-Z_0-9]*

mPositive = m[0-9][0-9]?
mNegative = m-[1-9][0-9]?

MValue = {mPositive}|{mNegative}

Named_Element = [a-zA-Z_0-9]+[a-zA-Z_][a-zA-Z_0-9]*|[a-zA-Z_]+[a-zA-Z_0-9]*
Float =  ({FLit1}|{FLit2}|{FLit3}) {Exponent}?  

new_line = \r|\n|\r\n;
white_space = {new_line} | [ \t\f]


%ignorecase
%state STRING

%%

<YYINITIAL>{
/* keywords - case insensitive! */
"CAP"              { return symbol("cap",CAP); }
"FLOOR"            { return symbol("floor",FLOOR); }
"MIN"              { return symbol("min",MIN); }
"MAX"            { return symbol("max",MAX); }
"SHIFT"           { return symbol("shift",SHIFT); }
"DATEDAVG"           { return symbol("datedavg",DATEDAVG); }
"SPLITMONTH"           { return symbol("splitmonth", SPLITMONTH); }
"S"           { return symbol("scurve", S); }
"VOLUMETIERM3"           { return symbol("volumetierm3", VOLUMETIERM3); }
"VOLUMETIERMMBTU"           { return symbol("volumetiermmbtu", VOLUMETIERMMBTU); }
"TIER"           { return symbol("tier", TIER); }
 
 
 "JAN" { return symbol("jan",MONTH, Month.JANUARY); }
 "FEB" { return symbol("feb",MONTH, Month.FEBRUARY); }
 "MAR" { return symbol("mar",MONTH, Month.MARCH); }
 "APR" { return symbol("apr",MONTH, Month.APRIL); }
 "MAY" { return symbol("may",MONTH, Month.MAY); }
 "JUN" { return symbol("jun",MONTH, Month.JUNE); }
 "JUL" { return symbol("jul",MONTH, Month.JULY); }
 "AUG" { return symbol("aug",MONTH, Month.AUGUST); }
 "SEP" { return symbol("sep",MONTH, Month.SEPTEMBER); }
 "OCT" { return symbol("oct",MONTH, Month.OCTOBER); }
 "NOV" { return symbol("nov",MONTH, Month.NOVEMBER); }
 "DEC" { return symbol("dec",MONTH, Month.DECEMBER); }
 
 
/* separators */

"<"               { return symbol("<",LT, ComparisonOperators.LT); }
"<="               { return symbol("<=",LTE, ComparisonOperators.LTE); }
">"               { return symbol(">",GT, ComparisonOperators.GT); }
">="               { return symbol(">=",GTE, ComparisonOperators.GTE); }

"["               { return symbol("[",LBRACKET); }
"]"               { return symbol("]",RBRACKET); }
  \"              { string.setLength(0); yybegin(STRING); }
","               { return symbol("comma",COMMA); }
":"               { return symbol("colon",COLON); }

";"               { return symbol("semicolon",SEMICOLON); }
"="               { return symbol("equals",EQUALS); }
"("               { return symbol("(",LPAREN); }
")"               { return symbol(")",RPAREN); }
"+"               { return symbol("plus",PLUS  ); }
"-"               { return symbol("minus",MINUS  ); }
"*"               { return symbol("mul",TIMES  ); }
"/"               { return symbol("div",DIVIDE  ); }
"%"               { return symbol("percent",PERCENT  ); }
"?"               { return symbol("question",QUESTION  ); }
{PARAM}        { return symbol("param", PARAM, new String(yytext()).replace("@","") ); }

{VAR}        { return symbol("var", VAR, new String(yytext()).replace("#","") ); }
m        { return symbol("m", M, Integer.valueOf(0) ); }
{MValue}        { return symbol("m", M, Integer.valueOf(yytext().replace("m","")) ); }


{Named_Element}        { return symbol("namedelement", NAMED_ELEMENT, new String(yytext()) ); }
{Integer}        { return symbol("integer", INTEGER, Integer.valueOf(yytext()) ); }
{Float}        { return symbol("float", FLOAT, new Double(yytext()) ); }

{white_space}     { /* ignore */ }



}


/* error fallback */
[^]|\n              {  /* throw new Error("Illegal character <"+ yytext()+">");*/
		    error("Illegal character <"+ yytext()+">");
                  }