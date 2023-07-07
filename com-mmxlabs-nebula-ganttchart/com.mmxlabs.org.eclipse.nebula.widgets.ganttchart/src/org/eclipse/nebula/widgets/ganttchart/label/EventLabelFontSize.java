/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package org.eclipse.nebula.widgets.ganttchart.label;

public enum EventLabelFontSize {
	SMALL(12, 0, 4),
	MEDIUM(16, 0, 6),
	LARGE(20, 0, 8);

	/*
	 * Size of the font. It will be converted to points (x3/4), so
	 * preferably should be a number that is divisible by 4
	 */
	private final int inPixels;
	private final int margin;
	private final int outerMargin;

	EventLabelFontSize(int fontHeightInPixels, int margin, int outerMargin) {
		this.inPixels = fontHeightInPixels;
		this.margin = margin;
		this.outerMargin = outerMargin;
	}
	
	public int getFontHeightInPixels() {
		return inPixels;
	}

	public int getMargin() {
		return margin;
	}
	
	public int getOuterMargin() {
		return outerMargin;
	}
	
	public int totalHeight() {
		return inPixels + margin * 2;
	}
}