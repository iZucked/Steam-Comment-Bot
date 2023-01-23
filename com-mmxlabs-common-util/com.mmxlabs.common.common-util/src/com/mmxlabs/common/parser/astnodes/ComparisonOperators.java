/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.astnodes;

public enum ComparisonOperators {
	LT("<"), LTE("<="), GT(">"), GTE(">=");

	private final String op;

	private ComparisonOperators(String op) {
		this.op = op;
	}

	public String asString() {
		return op;
	}

}
