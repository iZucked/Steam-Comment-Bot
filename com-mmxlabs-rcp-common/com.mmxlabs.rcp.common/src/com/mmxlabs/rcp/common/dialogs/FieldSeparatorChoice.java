/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.dialogs;

public enum FieldSeparatorChoice {
	COMMA("Comma", ','), SEMICOLON("Semicolon", ';');

	private final String display;
	private final char c;

	private FieldSeparatorChoice(String display, char c) {
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