package com.mmxlabs.common.parser.astnodes;

public enum Operator {
	PLUS('+'), MINUS('-'), TIMES('*'), DIVIDE('/'), PERCENT('%');

	private final char c;

	private Operator(char c) {
		this.c = c;
	}

	public String asString() {
		return String.valueOf(c);
	}

	public char asChar() {
		return c;
	}
}
