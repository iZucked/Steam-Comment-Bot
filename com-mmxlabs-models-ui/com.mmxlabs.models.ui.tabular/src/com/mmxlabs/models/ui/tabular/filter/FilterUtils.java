/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.filter;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.HashMap;

import com.mmxlabs.common.Pair;

/**
 * Utility class containing filter parser for a particular, made up grammar
 * 
 * said grammar is
 * 
 * matcher:= term | key:term | key=term | key<term | key>term
 * 
 * conj:= (term )+
 * 
 * disj:=(conj,)+
 * 
 * @author hinton
 * 
 */
public class FilterUtils {
	private static final int COLON = ':';
	private static final int EQ = '=';
	private static final int LT = '<';
	private static final int GT = '>';
	private static final int COMMA = ',';
	private static final int LP = '(';
	private static final int RP = ')';
	private static final int NOTEQ = '~';

	private class FilterBuilder {
		private final Group disjunctions = new Group(false);
		private Group conjunction = new Group(true);
		{
			disjunctions.addFilter(conjunction);
		}

		public void pushAnd(final IFilter filter) {
			conjunction.addFilter(filter);
		}

		@SuppressWarnings("unused")
		public void pushOr(final IFilter filter) {
			pushOr();
			pushAnd(filter);
		}

		public IFilter getFilter() {
			return disjunctions.collapse();
		}

		public void pushOr() {
			conjunction = new Group(true);
			disjunctions.addFilter(conjunction);
		}
	}

	private IFilter parseTokenizer(final StreamTokenizer tok) throws IOException {
		final FilterBuilder builder = new FilterBuilder();

		boolean inMatcher = false;
		String lastWord = null;
		int matchOp = -1;
		top: while (tok.nextToken() != StreamTokenizer.TT_EOF) {
			switch (tok.ttype) {
			case '"':
			case '\'':
			case StreamTokenizer.TT_WORD:
				if (inMatcher) {
					builder.pushAnd(new Match(Match.Operation.fromToken(matchOp), lastWord, tok.sval));

					lastWord = null;
					inMatcher = false;
				} else {
					if (lastWord != null) {
						builder.pushAnd(new Match(Match.Operation.LIKE, lastWord));
					}

					lastWord = tok.sval;
				}
				break;
			case NOTEQ:
			case COLON:
			case EQ:
			case LT:
			case GT:
				inMatcher = true;
				matchOp = tok.ttype;
				break;
			case COMMA:
				if (lastWord != null) {
					builder.pushAnd(new Match(Match.Operation.LIKE, lastWord));
					lastWord = null;
				}
				builder.pushOr();
				break;
			case LP:
				builder.pushAnd(parseTokenizer(tok));
				break;
			case RP:
				break top;
			}
		}
		// handle any trailing items
		if (lastWord != null) {
			builder.pushAnd(new Match(Match.Operation.LIKE, lastWord));
		}
		return builder.getFilter();
	}

	public IFilter parseFilterString(final String filterString) {
		final StreamTokenizer tok = new StreamTokenizer(new StringReader(filterString));

		tok.resetSyntax();
		tok.eolIsSignificant(false);
		tok.lowerCaseMode(true);
		tok.slashSlashComments(false);
		tok.slashStarComments(false);
		tok.eolIsSignificant(false);
		tok.quoteChar('"');
		tok.quoteChar('\'');
		tok.whitespaceChars(' ', ' ');
		tok.whitespaceChars('\t', '\t');
		tok.wordChars('0', '9');
		tok.wordChars('a', 'z');
		tok.wordChars('A', 'Z');
		tok.wordChars('/', '/');
		tok.wordChars('-', '-');
		tok.wordChars('+', '+');
		tok.ordinaryChar(',');

		try {
			final IFilter filter = parseTokenizer(tok);
			if (filter == null) {
				return null;
			}
			return filter.collapse();
		} catch (final IOException e) {
			return null;
		}
	}

	public static void main(final String[] args) {
		final FilterUtils fu = new FilterUtils();
		final IFilter filter = fu.parseFilterString(args[0]);
		System.out.println(filter);
		final HashMap<String, Pair<?, ?>> values = new HashMap<String, Pair<?, ?>>();

		for (int i = 1; i < args.length; i += 2) {
			values.put(args[i], new Pair<String, String>(args[i + 1], args[i + 1]));
		}

		System.out.println(filter.matches(values));
	}
}
