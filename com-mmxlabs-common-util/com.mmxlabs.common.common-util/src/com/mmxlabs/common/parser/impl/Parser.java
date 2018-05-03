
//----------------------------------------------------
// The following code was generated by CUP v0.11b 20160615 (GIT 4ac7450)
//----------------------------------------------------

package com.mmxlabs.common.parser.impl;

import java_cup.runtime.*;
import java.util.*;
import com.mmxlabs.common.parser.*;
import com.mmxlabs.common.parser.series.*;
import com.mmxlabs.common.parser.series.functions.*;
import java_cup.runtime.XMLElement;

/** CUP v0.11b 20160615 (GIT 4ac7450) generated parser.
  */
@SuppressWarnings({"rawtypes"})
public class Parser extends java_cup.runtime.lr_parser {

 public final Class getSymbolContainer() {
    return ParserSymbols.class;
}

  /** Default constructor. */
  @Deprecated
  public Parser() {super();}

  /** Constructor which sets the default scanner. */
  @Deprecated
  public Parser(java_cup.runtime.Scanner s) {super(s);}

  /** Constructor which sets the default scanner. */
  public Parser(java_cup.runtime.Scanner s, java_cup.runtime.SymbolFactory sf) {super(s,sf);}

  /** Production table. */
  protected static final short _production_table[][] = 
    unpackFromStrings(new String[] {
    "\000\025\000\002\002\005\000\002\002\004\000\002\002" +
    "\005\000\002\002\005\000\002\002\005\000\002\002\010" +
    "\000\002\002\010\000\002\002\005\000\002\002\005\000" +
    "\002\002\010\000\002\002\011\000\002\002\014\000\002" +
    "\002\012\000\002\002\004\000\002\002\004\000\002\002" +
    "\005\000\002\002\003\000\002\002\003\000\002\002\003" +
    "\000\002\002\003\000\002\002\003" });

  /** Access to production table. */
  public short[][] production_table() {return _production_table;}

  /** Parse-action table. */
  protected static final short[][] _action_table = 
    unpackFromStrings(new String[] {
    "\000\102\000\034\004\013\005\004\012\020\016\014\017" +
    "\021\020\007\021\017\022\006\023\015\024\005\025\010" +
    "\026\012\027\011\001\002\000\034\004\013\005\004\012" +
    "\020\016\014\017\021\020\007\021\017\022\006\023\015" +
    "\024\005\025\010\026\012\027\011\001\002\000\022\002" +
    "\uffee\004\uffee\005\uffee\006\uffee\007\uffee\013\uffee\014\uffee" +
    "\015\102\001\002\000\004\012\071\001\002\000\004\012" +
    "\064\001\002\000\022\002\uffef\004\uffef\005\uffef\006\uffef" +
    "\007\uffef\013\uffef\014\uffef\015\062\001\002\000\020\002" +
    "\ufff0\004\ufff0\005\ufff0\006\ufff0\007\ufff0\013\ufff0\014\ufff0" +
    "\001\002\000\020\002\ufff1\004\ufff1\005\ufff1\006\ufff1\007" +
    "\ufff1\013\ufff1\014\ufff1\001\002\000\034\004\013\005\004" +
    "\012\020\016\014\017\021\020\007\021\017\022\006\023" +
    "\015\024\005\025\010\026\012\027\011\001\002\000\020" +
    "\002\uffed\004\uffed\005\uffed\006\uffed\007\uffed\013\uffed\014" +
    "\uffed\001\002\000\004\012\052\001\002\000\014\002\051" +
    "\004\025\005\026\006\030\007\027\001\002\000\004\012" +
    "\041\001\002\000\034\004\013\005\004\012\020\016\014" +
    "\017\021\020\007\021\017\022\006\023\015\024\005\025" +
    "\010\026\012\027\011\001\002\000\004\012\022\001\002" +
    "\000\034\004\013\005\004\012\020\016\014\017\021\020" +
    "\007\021\017\022\006\023\015\024\005\025\010\026\012" +
    "\027\011\001\002\000\014\004\025\005\026\006\030\007" +
    "\027\014\024\001\002\000\034\004\013\005\004\012\020" +
    "\016\014\017\021\020\007\021\017\022\006\023\015\024" +
    "\005\025\010\026\012\027\011\001\002\000\034\004\013" +
    "\005\004\012\020\016\014\017\021\020\007\021\017\022" +
    "\006\023\015\024\005\025\010\026\012\027\011\001\002" +
    "\000\034\004\013\005\004\012\020\016\014\017\021\020" +
    "\007\021\017\022\006\023\015\024\005\025\010\026\012" +
    "\027\011\001\002\000\034\004\013\005\004\012\020\016" +
    "\014\017\021\020\007\021\017\022\006\023\015\024\005" +
    "\025\010\026\012\027\011\001\002\000\034\004\013\005" +
    "\004\012\020\016\014\017\021\020\007\021\017\022\006" +
    "\023\015\024\005\025\010\026\012\027\011\001\002\000" +
    "\020\002\ufffe\004\ufffe\005\ufffe\006\ufffe\007\027\013\ufffe" +
    "\014\ufffe\001\002\000\020\002\ufffd\004\ufffd\005\ufffd\006" +
    "\ufffd\007\ufffd\013\ufffd\014\ufffd\001\002\000\020\002\uffff" +
    "\004\uffff\005\uffff\006\030\007\027\013\uffff\014\uffff\001" +
    "\002\000\020\002\001\004\001\005\001\006\030\007\027" +
    "\013\001\014\001\001\002\000\014\004\025\005\026\006" +
    "\030\007\027\013\036\001\002\000\020\002\ufffc\004\ufffc" +
    "\005\ufffc\006\ufffc\007\ufffc\013\ufffc\014\ufffc\001\002\000" +
    "\014\004\025\005\026\006\030\007\027\013\040\001\002" +
    "\000\020\002\ufff2\004\ufff2\005\ufff2\006\ufff2\007\ufff2\013" +
    "\ufff2\014\ufff2\001\002\000\034\004\013\005\004\012\020" +
    "\016\014\017\021\020\007\021\017\022\006\023\015\024" +
    "\005\025\010\026\012\027\011\001\002\000\014\004\025" +
    "\005\026\006\030\007\027\014\043\001\002\000\006\005" +
    "\045\024\044\001\002\000\004\013\050\001\002\000\004" +
    "\024\046\001\002\000\004\013\047\001\002\000\020\002" +
    "\ufff7\004\ufff7\005\ufff7\006\ufff7\007\ufff7\013\ufff7\014\ufff7" +
    "\001\002\000\020\002\ufff8\004\ufff8\005\ufff8\006\ufff8\007" +
    "\ufff8\013\ufff8\014\ufff8\001\002\000\004\002\000\001\002" +
    "\000\034\004\013\005\004\012\020\016\014\017\021\020" +
    "\007\021\017\022\006\023\015\024\005\025\010\026\012" +
    "\027\011\001\002\000\014\004\025\005\026\006\030\007" +
    "\027\014\054\001\002\000\034\004\013\005\004\012\020" +
    "\016\014\017\021\020\007\021\017\022\006\023\015\024" +
    "\005\025\010\026\012\027\011\001\002\000\014\004\025" +
    "\005\026\006\030\007\027\014\056\001\002\000\004\024" +
    "\057\001\002\000\004\013\060\001\002\000\020\002\ufff5" +
    "\004\ufff5\005\ufff5\006\ufff5\007\ufff5\013\ufff5\014\ufff5\001" +
    "\002\000\020\002\ufff3\004\ufff3\005\ufff3\006\ufff3\007\ufff3" +
    "\013\ufff3\014\ufff3\001\002\000\034\004\013\005\004\012" +
    "\020\016\014\017\021\020\007\021\017\022\006\023\015" +
    "\024\005\025\010\026\012\027\011\001\002\000\020\002" +
    "\ufff9\004\ufff9\005\ufff9\006\ufff9\007\ufff9\013\ufff9\014\ufff9" +
    "\001\002\000\034\004\013\005\004\012\020\016\014\017" +
    "\021\020\007\021\017\022\006\023\015\024\005\025\010" +
    "\026\012\027\011\001\002\000\014\004\025\005\026\006" +
    "\030\007\027\014\066\001\002\000\034\004\013\005\004" +
    "\012\020\016\014\017\021\020\007\021\017\022\006\023" +
    "\015\024\005\025\010\026\012\027\011\001\002\000\014" +
    "\004\025\005\026\006\030\007\027\013\070\001\002\000" +
    "\020\002\ufffb\004\ufffb\005\ufffb\006\ufffb\007\ufffb\013\ufffb" +
    "\014\ufffb\001\002\000\034\004\013\005\004\012\020\016" +
    "\014\017\021\020\007\021\017\022\006\023\015\024\005" +
    "\025\010\026\012\027\011\001\002\000\014\004\025\005" +
    "\026\006\030\007\027\014\073\001\002\000\004\024\074" +
    "\001\002\000\004\014\075\001\002\000\004\024\076\001" +
    "\002\000\004\014\077\001\002\000\004\024\100\001\002" +
    "\000\004\013\101\001\002\000\020\002\ufff6\004\ufff6\005" +
    "\ufff6\006\ufff6\007\ufff6\013\ufff6\014\ufff6\001\002\000\034" +
    "\004\013\005\004\012\020\016\014\017\021\020\007\021" +
    "\017\022\006\023\015\024\005\025\010\026\012\027\011" +
    "\001\002\000\020\002\ufffa\004\ufffa\005\ufffa\006\ufffa\007" +
    "\ufffa\013\ufffa\014\ufffa\001\002\000\020\002\ufff4\004\ufff4" +
    "\005\ufff4\006\ufff4\007\ufff4\013\ufff4\014\ufff4\001\002" });

  /** Access to parse-action table. */
  public short[][] action_table() {return _action_table;}

  /** <code>reduce_goto</code> table. */
  protected static final short[][] _reduce_table = 
    unpackFromStrings(new String[] {
    "\000\102\000\004\002\015\001\001\000\004\002\103\001" +
    "\001\000\002\001\001\000\002\001\001\000\002\001\001" +
    "\000\002\001\001\000\002\001\001\000\002\001\001\000" +
    "\004\002\060\001\001\000\002\001\001\000\002\001\001" +
    "\000\002\001\001\000\002\001\001\000\004\002\036\001" +
    "\001\000\002\001\001\000\004\002\022\001\001\000\002" +
    "\001\001\000\004\002\034\001\001\000\004\002\033\001" +
    "\001\000\004\002\032\001\001\000\004\002\031\001\001" +
    "\000\004\002\030\001\001\000\002\001\001\000\002\001" +
    "\001\000\002\001\001\000\002\001\001\000\002\001\001" +
    "\000\002\001\001\000\002\001\001\000\002\001\001\000" +
    "\004\002\041\001\001\000\002\001\001\000\002\001\001" +
    "\000\002\001\001\000\002\001\001\000\002\001\001\000" +
    "\002\001\001\000\002\001\001\000\002\001\001\000\004" +
    "\002\052\001\001\000\002\001\001\000\004\002\054\001" +
    "\001\000\002\001\001\000\002\001\001\000\002\001\001" +
    "\000\002\001\001\000\002\001\001\000\004\002\062\001" +
    "\001\000\002\001\001\000\004\002\064\001\001\000\002" +
    "\001\001\000\004\002\066\001\001\000\002\001\001\000" +
    "\002\001\001\000\004\002\071\001\001\000\002\001\001" +
    "\000\002\001\001\000\002\001\001\000\002\001\001\000" +
    "\002\001\001\000\002\001\001\000\002\001\001\000\002" +
    "\001\001\000\004\002\102\001\001\000\002\001\001\000" +
    "\002\001\001" });

  /** Access to <code>reduce_goto</code> table. */
  public short[][] reduce_table() {return _reduce_table;}

  /** Instance of action encapsulation class. */
  protected CUP$Parser$actions action_obj;

  /** Action encapsulation object initializer. */
  protected void init_actions()
    {
      action_obj = new CUP$Parser$actions(this);
    }

  /** Invoke a user supplied parse action. */
  public java_cup.runtime.Symbol do_action(
    int                        act_num,
    java_cup.runtime.lr_parser parser,
    java.util.Stack            stack,
    int                        top)
    throws java.lang.Exception
  {
    /* call code in generated class */
    return action_obj.CUP$Parser$do_action(act_num, parser, stack, top);
  }

  /** Indicates start state. */
  public int start_state() {return 0;}
  /** Indicates start production. */
  public int start_production() {return 1;}

  /** <code>EOF</code> Symbol index. */
  public int EOF_sym() {return 0;}

  /** <code>error</code> Symbol index. */
  public int error_sym() {return 1;}


  /** Scan to get the next Symbol. */
  public java_cup.runtime.Symbol scan()
    throws java.lang.Exception
    {
 return s.next_token(); 
    }


    // Constructor, needed for call to scan later
    private Lexer s;
   public Parser(Lexer s){super(s,s.symbolFactory); this.s=s; }
    
    private ShiftFunctionMapper shiftMapper;
    public ShiftFunctionMapper getShiftFunctionMapper() { return shiftMapper; }
    public void setShiftFunctionMapper(ShiftFunctionMapper shiftMapper) { this.shiftMapper = shiftMapper; }
    
    private CalendarMonthMapper calendarMonthMapper;
	public void setCalendarMonthMapper(CalendarMonthMapper calendarMonthMapper) {	this.calendarMonthMapper = calendarMonthMapper;	}
	public CalendarMonthMapper getCalendarMonthMapper() { return calendarMonthMapper; }
    
    private SeriesParser seriesParser;
    public SeriesParser getSeriesParser() { return seriesParser; }
    public void setSeriesParser(SeriesParser seriesParser) { this.seriesParser = seriesParser; }
    
    // Error capturing
    public List<Symbol> errors = new LinkedList<>();
	public void syntax_error(Symbol s){	errors.add(s); }
    


/** Cup generated class to encapsulate user supplied action code.*/
@SuppressWarnings({"rawtypes", "unchecked", "unused"})
class CUP$Parser$actions {
  private final Parser parser;

  /** Constructor */
  CUP$Parser$actions(Parser parser) {
    this.parser = parser;
  }

  /** Method 0 with the actual generated action code for actions 0 to 300. */
  public final java_cup.runtime.Symbol CUP$Parser$do_action_part00000000(
    int                        CUP$Parser$act_num,
    java_cup.runtime.lr_parser CUP$Parser$parser,
    java.util.Stack            CUP$Parser$stack,
    int                        CUP$Parser$top)
    throws java.lang.Exception
    {
      /* Symbol object for return from actions */
      java_cup.runtime.Symbol CUP$Parser$result;

      /* select the action based on the action number */
      switch (CUP$Parser$act_num)
        {
          /*. . . . . . . . . . . . . . . . . . . .*/
          case 0: // expr ::= expr PLUS expr 
            {
              IExpression<ISeries> RESULT =null;
		int lhsleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).left;
		int lhsright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).right;
		IExpression<ISeries> lhs = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-2)).value;
		int rhsleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int rhsright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		IExpression<ISeries> rhs = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT =new SeriesOperatorExpression('+', lhs, rhs);       
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expr",0, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 1: // $START ::= expr EOF 
            {
              Object RESULT =null;
		int start_valleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).left;
		int start_valright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).right;
		IExpression<ISeries> start_val = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		RESULT = start_val;
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("$START",0, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          /* ACCEPT */
          CUP$Parser$parser.done_parsing();
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 2: // expr ::= expr MINUS expr 
            {
              IExpression<ISeries> RESULT =null;
		int lhsleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).left;
		int lhsright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).right;
		IExpression<ISeries> lhs = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-2)).value;
		int rhsleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int rhsright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		IExpression<ISeries> rhs = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new SeriesOperatorExpression('-', lhs, rhs);      
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expr",0, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 3: // expr ::= expr TIMES expr 
            {
              IExpression<ISeries> RESULT =null;
		int lhsleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).left;
		int lhsright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).right;
		IExpression<ISeries> lhs = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-2)).value;
		int rhsleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int rhsright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		IExpression<ISeries> rhs = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new SeriesOperatorExpression('*', lhs, rhs);       
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expr",0, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 4: // expr ::= expr DIVIDE expr 
            {
              IExpression<ISeries> RESULT =null;
		int lhsleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).left;
		int lhsright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).right;
		IExpression<ISeries> lhs = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-2)).value;
		int rhsleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int rhsright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		IExpression<ISeries> rhs = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new SeriesOperatorExpression('/', lhs, rhs);       
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expr",0, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 5: // expr ::= MIN LPAREN expr COMMA expr RPAREN 
            {
              IExpression<ISeries> RESULT =null;
		int e1left = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-3)).left;
		int e1right = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-3)).right;
		IExpression<ISeries> e1 = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-3)).value;
		int e2left = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).left;
		int e2right = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).right;
		IExpression<ISeries> e2 = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		 RESULT = new FunctionConstructor(Min.class, e1, e2);       
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expr",0, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-5)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 6: // expr ::= MAX LPAREN expr COMMA expr RPAREN 
            {
              IExpression<ISeries> RESULT =null;
		int e1left = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-3)).left;
		int e1right = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-3)).right;
		IExpression<ISeries> e1 = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-3)).value;
		int e2left = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).left;
		int e2right = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).right;
		IExpression<ISeries> e2 = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		 RESULT = new FunctionConstructor(Max.class, e1, e2);       
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expr",0, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-5)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 7: // expr ::= INTEGER PERCENT expr 
            {
              IExpression<ISeries> RESULT =null;
		int lhsleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).left;
		int lhsright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).right;
		Integer lhs = (Integer)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-2)).value;
		int rhsleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int rhsright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		IExpression<ISeries> rhs = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new SeriesOperatorExpression('%',    new ConstantSeriesExpression(lhs), rhs); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expr",0, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 8: // expr ::= NUMBER PERCENT expr 
            {
              IExpression<ISeries> RESULT =null;
		int lhsleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).left;
		int lhsright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).right;
		Double lhs = (Double)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-2)).value;
		int rhsleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int rhsright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		IExpression<ISeries> rhs = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new SeriesOperatorExpression('%',    new ConstantSeriesExpression(lhs), rhs); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expr",0, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 9: // expr ::= SHIFT LPAREN expr COMMA INTEGER RPAREN 
            {
              IExpression<ISeries> RESULT =null;
		int e1left = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-3)).left;
		int e1right = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-3)).right;
		IExpression<ISeries> e1 = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-3)).value;
		int i1left = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).left;
		int i1right = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).right;
		Integer i1 = (Integer)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		 RESULT = new ShiftFunctionConstructor(shiftMapper, e1, i1);       
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expr",0, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-5)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 10: // expr ::= SHIFT LPAREN expr COMMA MINUS INTEGER RPAREN 
            {
              IExpression<ISeries> RESULT =null;
		int e1left = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-4)).left;
		int e1right = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-4)).right;
		IExpression<ISeries> e1 = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-4)).value;
		int i1left = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).left;
		int i1right = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).right;
		Integer i1 = (Integer)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		 RESULT = new ShiftFunctionConstructor(shiftMapper,e1,  -i1);       
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expr",0, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-6)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 11: // expr ::= DATEDAVG LPAREN expr COMMA INTEGER COMMA INTEGER COMMA INTEGER RPAREN 
            {
              IExpression<ISeries> RESULT =null;
		int lhsleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-7)).left;
		int lhsright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-7)).right;
		IExpression<ISeries> lhs = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-7)).value;
		int n1left = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-5)).left;
		int n1right = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-5)).right;
		Integer n1 = (Integer)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-5)).value;
		int n2left = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-3)).left;
		int n2right = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-3)).right;
		Integer n2 = (Integer)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-3)).value;
		int n3left = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).left;
		int n3right = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).right;
		Integer n3 = (Integer)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		 RESULT =  new DatedAvgFunctionConstructor(calendarMonthMapper, lhs, n1, n2, n3); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expr",0, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-9)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 12: // expr ::= SPLITMONTH LPAREN expr COMMA expr COMMA INTEGER RPAREN 
            {
              IExpression<ISeries> RESULT =null;
		int e1left = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-5)).left;
		int e1right = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-5)).right;
		IExpression<ISeries> e1 = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-5)).value;
		int e2left = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-3)).left;
		int e2right = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-3)).right;
		IExpression<ISeries> e2 = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-3)).value;
		int sleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).left;
		int sright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).right;
		Integer s = (Integer)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		 RESULT =  new SplitMonthFunctionConstructor(e1, e2, s); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expr",0, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-7)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 13: // expr ::= MINUS expr 
            {
              IExpression<ISeries> RESULT =null;
		int eleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int eright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		IExpression<ISeries> e = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new IExpression<ISeries>() {
						@Override
						public ISeries evaluate() {
							return new Minus(e.evaluate());
						}
					};         
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expr",0, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 14: // expr ::= PLUS expr 
            {
              IExpression<ISeries> RESULT =null;
		int eleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int eright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		IExpression<ISeries> e = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = e;         
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expr",0, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 15: // expr ::= LPAREN expr RPAREN 
            {
              IExpression<ISeries> RESULT =null;
		int eleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).left;
		int eright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).right;
		IExpression<ISeries> e = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		 RESULT = e;           
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expr",0, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 16: // expr ::= SERIES 
            {
              IExpression<ISeries> RESULT =null;
		int nleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int nright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		IExpression<ISeries> n = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = n;           
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expr",0, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 17: // expr ::= NAMED_ELEMENT 
            {
              IExpression<ISeries> RESULT =null;
		int nleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int nright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		String n = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT =  new NamedSeriesExpression(seriesParser.getSeries(n));           
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expr",0, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 18: // expr ::= NUMBER 
            {
              IExpression<ISeries> RESULT =null;
		int nleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int nright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		Double n = (Double)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT =  new ConstantSeriesExpression(n);           
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expr",0, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 19: // expr ::= INTEGER 
            {
              IExpression<ISeries> RESULT =null;
		int nleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int nright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		Integer n = (Integer)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT =  new ConstantSeriesExpression(n);           
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expr",0, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 20: // expr ::= QUESTION 
            {
              IExpression<ISeries> RESULT =null;
		int nleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int nright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		Object n = (Object)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT =  new ConstantSeriesExpression(0.0);           
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expr",0, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /* . . . . . .*/
          default:
            throw new Exception(
               "Invalid action number "+CUP$Parser$act_num+"found in internal parse table");

        }
    } /* end of method */

  /** Method splitting the generated action code into several parts. */
  public final java_cup.runtime.Symbol CUP$Parser$do_action(
    int                        CUP$Parser$act_num,
    java_cup.runtime.lr_parser CUP$Parser$parser,
    java.util.Stack            CUP$Parser$stack,
    int                        CUP$Parser$top)
    throws java.lang.Exception
    {
              return CUP$Parser$do_action_part00000000(
                               CUP$Parser$act_num,
                               CUP$Parser$parser,
                               CUP$Parser$stack,
                               CUP$Parser$top);
    }
}

}
