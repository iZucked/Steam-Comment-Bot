/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart;

public enum EventSize {
	SMALL(9, 14),
	MEDIUM(11, 19),
	LARGE(14, 26);

	/*
	 * Size of the font. This should infuence the height of the event.
	 */
	private final int fontSize;
	private final int eventHeight;
	

	EventSize(int fontSize, int eventHeight) {
		this.fontSize = fontSize;
		this.eventHeight = eventHeight;
	}
	
	public int getFontSize() {
		return fontSize;
	}

	public int getEventHeight() {
		return eventHeight;
	}
}
