
//----------------------------------------------------
// The following code was generated by CUP v0.11b 20160615 (GIT 4ac7450)
//----------------------------------------------------

package com.mmxlabs.common.parser.impl;

import java_cup.runtime.*;
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
    "\000\021\000\002\002\004\000\002\002\004\000\002\002" +
    "\003\000\002\003\005\000\002\003\005\000\002\003\005" +
    "\000\002\003\005\000\002\003\010\000\002\003\010\000" +
    "\002\003\010\000\002\003\005\000\002\003\014\000\002" +
    "\003\004\000\002\003\005\000\002\003\003\000\002\003" +
    "\003\000\002\003\003" });

  /** Access to production table. */
  public short[][] production_table() {return _production_table;}

  /** Parse-action table. */
  protected static final short[][] _action_table = 
    unpackFromStrings(new String[] {
    "\000\063\000\024\006\004\012\015\016\016\017\006\020" +
    "\014\021\005\023\010\024\012\025\011\001\002\000\024" +
    "\006\004\012\015\016\016\017\006\020\014\021\005\023" +
    "\010\024\012\025\011\001\002\000\004\012\054\001\002" +
    "\000\004\012\047\001\002\000\026\002\045\006\004\012" +
    "\015\016\016\017\006\020\014\021\005\023\010\024\012" +
    "\025\011\001\002\000\042\002\ufff1\005\ufff1\006\ufff1\007" +
    "\ufff1\010\ufff1\012\ufff1\013\ufff1\014\ufff1\015\043\016\ufff1" +
    "\017\ufff1\020\ufff1\021\ufff1\023\ufff1\024\ufff1\025\ufff1\001" +
    "\002\000\040\002\ufff2\005\ufff2\006\ufff2\007\ufff2\010\ufff2" +
    "\012\ufff2\013\ufff2\014\ufff2\016\ufff2\017\ufff2\020\ufff2\021" +
    "\ufff2\023\ufff2\024\ufff2\025\ufff2\001\002\000\040\002\ufff3" +
    "\005\ufff3\006\ufff3\007\ufff3\010\ufff3\012\ufff3\013\ufff3\014" +
    "\ufff3\016\ufff3\017\ufff3\020\ufff3\021\ufff3\023\ufff3\024\ufff3" +
    "\025\ufff3\001\002\000\034\002\uffff\005\022\006\023\007" +
    "\025\010\024\012\uffff\016\uffff\017\uffff\020\uffff\021\uffff" +
    "\023\uffff\024\uffff\025\uffff\001\002\000\004\012\036\001" +
    "\002\000\024\006\004\012\015\016\016\017\006\020\014" +
    "\021\005\023\010\024\012\025\011\001\002\000\004\012" +
    "\017\001\002\000\024\006\004\012\015\016\016\017\006" +
    "\020\014\021\005\023\010\024\012\025\011\001\002\000" +
    "\014\005\022\006\023\007\025\010\024\014\021\001\002" +
    "\000\024\006\004\012\015\016\016\017\006\020\014\021" +
    "\005\023\010\024\012\025\011\001\002\000\024\006\004" +
    "\012\015\016\016\017\006\020\014\021\005\023\010\024" +
    "\012\025\011\001\002\000\024\006\004\012\015\016\016" +
    "\017\006\020\014\021\005\023\010\024\012\025\011\001" +
    "\002\000\024\006\004\012\015\016\016\017\006\020\014" +
    "\021\005\023\010\024\012\025\011\001\002\000\024\006" +
    "\004\012\015\016\016\017\006\020\014\021\005\023\010" +
    "\024\012\025\011\001\002\000\040\002\ufffc\005\ufffc\006" +
    "\ufffc\007\ufffc\010\024\012\ufffc\013\ufffc\014\ufffc\016\ufffc" +
    "\017\ufffc\020\ufffc\021\ufffc\023\ufffc\024\ufffc\025\ufffc\001" +
    "\002\000\040\002\ufffb\005\ufffb\006\ufffb\007\ufffb\010\ufffb" +
    "\012\ufffb\013\ufffb\014\ufffb\016\ufffb\017\ufffb\020\ufffb\021" +
    "\ufffb\023\ufffb\024\ufffb\025\ufffb\001\002\000\040\002\ufffd" +
    "\005\ufffd\006\ufffd\007\025\010\024\012\ufffd\013\ufffd\014" +
    "\ufffd\016\ufffd\017\ufffd\020\ufffd\021\ufffd\023\ufffd\024\ufffd" +
    "\025\ufffd\001\002\000\040\002\ufffe\005\ufffe\006\ufffe\007" +
    "\025\010\024\012\ufffe\013\ufffe\014\ufffe\016\ufffe\017\ufffe" +
    "\020\ufffe\021\ufffe\023\ufffe\024\ufffe\025\ufffe\001\002\000" +
    "\014\005\022\006\023\007\025\010\024\013\033\001\002" +
    "\000\040\002\ufffa\005\ufffa\006\ufffa\007\ufffa\010\ufffa\012" +
    "\ufffa\013\ufffa\014\ufffa\016\ufffa\017\ufffa\020\ufffa\021\ufffa" +
    "\023\ufffa\024\ufffa\025\ufffa\001\002\000\014\005\022\006" +
    "\023\007\025\010\024\013\035\001\002\000\040\002\ufff4" +
    "\005\ufff4\006\ufff4\007\ufff4\010\ufff4\012\ufff4\013\ufff4\014" +
    "\ufff4\016\ufff4\017\ufff4\020\ufff4\021\ufff4\023\ufff4\024\ufff4" +
    "\025\ufff4\001\002\000\024\006\004\012\015\016\016\017" +
    "\006\020\014\021\005\023\010\024\012\025\011\001\002" +
    "\000\014\005\022\006\023\007\025\010\024\014\040\001" +
    "\002\000\024\006\004\012\015\016\016\017\006\020\014" +
    "\021\005\023\010\024\012\025\011\001\002\000\014\005" +
    "\022\006\023\007\025\010\024\013\042\001\002\000\040" +
    "\002\ufff8\005\ufff8\006\ufff8\007\ufff8\010\ufff8\012\ufff8\013" +
    "\ufff8\014\ufff8\016\ufff8\017\ufff8\020\ufff8\021\ufff8\023\ufff8" +
    "\024\ufff8\025\ufff8\001\002\000\024\006\004\012\015\016" +
    "\016\017\006\020\014\021\005\023\010\024\012\025\011" +
    "\001\002\000\040\002\ufff7\005\ufff7\006\ufff7\007\ufff7\010" +
    "\ufff7\012\ufff7\013\ufff7\014\ufff7\016\ufff7\017\ufff7\020\ufff7" +
    "\021\ufff7\023\ufff7\024\ufff7\025\ufff7\001\002\000\004\002" +
    "\000\001\002\000\034\002\001\005\022\006\023\007\025" +
    "\010\024\012\001\016\001\017\001\020\001\021\001\023" +
    "\001\024\001\025\001\001\002\000\024\006\004\012\015" +
    "\016\016\017\006\020\014\021\005\023\010\024\012\025" +
    "\011\001\002\000\014\005\022\006\023\007\025\010\024" +
    "\014\051\001\002\000\024\006\004\012\015\016\016\017" +
    "\006\020\014\021\005\023\010\024\012\025\011\001\002" +
    "\000\014\005\022\006\023\007\025\010\024\013\053\001" +
    "\002\000\040\002\ufff9\005\ufff9\006\ufff9\007\ufff9\010\ufff9" +
    "\012\ufff9\013\ufff9\014\ufff9\016\ufff9\017\ufff9\020\ufff9\021" +
    "\ufff9\023\ufff9\024\ufff9\025\ufff9\001\002\000\024\006\004" +
    "\012\015\016\016\017\006\020\014\021\005\023\010\024" +
    "\012\025\011\001\002\000\014\005\022\006\023\007\025" +
    "\010\024\014\056\001\002\000\004\023\057\001\002\000" +
    "\004\014\060\001\002\000\004\023\061\001\002\000\004" +
    "\014\062\001\002\000\004\023\063\001\002\000\004\013" +
    "\064\001\002\000\040\002\ufff6\005\ufff6\006\ufff6\007\ufff6" +
    "\010\ufff6\012\ufff6\013\ufff6\014\ufff6\016\ufff6\017\ufff6\020" +
    "\ufff6\021\ufff6\023\ufff6\024\ufff6\025\ufff6\001\002\000\040" +
    "\002\ufff5\005\ufff5\006\ufff5\007\ufff5\010\ufff5\012\ufff5\013" +
    "\ufff5\014\ufff5\016\ufff5\017\ufff5\020\ufff5\021\ufff5\023\ufff5" +
    "\024\ufff5\025\ufff5\001\002" });

  /** Access to parse-action table. */
  public short[][] action_table() {return _action_table;}

  /** <code>reduce_goto</code> table. */
  protected static final short[][] _reduce_table = 
    unpackFromStrings(new String[] {
    "\000\063\000\006\002\006\003\012\001\001\000\004\003" +
    "\064\001\001\000\002\001\001\000\002\001\001\000\004" +
    "\003\045\001\001\000\002\001\001\000\002\001\001\000" +
    "\002\001\001\000\002\001\001\000\002\001\001\000\004" +
    "\003\033\001\001\000\002\001\001\000\004\003\017\001" +
    "\001\000\002\001\001\000\004\003\031\001\001\000\004" +
    "\003\030\001\001\000\004\003\027\001\001\000\004\003" +
    "\026\001\001\000\004\003\025\001\001\000\002\001\001" +
    "\000\002\001\001\000\002\001\001\000\002\001\001\000" +
    "\002\001\001\000\002\001\001\000\002\001\001\000\002" +
    "\001\001\000\004\003\036\001\001\000\002\001\001\000" +
    "\004\003\040\001\001\000\002\001\001\000\002\001\001" +
    "\000\004\003\043\001\001\000\002\001\001\000\002\001" +
    "\001\000\002\001\001\000\004\003\047\001\001\000\002" +
    "\001\001\000\004\003\051\001\001\000\002\001\001\000" +
    "\002\001\001\000\004\003\054\001\001\000\002\001\001" +
    "\000\002\001\001\000\002\001\001\000\002\001\001\000" +
    "\002\001\001\000\002\001\001\000\002\001\001\000\002" +
    "\001\001\000\002\001\001" });

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
          case 0: // expr_list ::= expr_list expr 
            {
              Object RESULT =null;
		int eleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int eright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		IExpression<ISeries> e = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT =e; 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expr_list",0, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 1: // $START ::= expr_list EOF 
            {
              Object RESULT =null;
		int start_valleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).left;
		int start_valright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).right;
		Object start_val = (Object)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		RESULT = start_val;
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("$START",0, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          /* ACCEPT */
          CUP$Parser$parser.done_parsing();
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 2: // expr_list ::= expr 
            {
              Object RESULT =null;
		int eleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int eright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		IExpression<ISeries> e = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		RESULT = e; 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expr_list",0, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 3: // expr ::= expr PLUS expr 
            {
              IExpression<ISeries> RESULT =null;
		int lhsleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).left;
		int lhsright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).right;
		IExpression<ISeries> lhs = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-2)).value;
		int rhsleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int rhsright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		IExpression<ISeries> rhs = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT =new SeriesOperatorExpression('+', lhs, rhs);       
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expr",1, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 4: // expr ::= expr MINUS expr 
            {
              IExpression<ISeries> RESULT =null;
		int lhsleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).left;
		int lhsright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).right;
		IExpression<ISeries> lhs = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-2)).value;
		int rhsleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int rhsright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		IExpression<ISeries> rhs = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new SeriesOperatorExpression('-', lhs, rhs);      
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expr",1, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 5: // expr ::= expr TIMES expr 
            {
              IExpression<ISeries> RESULT =null;
		int lhsleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).left;
		int lhsright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).right;
		IExpression<ISeries> lhs = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-2)).value;
		int rhsleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int rhsright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		IExpression<ISeries> rhs = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new SeriesOperatorExpression('*', lhs, rhs);       
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expr",1, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 6: // expr ::= expr DIVIDE expr 
            {
              IExpression<ISeries> RESULT =null;
		int lhsleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).left;
		int lhsright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).right;
		IExpression<ISeries> lhs = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-2)).value;
		int rhsleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int rhsright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		IExpression<ISeries> rhs = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new SeriesOperatorExpression('/', lhs, rhs);       
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expr",1, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 7: // expr ::= MIN LPAREN expr COMMA expr RPAREN 
            {
              IExpression<ISeries> RESULT =null;
		int e1left = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-3)).left;
		int e1right = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-3)).right;
		IExpression<ISeries> e1 = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-3)).value;
		int e2left = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).left;
		int e2right = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).right;
		IExpression<ISeries> e2 = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		 RESULT = new FunctionConstructor(Min.class, e1, e2);       
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expr",1, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-5)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 8: // expr ::= MAX LPAREN expr COMMA expr RPAREN 
            {
              IExpression<ISeries> RESULT =null;
		int e1left = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-3)).left;
		int e1right = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-3)).right;
		IExpression<ISeries> e1 = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-3)).value;
		int e2left = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).left;
		int e2right = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).right;
		IExpression<ISeries> e2 = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		 RESULT = new FunctionConstructor(Max.class, e1, e2);       
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expr",1, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-5)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 9: // expr ::= SHIFT LPAREN expr COMMA expr RPAREN 
            {
              IExpression<ISeries> RESULT =null;
		int e1left = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-3)).left;
		int e1right = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-3)).right;
		IExpression<ISeries> e1 = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-3)).value;
		int e2left = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).left;
		int e2right = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).right;
		IExpression<ISeries> e2 = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		 RESULT = new ShiftFunctionConstructor(shiftMapper, e1, e2);       
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expr",1, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-5)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 10: // expr ::= NUMBER PERCENT expr 
            {
              IExpression<ISeries> RESULT =null;
		int lhsleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).left;
		int lhsright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).right;
		Double lhs = (Double)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-2)).value;
		int rhsleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int rhsright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		IExpression<ISeries> rhs = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new SeriesOperatorExpression('%',  new ConstantSeriesExpression(lhs), rhs); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expr",1, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 11: // expr ::= DATEDAVG LPAREN expr COMMA NUMBER COMMA NUMBER COMMA NUMBER RPAREN 
            {
              IExpression<ISeries> RESULT =null;
		int lhsleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-7)).left;
		int lhsright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-7)).right;
		IExpression<ISeries> lhs = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-7)).value;
		int n1left = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-5)).left;
		int n1right = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-5)).right;
		Double n1 = (Double)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-5)).value;
		int n2left = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-3)).left;
		int n2right = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-3)).right;
		Double n2 = (Double)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-3)).value;
		int n3left = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).left;
		int n3right = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).right;
		Double n3 = (Double)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		 RESULT =  new DatedAvgFunctionConstructor(calendarMonthMapper, lhs, n1, n2, n3); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expr",1, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-9)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 12: // expr ::= MINUS expr 
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
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expr",1, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 13: // expr ::= LPAREN expr RPAREN 
            {
              IExpression<ISeries> RESULT =null;
		int eleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).left;
		int eright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).right;
		IExpression<ISeries> e = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		 RESULT = e;           
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expr",1, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 14: // expr ::= SERIES 
            {
              IExpression<ISeries> RESULT =null;
		int nleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int nright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		IExpression<ISeries> n = (IExpression<ISeries>)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = n;           
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expr",1, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 15: // expr ::= NAMED_ELEMENT 
            {
              IExpression<ISeries> RESULT =null;
		int nleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int nright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		String n = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT =  new NamedSeriesExpression(seriesParser.getSeries(n));           
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expr",1, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 16: // expr ::= NUMBER 
            {
              IExpression<ISeries> RESULT =null;
		int nleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int nright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		Double n = (Double)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT =  new ConstantSeriesExpression(n);           
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("expr",1, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
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
