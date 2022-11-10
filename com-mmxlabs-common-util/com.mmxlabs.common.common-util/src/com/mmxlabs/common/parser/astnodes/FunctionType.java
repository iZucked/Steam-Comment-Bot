/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.astnodes;

public enum FunctionType {
	MIN("MIN"), FLOOR("FLOOR"), MAX("MAX"), CAP("CAP"), IF("IF");

	private final String name;

	private FunctionType(String name) {
		this.name = name;
	}

	public String asString() {
		return name;
	}
}
