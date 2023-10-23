/* The following code was generated by JFlex 1.6.1 */

// Originally based on example from http://www2.cs.tum.edu/projekte/cup/examples.php

package com.mmxlabs.common.parser.impl;

import com.mmxlabs.common.parser.astnodes.*;

import java.time.Month;
import java_cup.runtime.Symbol;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.ComplexSymbolFactory.Location;



/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.6.1
 * from the specification file <tt>seriesparser.flex</tt>
 */
public class Lexer implements java_cup.runtime.Scanner, ParserSymbols {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;
  public static final int STRING = 2;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0,  0,  1, 1
  };

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\0\1\22\1\20\1\0\1\22\1\17\22\0\1\22\1\0\1\57"+
    "\1\10\1\0\1\66\2\0\1\62\1\63\1\64\1\4\1\60\1\13"+
    "\1\2\1\65\1\5\1\14\1\12\1\15\6\1\1\61\1\21\1\52"+
    "\1\53\1\54\1\67\1\6\1\24\1\46\1\23\1\43\1\3\1\26"+
    "\1\45\1\41\1\34\1\50\1\11\1\27\1\32\1\35\1\30\1\25"+
    "\1\11\1\31\1\40\1\42\1\47\1\44\1\11\1\36\1\51\1\11"+
    "\1\55\1\0\1\56\1\0\1\7\1\0\1\24\1\46\1\23\1\43"+
    "\1\3\1\26\1\45\1\41\1\34\1\50\1\11\1\27\1\16\1\35"+
    "\1\30\1\25\1\11\1\31\1\40\1\42\1\47\1\44\1\11\1\36"+
    "\1\51\1\11\265\0\2\33\115\0\1\37\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\ufe90\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\2\0\1\1\1\2\1\1\1\3\1\4\1\2\2\1"+
    "\1\2\1\5\1\6\2\7\1\10\4\3\1\6\1\3"+
    "\2\11\4\3\1\12\1\13\1\14\1\15\1\16\1\17"+
    "\1\20\1\21\1\22\1\23\1\24\1\25\1\26\1\27"+
    "\1\30\1\3\1\30\1\0\1\31\1\2\1\32\1\0"+
    "\1\3\1\0\1\3\1\0\7\3\3\0\3\3\1\0"+
    "\6\3\1\33\1\34\2\0\1\35\1\2\2\32\1\36"+
    "\1\37\1\40\2\41\1\42\1\43\1\44\1\45\1\3"+
    "\1\46\1\47\1\50\2\0\1\50\2\3\1\0\1\3"+
    "\1\51\2\3\1\52\1\53\1\54\1\30\1\2\1\32"+
    "\1\3\2\0\2\3\2\55\1\3\1\0\1\3\1\0"+
    "\1\56\1\0\1\57\1\3\1\57\1\0\2\3\2\60"+
    "\4\0\1\3\1\0\2\3\2\0\1\3\1\0\2\3"+
    "\1\61\1\0\1\61\1\0\1\3\1\0\1\3\1\62"+
    "\1\0\1\3\2\63\2\64";

  private static int [] zzUnpackAction() {
    int [] result = new int[160];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\70\0\160\0\250\0\340\0\u0118\0\160\0\u0150"+
    "\0\u0188\0\u01c0\0\u01f8\0\160\0\u0230\0\u0268\0\160\0\160"+
    "\0\u02a0\0\u02d8\0\u0310\0\u0348\0\u0380\0\u03b8\0\u03f0\0\u0428"+
    "\0\u0460\0\u0498\0\u04d0\0\u0508\0\u0540\0\160\0\u0578\0\160"+
    "\0\160\0\160\0\160\0\160\0\160\0\160\0\160\0\160"+
    "\0\160\0\160\0\u05b0\0\u05e8\0\u0150\0\u0620\0\u0658\0\u0690"+
    "\0\u06c8\0\u0700\0\u0738\0\u0770\0\u07a8\0\u07e0\0\u0818\0\u0850"+
    "\0\u0888\0\u08c0\0\u08f8\0\u0930\0\u0968\0\u09a0\0\u09d8\0\u0a10"+
    "\0\u0a48\0\u0a80\0\u0ab8\0\u0af0\0\u0b28\0\u0b60\0\u0b98\0\u0bd0"+
    "\0\u0c08\0\u0c40\0\160\0\160\0\u0c78\0\u0cb0\0\u0ce8\0\u0d20"+
    "\0\u0118\0\u0d58\0\u0118\0\u0118\0\u0118\0\160\0\u0118\0\u0118"+
    "\0\u0118\0\u0118\0\u0118\0\u0d90\0\u0118\0\u0118\0\160\0\u0dc8"+
    "\0\u0e00\0\u0118\0\u0e38\0\u0e70\0\u0ea8\0\u0ee0\0\u0118\0\u0f18"+
    "\0\u0f50\0\u0118\0\u0118\0\u0118\0\u0cb0\0\u0f88\0\160\0\u0fc0"+
    "\0\u0ff8\0\u1030\0\u1068\0\u10a0\0\u10d8\0\u1110\0\u1148\0\u1180"+
    "\0\u11b8\0\u11f0\0\u0118\0\u1228\0\160\0\u1260\0\u0118\0\u1298"+
    "\0\u12d0\0\u1308\0\160\0\u0118\0\u1340\0\u1378\0\u13b0\0\u13e8"+
    "\0\u1420\0\u1458\0\u1490\0\u14c8\0\u1500\0\u1538\0\u1570\0\u15a8"+
    "\0\u15e0\0\u1618\0\160\0\u1650\0\u1650\0\u1688\0\u16c0\0\u16f8"+
    "\0\u1730\0\u0118\0\u1768\0\u17a0\0\160\0\u0118\0\160\0\u0118";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[160];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\6"+
    "\1\12\1\6\1\13\1\14\2\4\1\15\1\16\1\17"+
    "\1\20\1\17\1\21\1\22\1\6\1\23\1\6\1\24"+
    "\1\6\1\25\1\3\1\6\1\26\1\6\1\27\1\30"+
    "\1\6\1\31\1\32\3\6\1\33\1\34\1\6\1\35"+
    "\1\36\1\37\1\40\1\41\1\42\1\43\1\44\1\45"+
    "\1\46\1\47\1\50\1\51\1\52\70\3\71\0\1\4"+
    "\1\53\1\54\1\0\1\4\1\0\1\6\1\0\1\6"+
    "\1\4\1\0\2\4\1\6\4\0\10\6\1\0\3\6"+
    "\1\0\12\6\17\0\1\53\3\0\1\53\4\0\1\53"+
    "\1\0\2\53\53\0\1\6\1\0\1\6\1\0\1\6"+
    "\1\0\1\6\1\0\2\6\1\0\3\6\4\0\10\6"+
    "\1\0\3\6\1\0\12\6\17\0\1\55\1\53\1\54"+
    "\1\0\1\55\1\0\1\6\1\0\1\6\1\55\1\0"+
    "\2\55\1\6\4\0\10\6\1\0\3\6\1\0\12\6"+
    "\17\0\1\56\1\0\1\56\1\0\1\56\1\0\1\56"+
    "\1\0\2\56\1\0\3\56\4\0\10\56\1\0\3\56"+
    "\1\0\12\56\21\0\1\57\5\0\1\57\4\0\1\57"+
    "\4\0\10\57\1\0\3\57\1\0\12\57\17\0\1\4"+
    "\1\53\1\54\1\0\1\60\1\0\1\6\1\0\1\6"+
    "\1\4\1\0\2\4\1\6\4\0\10\6\1\0\3\6"+
    "\1\0\12\6\17\0\1\61\1\0\1\6\1\0\1\61"+
    "\1\0\1\6\1\0\1\6\1\61\1\62\2\61\1\6"+
    "\4\0\1\6\1\63\6\6\1\64\1\65\2\6\1\0"+
    "\12\6\36\0\1\66\50\0\1\6\1\0\1\6\1\0"+
    "\1\6\1\0\1\6\1\0\2\6\1\0\3\6\4\0"+
    "\1\6\1\67\6\6\1\0\3\6\1\0\12\6\17\0"+
    "\1\6\1\0\1\6\1\0\1\6\1\0\1\6\1\0"+
    "\2\6\1\0\3\6\4\0\2\6\1\70\5\6\1\0"+
    "\3\6\1\0\7\6\1\71\2\6\17\0\1\6\1\0"+
    "\1\72\1\0\1\6\1\0\1\6\1\0\2\6\1\0"+
    "\3\6\4\0\4\6\1\73\3\6\1\0\3\6\1\0"+
    "\12\6\17\0\1\6\1\0\1\6\1\0\1\6\1\0"+
    "\1\6\1\0\2\6\1\0\3\6\4\0\1\74\7\6"+
    "\1\0\3\6\1\0\12\6\17\0\1\6\1\0\1\6"+
    "\1\0\1\6\1\0\1\6\1\0\2\6\1\0\3\6"+
    "\4\0\1\6\1\63\6\6\1\64\1\65\2\6\1\0"+
    "\12\6\17\0\1\6\1\0\1\6\1\0\1\6\1\0"+
    "\1\6\1\0\2\6\1\0\3\6\4\0\5\6\1\75"+
    "\2\6\1\0\3\6\1\0\12\6\21\0\1\76\21\0"+
    "\1\77\13\0\1\100\27\0\1\6\1\0\1\101\1\0"+
    "\1\6\1\0\1\6\1\0\2\6\1\0\3\6\4\0"+
    "\2\6\1\102\5\6\1\0\3\6\1\0\1\6\1\103"+
    "\10\6\17\0\1\6\1\0\1\6\1\0\1\6\1\0"+
    "\1\6\1\0\2\6\1\0\3\6\4\0\10\6\1\104"+
    "\1\105\2\6\1\0\12\6\17\0\1\6\1\0\1\106"+
    "\1\0\1\6\1\0\1\6\1\0\2\6\1\0\3\6"+
    "\4\0\1\6\1\107\6\6\1\0\3\6\1\0\12\6"+
    "\17\0\1\6\1\0\1\6\1\0\1\6\1\0\1\6"+
    "\1\0\2\6\1\0\3\6\4\0\10\6\1\0\1\6"+
    "\1\110\1\6\1\0\12\6\17\0\1\6\1\0\1\6"+
    "\1\0\1\6\1\0\1\6\1\0\2\6\1\0\3\6"+
    "\4\0\1\6\1\111\6\6\1\0\3\6\1\0\7\6"+
    "\1\112\2\6\71\0\1\113\67\0\1\114\15\0\1\53"+
    "\1\0\1\115\1\0\1\53\4\0\1\53\1\0\2\53"+
    "\53\0\1\6\1\0\1\6\1\116\1\6\1\0\1\6"+
    "\1\0\2\6\1\116\3\6\4\0\10\6\1\0\3\6"+
    "\1\0\12\6\17\0\1\56\1\0\1\117\1\0\1\56"+
    "\1\0\1\117\1\0\1\117\1\56\1\0\2\56\1\117"+
    "\4\0\10\117\1\0\3\117\1\0\12\117\17\0\1\57"+
    "\1\0\1\57\1\0\1\57\1\0\1\57\1\0\2\57"+
    "\1\0\3\57\4\0\10\57\1\0\3\57\1\0\12\57"+
    "\17\0\1\120\1\53\1\54\1\0\1\120\1\0\1\6"+
    "\1\0\1\6\1\120\1\0\2\120\1\6\4\0\10\6"+
    "\1\0\3\6\1\0\12\6\17\0\1\121\1\0\1\6"+
    "\1\0\1\121\1\0\1\6\1\0\1\6\1\121\1\0"+
    "\2\121\1\6\4\0\10\6\1\0\3\6\1\0\12\6"+
    "\17\0\1\122\10\0\1\122\1\0\2\122\53\0\1\6"+
    "\1\0\1\6\1\0\1\6\1\0\1\6\1\0\2\6"+
    "\1\0\3\6\4\0\6\6\1\123\1\6\1\0\2\6"+
    "\1\124\1\0\11\6\1\125\53\0\1\126\33\0\1\6"+
    "\1\0\1\6\1\0\1\6\1\0\1\6\1\0\2\6"+
    "\1\0\3\6\4\0\10\6\1\0\1\6\1\127\1\6"+
    "\1\0\12\6\37\0\1\17\47\0\1\6\1\0\1\6"+
    "\1\0\1\6\1\0\1\6\1\0\2\6\1\0\3\6"+
    "\4\0\2\6\1\130\5\6\1\0\3\6\1\0\12\6"+
    "\17\0\1\6\1\0\1\6\1\0\1\6\1\0\1\6"+
    "\1\0\2\6\1\0\3\6\4\0\6\6\1\131\1\6"+
    "\1\0\3\6\1\0\12\6\17\0\1\6\1\0\1\6"+
    "\1\0\1\6\1\0\1\6\1\0\2\6\1\0\3\6"+
    "\4\0\10\6\1\0\3\6\1\0\5\6\1\132\4\6"+
    "\17\0\1\6\1\0\1\6\1\0\1\6\1\0\1\6"+
    "\1\0\2\6\1\0\3\6\4\0\10\6\1\0\3\6"+
    "\1\0\6\6\1\133\3\6\17\0\1\6\1\0\1\6"+
    "\1\0\1\6\1\0\1\6\1\0\2\6\1\0\3\6"+
    "\4\0\5\6\1\134\2\6\1\0\3\6\1\0\12\6"+
    "\17\0\1\6\1\0\1\6\1\0\1\6\1\0\1\6"+
    "\1\0\2\6\1\0\3\6\4\0\10\6\1\0\3\6"+
    "\1\0\2\6\1\135\7\6\17\0\1\6\1\0\1\6"+
    "\1\0\1\6\1\0\1\6\1\0\2\6\1\0\3\6"+
    "\4\0\10\6\1\0\3\6\1\0\4\6\1\136\5\6"+
    "\43\0\1\137\71\0\1\140\73\0\2\141\34\0\1\6"+
    "\1\0\1\6\1\0\1\6\1\0\1\6\1\0\2\6"+
    "\1\0\3\6\4\0\2\6\1\142\5\6\1\0\3\6"+
    "\1\0\12\6\17\0\1\6\1\0\1\6\1\0\1\6"+
    "\1\0\1\6\1\0\2\6\1\0\3\6\4\0\4\6"+
    "\1\143\3\6\1\0\3\6\1\0\12\6\17\0\1\6"+
    "\1\0\1\6\1\0\1\6\1\0\1\6\1\0\2\6"+
    "\1\0\3\6\4\0\10\6\1\141\1\144\2\6\1\0"+
    "\12\6\21\0\1\145\65\0\1\6\1\0\1\146\1\0"+
    "\1\6\1\0\1\6\1\0\2\6\1\0\3\6\4\0"+
    "\10\6\1\0\3\6\1\0\12\6\17\0\1\6\1\0"+
    "\1\6\1\0\1\6\1\0\1\6\1\0\2\6\1\0"+
    "\3\6\4\0\1\147\7\6\1\0\3\6\1\0\12\6"+
    "\17\0\1\6\1\0\1\6\1\0\1\6\1\0\1\6"+
    "\1\0\2\6\1\0\3\6\4\0\10\6\1\0\3\6"+
    "\1\0\2\6\1\150\7\6\17\0\1\6\1\0\1\6"+
    "\1\0\1\6\1\0\1\6\1\0\2\6\1\0\3\6"+
    "\4\0\10\6\1\0\3\6\1\0\2\6\1\151\7\6"+
    "\17\0\1\6\1\0\1\6\1\0\1\6\1\0\1\6"+
    "\1\0\2\6\1\0\3\6\4\0\10\6\1\0\1\6"+
    "\1\152\1\6\1\0\12\6\17\0\1\6\1\0\1\6"+
    "\1\0\1\6\1\0\1\6\1\0\2\6\1\0\3\6"+
    "\4\0\4\6\1\153\3\6\1\0\1\6\1\154\1\6"+
    "\1\0\12\6\17\0\1\155\2\0\1\116\1\155\4\0"+
    "\1\155\1\116\2\155\53\0\1\155\3\0\1\155\4\0"+
    "\1\155\1\0\2\155\53\0\1\117\1\0\1\117\1\0"+
    "\1\117\1\0\1\117\1\0\2\117\1\0\3\117\4\0"+
    "\10\117\1\0\3\117\1\0\12\117\17\0\1\156\1\53"+
    "\1\54\1\0\1\156\1\0\1\6\1\0\1\6\1\156"+
    "\1\0\2\156\1\6\4\0\10\6\1\0\3\6\1\0"+
    "\12\6\17\0\1\157\3\0\1\157\4\0\1\157\1\0"+
    "\2\157\53\0\1\6\1\0\1\6\1\0\1\6\1\0"+
    "\1\6\1\0\2\6\1\0\3\6\4\0\5\6\1\160"+
    "\2\6\1\0\3\6\1\0\12\6\51\0\2\161\61\0"+
    "\1\162\42\0\1\6\1\0\1\6\1\0\1\6\1\0"+
    "\1\6\1\0\2\6\1\0\3\6\4\0\10\6\1\161"+
    "\1\163\2\6\1\0\12\6\17\0\1\6\1\0\1\6"+
    "\1\0\1\6\1\0\1\6\1\0\2\6\1\0\3\6"+
    "\4\0\3\6\1\164\4\6\1\0\3\6\1\0\12\6"+
    "\47\0\1\165\37\0\1\6\1\0\1\6\1\0\1\6"+
    "\1\0\1\6\1\0\2\6\1\0\3\6\4\0\6\6"+
    "\1\166\1\6\1\0\3\6\1\0\12\6\17\0\1\6"+
    "\1\0\1\167\1\0\1\6\1\0\1\6\1\0\2\6"+
    "\1\0\3\6\4\0\10\6\1\0\3\6\1\0\12\6"+
    "\17\0\1\6\1\0\1\6\1\0\1\6\1\0\1\6"+
    "\1\0\2\6\1\0\3\6\4\0\10\6\1\170\1\171"+
    "\2\6\1\0\12\6\17\0\1\4\1\53\1\54\1\0"+
    "\1\4\1\0\1\6\1\0\1\6\1\4\1\172\2\4"+
    "\1\6\4\0\10\6\1\0\3\6\1\0\12\6\17\0"+
    "\1\6\1\0\1\6\1\0\1\6\1\0\1\6\1\0"+
    "\2\6\1\0\3\6\4\0\6\6\1\173\1\6\1\0"+
    "\3\6\1\0\12\6\60\0\1\174\67\0\1\175\26\0"+
    "\1\6\1\0\1\6\1\0\1\6\1\0\1\6\1\0"+
    "\2\6\1\0\3\6\4\0\10\6\1\0\3\6\1\0"+
    "\2\6\1\176\7\6\17\0\1\6\1\0\1\6\1\0"+
    "\1\6\1\0\1\6\1\0\2\6\1\0\3\6\4\0"+
    "\10\6\1\0\3\6\1\0\2\6\1\177\7\6\64\0"+
    "\1\200\22\0\1\6\1\0\1\6\1\0\1\6\1\0"+
    "\1\6\1\0\2\6\1\0\3\6\4\0\10\6\1\0"+
    "\3\6\1\0\6\6\1\201\3\6\17\0\1\6\1\0"+
    "\1\6\1\0\1\6\1\0\1\6\1\0\2\6\1\0"+
    "\3\6\4\0\10\6\1\0\3\6\1\0\3\6\1\202"+
    "\6\6\45\0\1\203\41\0\1\6\1\0\1\6\1\0"+
    "\1\6\1\0\1\6\1\0\2\6\1\0\3\6\4\0"+
    "\4\6\1\204\3\6\1\0\3\6\1\0\12\6\17\0"+
    "\1\205\3\0\1\206\4\0\1\205\1\0\1\207\1\205"+
    "\70\0\1\210\13\0\1\210\36\0\1\6\1\0\1\6"+
    "\1\0\1\6\1\0\1\6\1\0\2\6\1\0\2\6"+
    "\1\211\4\0\7\6\1\211\1\0\3\6\1\0\12\6"+
    "\45\0\1\212\41\0\1\6\1\0\1\6\1\0\1\6"+
    "\1\0\1\6\1\0\2\6\1\0\3\6\4\0\4\6"+
    "\1\213\3\6\1\0\3\6\1\0\12\6\17\0\1\6"+
    "\1\0\1\6\1\0\1\6\1\0\1\6\1\0\2\6"+
    "\1\0\3\6\4\0\1\6\1\214\6\6\1\0\3\6"+
    "\1\0\12\6\31\0\1\215\55\0\1\205\3\0\1\205"+
    "\4\0\1\205\1\0\2\205\53\0\1\205\3\0\1\205"+
    "\4\0\1\205\1\215\2\205\102\0\1\216\40\0\1\6"+
    "\1\0\1\6\1\0\1\6\1\0\1\6\1\0\2\6"+
    "\1\0\3\6\4\0\5\6\1\217\2\6\1\0\3\6"+
    "\1\0\12\6\21\0\1\220\65\0\1\6\1\0\1\221"+
    "\1\0\1\6\1\0\1\6\1\0\2\6\1\0\3\6"+
    "\4\0\10\6\1\0\3\6\1\0\12\6\17\0\1\6"+
    "\1\0\1\6\1\0\1\6\1\0\1\6\1\0\2\6"+
    "\1\0\3\6\4\0\10\6\1\0\3\6\1\0\4\6"+
    "\1\222\5\6\17\0\1\223\3\0\1\224\4\0\1\225"+
    "\1\0\2\225\107\0\1\226\33\0\1\6\1\0\1\6"+
    "\1\0\1\6\1\0\1\6\1\0\2\6\1\0\3\6"+
    "\4\0\10\6\1\0\1\6\1\227\1\6\1\0\12\6"+
    "\53\0\1\230\33\0\1\6\1\0\1\6\1\0\1\6"+
    "\1\0\1\6\1\0\2\6\1\0\3\6\4\0\10\6"+
    "\1\0\1\6\1\231\1\6\1\0\12\6\17\0\1\6"+
    "\1\0\1\6\1\0\1\6\1\0\1\6\1\0\2\6"+
    "\1\0\3\6\4\0\10\6\1\0\3\6\1\0\5\6"+
    "\1\232\4\6\17\0\1\223\3\0\1\223\4\0\1\223"+
    "\1\0\2\223\114\0\1\233\26\0\1\6\1\0\1\6"+
    "\1\0\1\6\1\0\1\6\1\0\2\6\1\0\3\6"+
    "\4\0\10\6\1\0\3\6\1\0\2\6\1\234\7\6"+
    "\61\0\1\235\25\0\1\6\1\0\1\6\1\0\1\6"+
    "\1\0\1\6\1\0\2\6\1\0\3\6\4\0\10\6"+
    "\1\0\3\6\1\0\3\6\1\236\6\6\57\0\1\237"+
    "\27\0\1\6\1\0\1\6\1\0\1\6\1\0\1\6"+
    "\1\0\2\6\1\0\3\6\4\0\10\6\1\0\3\6"+
    "\1\0\1\6\1\240\10\6\16\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[6104];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unknown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\2\0\1\11\3\1\1\11\4\1\1\11\2\1\2\11"+
    "\15\1\1\11\1\1\13\11\3\1\1\0\3\1\1\0"+
    "\1\1\1\0\1\1\1\0\7\1\3\0\3\1\1\0"+
    "\6\1\2\11\2\0\7\1\1\11\10\1\1\11\2\0"+
    "\3\1\1\0\11\1\1\11\1\1\2\0\5\1\1\0"+
    "\1\1\1\0\1\1\1\0\1\11\2\1\1\0\2\1"+
    "\1\11\1\1\4\0\1\1\1\0\2\1\2\0\1\1"+
    "\1\0\2\1\1\11\1\0\1\1\1\0\1\1\1\0"+
    "\2\1\1\0\1\1\1\11\1\1\1\11\1\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[160];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char zzBuffer[] = new char[ZZ_BUFFERSIZE];

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** number of newlines encountered up to the start of the matched text */
  private int yyline;

  /** the number of characters up to the start of the matched text */
  private int yychar;

  /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
  private int yycolumn;

  /** 
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;
  
  /** 
   * The number of occupied positions in zzBuffer beyond zzEndRead.
   * When a lead/high surrogate has been read from the input stream
   * into the final zzBuffer position, this will have a value of 1;
   * otherwise, it will have a value of 0.
   */
  private int zzFinalHighSurrogate = 0;

  /* user code: */
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


  /**
   * Creates a new scanner
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public Lexer(java.io.Reader in) {
    this.zzReader = in;
  }


  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x110000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 226) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }


  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   * 
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {

    /* first: make room (if you can) */
    if (zzStartRead > 0) {
      zzEndRead += zzFinalHighSurrogate;
      zzFinalHighSurrogate = 0;
      System.arraycopy(zzBuffer, zzStartRead,
                       zzBuffer, 0,
                       zzEndRead-zzStartRead);

      /* translate stored positions */
      zzEndRead-= zzStartRead;
      zzCurrentPos-= zzStartRead;
      zzMarkedPos-= zzStartRead;
      zzStartRead = 0;
    }

    /* is the buffer big enough? */
    if (zzCurrentPos >= zzBuffer.length - zzFinalHighSurrogate) {
      /* if not: blow it up */
      char newBuffer[] = new char[zzBuffer.length*2];
      System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
      zzBuffer = newBuffer;
      zzEndRead += zzFinalHighSurrogate;
      zzFinalHighSurrogate = 0;
    }

    /* fill the buffer with new input */
    int requested = zzBuffer.length - zzEndRead;
    int numRead = zzReader.read(zzBuffer, zzEndRead, requested);

    /* not supposed to occur according to specification of java.io.Reader */
    if (numRead == 0) {
      throw new java.io.IOException("Reader returned 0 characters. See JFlex examples for workaround.");
    }
    if (numRead > 0) {
      zzEndRead += numRead;
      /* If numRead == requested, we might have requested to few chars to
         encode a full Unicode character. We assume that a Reader would
         otherwise never return half characters. */
      if (numRead == requested) {
        if (Character.isHighSurrogate(zzBuffer[zzEndRead - 1])) {
          --zzEndRead;
          zzFinalHighSurrogate = 1;
        }
      }
      /* potentially more input available */
      return false;
    }

    /* numRead < 0 ==> end of stream */
    return true;
  }

    
  /**
   * Closes the input stream.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true;            /* indicate end of file */
    zzEndRead = zzStartRead;  /* invalidate buffer    */

    if (zzReader != null)
      zzReader.close();
  }


  /**
   * Resets the scanner to read from a new input stream.
   * Does not close the old reader.
   *
   * All internal variables are reset, the old input stream 
   * <b>cannot</b> be reused (internal buffer is discarded and lost).
   * Lexical state is set to <tt>ZZ_INITIAL</tt>.
   *
   * Internal scan buffer is resized down to its initial length, if it has grown.
   *
   * @param reader   the new input stream 
   */
  public final void yyreset(java.io.Reader reader) {
    zzReader = reader;
    zzAtBOL  = true;
    zzAtEOF  = false;
    zzEOFDone = false;
    zzEndRead = zzStartRead = 0;
    zzCurrentPos = zzMarkedPos = 0;
    zzFinalHighSurrogate = 0;
    yyline = yychar = yycolumn = 0;
    zzLexicalState = YYINITIAL;
    if (zzBuffer.length > ZZ_BUFFERSIZE)
      zzBuffer = new char[ZZ_BUFFERSIZE];
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final String yytext() {
    return new String( zzBuffer, zzStartRead, zzMarkedPos-zzStartRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer[zzStartRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void zzDoEOF() throws java.io.IOException {
    if (!zzEOFDone) {
      zzEOFDone = true;
      yyclose();
    }
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public java_cup.runtime.Symbol next_token() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char [] zzBufferL = zzBuffer;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      yychar+= zzMarkedPosL-zzStartRead;

      boolean zzR = false;
      int zzCh;
      int zzCharCount;
      for (zzCurrentPosL = zzStartRead  ;
           zzCurrentPosL < zzMarkedPosL ;
           zzCurrentPosL += zzCharCount ) {
        zzCh = Character.codePointAt(zzBufferL, zzCurrentPosL, zzMarkedPosL);
        zzCharCount = Character.charCount(zzCh);
        switch (zzCh) {
        case '\u000B':
        case '\u000C':
        case '\u0085':
        case '\u2028':
        case '\u2029':
          yyline++;
          yycolumn = 0;
          zzR = false;
          break;
        case '\r':
          yyline++;
          yycolumn = 0;
          zzR = true;
          break;
        case '\n':
          if (zzR)
            zzR = false;
          else {
            yyline++;
            yycolumn = 0;
          }
          break;
        default:
          zzR = false;
          yycolumn += zzCharCount;
        }
      }

      if (zzR) {
        // peek one character ahead if it is \n (if we have counted one line too much)
        boolean zzPeek;
        if (zzMarkedPosL < zzEndReadL)
          zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        else if (zzAtEOF)
          zzPeek = false;
        else {
          boolean eof = zzRefill();
          zzEndReadL = zzEndRead;
          zzMarkedPosL = zzMarkedPos;
          zzBufferL = zzBuffer;
          if (eof) 
            zzPeek = false;
          else 
            zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        }
        if (zzPeek) yyline--;
      }
      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
  
      zzState = ZZ_LEXSTATE[zzLexicalState];

      // set up zzAction for empty match case:
      int zzAttributes = zzAttrL[zzState];
      if ( (zzAttributes & 1) == 1 ) {
        zzAction = zzState;
      }


      zzForAction: {
        while (true) {
    
          if (zzCurrentPosL < zzEndReadL) {
            zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL);
            zzCurrentPosL += Character.charCount(zzInput);
          }
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL);
              zzCurrentPosL += Character.charCount(zzInput);
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
        zzAtEOF = true;
            zzDoEOF();
          {      return symbolFactory.newSymbol("EOF", EOF, new Location(yyline+1,yycolumn+1,yychar), new Location(yyline+1,yycolumn+1,yychar+1));
 }
      }
      else {
        switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
          case 1: 
            { /* throw new Error("Illegal character <"+ yytext()+">");*/
		    error("Illegal character <"+ yytext()+">");
            }
          case 53: break;
          case 2: 
            { return symbol("integer", INTEGER, Integer.valueOf(yytext()) );
            }
          case 54: break;
          case 3: 
            { return symbol("namedelement", NAMED_ELEMENT, new String(yytext()) );
            }
          case 55: break;
          case 4: 
            { return symbol("plus",PLUS  );
            }
          case 56: break;
          case 5: 
            { return symbol("minus",MINUS  );
            }
          case 57: break;
          case 6: 
            { return symbol("m", M, Integer.valueOf(0) );
            }
          case 58: break;
          case 7: 
            { /* ignore */
            }
          case 59: break;
          case 8: 
            { return symbol("semicolon",SEMICOLON);
            }
          case 60: break;
          case 9: 
            { return symbol("scurve", S);
            }
          case 61: break;
          case 10: 
            { return symbol("<",LT, ComparisonOperators.LT);
            }
          case 62: break;
          case 11: 
            { return symbol("equals",EQUALS);
            }
          case 63: break;
          case 12: 
            { return symbol(">",GT, ComparisonOperators.GT);
            }
          case 64: break;
          case 13: 
            { return symbol("[",LBRACKET);
            }
          case 65: break;
          case 14: 
            { return symbol("]",RBRACKET);
            }
          case 66: break;
          case 15: 
            { string.setLength(0); yybegin(STRING);
            }
          case 67: break;
          case 16: 
            { return symbol("comma",COMMA);
            }
          case 68: break;
          case 17: 
            { return symbol("colon",COLON);
            }
          case 69: break;
          case 18: 
            { return symbol("(",LPAREN);
            }
          case 70: break;
          case 19: 
            { return symbol(")",RPAREN);
            }
          case 71: break;
          case 20: 
            { return symbol("mul",TIMES  );
            }
          case 72: break;
          case 21: 
            { return symbol("div",DIVIDE  );
            }
          case 73: break;
          case 22: 
            { return symbol("percent",PERCENT  );
            }
          case 74: break;
          case 23: 
            { return symbol("question",QUESTION  );
            }
          case 75: break;
          case 24: 
            { return symbol("float", FLOAT, new Double(yytext()) );
            }
          case 76: break;
          case 25: 
            { return symbol("var", VAR, yytext().replace("#","") );
            }
          case 77: break;
          case 26: 
            { return symbol("m", M, Integer.valueOf(yytext().replace("m","")) );
            }
          case 78: break;
          case 27: 
            { return symbol("<=",LTE, ComparisonOperators.LTE);
            }
          case 79: break;
          case 28: 
            { return symbol(">=",GTE, ComparisonOperators.GTE);
            }
          case 80: break;
          case 29: 
            { return symbol("param", PARAM, yytext().replace("@","") );
            }
          case 81: break;
          case 30: 
            { return symbol("mar",MONTH, Month.MARCH);
            }
          case 82: break;
          case 31: 
            { return symbol("max",MAX);
            }
          case 83: break;
          case 32: 
            { return symbol("may",MONTH, Month.MAY);
            }
          case 84: break;
          case 33: 
            { return symbol("min",MIN);
            }
          case 85: break;
          case 34: 
            { return symbol("cap",CAP);
            }
          case 86: break;
          case 35: 
            { return symbol("apr",MONTH, Month.APRIL);
            }
          case 87: break;
          case 36: 
            { return symbol("aug",MONTH, Month.AUGUST);
            }
          case 88: break;
          case 37: 
            { return symbol("feb",MONTH, Month.FEBRUARY);
            }
          case 89: break;
          case 38: 
            { return symbol("oct",MONTH, Month.OCTOBER);
            }
          case 90: break;
          case 39: 
            { return symbol("nov",MONTH, Month.NOVEMBER);
            }
          case 91: break;
          case 40: 
            { return symbol("sep",MONTH, Month.SEPTEMBER);
            }
          case 92: break;
          case 41: 
            { return symbol("dec",MONTH, Month.DECEMBER);
            }
          case 93: break;
          case 42: 
            { return symbol("jan",MONTH, Month.JANUARY);
            }
          case 94: break;
          case 43: 
            { return symbol("jul",MONTH, Month.JULY);
            }
          case 95: break;
          case 44: 
            { return symbol("jun",MONTH, Month.JUNE);
            }
          case 96: break;
          case 45: 
            { return symbol("tier", TIER);
            }
          case 97: break;
          case 46: 
            { return symbol("floor",FLOOR);
            }
          case 98: break;
          case 47: 
            { return symbol("shift",SHIFT);
            }
          case 99: break;
          case 48: 
            { return symbol("until", UNTIL);
            }
          case 100: break;
          case 49: 
            { return symbol("date", DATE, yytext());
            }
          case 101: break;
          case 50: 
            { return symbol("datedavg",DATEDAVG);
            }
          case 102: break;
          case 51: 
            { return symbol("tierblend", TIERBLEND);
            }
          case 103: break;
          case 52: 
            { return symbol("splitmonth", SPLITMONTH);
            }
          case 104: break;
          default:
            zzScanError(ZZ_NO_MATCH);
        }
      }
    }
  }


}
