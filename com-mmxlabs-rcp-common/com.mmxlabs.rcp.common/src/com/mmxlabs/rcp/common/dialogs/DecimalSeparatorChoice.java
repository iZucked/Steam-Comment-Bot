/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.dialogs;

public enum DecimalSeparatorChoice {
	COMMA("Comma", ','), PERIOD("Period", '.');

	private final String display;
	private final char c;

	private DecimalSeparatorChoice(String display, char c) {
		this.display = display;
		this.c = c;

	}

	public String getDisplayName() {
		return display;
	}

	public char getChar() {
		return c;
	}
}