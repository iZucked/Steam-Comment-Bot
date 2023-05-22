package org.eclipse.nebula.widgets.ganttchart.label;

public enum EventLabelFontSize {
	SMALL(12, 0),
	MEDIUM(16, 0),
	LARGE(20, 0);

	/*
	 * Preferably should be a number that is divisible by 4
	 */
	private final int inPixels;
	private final int margin;

	EventLabelFontSize(int fontHeightInPixels, int margin) {
		this.inPixels = fontHeightInPixels;
		this.margin = margin;
	}
	
	public int getFontHeightInPixels() {
		return inPixels;
	}

	public int getMargin() {
		return margin;
	}
	
	public int totalHeight() {
		return inPixels + margin * 2;
	}
}