// Originally based on example from http://www2.cs.tum.edu/projekte/cup/examples.php

package com.mmxlabs.common.parser.impl;

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

Named_Element = [a-zA-Z_0-9]+[a-zA-Z_][a-zA-Z_0-9]*
Number = {Integer}|({FLit1}|{FLit2}|{FLit3}) {Exponent}?  

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
 
/* separators */
  \"              { string.setLength(0); yybegin(STRING); }
","               { return symbol("comma",COMMA); }
"("               { return symbol("(",LPAREN); }
")"               { return symbol(")",RPAREN); }
"+"               { return symbol("plus",PLUS  ); }
"-"               { return symbol("minus",MINUS  ); }
"*"               { return symbol("mul",TIMES  ); }
"/"               { return symbol("div",DIVIDE  ); }
"%"               { return symbol("percent",PERCENT  ); }
"?"               { return symbol("question",QUESTION  ); }
{Named_Element}        { return symbol("namedelement", NAMED_ELEMENT, new String(yytext()) ); }
{Integer}        { return symbol("integer", INTEGER, new Integer(yytext()) ); }
{Number}        { return symbol("number", NUMBER, new Double(yytext()) ); }
{white_space}     { /* ignore */ }

}


/* error fallback */
[^]|\n              {  /* throw new Error("Illegal character <"+ yytext()+">");*/
		    error("Illegal character <"+ yytext()+">");
                  }